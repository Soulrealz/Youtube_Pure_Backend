package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.IOException;
import youtube.exceptions.NotFoundException;
import youtube.model.dao.VideoDAO;
import youtube.model.dto.GenericResponseDTO;
import youtube.model.dto.usersDTO.SearchUserDTO;
import youtube.model.dto.usersDTO.UserWithIDAndUsernameDTO;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithIDTitleDateDescDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDAndDislikesDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDDTO;
import youtube.model.pojo.HistoryRecord;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.HistoryRecordRepository;
import youtube.model.repository.UserRepository;
import youtube.model.repository.VideoRepository;
import youtube.model.utils.Log4JLogger;
import youtube.model.utils.FileValidator;
import youtube.model.utils.PairVideoInt;
import youtube.model.utils.VideoCredentialsValidator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private HistoryRecordRepository historyRecordRepository;
    @Autowired
    private VideoDAO videoDAO;

    public VideoWithoutIDDTO getByName(String title) {
        // Checks if video with this name actually exists
        if (videoRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no video with that name.");
        }

        Video video = videoRepository.findByTitle(title);
        return new VideoWithoutIDDTO(video);
    }

    public UserWithoutPasswordDTO createVideo(UploadVideoDTO videoDTO, User user) {
        VideoCredentialsValidator.validate(videoDTO.getTitle(), videoDTO.getDescription());

        // Checks if there is already video with that name
        if (videoRepository.findByTitle(videoDTO.getTitle()) != null) {
            throw new BadRequestException("This video title is already used.");
        }

        Video video = new Video(videoDTO);
        video.setOwner(user);

        video = videoRepository.save(video);
        return new UserWithoutPasswordDTO(userRepository.findByUsername(user.getUsername()));
    }
    public GenericResponseDTO uploadVideoFile(MultipartFile videoFile, int id, User user) {
        Optional<Video> video = videoRepository.findById(id);
        // Checks if there is video with this id before we can upload media to it
        if (video.isEmpty()) {
            throw new NotFoundException("The video you want to add media to, doesn't exist.");
        }

        // Checking if the file extension is mp4
        FileValidator.validateFileExtension(videoFile);
        // Checking if the file size exceeds the max allowed file size
        FileValidator.validateMaxSize(videoFile);

        // Checks if the video we want to upload media to doesn't have one already
        if (video.get().getPath() != null) {
            throw new BadRequestException("This video already has media file.");
        }

        // Checks if the video we want to upload media to is ours
        if (video.get().getOwner() != user) {
            throw new BadRequestException("You can't upload files on other user videos.");
        }

        File pFile = new File(filePath + File.separator + id + "_" + System.nanoTime() + ".mp4");

        try (OutputStream os = new FileOutputStream(pFile)) {
            os.write(videoFile.getBytes());
            video.get().setPath(pFile.getAbsolutePath());
        } catch (java.io.IOException e) {
            Log4JLogger.getLogger().error("Critical IO exception.\n", e);
            throw new IOException("Bad Input/Output");
        }

        videoRepository.save(video.get());
        return new GenericResponseDTO("You have successfully added a media file to your video");
    }

    public byte[] getMedia(int id, User user) {
        Optional<Video> video = videoRepository.findById(id);
        // Checks if video with this id actually exists
        if (video.isEmpty()) {
            throw new NotFoundException("The video you want to watch doesn't exist.");
        }

        if(user != null) {
            HistoryRecord historyRecord = new HistoryRecord();
            historyRecord.setWatchedBy(user);
            historyRecord.setWatchedVideo(video.get());
            historyRecord.setViewDate(LocalDateTime.now());

            historyRecordRepository.save(historyRecord);
        }

        // Checks if the video we want to watch has media
        if(video.get().getPath() == null) {
            throw new NotFoundException("This video doesn't have media file.");
        }

        String path = video.get().getPath();
        File pFile = new File(path);
        try {
            return Files.readAllBytes(pFile.toPath());
        } catch (java.io.IOException e) {
            throw new NotFoundException("There was a problem with the video.");
        }
    }

    public VideoWithoutIDDTO likeVideo(User user, int videoID) {
        List<Video> likedVideos = user.getLikedVideos();

        // Check if video exists or is liked
        Video video = returnExistingVideo(videoRepository.findById(videoID));
        if (likedVideos.contains(video)) {
            throw new BadRequestException("Cannot like an already liked video");
        }

        // Remove opposite status
        user.getDislikedVideos().remove(video);

        // Add status
        likedVideos.add(video);
        userRepository.save(user);

        return new VideoWithoutIDDTO(video);
    }
    public VideoWithoutIDDTO dislikeVideo(User user, int videoID) {
        List<Video> dislikedVideos = user.getDislikedVideos();

        // Check if video exists or is disliked
        Video video = returnExistingVideo(videoRepository.findById(videoID));
        if (dislikedVideos.contains(video)) {
            throw new BadRequestException("Cannot dislike an already disliked video");
        }

        // Remove opposite status
        user.getLikedVideos().remove(video);

        // Add status
        dislikedVideos.add(video);
        userRepository.save(user);

        return new VideoWithoutIDDTO(video);
    }
    public VideoWithoutIDDTO neutralStateVideo(User user, int videoID) {
        // Check if video exists
        Video video = returnExistingVideo(videoRepository.findById(videoID));

        // Remove any status
        user.getDislikedVideos().remove(video);
        user.getLikedVideos().remove(video);

        userRepository.save(user);

        return new VideoWithoutIDDTO(video);
    }

    private Video returnExistingVideo(Optional<Video> video) {
        if (video.isEmpty()) {
            throw new NotFoundException("Video doesn't exist");
        } else return video.get();
    }

    // Retrieving videos and ordering them by upload date
    public List<VideoWithoutIDDTO> orderByUploadDate() {
        List<Video> videos = videoRepository.findAllByIdGreaterThanOrderByUploadDate(0);
        List<VideoWithoutIDDTO> returnedVideos = new ArrayList<>();
        for(Video video: videos) {
            returnedVideos.add(new VideoWithoutIDDTO(video));
        }

        return returnedVideos;
    }

    public List<VideoWithoutIDAndDislikesDTO> orderByLikes(int limit, int offset) {
        // Get all videos and their like count
        List<PairVideoInt> videos = videoDAO.orderByLikes(limit, offset);
        List<VideoWithoutIDAndDislikesDTO> videosDTO = new ArrayList<>();

        // Build DTO for response
        for (PairVideoInt v : videos) {
            videosDTO.add(new VideoWithoutIDAndDislikesDTO(v.getVideo(), v.getLikes()));
        }

        return videosDTO;
    }

    public UserWithoutPasswordDTO deleteVideo(int id, User user) {
        Optional<Video> video = videoRepository.findById(id);

        if (video.isEmpty()) {
            throw new NotFoundException("This video doesn't exist.");
        }

        if (video.get().getOwner() != user) {
            throw new BadRequestException("You can't delete someone else's video.");
        }

        videoRepository.delete(video.get());
        return new UserWithoutPasswordDTO(userRepository.findByUsername(user.getUsername()));
    }

    // Getting views of certain video
    public GenericResponseDTO getViews(int id) {
        Optional<Video> video = videoRepository.findById(id);

        if(video.isEmpty()) {
            throw new NotFoundException("This video doesn't exist.");
        }

        List<HistoryRecord> currentRecord =  historyRecordRepository.findAllByWatchedVideo(video.get());
        return new GenericResponseDTO("This video has " + currentRecord.size() + " views.");
    }

    public List<VideoWithIDTitleDateDescDTO> searchByName(SearchUserDTO name) {
        List<Video> videos = videoDAO.searchByName(name.getName());
        List<VideoWithIDTitleDateDescDTO> videosDTO = new ArrayList<>();

        for (Video v : videos) {
            videosDTO.add(new VideoWithIDTitleDateDescDTO(v.getId(), v.getTitle(), v.getUploadDate(), v.getDescription()));
        }

        return videosDTO;
    }
}
