package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import youtube.model.dto.PlaylistWithoutIdDTO;
import youtube.model.services.PlaylistService;

@RestController
public class PlaylistController extends AbstractController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/playlists")
    public PlaylistWithoutIdDTO getPlaylistByName(@RequestParam String title) {
        return playlistService.getByName(title);
    }
}
