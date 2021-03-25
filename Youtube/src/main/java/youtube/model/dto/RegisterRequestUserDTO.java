package youtube.model.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Component
public class RegisterRequestUserDTO {
    private String username;
    private String email;
    private int age;
    private String password;
    private String confirmPassword;
    private String city;
}
