package example.integration.entity;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ProblemeIntegrationTest {

    @Autowired
    private ProblemeRepository problemeRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testCreateProbleme() {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Test Description");
        probleme.setDetailsProbleme("Test Details");

        // Act
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);

        // Assert
        assertNotNull(savedProbleme.getId());
        assertEquals("Test Description", savedProbleme.getDescriptionProbleme());
        assertEquals("Test Details", savedProbleme.getDetailsProbleme());
    }

    @Test
    void testReadProbleme() {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Read Description");
        probleme.setDetailsProbleme("Read Details");
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);

        // Act
        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());

        // Assert
        assertTrue(foundProbleme.isPresent());
        assertEquals("Read Description", foundProbleme.get().getDescriptionProbleme());
        assertEquals("Read Details", foundProbleme.get().getDetailsProbleme());
    }

    @Test
    void testUpdateProbleme() {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Old Description");
        probleme.setDetailsProbleme("Old Details");
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);

        // Act
        savedProbleme.setDescriptionProbleme("Updated Description");
        savedProbleme.setDetailsProbleme("Updated Details");
        Probleme updatedProbleme = problemeRepository.saveAndFlush(savedProbleme);

        // Assert
        assertEquals("Updated Description", updatedProbleme.getDescriptionProbleme());
        assertEquals("Updated Details", updatedProbleme.getDetailsProbleme());
    }

    @Test
    void testDeleteProbleme() {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Delete Description");
        probleme.setDetailsProbleme("Delete Details");
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);

        // Act
        problemeRepository.delete(savedProbleme);
        Optional<Probleme> deletedProbleme = problemeRepository.findById(savedProbleme.getId());

        // Assert
        assertTrue(deletedProbleme.isEmpty());
    }


    @Test
    void testProblemeWithClients() {
        // Arrange
        Client client1 = new Client();
        client1.setName("Client One");
        client1.setEmail("client1@example.com");
        Client savedClient1 = clientRepository.saveAndFlush(client1);

        Client client2 = new Client();
        client2.setName("Client Two");
        client2.setEmail("client2@example.com");
        Client savedClient2 = clientRepository.saveAndFlush(client2);

        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Description with Clients");
        probleme.setDetailsProbleme("Details with Clients");
        probleme.getClients().add(savedClient1);
        probleme.getClients().add(savedClient2);
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);

        // Act
        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());

        // Assert
        assertTrue(foundProbleme.isPresent());
        assertEquals(2, foundProbleme.get().getClients().size());
    }
}
