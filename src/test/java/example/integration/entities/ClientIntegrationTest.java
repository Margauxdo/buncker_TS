    package example.integration.entities;

import example.entities.Client;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    public  void testFindClientById() {
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
        assertThrows(DataIntegrityViolationException.class, () ->
                clientRepository.save(client2));
    }
    @Test
    public void testSaveClientWithNullNameThrowsException() {
        Client client = new Client();
        client.setEmail("nullname@example.com");

        assertThrows(DataIntegrityViolationException.class, () -> {
            clientRepository.save(client);
        });
    }
    @Test
    public void testCascadeDeleteClientDeletesValises() {
        Client client = new Client();
        client.setName("Client with valises");
        client.setEmail("clientwithvalises@example.com");


        Valise valise = new Valise();
        valise.setClient(client);
        client.getValises().add(valise);

        clientRepository.saveAndFlush(client);


        clientRepository.delete(client);


        assertTrue(valiseRepository.findById(valise.getId()).isEmpty());
    }



}

