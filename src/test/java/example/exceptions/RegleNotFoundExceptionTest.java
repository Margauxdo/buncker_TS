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
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the provided message");
    }

    @Test
    void testRegleNotFoundExceptionInstance() {
        // Arrange
        String expectedMessage = "Another test message";

        // Act
        RegleNotFoundException exception = new RegleNotFoundException(expectedMessage);

        // Assert
        assertNotNull(exception, "Exception should not be null");
        assertInstanceOf(RuntimeException.class, exception, "Exception should be an instance of RuntimeException");
    }

    @Test
    void testRegleNotFoundExceptionThrownAndCaught() {
        // Arrange
        String expectedMessage = "Test throw exception";

        // Act & Assert
        try {
            throw new RegleNotFoundException(expectedMessage);
        } catch (RegleNotFoundException e) {
            assertNotNull(e, "Caught exception should not be null");
            assertEquals(expectedMessage, e.getMessage(), "Caught exception message should match the thrown message");
        }
    }
}
