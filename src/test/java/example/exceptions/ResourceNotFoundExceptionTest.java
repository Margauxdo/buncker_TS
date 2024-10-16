package example.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionMessage() {
        // Arrange
        String expectedMessage = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionIsRuntimeException() {
        // Arrange
        String message = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertTrue(exception instanceof RuntimeException, "Exception should be a RuntimeException");
    }

    @Test
    void testResourceNotFoundExceptionIsAnnotated() {
        // Act
        ResponseStatus responseStatus = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);

        // Assert
        assertNotNull(responseStatus, "ResponseStatus annotation should be present");
        assertEquals(HttpStatus.NOT_FOUND, responseStatus.value(), "HTTP status should be NOT_FOUND");
    }
}

