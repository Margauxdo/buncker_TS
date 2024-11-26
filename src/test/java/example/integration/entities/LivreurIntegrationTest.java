package example.integration.entities;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LivreurIntegrationTest {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        livreurRepository.deleteAll();
        mouvementRepository.deleteAll();
    }

    @Test
    public void testSaveLivreur() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Mouvement de test");
        mouvement.setValise(valise);
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("LIV001");
        livreur.setNomLivreur("Martin");
        livreur.setPrenomLivreur("Paul");
        livreur.setNumeroCartePro("12345");
        livreur.setTelephonePortable("123456789");
        livreur.setMouvement(mouvement);

        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        // Assertions
        assertTrue(savedLivreur.getId() > 0);
        assertEquals("LIV001", savedLivreur.getCodeLivreur());
        assertEquals("Martin", savedLivreur.getNomLivreur());
        assertEquals("Paul", savedLivreur.getPrenomLivreur());
        assertEquals("12345", savedLivreur.getNumeroCartePro());
        assertEquals("123456789", savedLivreur.getTelephonePortable());
        assertEquals(mouvement.getId(), savedLivreur.getMouvement().getId());
    }


    @Test
    public void testFindLivreurById() {

        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Mouvement pour test");
        mouvement.setValise(valise);
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("LIV002");
        livreur.setNomLivreur("Durand");
        livreur.setPrenomLivreur("Jean");
        livreur.setNumeroCartePro("67890");
        livreur.setTelephonePortable("987654321");
        livreur.setMouvement(mouvement);
        livreur = livreurRepository.saveAndFlush(livreur);

        Optional<Livreur> foundLivreur = livreurRepository.findById(livreur.getId());

        // Assertions
        assertTrue(foundLivreur.isPresent());
        assertEquals("LIV002", foundLivreur.get().getCodeLivreur());
        assertEquals("Durand", foundLivreur.get().getNomLivreur());
    }


    @Test
    public void testUpdateLivreur() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Test Mouvement");
        mouvement.setValise(valise);
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("LIV003");
        livreur.setNomLivreur("Lemoine");
        livreur.setPrenomLivreur("Sophie");
        livreur.setNumeroCartePro("54321");
        livreur.setTelephonePortable("1122334455");
        livreur.setMouvement(mouvement);
        livreur = livreurRepository.saveAndFlush(livreur);

        livreur.setNomLivreur("Lemoine Updated");
        livreur.setPrenomLivreur("Sophie Updated");
        livreur = livreurRepository.saveAndFlush(livreur);

        // Assertions
        assertEquals("Lemoine Updated", livreur.getNomLivreur());
        assertEquals("Sophie Updated", livreur.getPrenomLivreur());
    }



    @Test
    public void testDeleteLivreur() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Mouvement pour suppression");
        mouvement.setValise(valise); // Ensure that a Valise is associated
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("LIV004");
        livreur.setNomLivreur("Robert");
        livreur.setPrenomLivreur("Alice");
        livreur.setNumeroCartePro("98765");
        livreur.setTelephonePortable("6677889900");
        livreur.setMouvement(mouvement);
        livreur = livreurRepository.saveAndFlush(livreur);

        livreurRepository.deleteById(livreur.getId());

        Optional<Livreur> deletedLivreur = livreurRepository.findById(livreur.getId());
        assertTrue(deletedLivreur.isEmpty());
    }


    @Test
    public void testSaveLivreurWithMouvement() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Test Statut");
        mouvement.setValise(valise);
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("LIV005");
        livreur.setNomLivreur("Michel");
        livreur.setPrenomLivreur("Jean");
        livreur.setNumeroCartePro("67890");
        livreur.setTelephonePortable("987654321");
        livreur.setMouvement(mouvement);
        livreur = livreurRepository.saveAndFlush(livreur);

        assertEquals(livreur.getMouvement().getId(), mouvement.getId());
        assertEquals(livreur.getMouvement().getStatutSortie(), "Test Statut");
    }


}
