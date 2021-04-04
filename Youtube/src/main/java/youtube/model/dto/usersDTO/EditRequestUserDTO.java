package youtube.model.dto.usersDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

// User DTO used when someone wants to change certain information in his account
@Component
@Setter
@Getter
public class EditRequestUserDTO {
    private String email;
    private int age;
    private String password;
    private String confirmPassword;
    private String city;


    public EditRequestUserDTO(){
        email = null;
        age = 0;
        password = null;
        confirmPassword = null;
        city = null;
    }

}
