package example.controller.entities;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.transaction.PlatformTransactionManager;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ProblemeIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

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
    public void testSaveProbleme() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Valise description");
        valiseRepository.save(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Details of the problem");
        probleme.setDescriptionProbleme("Description of the problem");

        Probleme savedPb = problemeRepository.save(probleme);
        Assertions.assertNotNull(savedPb.getId());
        assertEquals(clientA, savedPb.getClient());
        assertEquals("Description of the problem", savedPb.getDescriptionProbleme());
    }


    @Test
    public void testFindProblemeById() {
        Client client = new Client();
        client.setName("Client Name");
        client.setEmail("client@example.com");
        Client savedClient = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setNumeroValise(12345L);
        valise.setDescription("Sample Description");
        valise.setClient(savedClient);
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        Probleme pb = new Probleme();
        pb.setDetailsProbleme("Details of the problem");
        pb.setDescriptionProbleme("Description of the problem");
        pb.setValise(savedValise);
        Probleme savedPb = problemeRepository.saveAndFlush(pb);

        Optional<Probleme> foundPb = problemeRepository.findById(savedPb.getId());
        assertTrue(foundPb.isPresent());
    }

    @Test
    public void testUpdateProbleme() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.saveAndFlush(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Initial description");
        val1 = valiseRepository.saveAndFlush(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Initial details");
        probleme.setDescriptionProbleme("Initial description");
        Probleme savedPb = problemeRepository.saveAndFlush(probleme);

        savedPb.setDetailsProbleme("Updated details");
        Probleme updatedPb = problemeRepository.saveAndFlush(savedPb);

        assertEquals("Updated details", updatedPb.getDetailsProbleme());
    }



    @Test
    public void testDeleteProbleme() {
        // Create and save the client
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Valise description");
        valiseRepository.save(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Details of the problem");
        probleme.setDescriptionProbleme("Description of the problem");

        Probleme savedPb = problemeRepository.save(probleme);

        problemeRepository.deleteById(savedPb.getId());
        Optional<Probleme> deletedPb = problemeRepository.findById(savedPb.getId());
        Assertions.assertFalse(deletedPb.isPresent());
    }


    @Test
    public void testFindAllProblemes() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Valise description");
        valiseRepository.save(val1);

        Probleme probleme1 = new Probleme();
        probleme1.setClient(clientA);
        probleme1.setValise(val1);
        probleme1.setDetailsProbleme("Details for problem 1");
        probleme1.setDescriptionProbleme("Problem 1");

        Probleme probleme2 = new Probleme();
        probleme2.setClient(clientA);
        probleme2.setValise(val1);
        probleme2.setDetailsProbleme("Details for problem 2");
        probleme2.setDescriptionProbleme("Problem 2");

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        List<Probleme> allProblemes = problemeRepository.findAll();
        Assertions.assertNotNull(allProblemes);
        assertEquals(2, allProblemes.size());

        assertTrue(allProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Problem 1")));
        assertTrue(allProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Problem 2")));
    }


    @Test
    public void testFindProblemesByValise() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Valise de Martin");
        val1 = valiseRepository.save(val1);

        Probleme probleme1 = new Probleme();
        probleme1.setClient(clientA);
        probleme1.setValise(val1);
        probleme1.setDetailsProbleme("Details for problem 1");
        probleme1.setDescriptionProbleme("Problem 1");

        Probleme probleme2 = new Probleme();
        probleme2.setClient(clientA);
        probleme2.setValise(val1);
        probleme2.setDetailsProbleme("Details for problem 2");
        probleme2.setDescriptionProbleme("Problem 2");

        problemeRepository.save(probleme1);
        problemeRepository.save(probleme2);

        List<Probleme> problemesByValise = problemeRepository.findByValise(val1);

        Assertions.assertNotNull(problemesByValise);
        assertEquals(2, problemesByValise.size());
    }

    @Test
    public void testSaveProblemeWithoutDescription() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setDescription("Valise description");
        valiseRepository.save(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            problemeRepository.save(probleme);
        });
    }










}

