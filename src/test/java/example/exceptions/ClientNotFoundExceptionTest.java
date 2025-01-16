package example.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientNotFoundExceptionTest {

    @Test
    void testClientNotFoundExceptionConstructor() {
        // Arrange
        String expectedMessage = "Client not found";

        // Act
        ClientNotFoundException exception = new ClientNotFoundException(expectedMessage);

        // Assert
        assertNotNull(exception, "Exception should not be null");
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the provided message");
    }

    @Test
    void testClientNotFoundExceptionWithoutMessage() {
        // Act
        ClientNotFoundException exception = new ClientNotFoundException(null);

        // Assert
        assertNotNull(exception, "Exception should not be null even without a message");
        assertEquals(null, exception.getMessage(), "Message should be null when none is provided");
    }
}
