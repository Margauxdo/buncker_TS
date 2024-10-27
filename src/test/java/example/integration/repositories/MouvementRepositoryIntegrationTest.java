package example.integration.repositories;

import example.entities.Client;
import example.entities.Livreur;
import example.entities.Mouvement;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class MouvementRepositoryIntegrationTest {

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
    }
    @Test
    public void testSaveMouvements() {
        Mouvement mouvement1 = new Mouvement();
        Livreur livreur1 = new Livreur();
        livreur1.setCodeLivreur("code123");
        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
        mouvement1.setStatutSortie("test stautut de sortie");
        mouvement1.setDateHeureMouvement(new Date());

        Mouvement savedMouv = mouvementRepository.save(mouvement1);
        assertNotNull(savedMouv.getId());


    }
    @Test
    public void testFindByIdMouvement() {
        Mouvement mouvement1 = new Mouvement();
        Livreur livreur1 = new Livreur();
        livreur1.setCodeLivreur("code123");
        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
        mouvement1 = mouvementRepository.save(mouvement1);
        Optional<Mouvement> foundMvmt1 = mouvementRepository.findById(mouvement1.getId());
        assertTrue(foundMvmt1.isPresent());
        assertEquals(mouvement1.getId(), foundMvmt1.get().getId());

    }
    @Test
    public void testDeleteMouvement() {
        Mouvement mouvement1 = new Mouvement();
        Livreur livreur1 = new Livreur();
        livreur1.setCodeLivreur("code123");
        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
        mouvement1 = mouvementRepository.save(mouvement1);
        mouvementRepository.deleteById(mouvement1.getId());
        Optional<Mouvement> deletedMvmt1 = mouvementRepository.findById(mouvement1.getId());
        assertFalse(deletedMvmt1.isPresent());

    }
    @Test
    public void testUpdateMouvement() {
        Mouvement mouvement1 = new Mouvement();
        Livreur livreur1 = new Livreur();
        livreur1.setCodeLivreur("code123");
        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
         Mouvement savedMouv = mouvementRepository.save(mouvement1);
         savedMouv.setDateHeureMouvement(new Date());
         savedMouv.setValise(valise1);
         Mouvement updatedMvmt1 = mouvementRepository.save(savedMouv);
         Optional<Mouvement> foundMvmt1 = mouvementRepository.findById(updatedMvmt1.getId());
         assertTrue(foundMvmt1.isPresent());
         assertEquals(updatedMvmt1.getId(), foundMvmt1.get().getId());


    }
    @Test
    public void testFindAllMouvements() {
        Livreur livreur1 = new Livreur();
        livreur1.setNomLivreur("Jean");
        livreur1.setCodeLivreur("code123");
        livreur1 = livreurRepository.save(livreur1);

        Client client1 = new Client();
        client1.setName("Client 1");
        client1.setEmail("client1@example.com");
        client1 = clientRepository.save(client1);  // Enregistrer le client

        // Créer une valise et l'associer au client
        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        valise1.setClient(client1);  // Associer la valise au client
        valise1 = valiseRepository.save(valise1);  // Enregistrer la valise

        // Créer un mouvement et l'associer au livreur et à la valise
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
        mouvement1 = mouvementRepository.save(mouvement1);  // Enregistrer le mouvement

        // Créer un deuxième livreur
        Livreur livreur2 = new Livreur();
        livreur2.setNomLivreur("Louis");
        livreur2.setCodeLivreur("code456");
        livreur2 = livreurRepository.save(livreur2);  // Enregistrer le deuxième livreur

        // Créer un autre client et l'associer à la deuxième valise
        Client client2 = new Client();
        client2.setName("Client 2");
        client2.setEmail("client2@example.com");  // Fournir un email pour satisfaire la contrainte NOT NULL
        client2 = clientRepository.save(client2);  // Enregistrer le deuxième client

        // Créer une deuxième valise et l'associer au deuxième client
        Valise valise2 = new Valise();
        valise2.setNumeroValise(789L);
        valise2.setClient(client2);  // Associer la deuxième valise au deuxième client
        valise2 = valiseRepository.save(valise2);  // Enregistrer la deuxième valise

        // Créer un deuxième mouvement et l'associer au deuxième livreur et à la deuxième valise
        Mouvement mouvement2 = new Mouvement();
        mouvement2.setLivreur(livreur2);
        mouvement2.setValise(valise2);
        mouvement2 = mouvementRepository.save(mouvement2);  // Enregistrer le deuxième mouvement

        // Vérifier que tous les mouvements sont présents
        List<Mouvement> mouvements = mouvementRepository.findAll();
        assertNotNull(mouvements);
        assertTrue(mouvements.size() >= 2);  // Vérifier que les deux mouvements sont bien enregistrés

        mouvements.forEach(mouvement -> System.out.println(mouvement.getId()));
    }


    @Test
    public void testDeleteMouvementWithRelations() {
        Livreur livreur1 = new Livreur();
        livreur1.setNomLivreur("Jean");
        livreur1.setCodeLivreur("code123");
        livreur1 = livreurRepository.save(livreur1);

        Client client1 = new Client();
        client1.setName("Nom Client Test");
        client1.setEmail("testclient@example.com");
        client1 = clientRepository.save(client1);

        Valise valise1 = new Valise();
        valise1.setNumeroValise(456L);
        valise1.setClient(client1);
        valise1 = valiseRepository.save(valise1);

        Mouvement mouvement1 = new Mouvement();
        mouvement1.setLivreur(livreur1);
        mouvement1.setValise(valise1);
        mouvement1 = mouvementRepository.save(mouvement1);

        mouvementRepository.deleteById(mouvement1.getId());

        Optional<Mouvement> deletedMvmt1 = mouvementRepository.findById(mouvement1.getId());
        assertFalse(deletedMvmt1.isPresent());
    }








}
