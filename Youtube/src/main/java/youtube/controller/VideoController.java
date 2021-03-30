package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDDTO;
import youtube.model.pojo.User;
import youtube.model.services.VideoService;

import javax.servlet.http.HttpSession;

@RestController
public class VideoController extends AbstractController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private SessionManager sessionManager;

    // Getting all videos with {title} name
   // @GetMapping("/videos")
   // public List<VideoWithoutIDDTO> getVideo(@RequestParam String title) {
        //return videoService.getAllVideos(title);
  //  }

    @GetMapping("/videos")
    public VideoWithoutIDDTO getVideoByName(@RequestParam String title) {
        return videoService.getByName(title);
    }

    @GetMapping(value = "/videos/media/{id}", produces = "video/mp4")
    public byte[] getMediaOfVideo(@PathVariable int id) {
        System.out.println("kur");
       return videoService.getMedia(id);
    }

    @PutMapping("/videos/create_video")
    public UserWithoutPasswordDTO createVideo( @RequestBody UploadVideoDTO videoDTO, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return videoService.createVideo(videoDTO, user);
    }

    @PostMapping("/videos/upload/{id}")
    public String uploadVideoFile(@RequestPart MultipartFile videoFile, @PathVariable int id, HttpSession ses){
        User user = sessionManager.getLoggedUser(ses);
        return videoService.uploadVideoFile(videoFile, id, user);
    }
}
