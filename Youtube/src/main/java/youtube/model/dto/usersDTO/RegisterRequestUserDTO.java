package youtube.model.dto.usersDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.exceptions.BadRequestException;
import youtube.model.utils.UserValidator;

import java.util.regex.Pattern;

// User DTO used when new person want to register in our Youtube
@Setter
@Getter
@NoArgsConstructor
@Component
public class RegisterRequestUserDTO {
    private String username;
    private String email;
    private int age;
    private String password;
    private String confirmPassword;
    private String city;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void validateUserInformation(){

        // Checks if the username format is correct
        if(!UserValidator.validateUsername(username)){
            throw new BadRequestException("You have entered invalid username.");
        }

        // Checks if the email format is correct
        if(!UserValidator.validateEmail(email)) {
            throw new BadRequestException("You have entered invalid email.");
        }

        // Checks if the entered confirmation password is correct
        if(!UserValidator.validatePasswordConfirmation(password, confirmPassword)) {
            throw new BadRequestException("Passwords do not match.");
        }

        // Checks if the entered city is correct
        if(!UserValidator.validateCity(city)) {
            throw new BadRequestException("You have entered invalid city.");
        }

        // Checks if the entered age are correct
        if(!UserValidator.validateAge(age)) {
            throw new BadRequestException("You have entered invalid age.");
        }
    }



}
