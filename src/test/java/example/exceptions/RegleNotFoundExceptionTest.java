package example.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RegleNotFoundExceptionTest {

    @Test
    void testRegleNotFoundExceptionMessage() {
        // Arrange
        String expectedMessage = "Ruler not found";

        // Act
        RegleNotFoundException exception = new RegleNotFoundException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testRegleNotFoundExceptionInstance() {
        // Arrange
        String expectedMessage = "Another test message";

        // Act
        RegleNotFoundException exception = new RegleNotFoundException(expectedMessage);

        // Assert
        assertNotNull(exception);
        assertInstanceOf(RuntimeException.class, exception, "Exception should be a RuntimeException");
    }
}

