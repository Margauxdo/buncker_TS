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

    private ClientDTO createClientDTO() {
        return ClientDTO.builder()
                .id(1)
                .name("John Doe")
                .adresse("123 Main St")
                .email("john.doe@example.com")
                .telephoneExploitation("555-1234")
                .ville("Springfield")
                .personnelEtFonction("Manager")
                .ramassage1("Ramassage 1")
                .envoiparDefaut("Standard")
                .memoRetourSecurite1("Memo 1")
                .memoRetourSecurite2("Memo 2")
                .typeSuivie("Type 1")
                .codeClient("C001")
                .problemeIds(List.of())
                .retourSecuriteIds(List.of())
                .valiseIds(List.of())
                .build();
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
        ClientDTO result = clientService.createClient(clientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
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
        existingClient.setValises(new ArrayList<>()); // Initialisation des valises
        existingClient.setRetourSecurites(new ArrayList<>()); // Initialisation des retours sécurité

        ClientDTO updateDTO = createClientDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ClientDTO result = clientService.updateClient(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
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

        assertEquals("Client introuvable avec l'ID : 1", exception.getMessage());
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

        assertEquals("Client introuvable avec l'ID : 1", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
    }


    @Test
    public void testGetClientById_Success() {
        // Arrange
        int id = 1;
        Client client = new Client();
        client.setId(id);
        client.setValises(new ArrayList<>()); // Initialisation des valises
        client.setRetourSecurites(new ArrayList<>()); // Initialisation des retours sécurité

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        // Act
        ClientDTO result = clientService.getClientById(id);

        // Assert
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

        assertEquals("Client introuvable avec l'ID : 1", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
    }


    @Test
    public void testGetAllClients_Success() {
        // Arrange
        List<Client> clients = new ArrayList<>();
        Client client1 = new Client();
        client1.setValises(new ArrayList<>()); // Initialisation des valises
        client1.setRetourSecurites(new ArrayList<>()); // Initialisation des retours sécurité

        Client client2 = new Client();
        client2.setValises(new ArrayList<>()); // Initialisation des valises
        client2.setRetourSecurites(new ArrayList<>()); // Initialisation des retours sécurité

        clients.add(client1);
        clients.add(client2);

        when(clientRepository.findAll()).thenReturn(clients);

        // Act
        List<ClientDTO> result = clientService.getAllClients();

        // Assert
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
    }
}
