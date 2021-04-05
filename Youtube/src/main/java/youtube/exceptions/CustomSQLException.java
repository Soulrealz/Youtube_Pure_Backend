package youtube.exceptions;

public class CustomSQLException extends RuntimeException {
    public CustomSQLException(String msg) {
        super(msg);
    }
}
