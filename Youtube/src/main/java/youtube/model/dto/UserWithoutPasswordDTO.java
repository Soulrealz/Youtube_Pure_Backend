package youtube.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
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

    public UserWithoutPasswordDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        age = user.getAge();
        city = user.getCity();
        videos = new ArrayList<>();
        for(Video v : user.getVideos()) {
            videos.add(new VideoWithoutOwnerDTO(v));
        }
    }
}
