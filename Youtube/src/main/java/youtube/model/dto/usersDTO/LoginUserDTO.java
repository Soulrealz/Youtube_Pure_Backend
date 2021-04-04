package youtube.model.dto.usersDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

// User DTO used for logging in account
@NoArgsConstructor
@Getter
@Setter
@Component
public class LoginUserDTO {
    private String username;
    private String password;
}
