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

        ResponseEntity<List<Client>> response = clientController.getAllClientsApi(); // Utilisation du bon nom

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }

    @Test
    public void testGetClientById_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.getClientById(1)).thenReturn(client);

        ResponseEntity<Client> response = clientController.getClientByIdApi(1); // Utilisation du bon nom

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }





    @Test
    public void testGetAllClients_Failure() {
        when(clientService.getAllClients()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> clientController.getAllClientsApi());
    }



    @Test
    public void testGetClientById_NotFound() {
        when(clientService.getClientById(1)).thenReturn(null);

        ResponseEntity<Client> response = clientController.getClientByIdApi(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateClientApi_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.createClient(client)).thenReturn(client);

        ResponseEntity<Client> response = clientController.createClientApi(client); // Appel de l'API REST

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }



    @Test
    public void testCreateClient_InvalidInput_ThrowsException() {
        Client invalidClient = new Client(); // Simulez des données client invalides
        doThrow(new IllegalArgumentException("Données client invalides"))
                .when(clientService).createClient(invalidClient);

        // Vérifiez que l'exception est levée
        assertThrows(IllegalArgumentException.class, () -> clientController.createClient(invalidClient));
    }


    @Test
    public void testCreateClient_Conflict() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        doThrow(new IllegalStateException("Client conflict"))
                .when(clientService).createClient(client);

        assertThrows(IllegalStateException.class, () -> clientController.createClient(client));
    }



    @Test
    public void testUpdateClient_Success() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);
        when(clientService.updateClient(1, client)).thenReturn(client);

        String response = clientController.updateClient(1, client); // Appel de la méthode

        // Vérifiez la redirection
        assertEquals("redirect:/clients/list", response); // Adaptez si une autre redirection est attendue
    }


    @Test
    public void testUpdateClient_NotFound() {
        Client client = new Client(1, "Client 1", "Adresse 1", "client1@example.com", "0123456789", "Ville 1", null, null, null, null, null, null, null, null, null, null, null, null);

        when(clientService.updateClient(1, client)).thenReturn(null);

        String response = clientController.updateClient(1, client); // Appel de la méthode

        // Vérifiez que la méthode retourne la redirection attendue
        assertEquals("redirect:/clients/list", response);
    }




    @Test
    public void testUpdateClient_InvalidInput_ThrowsException() {
        Client invalidClient = new Client(); // Données invalides
        doThrow(new IllegalArgumentException("Invalid client data"))
                .when(clientService).updateClient(eq(1), any(Client.class));

        assertThrows(IllegalArgumentException.class, () -> clientController.updateClient(1, invalidClient));
    }


    @Test
    public void testDeleteClient_Success() {
        when(clientService.existsById(1)).thenReturn(true);
        doNothing().when(clientService).deleteClient(1);

        String response = clientController.deleteClient(1); // Appel de la méthode

        // Vérifiez que la redirection est correcte
        assertEquals("redirect:/clients/list", response); // Remplacez par la chaîne attendue si différente
    }



    @Test
    public void testDeleteClient_NotFound_ThrowsException() {
        doThrow(new IllegalArgumentException("Client not found"))
                .when(clientService).deleteClient(1);

        // Vérifiez que l'exception est levée
        assertThrows(IllegalArgumentException.class, () -> clientController.deleteClient(1));
    }




    @Test
    public void testDeleteClient_Failure_ThrowsException() {
        when(clientService.existsById(1)).thenReturn(true);

        doThrow(new RuntimeException("Database error"))
                .when(clientService).deleteClient(1);

        // Vérifiez que l'exception est levée
        assertThrows(RuntimeException.class, () -> clientController.deleteClient(1));
    }


    @Test
    public void testClientController(){
            assertNotNull(clientController);
            assertNotNull(clientService);
        }

}


