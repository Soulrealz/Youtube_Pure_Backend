package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDAndDislikes;
import youtube.model.dto.videosDTO.VideoWithoutIDDTO;
import youtube.model.pojo.User;
import youtube.model.services.VideoService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class VideoController extends AbstractController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private SessionManager sessionManager;

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


    // PathVar - which video to like/dislike/remove like/remove dislike
    // RequestParam - 1 = like, -1 = dislike, 0 = remove current status(if any)
    @PostMapping("/videos/{id}")
    public VideoWithoutIDDTO reactToVideo(@PathVariable(name = "id") int videoID, @RequestParam(name = "react") String action, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        if (action.equals("1")) return videoService.likeVideo(user, videoID);
        else if (action.equals("-1")) return videoService.dislikeVideo(user, videoID);
        else return videoService.neutralStateVideo(user, videoID);
    }

    @GetMapping("/videos/sort_upload_date")
    public List<VideoWithoutIDDTO> sortByUploadDate(){
        return videoService.sortByUploadDate();
    }


    // RequestParam limit = how many to show
    // RequestParam offset = how many to skip (paging)
    @GetMapping("/videos/sort_by_likes")
    public List<VideoWithoutIDAndDislikes> sortByLikes(@RequestParam(name = "limit") int limit, @RequestParam(name = "offset") int offset) {
        return videoService.sortByLikes(limit, offset);
    }
}
