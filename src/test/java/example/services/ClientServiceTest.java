package example.services;

import example.DTO.ClientDTO;
import example.entity.Client;
import example.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*private ClientDTO createClientDTO() {
        return new ClientDTO(
                null,
                "John Doe",
                "123 Main St",
                "john.doe@example.com",
                "555-1234",
                "Springfield",
                "Manager",
                "Ramassage 1", null, null, null, null, null, null,
                "Standard", "Memo 1", "Memo 2",
                "Type 1", "C001",
                List.of(), // valiseIds
                List.of(), // problemeIds
                null,      // retourSecuriteId
                null       // regleId
        );
    }

    @Test
    public void testCreateClient_Success() {
        // Arrange
        ClientDTO clientDTO = createClientDTO();
        Client client = new Client();
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client savedClient = invocation.getArgument(0);
            savedClient.setId(1); // Simule un ID généré
            return savedClient;
        });

        // Act
        clientService.createClient(clientDTO);

        // Assert
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testUpdateClient_Success() {
        // Arrange
        int id = 1;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Old Name");
        existingClient.setEmail("old@example.com");

        ClientDTO updateDTO = createClientDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        clientService.updateClient(id, updateDTO);

        // Assert
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    public void testUpdateClient_NotFound() {
        int id = 1;
        ClientDTO updateDTO = createClientDTO();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> clientService.updateClient(id, updateDTO)
        );

        assertEquals("Client non trouvé", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteClient_Success() {
        int id = 1;
        Client client = new Client();
        client.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.deleteClient(id);

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    public void testDeleteClient_NotFound() {
        int id = 1;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> clientService.deleteClient(id)
        );

        assertEquals("Client non trouvé", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    public void testGetClientById_Success() {
        int id = 1;
        Client client = new Client();
        client.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    public void testGetClientById_NotFound() {
        int id = 1;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> clientService.getClientById(id)
        );

        assertEquals("Client non trouvé", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllClients_Success() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());

        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDTO> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllClients_EmptyList() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClientDTO> result = clientService.getAllClients();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findAll();
    }*/
}
