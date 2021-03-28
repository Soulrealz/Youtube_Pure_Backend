package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.usersDTO.*;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.PlaylistRepository;
import youtube.model.repository.UserRepository;
import youtube.model.repository.VideoRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private PlaylistRepository playlistRepository;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO userDTO){
        //check if there is already user with this email
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("User with this email already exists.");
        }

        //check if there is already user with this username
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new BadRequestException("This username is already taken.");
        }

        //validate all users information
        userDTO.validateUserInformation();

        //encode the password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        User user = new User(userDTO);
        user.setRegisterDate(LocalDateTime.now());
        user = userRepository.save(user);

        return new RegisterResponseUserDTO(user);
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

    public UserWithoutPasswordDTO editUser(EditRequestUserDTO userDTO, User user) {
        user.editUser(userDTO);

        user = userRepository.save(user);
        return new UserWithoutPasswordDTO(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public UserWithoutPasswordDTO uploadVideo(UploadVideoDTO videoDTO, User user) {
       if(videoRepository.findByTitle(videoDTO.getTitle()) != null) {
           throw new BadRequestException("This video title is already used.");
       }
       Video video = new Video(videoDTO);
       video.setOwner(user);
       video = videoRepository.save(video);
       return new UserWithoutPasswordDTO(userRepository.findByUsername(user.getUsername()));
    }

    public void createPlaylist(String title, User user) {
        if(playlistRepository.findByTitle(title) != null) {
            throw new BadRequestException("This playlist title is already used.");
        }

        Playlist playlist = new Playlist();
        playlist.setTitle(title);
        playlist.setOwner(user);
        playlist.setCreatedDate(LocalDateTime.now());
        playlistRepository.save(playlist);
    }
}
