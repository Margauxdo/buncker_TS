package example.integration.repositories;

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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class ProblemeRepositoryIntegrationTest {

    @Autowired
    private ProblemeRepository problemeRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Valise valise;
    private Client client;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();

        client = Client.builder()
                .name("Test Client")
                .email("client@example.com")
                .build();
        client = clientRepository.save(client);

        valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .client(client)
                .build();
        valise = valiseRepository.save(valise);
    }

    @Test
    public void testSaveProbleme() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .valise(valise)
                .client(client)
                .build();

        Probleme savedProbleme = problemeRepository.save(probleme);

        assertNotNull(savedProbleme.getId());
        assertEquals("Test Description", savedProbleme.getDescriptionProbleme());
        assertEquals("Test Details", savedProbleme.getDetailsProbleme());
        assertEquals(valise.getId(), savedProbleme.getValise().getId());
        assertEquals(client.getId(), savedProbleme.getClient().getId());
    }

    @Test
    public void testFindById() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Another Description")
                .detailsProbleme("Another Details")
                .valise(valise)
                .client(client)
                .build();

        Probleme savedProbleme = problemeRepository.save(probleme);

        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());
        assertTrue(foundProbleme.isPresent());
        assertEquals("Another Description", foundProbleme.get().getDescriptionProbleme());
    }

    @Test
    public void testUpdateProbleme() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Initial Description")
                .detailsProbleme("Initial Details")
                .valise(valise)
                .client(client)
                .build();

        Probleme savedProbleme = problemeRepository.save(probleme);

        savedProbleme.setDescriptionProbleme("Updated Description");
        Probleme updatedProbleme = problemeRepository.save(savedProbleme);

        assertEquals("Updated Description", updatedProbleme.getDescriptionProbleme());
    }

    @Test
    public void testDeleteProbleme() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("To Be Deleted")
                .detailsProbleme("Details To Be Deleted")
                .valise(valise)
                .client(client)
                .build();

        Probleme savedProbleme = problemeRepository.save(probleme);
        problemeRepository.delete(savedProbleme);

        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());
        assertFalse(foundProbleme.isPresent());
    }

    @Test
    public void testFindAll() {
        Probleme probleme1 = Probleme.builder()
                .descriptionProbleme("Description 1")
                .detailsProbleme("Details 1")
                .valise(valise)
                .client(client)
                .build();

        Probleme probleme2 = Probleme.builder()
                .descriptionProbleme("Description 2")
                .detailsProbleme("Details 2")
                .valise(valise)
                .client(client)
                .build();

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        Iterable<Probleme> problemes = problemeRepository.findAll();
        assertEquals(2, ((Collection<?>) problemes).size());
    }

    @Test
    public void testFindByValise() {
        Probleme probleme1 = Probleme.builder()
                .descriptionProbleme("Description 1")
                .detailsProbleme("Details 1")
                .valise(valise)
                .client(client)
                .build();

        Probleme probleme2 = Probleme.builder()
                .descriptionProbleme("Description 2")
                .detailsProbleme("Details 2")
                .valise(valise)
                .client(client)
                .build();

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        List<Probleme> foundProblemes = problemeRepository.findByValise(valise);

        assertEquals(2, foundProblemes.size());
        assertTrue(foundProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Description 1")));
        assertTrue(foundProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Description 2")));
    }

    @Test
    public void testExistsByDescriptionProblemeAndDetailsProbleme() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Unique Description")
                .detailsProbleme("Unique Details")
                .valise(valise)
                .client(client)
                .build();

        problemeRepository.save(probleme);

        boolean exists = problemeRepository.existsByDescriptionProblemeAndDetailsProbleme("Unique Description", "Unique Details");
        assertTrue(exists);

        boolean notExists = problemeRepository.existsByDescriptionProblemeAndDetailsProbleme("Nonexistent Description", "Nonexistent Details");
        assertFalse(notExists);
    }

    @Test
    public void testDeleteAllByValise() {
        Probleme probleme1 = Probleme.builder()
                .descriptionProbleme("To Delete 1")
                .detailsProbleme("Details 1")
                .valise(valise)
                .client(client)
                .build();

        Probleme probleme2 = Probleme.builder()
                .descriptionProbleme("To Delete 2")
                .detailsProbleme("Details 2")
                .valise(valise)
                .client(client)
                .build();

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        problemeRepository.deleteAllByValise(valise);

        List<Probleme> foundProblemes = problemeRepository.findByValise(valise);
        assertTrue(foundProblemes.isEmpty());
    }

    @Test
    public void testFindByClientId() {
        Probleme probleme1 = Probleme.builder()
                .descriptionProbleme("Client Problem 1")
                .detailsProbleme("Details 1")
                .valise(valise)
                .client(client)
                .build();

        Probleme probleme2 = Probleme.builder()
                .descriptionProbleme("Client Problem 2")
                .detailsProbleme("Details 2")
                .valise(valise)
                .client(client)
                .build();

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        List<Probleme> foundProblemes = problemeRepository.findByClientId(client.getId());

        assertEquals(2, foundProblemes.size());
        assertTrue(foundProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Client Problem 1")));
        assertTrue(foundProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Client Problem 2")));
    }





}
