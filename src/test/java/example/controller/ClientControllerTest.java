package example.controller;

import example.entities.Client;
import example.interfaces.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null));
        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<Client>> response = clientController.getAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }

    @Test
    void testGetAllClients_Failure() {
        when(clientService.getAllClients()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> clientController.getAllClients());
    }

    @Test
    void testGetClientById_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.getClientById(1)).thenReturn(client);

        ResponseEntity<Client> response = clientController.getClientById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientService.getClientById(1)).thenReturn(null);

        ResponseEntity<Client> response = clientController.getClientById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateClient_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.createClient(client)).thenReturn(client);

        ResponseEntity<Client> response = clientController.createClient(client);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testCreateClient_InvalidInput() {
        Client invalidClient = new Client(); // Simulate invalid client data
        when(clientService.createClient(invalidClient)).thenThrow(new IllegalArgumentException("Invalid client data"));

        ResponseEntity<Client> response = clientController.createClient(invalidClient);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testCreateClient_Conflict() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.createClient(client)).thenThrow(new IllegalStateException("Client conflict"));

        ResponseEntity<Client> response = clientController.createClient(client);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    void testUpdateClient_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.updateClient(1, client)).thenReturn(client);

        ResponseEntity<Client> response = clientController.updateClient(1, client);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testUpdateClient_NotFound() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.updateClient(1, client)).thenReturn(null);

        ResponseEntity<Client> response = clientController.updateClient(1, client);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateClient_InvalidInput() {
        Client invalidClient = new Client(); // Simulate invalid client data
        when(clientService.updateClient(eq(1), any(Client.class)))
                .thenThrow(new IllegalArgumentException("Invalid client data"));

        ResponseEntity<Client> response = clientController.updateClient(1, invalidClient);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }




    @Test
    void testDeleteClient_Success() {
        doNothing().when(clientService).deleteClient(1);

        ResponseEntity<Void> response = clientController.deleteClient(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteClient_NotFound() {
        doThrow(new IllegalArgumentException("Client not found")).when(clientService).deleteClient(1);

        ResponseEntity<Void> response = clientController.deleteClient(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testDeleteClient_Failure() {
        doThrow(new RuntimeException("Database error")).when(clientService).deleteClient(1);

        ResponseEntity<Void> response = clientController.deleteClient(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
