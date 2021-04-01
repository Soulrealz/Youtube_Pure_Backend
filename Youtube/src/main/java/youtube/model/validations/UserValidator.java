package youtube.model.validations;

import youtube.exceptions.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //method for checking if the email is valid
    public static boolean validateEmail (String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    //method for checking if the city is valid
    public static boolean validateCity(String city) {
        return city.matches("[a-zA-Z]+");
    }

    //method for checking if the age are valid
    public static boolean validateAge(int age){
        return age > 0 && age <= 100;
    }

    //method for checking if the password confirmation is correct
    public static boolean validatePasswordConfirmation(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
