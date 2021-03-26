package youtube.model.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.exceptions.BadRequestException;

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


    public void validateUserInformation(){
        //checking if the email format is correct
        if(!validateEmail()) {
            throw new BadRequestException("You have entered invalid email.");
        }

        //checking if the entered confirmation password is correct
        if(!validatePasswordConfirmation()) {
            throw new BadRequestException("Passwords do not match.");
        }

        //check if the entered city is correct
        if(!validateCity()) {
            throw new BadRequestException("You have entered invalid city.");
        }

        //check if the entered age are correct
        if(!validateAge()) {
            throw new BadRequestException("You have entered invalid age.");
        }
    }


    //method for checking if the password confirmation is correct
    public boolean validatePasswordConfirmation() {
        return password.equals(confirmPassword);
    }

    //method for checking if the email is valid
    public boolean validateEmail () {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //method for checking if the city is valid
    public boolean validateCity() {
        return city.matches("[a-zA-Z]+");
    }

    //method for checking if the age are valid
    public boolean validateAge(){
        return age > 0 && age <= 100;
    }
}
