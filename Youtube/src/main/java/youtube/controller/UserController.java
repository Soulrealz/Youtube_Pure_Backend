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
}
