package youtube.model.utils;

import youtube.exceptions.BadRequestException;

public class VideoCredentialsValidator {
    public static void validate(String title, String description){
        if(title.length() <= 0) {
            throw new BadRequestException("This title is invalid.");
        }
        if(description.length() <= 0){
            throw new BadRequestException("This description is invalid.");
        }
    }
}
