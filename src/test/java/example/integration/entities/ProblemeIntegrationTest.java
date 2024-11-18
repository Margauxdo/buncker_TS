package example.integration.entities;

import example.entities.Client;
import example.entities.Probleme;
import example.entities.Valise;
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
import org.springframework.test.annotation.Commit;
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
        Probleme pb = new Probleme();
        pb.setDetailsProbleme("Details of the problem");
        pb.setDescriptionProbleme("Description of the problem");
        Probleme savedPb = problemeRepository.saveAndFlush(pb);
        Optional<Probleme> foundPb = problemeRepository.findById(savedPb.getId());
        assertTrue(foundPb.isPresent());
    }


    @Test
    public void testSaveProblemeWithDuplicateDescriptionThrowsException() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Client clientA = new Client();
            clientA.setName("Martin");
            clientA.setEmail("martin@example.com");
            clientRepository.save(clientA);

            Valise val1 = new Valise();
            val1.setClient(clientA);
            valiseRepository.save(val1);

            Probleme probleme1 = new Probleme();
            probleme1.setClient(clientA);
            probleme1.setValise(val1);
            probleme1.setDetailsProbleme("First problem details");
            probleme1.setDescriptionProbleme("Same description");

            problemeRepository.saveAndFlush(probleme1);

            Probleme probleme2 = new Probleme();
            probleme2.setClient(clientA);
            probleme2.setValise(val1);
            probleme2.setDetailsProbleme("Second problem details");
            probleme2.setDescriptionProbleme("Same description");

            Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
                problemeRepository.save(probleme2);
                entityManager.flush();
            });


        } finally {
            transactionManager.rollback(status);
        }
    }


    @Test
    public void testUpdateProbleme() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.saveAndFlush(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
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
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
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
    public void testCascadeDeleteProblemeWithValise() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.saveAndFlush(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1 = valiseRepository.saveAndFlush(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Détails du problème");
        probleme.setDescriptionProbleme("Description du problème");
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);
        problemeRepository.delete(savedProbleme);
        problemeRepository.flush();

        valiseRepository.delete(val1);
        valiseRepository.flush();

        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());
        Assertions.assertFalse(foundProbleme.isPresent(), "The problem should be removed.");
    }


    @Test
    public void testFindAllProblemes() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
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
    public void testPartialUpdateProbleme() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.saveAndFlush(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        valiseRepository.saveAndFlush(val1);

        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Initial details");
        probleme.setDescriptionProbleme("Initial description");

        Probleme savedPb = problemeRepository.saveAndFlush(probleme);

        savedPb.setDescriptionProbleme("Updated description");
        problemeRepository.saveAndFlush(savedPb);

        Probleme updatedPb = problemeRepository.findById(savedPb.getId()).orElseThrow();
        assertEquals("Initial details", updatedPb.getDetailsProbleme());
        assertEquals("Updated description", updatedPb.getDescriptionProbleme());
    }


    @Test
    public void testSaveProblemeWithoutDescriptionThrowsException() {
        // Créer un client et l'enregistrer
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientRepository.save(clientA);

        // Créer une valise et l'enregistrer
        Valise val1 = new Valise();
        val1.setClient(clientA);
        valiseRepository.save(val1);

        // Créer un problème sans description
        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Details without description");

        // S'attendre à une ConstraintViolationException
        Assertions.assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            problemeRepository.saveAndFlush(probleme);
        });
    }

    @Test
    public void testFindProblemesByValise() {
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
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
    void testProblemeRelationWithClient() {
        // Création du client
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        clientRepository.save(client);

        // Création du problème avec les données requises
        Probleme probleme = new Probleme();
        probleme.setClient(client);
        probleme.setDescriptionProbleme("Problème de test");
        probleme.setDetailsProbleme("Détails du problème requis");
        problemeRepository.save(probleme);

        // Récupération et vérification du problème
        Optional<Probleme> found = problemeRepository.findById(probleme.getId());
        assertTrue(found.isPresent(), "Le problème doit être trouvé");
        assertEquals("Test Client", found.get().getClient().getName());
        assertEquals("Problème de test", found.get().getDescriptionProbleme());
        assertEquals("Détails du problème requis", found.get().getDetailsProbleme());
    }







}

