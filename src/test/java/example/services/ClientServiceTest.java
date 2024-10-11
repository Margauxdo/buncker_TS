package example.services;

import example.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testCreateClient_Success() {

    }
    @Test
    public void testCreateClient_Failure_Exception(){

    }
    @Test
    public void testUpdateClient_Success() {

    }
    @Test
    public void testUpdateClient_Failure_Exception(){

    }
    @Test
    public void testDeleteClient_Success() {

    }
    @Test
    public void testDeleteClient_Failure_Exception(){

    }
    @Test
    public void testGetClientById_Success() {

    }
    @Test
    public void testGetClientById_Failure_Exception() {

    }
    @Test
    public void testGetAllClients_Success() {

    }
    @Test
    public void testGetAllClients_Failure_Exception() {

    }
    @Test
    public void testNoInteractionWithClientRepository_Success() {

    }
    @Test
    public void testNoInteractionWithClientRepository_Failure_Exception() {

    }
}
