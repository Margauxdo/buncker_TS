package example.integration.repositories;

import example.entity.Client;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveClientSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
    }

    @Test
    public void testFindByIdSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        Client savedClient = clientRepository.save(client);

        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());
        assertTrue(foundClient.isPresent());
        assertEquals("John Doe", foundClient.get().getName());
    }

    @Test
    public void testFindByName() {
        Client client1 = new Client();
        client1.setName("Jane Doe");
        client1.setEmail("jane.doe@example.com");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("John Smith");
        client2.setEmail("john.smith@example.com");
        clientRepository.save(client2);

        List<Client> clients = clientRepository.findByName("Jane Doe");
        assertEquals(1, clients.size());
        assertEquals("jane.doe@example.com", clients.get(0).getEmail());
    }

    @Test
    public void testFindByEmail() {
        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("jane.doe@example.com");
        clientRepository.save(client);

        List<Client> clients = clientRepository.findByEmail("jane.doe@example.com");
        assertEquals(1, clients.size());
        assertEquals("Jane Doe", clients.get(0).getName());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("jane.doe@example.com");
        Client savedClient = clientRepository.save(client);

        clientRepository.deleteById(savedClient.getId());
        Optional<Client> deletedClient = clientRepository.findById(savedClient.getId());

        assertFalse(deletedClient.isPresent());
    }
    @Test
    public void testFindByNameNotFound() {
        List<Client> clients = clientRepository.findByName("Non Existent Name");
        assertTrue(clients.isEmpty());
    }
    @Test
    public void testFindByEmailNotFound() {
        List<Client> clients = clientRepository.findByEmail("nonexistent.email@example.com");
        assertTrue(clients.isEmpty());
    }
    @Test
    public void testSaveClientFailureDueToMissingEmail() {
        Client client = new Client();
        client.setName("John Doe");
        // Not setting email, expecting a failure

        assertThrows(Exception.class, () -> clientRepository.save(client));
    }
    @Test
    public void testUpdateClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        Client savedClient = clientRepository.save(client);


        savedClient.setName("John Updated");
        savedClient.setEmail("john.updated@example.com");

        Client updatedClient = clientRepository.save(savedClient);

        Optional<Client> foundClient = clientRepository.findById(updatedClient.getId());
        assertTrue(foundClient.isPresent());
        assertEquals("John Updated", foundClient.get().getName());
        assertEquals("john.updated@example.com", foundClient.get().getEmail());
    }
    @Test
    public void testFindAllClients() {
        Client client1 = new Client();
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Jane Doe");
        client2.setEmail("jane.doe@example.com");
        clientRepository.save(client2);

        List<Client> clients = clientRepository.findAll();

        assertNotNull(clients);
        assertTrue(clients.size() >= 2);

        clients.forEach(client -> System.out.println(client.getName() + " - " + client.getEmail()));

        assertEquals("John Doe", clients.get(0).getName());
        assertEquals("jane.doe@example.com", clients.get(1).getEmail());
    }






}
