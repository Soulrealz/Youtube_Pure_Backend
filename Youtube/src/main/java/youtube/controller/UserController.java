package youtube.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.usersDTO.*;
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

    // Registers a user
    // RequestBody - json with username/email/age/password/confirmPass/city information
    @PutMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO) {
        return userService.register(userDTO);
    }

    // Login a user
    // RequestBody - json with username/password
    @PostMapping("/users")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO loginDTO, HttpSession session) {
        UserWithoutPasswordDTO loggedUser = userService.login(loginDTO);
        sessionManager.loginUser(session, loggedUser.getId());
        return loggedUser;
    }

    // Getting user by its username
    @GetMapping("/users")
    public UserWithoutPasswordDTO getUserByUsername(@RequestParam String username) {
        return userService.getUserByName(username);
    }

    // Editing user
    // RequestBody - json with email/age/password/confirmPass/city, if certain value is null it won't be changed
    @PostMapping("/users/edit")
    public UserWithoutPasswordDTO editUser(@RequestBody EditRequestUserDTO userDTO, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return userService.editUser(userDTO, user);
    }

    // Deleting user own account, if he is logged in only
    @DeleteMapping("/users")
    public String deleteUser(HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        userService.deleteUser(user);
        sessionManager.logoutUser(ses);
        return "You have deleted your profile successfully!";
    }

    // Logout
    @PostMapping("/users/logout")
    public String logout(HttpSession ses) {
        sessionManager.logoutUser(ses);
        return "You have logged out.";
    }

    // Getting all videos of the logged user
    @GetMapping("/users/user_videos")
    public List<VideoWithoutOwnerDTO> getUserVideos(HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return userService.getVideos(user);
    }

    // Getting all playlists of the logged user
    @GetMapping("/users/user_playlists")
    public List<PlaylistWithoutOwnerDTO> getUserPlaylists(HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return userService.getPlaylists(user);
    }

    // Subscribing to another user
    // PathVar - id of the user we want to subscribe to
    @PostMapping("/users/subscribe/{id}")
    public String subscribe(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return userService.subscribe(id, user);
    }

    // Unsubscribing from another user
    // PathVar - id of the user we want to unsubscribe from
    @PostMapping("/users/unsubscribe/{id}")
    public String unsubscribe(@PathVariable int id, HttpSession ses) {
        User user = sessionManager.getVerifiedLoggedUser(ses);
        return userService.unsubscribe(id, user);
    }

    // Verifying user's email
    // PathVar - token sent to user
    @GetMapping("/verify/{token}")
    public void verifyEmail(@PathVariable(name = "token") String token, HttpSession ses) {
        User user = sessionManager.getUnverifiedUser(ses);
        userService.verifyEmail(token, user);
    }
}
