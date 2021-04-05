package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.GenericResponseDTO;
import youtube.model.dto.playlistsDTO.ResponsePlaylistDTO;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.videosDTO.VideoWithIdDTO;
import youtube.model.pojo.User;
import youtube.model.services.PlaylistService;

import javax.servlet.http.HttpSession;

@RestController
public class PlaylistController extends AbstractController {

    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SessionManager sessionManager;

    // RequestParam - name of playlist we want to get
    @GetMapping("/playlists")
    public ResponsePlaylistDTO getPlaylistByName(@RequestParam String title) {
        return playlistService.getByName(title);
    }

    // Adding video to playlist
    // PathVar - id of playlist we want to add to
    // RequestBody - title of the video we want to add
    @PostMapping("/playlists/{id}")
    public PlaylistWithoutOwnerDTO addVideoToPlaylist(@RequestBody VideoWithIdDTO videoDTO, @PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.addVideo(user, id, videoDTO);
    }

    // Deleting video from playlist
    // PathVar - id of playlist we want to delete from
    // RequestBody - title of the video we want to delete
    @PostMapping("/playlists/remove_video/{id}")
    public PlaylistWithoutOwnerDTO removeVideoFromPlaylist(@RequestBody VideoWithIdDTO videoDTO, @PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.removeVideo(user, id, videoDTO);
    }

    // Creating playlist
    // RequestParam - title of the new playlist
    @PutMapping("/playlists/create_playlist")
    public ResponsePlaylistDTO createPlaylist(@RequestParam String title, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.createPlaylist(title, user);
    }

    // Deleting playlist
    // PathVar - id of playlist we want to delete
    @DeleteMapping("/playlists/{id}")
    public GenericResponseDTO deletePlaylist(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return playlistService.deletePlaylist(id, user);
    }
}
