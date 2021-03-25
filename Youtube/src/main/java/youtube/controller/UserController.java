package youtube.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import youtube.model.dto.RegisterRequestUserDTO;
import youtube.model.dto.RegisterResponseUserDTO;
import youtube.model.dto.UserDTO;
import youtube.model.pojo.User;
import youtube.model.services.UserService;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;

    @PutMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO) {
        return userService.register(userDTO);
    }
}
