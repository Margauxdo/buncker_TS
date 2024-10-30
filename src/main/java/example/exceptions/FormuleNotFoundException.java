package example.exceptions;

public class FormuleNotFoundException extends RuntimeException {
    public FormuleNotFoundException(String message) {
        super(message);
    }
}
