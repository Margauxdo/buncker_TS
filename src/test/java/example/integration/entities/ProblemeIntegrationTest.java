package example.integration.entities;

import example.entities.Client;
import example.entities.Probleme;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
        Assertions.assertEquals(clientA, savedPb.getClient());
        Assertions.assertEquals("Description of the problem", savedPb.getDescriptionProbleme());
    }

    @Test
    public void testFindProblemeById() {
        Probleme pb = new Probleme();
        pb.setDetailsProbleme("Details of the problem");
        pb.setDescriptionProbleme("Description of the problem");
        Probleme savedPb = problemeRepository.saveAndFlush(pb);
        Optional<Probleme> foundPb = problemeRepository.findById(savedPb.getId());
        Assertions.assertTrue(foundPb.isPresent());
    }


    @Test
    public void testSaveProblemeWithDuplicateDescriptionThrowsException() {
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

        Assertions.assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            problemeRepository.saveAndFlush(probleme2);
        });
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

        Assertions.assertEquals("Updated details", updatedPb.getDetailsProbleme());
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
        // Création et sauvegarde du client
        Client clientA = new Client();
        clientA.setName("Martin");
        clientA.setEmail("martin@example.com");
        clientA = clientRepository.saveAndFlush(clientA);  // Attache le client au contexte de persistance

        // Création et sauvegarde de la valise
        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1 = valiseRepository.saveAndFlush(val1);  // Attache la valise au contexte de persistance

        // Création et sauvegarde du problème avec le client et la valise attachés
        Probleme probleme = new Probleme();
        probleme.setClient(clientA);
        probleme.setValise(val1);
        probleme.setDetailsProbleme("Détails du problème");
        probleme.setDescriptionProbleme("Description du problème");
        Probleme savedProbleme = problemeRepository.saveAndFlush(probleme);  // Attache le problème au contexte de persistance

        // Suppression du problème
        problemeRepository.delete(savedProbleme);
        problemeRepository.flush(); // Force la suppression dans la base de données

        // Suppression de la valise
        valiseRepository.delete(val1);
        valiseRepository.flush();  // Force la suppression dans la base de données

        // Vérification que le problème n'existe plus
        Optional<Probleme> foundProbleme = problemeRepository.findById(savedProbleme.getId());
        Assertions.assertFalse(foundProbleme.isPresent(), "Le problème devrait être supprimé.");
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
        Assertions.assertEquals(2, allProblemes.size());

        Assertions.assertTrue(allProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Problem 1")));
        Assertions.assertTrue(allProblemes.stream().anyMatch(p -> p.getDescriptionProbleme().equals("Problem 2")));
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
        Assertions.assertEquals("Initial details", updatedPb.getDetailsProbleme());
        Assertions.assertEquals("Updated description", updatedPb.getDescriptionProbleme());
    }


    @Test
    public void testSaveProblemeWithoutDescriptionThrowsException() {
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
        probleme.setDetailsProbleme("Details without description");

        Assertions.assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
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
        Assertions.assertEquals(2, problemesByValise.size());
    }





}

