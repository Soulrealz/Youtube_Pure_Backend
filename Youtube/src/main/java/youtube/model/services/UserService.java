package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.LoginUserDTO;
import youtube.model.dto.RegisterRequestUserDTO;
import youtube.model.dto.RegisterResponseUserDTO;
import youtube.model.dto.UserWithoutPasswordDTO;
import youtube.model.pojo.User;
import youtube.model.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO userDTO){
        //check if there is already user with this email
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("User with this email already exists.");
        }

        //check if there is already user with this username
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new BadRequestException("This username is already taken.");
        }

        //validate all user's information
        userDTO.validateUserInformation();

        //encode the password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        User user = new User(userDTO);
        user.setRegisterDate(LocalDateTime.now());
        user = userRepository.save(user);
        RegisterResponseUserDTO responseUserDTO = new RegisterResponseUserDTO(user);

        return responseUserDTO;
    }

    public UserWithoutPasswordDTO login(LoginUserDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if(user == null) {
            throw new AuthenticationException("Wrong credentials.");
        }
        else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return new UserWithoutPasswordDTO(user);
            }
            else {
                throw new AuthenticationException("Wrong credentials.");
            }
        }
    }

    public UserWithoutPasswordDTO getUserByName(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new NotFoundException("User with this username doesn't exist.");
        }

        return new UserWithoutPasswordDTO(user);
    }
}
