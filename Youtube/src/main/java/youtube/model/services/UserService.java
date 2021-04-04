package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.GenericResponseDTO;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.usersDTO.*;
import youtube.model.dto.videosDTO.UploadVideoDTO;
import youtube.model.dto.videosDTO.VideoWithoutOwnerDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.PlaylistRepository;
import youtube.model.repository.UserRepository;
import youtube.model.repository.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private EmailService emailService;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO userDTO){
        // Check if there is already user with this email
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("User with this email already exists.");
        }

        // Check if there is already user with this username
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new BadRequestException("This username is already taken.");
        }

        // Validate all users information
        userDTO.validateUserInformation();

        // Encode the password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        User user = new User(userDTO);
        user.setRegisterDate(LocalDateTime.now());
        user = userRepository.save(user);

        emailService.sendEmail(user.getEmail());
        return new RegisterResponseUserDTO(user);
    }

    public UserWithoutPasswordDTO login(LoginUserDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        // Checks if the user entered correct username and password
        if(user == null) {
            throw new AuthenticationException("Wrong credentials.");
        }
        else {
            // Encode the entered pass and check if it matches the original one
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
        // Checks if there is user with that username
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

    // Deleting user's own profile
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    // Retrieving all videos of the logged user
    public List<VideoWithoutOwnerDTO> getVideos(User user) {
        List<Video> userVideos = videoRepository.findAllByOwner(user);
        // Checks if user has any videos at all
        if(userVideos.size() == 0) {
            throw new NotFoundException("You don't have uploaded videos.");
        }

        List<VideoWithoutOwnerDTO> returnedVideos = new ArrayList<>();
        for(Video video : userVideos) {
            returnedVideos.add(new VideoWithoutOwnerDTO(video));
        }

        return returnedVideos;
    }

    // Retrieving all playlists of the logged user
    public List<PlaylistWithoutOwnerDTO> getPlaylists(User user) {
        List<Playlist> userPlaylists = playlistRepository.findAllByOwner(user);
        // Checks if user has any playlists at all
        if(userPlaylists.size() == 0) {
            throw new NotFoundException("You don't have created playlists.");
        }

        List<PlaylistWithoutOwnerDTO> returnedPlaylists = new ArrayList<>();
        for(Playlist playlist : userPlaylists) {
            returnedPlaylists.add(new PlaylistWithoutOwnerDTO(playlist));
        }

        return returnedPlaylists;
    }

    public GenericResponseDTO subscribe(int id, User user) {
        Optional<User> subscribeToUser = userRepository.findById(id);

        // Checks if the user we want to subscribe to exists
        if(subscribeToUser.isEmpty()) {
            throw new NotFoundException("The user you want to subscribe to doesn't exist");
        }

        // Checks if the user account we want to subscribe to is not ourselves
        if(subscribeToUser.get() == user) {
            throw new BadRequestException("You can't subscribe to yourself.");
        }

        // Checks if we haven't been subscribed to this user already
        if(subscribeToUser.get().getSubscribers().contains(user)) {
            throw new BadRequestException("You are already subscribed to this user.");
        }

        subscribeToUser.get().getSubscribers().add(user);
        userRepository.save(subscribeToUser.get());
        return new GenericResponseDTO("You have successfully subscribed to " + subscribeToUser.get().getUsername());
    }

    public GenericResponseDTO unsubscribe(int id, User user) {
        Optional<User> unsubscribeToUser = userRepository.findById(id);

        // Checks if the user we want to unsubscribe from exists
        if(unsubscribeToUser.isEmpty()) {
            throw new NotFoundException("The user you want to unsubscribe to doesn't exist");
        }

        // Checks if the user account we want to unsubscribe from is not ourselves
        if(unsubscribeToUser.get() == user) {
            throw new BadRequestException("You can't unsubscribe to yourself.");
        }

        // Checks if we haven't been unsubscribed from this user already
        if(!unsubscribeToUser.get().getSubscribers().contains(user)) {
            throw new BadRequestException("You are already not subscribed to this user.");
        }

        unsubscribeToUser.get().getSubscribers().remove(user);
        userRepository.save(unsubscribeToUser.get());
        return new GenericResponseDTO("You have successfully unsubscribed to " + unsubscribeToUser.get().getUsername());
    }

    public GenericResponseDTO verifyEmail(String token, User user) {
        user.setVerified(true);
        userRepository.save(user);
        return new GenericResponseDTO("Email verified.");
    }
}
