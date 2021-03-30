package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.playlistsDTO.PlaylistWithoutIdDTO;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.pojo.User;
import youtube.model.services.PlaylistService;

import javax.servlet.http.HttpSession;

@RestController
public class PlaylistController extends AbstractController {

    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/playlists")
    public PlaylistWithoutIdDTO getPlaylistByName(@RequestParam String title) {
        return playlistService.getByName(title);
    }

    @PostMapping("/playlists/{id}")
    public PlaylistWithoutOwnerDTO addVideoToPlaylist(@RequestBody String title, @PathVariable int id, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return playlistService.addVideo(user, id, title);
    }

    @PutMapping("/playlists/create_playlist")
    public String createPlaylist(@RequestParam String title, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        playlistService.createPlaylist(title, user);
        return "You have created new playlist.";
    }
}
