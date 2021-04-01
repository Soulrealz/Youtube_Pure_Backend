package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dao.VideoDbDAO;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDAndDislikesDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDDTO;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.UserRepository;
import youtube.model.repository.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private JdbcTemplate jdbcTemplate;

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
    public byte[] getMedia(int id) {
        Optional<Video> video = videoRepository.findById(id);

        if (video.isEmpty()) {
            throw new NotFoundException("The video you want to watch doesn't exist.");
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

    public List<VideoWithoutIDAndDislikesDTO> sortByLikes(int limit, int offset) {
        String sql = VideoWithoutIDAndDislikesDTO.selectVideosAndSortByLikes;
        List<VideoWithoutIDAndDislikesDTO> videos = new ArrayList<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                VideoWithoutIDAndDislikesDTO video = new VideoWithoutIDAndDislikesDTO();
                video.setTitle(rs.getString("title"));
                video.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                video.setDescription(rs.getString("description"));
                video.setOwnerName(rs.getString("username"));
                video.setLikes(rs.getInt("likes"));

                videos.add(video);
            }

            return videos;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
}
