package example.controller.entities;

import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
class ClientIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
        valiseRepository.deleteAll();
    }

    @Test
    public void testSaveClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("johndoe@example.com");

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("johndoe@example.com", savedClient.getEmail());
    }

    @Test
    public void testFindClientById() {
        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("janedoe@example.com");

        Client savedClient = clientRepository.save(client);
        Client foundClient = clientRepository.findById(savedClient.getId()).orElse(null);

        assertNotNull(foundClient);
        assertEquals("Jane Doe", foundClient.getName());
        assertEquals("janedoe@example.com", foundClient.getEmail());
    }

    @Test
    public void testUpdateClient() {
        Client client = new Client();
        client.setName("Initial Name");
        client.setEmail("initial@example.com");

        Client savedClient = clientRepository.save(client);
        savedClient.setName("Updated Name");

        Client updatedClient = clientRepository.save(savedClient);

        assertEquals("Updated Name", updatedClient.getName());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client();
        client.setName("To Be Deleted");
        client.setEmail("delete@example.com");

        Client savedClient = clientRepository.save(client);
        clientRepository.deleteById(savedClient.getId());

        assertFalse(clientRepository.findById(savedClient.getId()).isPresent());
    }

    @Test
    public void testUniqueConstraintOnEmail() {
        Client client1 = new Client();
        client1.setName("Client One");
        client1.setEmail("unique@example.com");

        Client client2 = new Client();
        client2.setName("Client Two");
        client2.setEmail("unique@example.com");

        clientRepository.save(client1);
        assertThrows(DataIntegrityViolationException.class, () -> clientRepository.save(client2));
    }

    @Test
    public void testSaveClientWithNullNameThrowsException() {
        // Arrange
        Client client = new Client();
        client.setEmail("nullname@example.com");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> clientRepository.saveAndFlush(client));
    }




    @Test
    public void testFindAllClient() {
        Client clientA = new Client();
        clientA.setName("Client A");
        clientA.setEmail("clientA@test.com");
        clientRepository.save(clientA);

        Client clientB = new Client();
        clientB.setName("Client B");
        clientB.setEmail("clientB@test.com");
        clientRepository.save(clientB);

        List<Client> clients = clientRepository.findAll();

        assertEquals(2, clients.size(), "Expected two clients to be saved.");
        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client A")));
        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client B")));
    }
}
