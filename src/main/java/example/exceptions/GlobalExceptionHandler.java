package example.exceptions;

import example.DTO.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleClientNotFoundException(ClientNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Client Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FormuleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleFormuleNotFoundException(FormuleNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Formule Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProblemeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProblemeNotFoundException(ProblemeNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Problem Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRegleNotFoundException(RegleNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Rule Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Conflict",
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        ex.getConstraintViolations().forEach(violation ->
                message.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ")
        );
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Validation Error",
                message.toString(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Validation Error",
                errors.toString(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                "Internal Server Error",
                "Une erreur inattendue s'est produite.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
