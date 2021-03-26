package youtube.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.User;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@Component
public class RegisterResponseUserDTO {
    private int id;
    private String username;
    private String email;
    private int age;
    private String city;
    private LocalDateTime registerDate;


    public RegisterResponseUserDTO(User user){
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        age = user.getAge();
        city = user.getCity();
        registerDate = user.getRegisterDate();
    }
}
