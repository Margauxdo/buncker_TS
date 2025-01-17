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
                .numeroValise("VAL123")
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
                .build();

        Probleme savedProbleme = problemeRepository.save(probleme);

        assertNotNull(savedProbleme.getId());
        assertEquals("Test Description", savedProbleme.getDescriptionProbleme());
        assertEquals("Test Details", savedProbleme.getDetailsProbleme());
        assertEquals(valise.getId(), savedProbleme.getValise().getId());
    }

    @Test
    public void testFindByValise() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .valise(valise)
                .build();

        problemeRepository.save(probleme);

        List<Probleme> problemes = problemeRepository.findByValise(valise);
        assertFalse(problemes.isEmpty());
        assertEquals(1, problemes.size());
        assertEquals("Test Description", problemes.get(0).getDescriptionProbleme());
    }

    @Test
    public void testExistsByDescriptionProblemeAndDetailsProbleme() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Unique Description")
                .detailsProbleme("Unique Details")
                .valise(valise)
                .build();

        problemeRepository.save(probleme);

        boolean exists = problemeRepository.existsByDescriptionProblemeAndDetailsProbleme("Unique Description", "Unique Details");
        assertTrue(exists);
    }

    @Test
    public void testDeleteAllByValise() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .valise(valise)
                .build();

        problemeRepository.save(probleme);
        problemeRepository.deleteAllByValise(valise);

        List<Probleme> problemes = problemeRepository.findByValise(valise);
        assertTrue(problemes.isEmpty());
    }



    @Test
    public void testDissociateValise() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .valise(valise)
                .build();

        problemeRepository.save(probleme);
        problemeRepository.dissociateValise(valise.getId());

        List<Probleme> problemes = problemeRepository.findByValise(valise);
        assertTrue(problemes.isEmpty());
    }

    @Test
    public void testDeleteByValiseId() {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .valise(valise)
                .build();

        problemeRepository.save(probleme);
        problemeRepository.deleteByValiseId(valise.getId());

        List<Probleme> problemes = problemeRepository.findByValise(valise);
        assertTrue(problemes.isEmpty());
    }
}
