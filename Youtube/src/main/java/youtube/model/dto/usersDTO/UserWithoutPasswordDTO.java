package youtube.model.dto.usersDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.videosDTO.VideoWithoutOwnerDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordDTO {
    private int id;
    private String username;
    private String email;
    private int age;
    private String city;
    private List<VideoWithoutOwnerDTO> videos;
    private List<PlaylistWithoutOwnerDTO> playlists;
    private int subscribers;

    public UserWithoutPasswordDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        age = user.getAge();
        city = user.getCity();
        videos = new ArrayList<>();
        playlists = new ArrayList<>();
        for(Video video : user.getVideos()) {
            videos.add(new VideoWithoutOwnerDTO(video));
        }
        for(Playlist playlist : user.getPlaylists()) {
            playlists.add(new PlaylistWithoutOwnerDTO(playlist));
        }

        subscribers = user.getSubscribers().size();
    }
}
