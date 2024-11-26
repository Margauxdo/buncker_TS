package example.exceptions;

import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
        BindException bindException = new BindException(new Object(), "objectName");
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
    void testHandleIllegalArgument() {
        // Arrange
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argument non valide : " + errorMessage, response.getBody());
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
    void testHandleAllExceptions() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleAllExceptions(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Une erreur inattendue s'est produite", response.getBody());
    }

    @Test
    void testHandleProblemeNotFound() {
        // Arrange
        String errorMessage = "Probleme not found";
        ProblemeNotFoundException exception = new ProblemeNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleProblemeNotFound(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Le code HTTP attendu est 404 NOT FOUND.");
        assertNotNull(response.getBody(), "Le corps de la réponse ne doit pas être null.");
        assertEquals(errorMessage, response.getBody(), "Le message d'erreur attendu est incorrect.");
    }


    @Test
    void testHandleIllegalStateException() {
        // Arrange
        String errorMessage = "Illegal state";
        IllegalStateException exception = new IllegalStateException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalStateException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void testHandleEntityNotFoundException() {
        // Arrange
        String errorMessage = "Entity not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }


}
