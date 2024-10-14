package example.controller;

import example.entities.Client;
import example.interfaces.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private IClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients_Success() {
        // Arrange
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null));
        when(clientService.getAllClients()).thenReturn(clients);

        // Act
        ResponseEntity<List<Client>> response = clientController.getAllClients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }

    @Test
    void testGetClientById_Success() {
        // Arrange
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.getClientById(1)).thenReturn(client);

        // Act
        ResponseEntity<Client> response = clientController.getClientById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testGetClientById_NotFound() {
        // Arrange
        when(clientService.getClientById(1)).thenReturn(null);

        // Act
        ResponseEntity<Client> response = clientController.getClientById(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateClient_Success() {
        // Arrange
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.createClient(client)).thenReturn(client);

        // Act
        ResponseEntity<Client> response = clientController.createClient(client);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testUpdateClient_Success() {
        // Arrange
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.updateClient(1, client)).thenReturn(client);

        // Act
        ResponseEntity<Client> response = clientController.updateClient(1, client);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testUpdateClient_NotFound() {
        // Arrange
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.updateClient(1, client)).thenReturn(null);

        // Act
        ResponseEntity<Client> response = clientController.updateClient(1, client);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteClient_Success() {
        // Arrange
        doNothing().when(clientService).deleteClient(1);

        // Act
        ResponseEntity<Void> response = clientController.deleteClient(1);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




}
