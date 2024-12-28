package example.exceptions;

import example.DTO.ErrorResponseDTO;
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
    void testHandleRegleNotFoundException() {
        // Arrange
        String messageErreur = "La règle n'a pas été trouvée.";
        RegleNotFoundException exception = new RegleNotFoundException(messageErreur);

        // Act
        ResponseEntity<ErrorResponseDTO> reponse = globalExceptionHandler.handleRegleNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
        assertNotNull(reponse.getBody());
        assertEquals("Rule Not Found", reponse.getBody().getErreur());
        assertEquals(messageErreur, reponse.getBody().getMessage());
    }


    @Test
    void testHandleValidationExceptions() {
        // Arrange
        BindException bindException = new BindException(new Object(), "objectName");
        FieldError fieldError = new FieldError("objectName", "nomChamp", "Le champ est invalide.");
        bindException.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindException);

        // Act
        ResponseEntity<ErrorResponseDTO> reponse = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, reponse.getStatusCode());
        assertNotNull(reponse.getBody());
        assertTrue(reponse.getBody().getMessage().contains("nomChamp"));
        assertTrue(reponse.getBody().getMessage().contains("Le champ est invalide."));
    }



    @Test
    void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path chemin = mock(jakarta.validation.Path.class);

        when(chemin.toString()).thenReturn("nomChamp");
        when(violation.getPropertyPath()).thenReturn(chemin);
        when(violation.getMessage()).thenReturn("Le champ ne doit pas être nul.");
        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));

        // Act
        ResponseEntity<ErrorResponseDTO> reponse = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, reponse.getStatusCode());
        assertNotNull(reponse.getBody());
        assertTrue(reponse.getBody().getMessage().contains("nomChamp"));
        assertTrue(reponse.getBody().getMessage().contains("Le champ ne doit pas être nul."));
    }





}
