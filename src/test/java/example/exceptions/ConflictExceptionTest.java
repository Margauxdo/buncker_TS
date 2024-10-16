package example.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConflictExceptionTest {

    @Test
    void testConflictExceptionMessage() {
        // Arrange
        String expectedMessage = "This is a conflict error";

        // Act
        ConflictException exception = new ConflictException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConflictExceptionDefaultConstructor() {
        // Act
        ConflictException exception = new ConflictException("Default message");

        // Assert
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException, "Exception should be a RuntimeException");
    }
}

