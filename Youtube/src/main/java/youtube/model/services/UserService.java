package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.model.dto.RegisterRequestUserDTO;
import youtube.model.dto.RegisterResponseUserDTO;
import youtube.model.pojo.User;
import youtube.model.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO userDTO){
        User user = new User(userDTO);
        user.setRegisterDate(LocalDateTime.now());
        user = userRepository.save(user);
        RegisterResponseUserDTO responseUserDTO = new RegisterResponseUserDTO(user);

        return responseUserDTO;
    }
}
