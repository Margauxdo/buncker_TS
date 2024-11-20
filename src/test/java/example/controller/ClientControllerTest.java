package example.controller;

import example.entity.Client;
import example.interfaces.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;

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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllClients_Success() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null));
        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<Client>> response = clientController.getAllClientsApi();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }


    @Test
    public void testGetAllClients_Failure() {
        when(clientService.getAllClients()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> clientController.getAllClientsApi());
    }


    @Test
    public void testGetClientById_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.getClientById(1)).thenReturn(client);

        ResponseEntity<Client> response = clientController.getClientByIdApi(1);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }


    @Test
    public void testGetClientById_NotFound() {
        when(clientService.getClientById(1)).thenReturn(null);

        ResponseEntity<Client> response = clientController.getClientByIdApi(1);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void testCreateClientApi_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        ResponseEntity<Client> response = clientController.createClientApi(client);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }


    @Test
    public void testCreateClientForm() {
        ConcurrentModel model = new ConcurrentModel();
        String viewName = clientController.createClientForm(model);

        // Assertions
        assertEquals("clients/client_create", viewName);  // Modifiez ici
        assertTrue(model.containsAttribute("client"));
    }



    @Test
    public void testCreateClient_Success() {
        Client client = new Client();
        when(clientService.createClient(any(Client.class))).thenReturn(client); // Simule le retour d'un client créé

        String response = clientController.createClient(client);

        // Assertions
        assertEquals("redirect:/clients/list", response); // Vérifie la redirection après la création
    }


    @Test
    public void testCreateClient_InvalidInput() {
        doThrow(new IllegalArgumentException("Données invalides"))
                .when(clientService).createClient(any(Client.class));

        Client client = new Client();
        assertThrows(IllegalArgumentException.class, () -> clientController.createClient(client));
    }


    @Test
    public void testDeleteClient_Success() {
        when(clientService.existsById(1)).thenReturn(true);
        doNothing().when(clientService).deleteClient(1);

        String response = clientController.deleteClient(1);

        // Assertions
        assertEquals("redirect:/clients/list", response);
    }


    @Test
    public void testDeleteClient_NotFound() {
        doThrow(new IllegalArgumentException("Client non trouvé"))
                .when(clientService).deleteClient(1);

        assertThrows(IllegalArgumentException.class, () -> clientController.deleteClient(1));
    }

    @Test
    public void testDeleteClientWithRelations() {
        Client client = new Client();
        client.setId(1);
        client.setName("Client With Relations");

        when(clientService.existsById(1)).thenReturn(true);
        doNothing().when(clientService).deleteClient(1);

        String response = clientController.deleteClient(1);

        assertEquals("redirect:/clients/list", response);
        verify(clientService, times(1)).deleteClient(1);
    }

}
