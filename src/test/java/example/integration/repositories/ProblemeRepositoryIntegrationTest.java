package example.integration.repositories;

import example.entities.Client;
import example.entities.Probleme;
import example.entities.Valise;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
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

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
    }

    @Test
    public void testSaveProblemeSuccess() {
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");

        Valise valise = new Valise();
        valise.setNumeroValise(123L);

        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setClient(client);
        probleme.setDetailsProbleme("details test");
        probleme.setDescriptionProbleme("description test");
        Probleme saved = problemeRepository.save(probleme);

        assertNotNull(saved.getId());
        assertEquals("details test", saved.getDetailsProbleme());
        assertEquals("description test", saved.getDescriptionProbleme());
    }

    @Test
    public void testFindProblemeById(){
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");
        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setClient(client);
        probleme.setDetailsProbleme("details test");
        probleme.setDescriptionProbleme("description test");
        Probleme saved = problemeRepository.save(probleme);
        Optional<Probleme> found = problemeRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("details test", found.get().getDetailsProbleme());
        assertEquals("description test", found.get().getDescriptionProbleme());

    }

    @Test
    public void testDeleteProbleme(){
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");
        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setClient(client);
        probleme.setDetailsProbleme("details test");
        probleme.setDescriptionProbleme("description test");
        Probleme saved = problemeRepository.save(probleme);
        problemeRepository.delete(saved);
        Optional<Probleme> found = problemeRepository.findById(saved.getId());
        assertFalse(found.isPresent());

    }
    @Test
    public void testUpdateProbleme(){
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");
        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setClient(client);
        probleme.setDetailsProbleme("details test");
        probleme.setDescriptionProbleme("description test");
        Probleme saved = problemeRepository.save(probleme);
        saved.setDetailsProbleme("details updated");
        saved.setDescriptionProbleme("description updated");
        Probleme updated = problemeRepository.save(saved);
        Optional<Probleme> found = problemeRepository.findById(updated.getId());
        assertTrue(found.isPresent());
        assertEquals("details updated", found.get().getDetailsProbleme());
        assertEquals("description updated", found.get().getDescriptionProbleme());
    }
    @Test
    public void testFindAllProbleme() {
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");
        client = clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        valise.setClient(client);
        valise = valiseRepository.save(valise);

        Probleme probleme1 = new Probleme();
        probleme1.setValise(valise);
        probleme1.setClient(client);
        probleme1.setDetailsProbleme("details premier test");
        probleme1.setDescriptionProbleme("description premier test");
        problemeRepository.save(probleme1);
        Probleme probleme2 = new Probleme();
        probleme2.setValise(valise);
        probleme2.setClient(client);
        probleme2.setDetailsProbleme("details second test");
        probleme2.setDescriptionProbleme("description second test");
        problemeRepository.save(probleme2);
        List<Probleme> found = problemeRepository.findAll();
        assertNotNull(found);

        found.sort(Comparator.comparing(Probleme::getDetailsProbleme));
        assertEquals("details premier test", found.get(0).getDetailsProbleme());
        assertEquals("description premier test", found.get(0).getDescriptionProbleme());
        assertEquals("details second test", found.get(1).getDetailsProbleme());
        assertEquals("description second test", found.get(1).getDescriptionProbleme());

        found.forEach(System.out::println);
    }


    @Test
    public void testFindByValise() {
        // Setup
        Client client = new Client();
        client.setName("harry");
        client.setEmail("harry@gmail.com");
        client = clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        valise.setClient(client);
        valise = valiseRepository.save(valise);

        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setClient(client);
        probleme.setDetailsProbleme("details test");
        probleme.setDescriptionProbleme("description test");
        problemeRepository.save(probleme);

        // Test
        List<Probleme> foundProblemes = problemeRepository.findByValise(valise);
        assertNotNull(foundProblemes);
        assertFalse(foundProblemes.isEmpty());
        assertEquals(1, foundProblemes.size());
        assertEquals("details test", foundProblemes.get(0).getDetailsProbleme());
    }
    @Test
    public void testSaveProblemeWithNullValues() {
        Probleme probleme = new Probleme();
        // Missing required fields: valise, client, etc.
        assertThrows(Exception.class, () -> {
            problemeRepository.save(probleme);
        });
    }
    @Test
    public void testFindByValiseWithNoProblemes() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("testclient@gmail.com");
        client = clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(999L);
        valise.setClient(client);
        valise = valiseRepository.save(valise);

        List<Probleme> foundProblemes = problemeRepository.findByValise(valise);
        assertTrue(foundProblemes.isEmpty());
    }





}
