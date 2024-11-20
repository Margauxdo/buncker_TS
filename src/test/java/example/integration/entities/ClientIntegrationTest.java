    package example.integration.entities;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
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
    @Autowired
    private ProblemeRepository problemeRepository;

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

        assertThrows(ConstraintViolationException.class, () -> {
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
    @Test
    public void testFindAllClient() {
        Client clientA = new Client();
        clientA.setName("Client A");
        clientA.setEmail("clientA@test.com");
        clientA.setAdresse("Adresse A");
        clientRepository.save(clientA);

        Client clientB = new Client();
        clientB.setName("Client B");
        clientB.setEmail("clientB@test.com");
        clientB.setAdresse("Adresse B");
        clientRepository.save(clientB);

        List<Client> clients = clientRepository.findAll();

        Assertions.assertEquals(2, clients.size(), "Il devrait y avoir deux clients enregistrés.");

        Assertions.assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client A") && c.getEmail().equals("clientA@test.com")));
        Assertions.assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client B") && c.getEmail().equals("clientB@test.com")));
    }

    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Optional<Client> found = clientRepository.findById(client.getId());
        assertTrue(found.isPresent());
        assertEquals("Client Test", found.get().getName());
        assertEquals("client@test.com", found.get().getEmail());
    }


    @Test
    void testClientRelationshipWithProbleme() {
        // Arrange
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Probleme probleme = new Probleme();
        probleme.setClient(client);
        probleme.setDescriptionProbleme("Problème de test");
        probleme.setDetailsProbleme("Détails du problème"); // Ajoutez ce champ obligatoire
        problemeRepository.save(probleme);

        // Act
        List<Probleme> problemes = problemeRepository.findByClientId(client.getId());

        // Assert
        assertFalse(problemes.isEmpty(), "La liste des problèmes ne doit pas être vide");
        assertEquals(client.getId(), problemes.get(0).getClient().getId());
        assertEquals("Problème de test", problemes.get(0).getDescriptionProbleme());
    }
    @Test
    public void testCascadeSaveAndDeleteClient() {
        Client client = new Client();
        client.setName("Client with Valise");
        client.setEmail("test@relation.com");

        Valise valise = new Valise();
        valise.setClient(client);
        client.getValises().add(valise);

        clientRepository.save(client);

        Optional<Client> savedClient = clientRepository.findById(client.getId());
        Assertions.assertTrue(savedClient.isPresent());
        Assertions.assertEquals(1, savedClient.get().getValises().size());

        clientRepository.delete(client);
        Assertions.assertTrue(valiseRepository.findAll().isEmpty());
    }








}

