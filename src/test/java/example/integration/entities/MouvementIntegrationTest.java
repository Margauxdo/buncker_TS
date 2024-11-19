package example.integration.entities;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class MouvementIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        livreurRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveMouvement() {
        Client client = new Client();
        client.setName("Martin");
        client.setEmail("martin@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setClient(client);
        valiseRepository.save(valise);

        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Jean");
        livreurRepository.save(livreur);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En cours");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);
        mouvement.setLivreur(livreur);

        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        Assertions.assertNotNull(savedMouvement.getId());
        Assertions.assertEquals("En cours", savedMouvement.getStatutSortie());
    }


    @Test
    public void testFindMouvementById() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En cours");

        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        Optional<Mouvement> foundMouvement = mouvementRepository.findById(savedMouvement.getId());

        assertTrue(foundMouvement.isPresent());
    }

    @Test
    public void testUpdateMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Initial");

        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        savedMouvement.setStatutSortie("Terminé");
        Mouvement updatedMouvement = mouvementRepository.save(savedMouvement);

        Assertions.assertEquals("Terminé", updatedMouvement.getStatutSortie());
    }

    @Test
    public void testDeleteMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En cours");

        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        mouvementRepository.deleteById(savedMouvement.getId());

        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(savedMouvement.getId());
        Assertions.assertFalse(deletedMouvement.isPresent());
    }

    @Test
    public void testCascadeDeleteMouvementWithValiseAndLivreur() {
        Client client = new Client();
        client.setName("Martin");
        client.setEmail("martin@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setClient(client);
        valiseRepository.save(valise);

        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Jean");
        livreurRepository.save(livreur);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En cours");
        mouvement.setValise(valise);
        mouvement.setLivreur(livreur);

        mouvementRepository.save(mouvement);
        mouvementRepository.delete(mouvement);

        Optional<Mouvement> foundMouvement = mouvementRepository.findById(mouvement.getId());
        Assertions.assertFalse(foundMouvement.isPresent());
    }
    @Test
    void testMouvementRelationWithLivreur() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Jules verne");
        livreurRepository.save(livreur);

        Mouvement mouvement = new Mouvement();
        mouvement.setLivreur(livreur);
        mouvementRepository.save(mouvement);

        Optional<Mouvement> found = mouvementRepository.findById(mouvement.getId());
        assertTrue(found.isPresent());
        Assertions.assertEquals("Jules verne", found.get().getLivreur().getNomLivreur());
    }

    @Test
    void testCreateMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvementRepository.save(mouvement);

        Optional<Mouvement> found = mouvementRepository.findById(mouvement.getId());
        assertTrue(found.isPresent());
    }







}

