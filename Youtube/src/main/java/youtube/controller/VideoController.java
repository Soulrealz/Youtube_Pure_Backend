package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import youtube.model.dto.VideoWithoutIDDTO;
import youtube.model.services.VideoService;

import java.util.List;

@RestController
public class VideoController extends AbstractController {
    @Autowired
    private VideoService videoService;

    // Getting all videos with {title} name
    @GetMapping("/videos")
    public List<VideoWithoutIDDTO> getVideo(@RequestParam String title) {
        return videoService.getAllVideos(title);
    }
}
