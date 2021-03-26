package youtube.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.User;

import java.time.LocalDateTime;

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


    public UserWithoutPasswordDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        age = user.getAge();
        city = user.getCity();
    }
}
