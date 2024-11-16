package example.services;

import example.entities.Client;
import example.repositories.ClientRepository;
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
    public void testUpdateClient_Success() {
        int id = 1;
        Client client = new Client();
        client.setId(id);

        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client savedClient = invocation.getArgument(0);
            savedClient.setId(id);
            return savedClient;
        });

        Client result = clientService.updateClient(id, client);

        Assertions.assertNotNull(result, "Client should not be null");
        Assertions.assertEquals(id, result.getId(), "Client ID should be updated");

        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, times(1)).save(client);
        verifyNoMoreInteractions(clientRepository);
    }


    @Test
    public void testUpdateClient_Failure_Exception() {
        int id = 1;
        Client client = new Client();
        client.setId(2);

        when(clientRepository.existsById(id)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(id, client);
        });

        Assertions.assertEquals("Client ID mismatch", exception.getMessage(), "Exception message should match expected error");

        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, never()).save(any(Client.class));
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void testUpdateClient_Success_WhenIdMatches() {
        int id = 1;
        Client client = new Client();
        client.setId(id);  // Id correspondant à celui de la requête

        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.updateClient(id, client);

        Assertions.assertNotNull(updatedClient, "Client should be updated successfully");
        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, times(1)).save(client);
    }


    @Test
    public void testDeleteClient_Success() {
        int id = 1;
        clientService.deleteClient(id);
        verify(clientRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(clientRepository);
    }
    @Test
    public void testDeleteClient_Failure_Exception(){
        int id = 1;
        doThrow(new RuntimeException("database error")).when(clientRepository).deleteById(id);
        Exception exception = assertThrows(RuntimeException.class,()-> {
            clientService.deleteClient(id);
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(clientRepository, times(1)).deleteById(id);
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
        Client result = clientService.getClientById(id);
        Assertions.assertNull(result,"Client should not be null");
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
        int id = 1;
        Client client = new Client();
        client.setId(id);

        when(clientRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientService.updateClient(id, client);
        });

        Assertions.assertEquals("Client not found with ID 1", exception.getMessage(),
                "The exception should correspond to 'Client not found with ID 1'");

        verify(clientRepository, times(1)).existsById(id);
        verify(clientRepository, never()).save(any(Client.class));
        verifyNoMoreInteractions(clientRepository);
    }


    @Test
    public void testUpdateClient_ClientNotFound() {
        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clientService.updateClient(1, new Client()));
    }



}
