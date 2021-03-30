package youtube.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.usersDTO.*;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutOwnerDTO;
import youtube.model.pojo.User;
import youtube.model.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PutMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/users")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO loginDTO, HttpSession session) {
        UserWithoutPasswordDTO loggedUser = userService.login(loginDTO);
        sessionManager.loginUser(session, loggedUser.getId());
        return loggedUser;
    }

    @GetMapping("/users")
    public UserWithoutPasswordDTO getUserByUsername(@RequestParam String username) {
        return userService.getUserByName(username);
    }

    @PostMapping("/users/edit")
    public UserWithoutPasswordDTO editUser(@RequestBody EditRequestUserDTO userDTO, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return userService.editUser(userDTO, user);
    }

    @DeleteMapping("/users")
    public String deleteUser(HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        userService.deleteUser(user);
        sessionManager.logoutUser(ses);
        return "You have deleted your profile successfully!";
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession ses) {
        sessionManager.logoutUser(ses);
        return "You have logged out.";
    }

    @GetMapping("/users/user_videos")
    public List<VideoWithoutOwnerDTO> getUserVideos(HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return userService.getVideos(user);
    }

    @GetMapping("/users/user_playlists")
    public List<PlaylistWithoutOwnerDTO> getUserPlaylists(HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return userService.getPlaylists(user);
    }


    @PostMapping("/users/subscribe/{id}")
    public String subscribe(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return userService.subscribe(id, user);
    }
}
