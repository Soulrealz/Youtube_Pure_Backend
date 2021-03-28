package youtube.model.dto.usersDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.exceptions.BadRequestException;
import youtube.model.validations.UserValidation;

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
