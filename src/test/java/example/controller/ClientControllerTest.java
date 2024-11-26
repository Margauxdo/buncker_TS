package example.controller;

import example.entity.Client;
import example.interfaces.IClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private IClientService clientService;

    @Mock
    private Model model;

    @InjectMocks
    private ClientController clientController;

    // Test: Création d'un client - Succès
    @Test
    public void testCreateClient_Success() {
        Client client = new Client();
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        String response = clientController.createClient(client);

        assertEquals("redirect:/clients/clients_list", response);
        verify(clientService, times(1)).createClient(client); // Vérifie l'appel du service
    }

    // Test: Suppression d'un client - Succès
    @Test
    public void testDeleteClient_Success() {
        doNothing().when(clientService).deleteClient(1);

        String response = clientController.deleteClient(1);

        assertEquals("redirect:/clients/clients_list", response);
        verify(clientService, times(1)).deleteClient(1); // Vérifie l'appel du service
    }

    // Test: Liste des clients - Succès
    @Test
    public void testViewClients() {
        when(clientService.getAllClients()).thenReturn(List.of(new Client()));

        String response = clientController.viewClients(model);

        assertEquals("clients/client_list", response);
        verify(model, times(1)).addAttribute(eq("clients"), any());
    }

    // Test: Voir un client par ID - Succès
    @Test
    public void testViewClientById_Success() {
        Client client = new Client();
        when(clientService.getClientById(1)).thenReturn(client);

        String response = clientController.viewClientById(1, model);

        assertEquals("clients/client_detail", response);
        verify(model, times(1)).addAttribute("client", client);
    }

    // Test: Voir un client par ID - Non trouvé
    @Test
    public void testViewClientById_NotFound() {
        when(clientService.getClientById(1)).thenReturn(null);

        String response = clientController.viewClientById(1, model);

        assertEquals("clients/error", response);
        verify(model, times(1)).addAttribute(eq("errorMessage"), any());
    }

    // Test: Modifier un client - Succès
    @Test
    public void testEditClientForm_Success() {
        Client client = new Client();
        when(clientService.getClientById(1)).thenReturn(client);

        String response = clientController.editClientForm(1, model);

        assertEquals("clients/client_edit", response);
        verify(model, times(1)).addAttribute("client", client);
    }

    // Test: Modifier un client - Non trouvé
    @Test
    public void testEditClientForm_NotFound() {
        when(clientService.getClientById(1)).thenReturn(null);

        String response = clientController.editClientForm(1, model);

        assertEquals("clients/error", response);
        verify(clientService, times(1)).getClientById(1);
    }

    // Test: Mise à jour d'un client - Succès
    @Test
    public void testUpdateClient_Success() {
        Client client = new Client();
        when(clientService.updateClient(1, client)).thenReturn(client);

        String response = clientController.updateClient(1, client);

        assertEquals("redirect:/clients/clients_list", response);
        verify(clientService, times(1)).updateClient(1, client);
    }

    // Test: Formulaire de création d'un client
    @Test
    public void testCreateClientForm() {
        String response = clientController.createClientForm(model);

        assertEquals("clients/client_create", response);
        verify(model, times(1)).addAttribute(eq("client"), any(Client.class));
    }
}
