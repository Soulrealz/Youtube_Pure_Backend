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

    // Getting playlist by given name
    @GetMapping("/playlists")
    public PlaylistWithoutIdDTO getPlaylistByName(@RequestParam String title) {
        return playlistService.getByName(title);
    }

    // Adding video to playlist
    // PathVar - id of playlist we want to add to
    // RequestBody - title of the video we want to add
    @PostMapping("/playlists/{id}")
    public PlaylistWithoutOwnerDTO addVideoToPlaylist(@RequestBody String title, @PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.addVideo(user, id, title);
    }

    // Deleting video from playlist
    // PathVar - id of playlist we want to delete from
    // RequestBody - title of the video we want to delete
    @PostMapping("/playlists/remove_video/{id}")
    public PlaylistWithoutOwnerDTO removeVideoFromPlaylist(@RequestBody String title, @PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.removeVideo(user, id, title);
    }

    // Creating playlist
    // RequestParam - title of the new playlist
    @PutMapping("/playlists/create_playlist")
    public String createPlaylist(@RequestParam String title, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        playlistService.createPlaylist(title, user);
        return "You have created new playlist.";
    }

    // Deleting playlist
    // PathVar - id of playlist we want to delete
    @DeleteMapping("/playlists/{id}")
    public String deletePlaylist(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.deletePlaylist(id, user);
    }
}
