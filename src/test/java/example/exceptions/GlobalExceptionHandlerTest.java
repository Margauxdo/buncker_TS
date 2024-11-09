package example.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRegleNotFoundException() {
        // Arrange
        String errorMessage = "Regle not found";
        RegleNotFoundException exception = new RegleNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleRegleNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        Object target = new Object();
        BindException bindException = new BindException(target, "objectName");
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field error message");
        bindException.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindException);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field error message", response.getBody().get("fieldName"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);

        when(path.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Field must not be null");
        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field must not be null", response.getBody().get("fieldName"));
    }


    @Test
    void testHandleAllUncaughtException() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleAllUncaughtException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error has occurred", response.getBody());
    }
}
