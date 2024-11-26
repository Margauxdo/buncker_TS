package example.integration.entities;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.Regle;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.RegleRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ValiseIntegrationTest {

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        mouvementRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testSaveValise() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        // Assertions
        assertTrue(valise.getId() > 0);
        assertEquals("Test Valise", valise.getDescription());
        assertEquals(123456L, valise.getNumeroValise());
        assertEquals(client.getId(), valise.getClient().getId());
    }

    @Test
    public void testFindValiseById() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Optional<Valise> foundValise = valiseRepository.findById(valise.getId());

        // Assertions
        assertTrue(foundValise.isPresent());
        assertEquals("Test Valise", foundValise.get().getDescription());
    }

    @Test
    public void testUpdateValise() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Old Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        // Update the valise
        valise.setDescription("Updated Valise");
        valise = valiseRepository.saveAndFlush(valise);

        // Assertions
        assertEquals("Updated Valise", valise.getDescription());
    }

    @Test
    public void testDeleteValise() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        valiseRepository.deleteById(valise.getId());

        Optional<Valise> deletedValise = valiseRepository.findById(valise.getId());
        assertTrue(deletedValise.isEmpty());
    }

    @Test
    public void testValiseWithMouvementAndRegle() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise with Mouvement and Regle");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Test Statut");
        mouvement.setValise(valise);
        valise.getMouvementList().add(mouvement);
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Regle regle = new Regle();
        regle.setReglePourSortie("Test Regle");
        regle.setValise(valise);
        regle.setCoderegle("CODE123");
        valise.getRegleSortie().add(regle);
        regle = regleRepository.saveAndFlush(regle);

        valise = valiseRepository.findById(valise.getId()).orElseThrow();

        assertEquals(1, valise.getMouvementList().size());
        assertEquals(1, valise.getRegleSortie().size());
        assertEquals(mouvement.getId(), valise.getMouvementList().get(0).getId());
        assertEquals(regle.getId(), valise.getRegleSortie().get(0).getId());
    }

    @Test
    public void testValiseWithEmptyLists() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise with Empty Lists");
        valise.setNumeroValise(987654L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        assertTrue(valise.getMouvementList().isEmpty());
        assertTrue(valise.getRegleSortie().isEmpty());
    }

    @Test
    public void testValiseDeleteCascading() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Valise for Cascade Test");
        valise.setNumeroValise(111111L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Test Statut");
        mouvement.setValise(valise);
        valise.getMouvementList().add(mouvement);
        mouvementRepository.saveAndFlush(mouvement);

        Regle regle = new Regle();
        regle.setReglePourSortie("Test Regle");
        regle.setValise(valise);
        regle.setCoderegle("CODE123");
        valise.getRegleSortie().add(regle);
        regleRepository.saveAndFlush(regle);

        valiseRepository.deleteById(valise.getId());
        Optional<Valise> deletedValise = valiseRepository.findById(valise.getId());
        assertTrue(deletedValise.isEmpty());

        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(mouvement.getId());
        assertTrue(deletedMouvement.isEmpty());

        Optional<Regle> deletedRegle = regleRepository.findById(regle.getId());
        assertTrue(deletedRegle.isEmpty());
    }


}
