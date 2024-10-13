package example.exceptions;

public class RegleNotFoundException extends RuntimeException {
    public RegleNotFoundException(String message) {
        super(message);
    }
}
