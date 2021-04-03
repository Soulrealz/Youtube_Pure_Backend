package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dao.VideoDAO;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDAndDislikesDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDDTO;
import youtube.model.pojo.HistoryRecord;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.HistoryRecordRepository;
import youtube.model.repository.UserRepository;
import youtube.model.repository.VideoRepository;
import youtube.model.validations.PairVideoInt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        if (videoRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no video with that name.");
        }

        Video video = videoRepository.findByTitle(title);
        return new VideoWithoutIDDTO(video);
    }

    public UserWithoutPasswordDTO createVideo(UploadVideoDTO videoDTO, User user) {
        if (videoRepository.findByTitle(videoDTO.getTitle()) != null) {
            throw new BadRequestException("This video title is already used.");
        }
        Video video = new Video(videoDTO);
        video.setOwner(user);

        video = videoRepository.save(video);
        return new UserWithoutPasswordDTO(userRepository.findByUsername(user.getUsername()));
    }
    public String uploadVideoFile(MultipartFile videoFile, int id, User user) {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isEmpty()) {
            throw new NotFoundException("The video you want to add media to, doesn't exist.");
        }

        if (video.get().getPath() != null) {
            throw new BadRequestException("This video already has media file.");
        }

        if (video.get().getOwner() != user) {
            throw new BadRequestException("You can't upload files on other user videos.");
        }

        File pFile = new File(filePath + File.separator + id + "_" + System.nanoTime() + ".mp4");

        try (OutputStream os = new FileOutputStream(pFile)) {
            os.write(videoFile.getBytes());
            video.get().setPath(pFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoRepository.save(video.get());
        return "You have successfully added a media file to your video";
    }
    public byte[] getMedia(int id, User user) {
        Optional<Video> video = videoRepository.findById(id);

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

        if(video.get().getPath() == null) {
            throw new NotFoundException("This video doesn't have media file.");
        }

        String path = video.get().getPath();
        File pFile = new File(path);
        try {
            return Files.readAllBytes(pFile.toPath());
        } catch (IOException e) {
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
            throw new NotFoundException("Comment doesn't exist");
        } else return video.get();
    }

    public List<VideoWithoutIDDTO> orderByUploadDate(int id) {
        List<Video> videos = videoRepository.findAllByIdGreaterThanOrderByUploadDate(id);
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

    public String getViews(int id) {
        Optional<Video> video = videoRepository.findById(id);

        if(video.isEmpty()) {
            throw new NotFoundException("This video doesn't exist.");
        }

        List<HistoryRecord> currentRecord =  historyRecordRepository.findAllByWatchedVideo(video.get());
        return "This video has " + currentRecord.size() + " views.";
    }
}
