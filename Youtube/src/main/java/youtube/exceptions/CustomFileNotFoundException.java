package youtube.exceptions;

public class CustomFileNotFoundException extends RuntimeException {
    public CustomFileNotFoundException(String msg) {
        super(msg);
    }
}
