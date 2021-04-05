package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;
import youtube.model.dto.GenericResponseDTO;
import youtube.model.dto.usersDTO.SearchUserDTO;
import youtube.model.dto.usersDTO.UserWithoutPasswordDTO;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithIDTitleDateDescDTO;
import youtube.model.dto.videosDTO.VideoWithoutIDAndDislikesDTO;
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

    // RequestParam name of video we want to get (videos are unique)
    @GetMapping("/videos")
    public VideoWithoutIDDTO getVideoByName(@RequestParam String title) {
        return videoService.getByName(title);
    }

    // PathVar - which video to get
    @GetMapping(value = "/videos/media/{id}", produces = "video/mp4")
    public byte[] getMediaOfVideo(@PathVariable int id, HttpSession ses) {
        // Checking if there is logged user, so if there is we can add data to history table
        User user = sessionManager.checkIfThereIsLoggedUser(ses);
        return videoService.getMedia(id, user);
    }

    // RequestBody - title/description json
    @PutMapping("/videos")
    public UserWithoutPasswordDTO createVideo(@RequestBody UploadVideoDTO videoDTO, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return videoService.createVideo(videoDTO, user);
    }

    // RequestPart - actual video file
    @PostMapping("/videos/upload/{id}")
    public GenericResponseDTO uploadVideoFile(@RequestPart MultipartFile videoFile, @PathVariable int id, HttpSession ses){
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return videoService.uploadVideoFile(videoFile, id, user);
    }

    // PathVar - which video to like/dislike/remove like/remove dislike
    // RequestParam - 1 = like, -1 = dislike, 0 = remove current status(if any)
    @PostMapping("/videos/{id}")
    public VideoWithoutIDDTO reactToVideo(@PathVariable(name = "id") int videoID, @RequestParam(name = "react") String action, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        if (action.equals("1")) return videoService.likeVideo(user, videoID);
        else if (action.equals("-1")) return videoService.dislikeVideo(user, videoID);
        else if (action.equals("0")) return videoService.neutralStateVideo(user, videoID);
        else throw new BadRequestException("No such reaction possible");
    }

    @GetMapping("/videos/order_upload_date")
    public List<VideoWithoutIDDTO> orderByUploadDate(){
        return videoService.orderByUploadDate();
    }

    // RequestParam limit = how many to show
    // RequestParam offset = how many to skip (paging)
    @GetMapping("/videos/sort_by_likes")
    public List<VideoWithoutIDAndDislikesDTO> sortByLikes(@RequestParam(name = "limit") int limit, @RequestParam(name = "offset") int offset) {
        return videoService.orderByLikes(limit, offset);
    }

    // PathVar - which video to delete
    @DeleteMapping("/videos/{id}")
    public UserWithoutPasswordDTO deleteVideo(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return videoService.deleteVideo(id, user);
    }

    // PathVar - which video's views to show
    @GetMapping("/videos/views/{id}")
    public GenericResponseDTO getViews(@PathVariable int id){
        return videoService.getViews(id);
    }

    // Returns all users that match (even if not entirely) the string in the dto
    // RequestBody - search keyword
    @GetMapping("/videos/search")
    public List<VideoWithIDTitleDateDescDTO> searchByName(@RequestBody SearchUserDTO name) {
        return videoService.searchByName(name);
    }
}
