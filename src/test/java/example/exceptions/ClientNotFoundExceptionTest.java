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
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
}

