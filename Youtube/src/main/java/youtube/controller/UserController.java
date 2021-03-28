package youtube.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.*;
import youtube.model.pojo.User;
import youtube.model.services.UserService;

import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController{

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

    @DeleteMapping("/users/delete")
    public String deleteUser(HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        userService.deleteUser(user);
        sessionManager.logoutUser(ses);
        return "You have deleted your profile successfully!";
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession ses){
        sessionManager.logoutUser(ses);
        return "You have logged out.";
    }

    @PutMapping("/users/upload")
    public UserWithoutPasswordDTO uploadVideo(@RequestBody UploadVideoDTO videoDTO, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return userService.uploadVideo(videoDTO, user);
    }

    @PutMapping("/users/create_playlist")
    public String createPlaylist(@RequestParam String title, HttpSession ses){
        User user = sessionManager.getLoggedUser(ses);
        userService.createPlaylist(title, user);
        return "You have created new playlist.";
    }
}
