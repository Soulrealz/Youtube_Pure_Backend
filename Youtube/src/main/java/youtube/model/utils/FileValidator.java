package youtube.model.utils;

import org.springframework.web.multipart.MultipartFile;
import youtube.exceptions.BadRequestException;


// Validating the max file size
public class FileValidator {
    private static double CONVERT_FROM_BYTES_TO_MB = 0.00000095367432;
    private static double MAX_SIZE = 10.0;

    public static void validateMaxSize(MultipartFile file){
        double fileSize = file.getSize()*CONVERT_FROM_BYTES_TO_MB;

        if(fileSize > MAX_SIZE) {
            throw new BadRequestException("This file is too big.");
        }
    }

    // Validating if the extension of the file is ONLY mp4
    public static void validateFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf('.');
        String extension = fileName.substring(index);

        if(!extension.equals(".mp4")) {
            throw new BadRequestException("This file extension is not allowed.");
        }
        System.out.println();
    }
}
