package example.exceptions;

import example.DTO.ErrorResponseDTO;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleClientNotFoundException() {
        // Arrange
        String expectedMessage = "Client not found";
        ClientNotFoundException exception = new ClientNotFoundException(expectedMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleClientNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Client Not Found", response.getBody().getErreur());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    void testHandleRegleNotFoundException() {
        // Arrange
        String expectedMessage = "Rule not found";
        RegleNotFoundException exception = new RegleNotFoundException(expectedMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleRegleNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Rule Not Found", response.getBody().getErreur());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    void testHandleConflictException() {
        // Arrange
        String expectedMessage = "Conflict occurred";
        ConflictException exception = new ConflictException(expectedMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleConflictException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflict", response.getBody().getErreur());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);

        when(path.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);
        when(exception.getConstraintViolations()).thenReturn(violations);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("fieldName: must not be null"));
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        BindException bindException = new BindException(new Object(), "objectName");
        FieldError fieldError = new FieldError("objectName", "fieldName", "must not be null");
        bindException.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindException);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("fieldName=must not be null"));
    }

    @Test
    void testHandleAllExceptions() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleAllExceptions(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getErreur());
        assertEquals("Une erreur inattendue s'est produite.", response.getBody().getMessage());
    }
}
