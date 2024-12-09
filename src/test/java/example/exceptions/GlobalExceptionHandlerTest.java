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
    void testHandleIllegalArgument() {
        // Arrange
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argument non valide : " + errorMessage, response.getBody());
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


    @Test
    void testHandleAllExceptions() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleAllExceptions(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Une erreur inattendue s'est produite", response.getBody());
    }

    @Test
    void testHandleProblemeNotFound() {
        // Arrange
        String messageErreur = "Le problème n'a pas été trouvé.";
        ProblemeNotFoundException exception = new ProblemeNotFoundException(messageErreur);

        // Act
        ResponseEntity<ErrorResponseDTO> reponse = globalExceptionHandler.handleProblemeNotFound(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode());
        assertNotNull(reponse.getBody());
        assertEquals("Problem Not Found", reponse.getBody().getErreur());
        assertEquals(messageErreur, reponse.getBody().getMessage());
    }



    @Test
    void testHandleIllegalStateException() {
        // Arrange
        String errorMessage = "Illegal state";
        IllegalStateException exception = new IllegalStateException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleIllegalStateException(exception);

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
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }


}
