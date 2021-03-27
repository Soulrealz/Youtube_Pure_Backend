package youtube.model.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.exceptions.BadRequestException;
import youtube.model.validations.UserValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //checking if the email format is correct
        if(!UserValidation.validateEmail(email)) {
            throw new BadRequestException("You have entered invalid email.");
        }

        //checking if the entered confirmation password is correct
        if(!UserValidation.validatePasswordConfirmation(password, confirmPassword)) {
            throw new BadRequestException("Passwords do not match.");
        }

        //check if the entered city is correct
        if(!UserValidation.validateCity(city)) {
            throw new BadRequestException("You have entered invalid city.");
        }

        //check if the entered age are correct
        if(!UserValidation.validateAge(age)) {
            throw new BadRequestException("You have entered invalid age.");
        }
    }



}
