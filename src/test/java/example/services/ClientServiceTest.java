package example.services;

import example.entity.Client;
import example.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Test
    public void testCreateClient_Success() {
        Client client = new Client();
        when(clientRepository.save(client)).thenReturn(client);
        Client result = clientService.createClient(client);
        Assertions.assertNotNull(result,"client ne doit pas être nul");
        verify(clientRepository, times(1)).save(client);
        verifyNoMoreInteractions(clientRepository);
    }
    @Test
    public void testCreateClient_Failure_Exception(){
        Client client = new Client();
        when(clientRepository.save(client)).thenThrow(new RuntimeException("database error"));
        Exception exception = assertThrows(RuntimeException.class,()-> {
            clientService.createClient(client);
        });
        Assertions.assertEquals("database error",exception.getMessage(),
                "Exception de creation");
        verify(clientRepository, times(1)).save(client);
        verifyNoMoreInteractions(clientRepository);

    }

    @Test
    public void testPartialUpdateClient() {
        int id = 1;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Original Name");
        existingClient.setEmail("original@example.com");

        Client update = new Client();
        update.setName("Updated Name");

        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Client updatedClient = clientService.updateClient(id, update);
        Assertions.assertNotNull(updatedClient, "Updated client should not be null");
        Assertions.assertEquals("Updated Name", updatedClient.getName(), "Name should be updated");
        Assertions.assertEquals("original@example.com", updatedClient.getEmail(), "Email should remain unchanged");

        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(existingClient);
        verifyNoMoreInteractions(clientRepository);
    }



    @Test
    public void testUpdateClient_Success() {
        // Arrange
        int id = 1;

        // Existing client in the database
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Old Name");
        existingClient.setEmail("old@example.com");

        // Updates to be applied
        Client updatedClient = new Client();
        updatedClient.setId(id); // Same ID as the path variable
        updatedClient.setName("Updated Name");
        updatedClient.setEmail("updated@example.com");

        // Mock the repository behavior
        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = clientService.updateClient(id, updatedClient);

        // Assert
        Assertions.assertNotNull(result, "Client should not be null");
        Assertions.assertEquals(id, result.getId(), "Client ID should match");
        Assertions.assertEquals("Updated Name", result.getName(), "Name should be updated");
        Assertions.assertEquals("updated@example.com", result.getEmail(), "Email should be updated");

        // Verify interactions
        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(argThat(client ->
                client.getId() == id &&
                        "Updated Name".equals(client.getName()) &&
                        "updated@example.com".equals(client.getEmail())
        ));
        verifyNoMoreInteractions(clientRepository);
    }



    @Test
    public void testUpdateClient_NotFound() {
        int id = 1;
        Client updatedClient = new Client();
        updatedClient.setId(id);

        when(clientRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> clientService.updateClient(id, updatedClient)
        );

        Assertions.assertEquals("Client not found with ID " + id, exception.getMessage());
        verify(clientRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(clientRepository);
    }



    @Test
    public void testUpdateClient_Failure_Exception() {
        int id = 1;
        Client client = new Client();
        client.setId(2); // ID incohérent
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(id, client);
        });
        Assertions.assertEquals("Client ID mismatch", exception.getMessage(), "Exception message should match expected error");
        verify(clientRepository, never()).existsById(anyInt());
        verify(clientRepository, never()).findById(anyInt());
        verify(clientRepository, never()).save(any(Client.class));
        verifyNoMoreInteractions(clientRepository);
    }



    @Test
    public void testUpdateClient_Success_WhenIdMatches() {
        // Arrange
        int id = 1;

        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Old Name");
        existingClient.setEmail("old@example.com");

        Client clientToUpdate = new Client();
        clientToUpdate.setId(id);
        clientToUpdate.setName("Updated Name");
        clientToUpdate.setEmail("updated@example.com");

        // Mock du repository
        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client savedClient = invocation.getArgument(0);
            System.out.println("Saving client: " + savedClient);
            return savedClient;
        });

        // Act
        Client updatedClient = clientService.updateClient(id, clientToUpdate);

        // Assert
        Assertions.assertNotNull(updatedClient, "Client should not be null");
        Assertions.assertEquals(id, updatedClient.getId(), "Client ID should match");
        Assertions.assertEquals("Updated Name", updatedClient.getName(), "Name should be updated");
        Assertions.assertEquals("updated@example.com", updatedClient.getEmail(), "Email should be updated");

        // Vérification des interactions
        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(argThat(client ->
                client.getId() == id &&
                        "Updated Name".equals(client.getName()) &&
                        "updated@example.com".equals(client.getEmail())
        ));
        verifyNoMoreInteractions(clientRepository);
    }



    @Test
    public void testDeleteClient_Success() {
        int id = 1;

        Client existingClient = new Client();
        existingClient.setId(id);
        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        clientService.deleteClient(id);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).delete(existingClient);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void testDeleteClient_Failure_Exception() {
        int id = 1;

        Client client = new Client();
        client.setId(id);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("database error")).when(clientRepository).delete(client);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientService.deleteClient(id);
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).delete(client);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void testGetClientById_Success() {
        int id = 1;
        Client client = new Client();
        client.setId(id);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        Client result = clientService.getClientById(id);
        Assertions.assertNotNull(result,"Client should not be null");
        Assertions.assertEquals(id,result.getId(),"id du client devrait correspondre");
        verify(clientRepository, times(1)).findById(id);
        verifyNoMoreInteractions(clientRepository);
    }
    @Test
    public void testGetClientById_Failure_Exception() {
        int id = 1;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> clientService.getClientById(id)
        );
        Assertions.assertEquals("Client not found with ID " + id, exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void testGetAllClients_Success() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());

        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> result = clientService.getAllClients();
        Assertions.assertEquals(2, result.size(),"Liste des clients devrait contenir deux elements");
        verify(clientRepository, times(1)).findAll();
        verifyNoMoreInteractions(clientRepository);
    }
    @Test
    public void testGetAllClients_Failure_Exception() {
        when(clientRepository.findAll()).thenThrow(new RuntimeException("database error"));
        Exception exception = assertThrows(RuntimeException.class,()-> {
            clientService.getAllClients();
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(clientRepository, times(1)).findAll();
        verifyNoMoreInteractions(clientRepository);

    }
    @Test
    public void testNoInteractionWithClientRepository_Success() {

        verifyNoInteractions(clientRepository);
    }
    @Test
    public void testNoInteractionWithClientRepository_Failure_Exception() {
        // Arrange
        int id = 1;
        Client client = new Client();
        client.setId(id);

        // Simuler le comportement attendu du mock
        when(clientRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.updateClient(id, client);
        });

        // Vérification du message d'exception
        Assertions.assertEquals("Client not found with ID 1", exception.getMessage(),
                "The exception should correspond to 'Client not found with ID 1'");

        // Vérifications des interactions avec le mock
        verify(clientRepository, times(1)).existsById(id); // Vérifie l'appel à existsById
        verify(clientRepository, never()).save(any(Client.class)); // Vérifie que save() n'a pas été appelé
        verifyNoMoreInteractions(clientRepository); // Vérifie qu'il n'y a pas d'autres interactions
    }




    @Test
    public void testUpdateClient_ClientNotFound() {
        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clientService.updateClient(1, new Client()));
    }



}
