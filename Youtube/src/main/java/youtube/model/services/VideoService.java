package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
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
import java.util.LinkedList;
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



    public VideoWithoutIDDTO getByName(String title) {
        if(videoRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no video with that name.");
        }

        Video video = videoRepository.findByTitle(title);
        return new VideoWithoutIDDTO(video);
    }

    public UserWithoutPasswordDTO createVideo(UploadVideoDTO videoDTO, User user) {
        if(videoRepository.findByTitle(videoDTO.getTitle()) != null) {
            throw new BadRequestException("This video title is already used.");
        }
        Video video = new Video(videoDTO);
        video.setOwner(user);

        video = videoRepository.save(video);
        return new UserWithoutPasswordDTO(userRepository.findByUsername(user.getUsername()));
    }

    public String uploadVideoFile(MultipartFile videoFile, int id, User user) {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()) {
            throw new NotFoundException("The video you want to add media to, doesn't exist.");
        }

        if(video.get().getPath() != null) {
            throw new BadRequestException("This video already has media file.");
        }

        if(video.get().getOwner() != user) {
            throw new BadRequestException("You can't upload files on other user videos.");
        }

        File pFile = new File(filePath + File.separator + id+"_"+System.nanoTime() +".mp4");

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

        if(video.isEmpty()) {
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
}
