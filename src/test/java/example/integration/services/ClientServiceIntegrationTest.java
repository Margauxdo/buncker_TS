package example.integration.services;

import example.entities.Client;
import example.services.ClientService;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();

        client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Main St")
                .telephoneExploitation("555-1234")
                .ville("Springfield")
                .PersonnelEtFonction("Manager")
                .build();
    }


    @Test
    public void testCreateClientSuccess() {
        // Act
        Client savedClient = clientService.createClient(client);

        // Assert
        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
        assertEquals("123 Main St", savedClient.getAdresse());
        assertEquals("555-1234", savedClient.getTelephoneExploitation());
        assertEquals("Springfield", savedClient.getVille());
        assertEquals("Manager", savedClient.getPersonnelEtFonction());
    }
    @Test
    public void testUpdateClientSuccess() {
        // Arrange: Save the client first
        Client savedClient = clientService.createClient(client);

        // Act
        savedClient.setName("Jane Doe");
        savedClient.setEmail("jane.doe@example.com");
        Client updatedClient = clientService.updateClient(savedClient.getId(), savedClient);

        // Assert
        assertNotNull(updatedClient);
        assertEquals("Jane Doe", updatedClient.getName());
        assertEquals("jane.doe@example.com", updatedClient.getEmail());
    }

    @Test
    public void testDeleteClientSuccess() {
        // Arrange
        Client savedClient = clientService.createClient(client);

        // Act
        clientService.deleteClient(savedClient.getId());

        // Assert
        Client deletedClient = clientService.getClientById(savedClient.getId());
        assertNull(deletedClient);
    }
    @Test
    public void testGetClientByIdSuccess() {
        // Arrange
        Client savedClient = clientService.createClient(client);

        // Act
        Client foundClient = clientService.getClientById(savedClient.getId());

        // Assert
        assertNotNull(foundClient);
        assertEquals(savedClient.getId(), foundClient.getId());
        assertEquals("John Doe", foundClient.getName());
    }

    @Test
    public void testGetAllClientsSuccess() {
        // Arrange
        Client client2 = Client.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .adresse("456 Main St")
                .telephoneExploitation("555-5678")
                .ville("Springfield")
                .PersonnelEtFonction("Supervisor")
                .build();

        clientService.createClient(client);
        clientService.createClient(client2);

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertEquals(2, clients.size(), "Expected only 2 clients in the repository");
    }

}






