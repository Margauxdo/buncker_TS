package example.controller;

import example.DTO.ClientDTO;
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

    // Test: Liste des clients - Succès
    @Test
    public void testViewClients() {
        when(clientService.getAllClients()).thenReturn(List.of(new ClientDTO()));

        String response = clientController.viewClients(model);

        assertEquals("clients/client_list", response);
        verify(model, times(1)).addAttribute(eq("clients"), any());
    }

    // Test: Voir un client par ID - Succès
    @Test
    public void testViewClientById_Success() {
        ClientDTO client = new ClientDTO();
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

    // Test: Création d'un client - Succès
    @Test
    public void testCreateClient_Success() {
        ClientDTO clientDTO = new ClientDTO();
        doNothing().when(clientService).createClient(clientDTO);

        String response = clientController.createClient(clientDTO, model);

        assertEquals("redirect:/clients/list", response);
        verify(clientService, times(1)).createClient(clientDTO);
    }

    // Test: Modification d'un client - Succès
    @Test
    public void testUpdateClient_Success() {
        ClientDTO clientDTO = new ClientDTO();
        doNothing().when(clientService).updateClient(eq(1), eq(clientDTO));

        String response = clientController.updateClient(1, clientDTO, model);

        assertEquals("redirect:/clients/list", response);
        verify(clientService, times(1)).updateClient(eq(1), eq(clientDTO));
    }

    // Test: Suppression d'un client - Succès
    @Test
    public void testDeleteClient_Success() {
        doNothing().when(clientService).deleteClient(1);

        String response = clientController.deleteClient(1, model);

        assertEquals("redirect:/clients/list", response);
        verify(clientService, times(1)).deleteClient(1);
    }
}
