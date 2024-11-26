package example.exceptions;

public class ProblemeNotFoundException extends RuntimeException {
    public ProblemeNotFoundException(String message) {
        super(message); // Ensure the parent class is initialized with the message
    }
}
