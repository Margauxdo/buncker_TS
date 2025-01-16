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
        assertNotNull(exception, "The exception instance should not be null");
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message");
    }

    @Test
    void testConflictExceptionDefaultConstructor() {
        // Act
        ConflictException exception = new ConflictException("Default message");

        // Assert
        assertNotNull(exception, "The exception instance should not be null");
        assertTrue(exception instanceof RuntimeException, "Exception should be an instance of RuntimeException");
        assertEquals("Default message", exception.getMessage(), "The exception message should match the default message");
    }
}
