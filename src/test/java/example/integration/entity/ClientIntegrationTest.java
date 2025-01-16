package example.integration.entity;

import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import jakarta.validation.*;
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
import java.util.Set;

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
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testSaveClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("johndoe@example.com");

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("johndoe@example.com", savedClient.getEmail());
    }

    @Test
    void testFindClientById() {
        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("janedoe@example.com");

        Client savedClient = clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());

        assertTrue(foundClient.isPresent());
        assertEquals("Jane Doe", foundClient.get().getName());
        assertEquals("janedoe@example.com", foundClient.get().getEmail());
    }

    @Test
    void testDeleteClient() {
        Client client = new Client();
        client.setName("To Be Deleted");
        client.setEmail("delete@example.com");

        Client savedClient = clientRepository.save(client);
        clientRepository.deleteById(savedClient.getId());

        assertFalse(clientRepository.findById(savedClient.getId()).isPresent());
    }

    @Test
    void testSaveClientWithValises() {
        // Arrange: Create a client
        Client client = new Client();
        client.setName("Client With Valises");
        client.setEmail("valise@example.com");

        // Arrange: Create a valise and set required fields
        Valise valise = new Valise();
        valise.setNumeroValise("V123");
        valise.setDescription("Sample description"); // Ensure this field is set
        valise.setClient(client);

        // Add the valise to the client's list
        client.setValises(List.of(valise));

        // Act: Save the client (cascading will save the valise)
        Client savedClient = clientRepository.save(client);

        // Assert: Validate client and valise were saved correctly
        assertNotNull(savedClient.getId(), "Client ID should not be null");
        assertEquals(1, savedClient.getValises().size(), "Client should have one valise");
        assertEquals("V123", savedClient.getValises().get(0).getNumeroValise(), "Valise number should match");
        assertEquals("Sample description", savedClient.getValises().get(0).getDescription(), "Valise description should match");
    }


    @Test
    void testUniqueConstraintOnEmail() {
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
    void testFindAllClients() {
        Client clientA = new Client();
        clientA.setName("Client A");
        clientA.setEmail("clientA@example.com");
        clientRepository.save(clientA);

        Client clientB = new Client();
        clientB.setName("Client B");
        clientB.setEmail("clientB@example.com");
        clientRepository.save(clientB);

        List<Client> clients = clientRepository.findAll();

        assertEquals(2, clients.size());
        assertTrue(clients.stream().anyMatch(client -> client.getName().equals("Client A")));
        assertTrue(clients.stream().anyMatch(client -> client.getName().equals("Client B")));
    }
}
