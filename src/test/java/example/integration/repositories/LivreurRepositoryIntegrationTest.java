package example.integration.repositories;

import example.entity.*;
import example.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class LivreurRepositoryIntegrationTest {

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
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveLivreur() {
        // Create and persist a Client entity
        Client client = Client.builder()
                .name("Sample Client")
                .email("client@example.com")
                .build();

        client = clientRepository.save(client);

        // Create and persist a Valise entity associated with the Client
        Valise valise = Valise.builder()
                .numeroValise(123L)
                .description("Sample Valise")
                .client(client) // Associate the Client
                .build();

        valise = valiseRepository.save(valise);

        // Create and persist a Mouvement entity associated with the Valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .valise(valise) // Associate the Valise
                .build();

        mouvement = mouvementRepository.save(mouvement);

        // Create and persist a Livreur entity associated with the Mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("Jean Dupont")
                .prenomLivreur("Jean")
                .telephonePortable("0123456789")
                .mouvements((List<Mouvement>) mouvement) // Associate the Mouvement
                .build();

        Livreur savedLivreur = livreurRepository.save(livreur);

        // Assertions
        assertNotNull(savedLivreur.getId(), "Livreur ID should not be null after saving.");
        assertEquals("Jean Dupont", savedLivreur.getNomLivreur(), "NomLivreur did not match.");
        assertEquals("Jean", savedLivreur.getPrenomLivreur(), "PrenomLivreur did not match.");
        assertNotNull(savedLivreur.getMouvements(), "Mouvement should not be null.");
        assertNotNull(savedLivreur.getMouvements().get(1), "Valise should not be null.");
        assertNotNull(savedLivreur.getMouvements().get(1).getLivreur(), "Client should not be null.");
    }




    @Test
    public void testFindByIdLivreur() {
        // Step 1: Create and save a Client
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        Client savedClient = clientRepository.save(client);

        // Step 2: Create and save a Valise associated with the Client
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(savedClient) // Associate the Valise with the Client
                .build();
        Valise savedValise = valiseRepository.save(valise);

        // Step 3: Create and save a Mouvement associated with the Valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .valise(savedValise) // Associate the Valise with the Mouvement
                .build();
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        // Step 4: Create and save a Livreur associated with the Mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("Paul Martin")
                .telephonePortable("0987654321")
                .mouvements((List<Mouvement>) savedMouvement) // Associate the Mouvement with the Livreur
                .build();
        Livreur savedLivreur = livreurRepository.save(livreur);

        // Step 5: Fetch the Livreur by ID and verify
        Optional<Livreur> foundLivreur = livreurRepository.findById(savedLivreur.getId());

        // Assertions
        assertTrue(foundLivreur.isPresent(), "Livreur should be found by ID.");
        assertEquals("Paul Martin", foundLivreur.get().getNomLivreur(), "NomLivreur did not match.");
        assertEquals("0987654321", foundLivreur.get().getTelephonePortable(), "TelephonePortable did not match.");
        assertEquals(savedMouvement.getId(), foundLivreur.get().getMouvements().get(1), "Associated Mouvement did not match.");
    }




    @Test
    public void testSaveLivreurWithMouvement() {
        Client client = Client.builder()
                .name("Test Client")
                .email("test.client@example.com")
                .build();
        client = clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .statutSortie("En cours")
                .valise(valise)
                .dateHeureMouvement(new Date())
                .build();
        mouvement = mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("Livreur Mouvement")
                .mouvements((List<Mouvement>) mouvement)
                .build();
        Livreur savedLivreur = livreurRepository.save(livreur);

        assertNotNull(savedLivreur.getId(), "Livreur ID should not be null after saving.");
        assertEquals("Livreur Mouvement", savedLivreur.getNomLivreur(), "NomLivreur did not match.");
        assertEquals(mouvement.getId(), savedLivreur.getMouvements(), "Mouvement ID did not match.");
    }

    @Test
    public void testFindAllLivreurs() {
        // Create and save a Client
        Client client = Client.builder()
                .name("Test Client")
                .email("test.client@example.com")
                .build();
        client = clientRepository.save(client);

        // Create and save a Valise
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        // Create and save a Mouvement
        Mouvement mouvement = Mouvement.builder()
                .statutSortie("En cours")
                .valise(valise)
                .dateHeureMouvement(new Date())
                .build();
        mouvement = mouvementRepository.save(mouvement);

        // Create and save Livreurs
        Livreur livreur1 = Livreur.builder()
                .nomLivreur("Livreur 1")
                .mouvements((List<Mouvement>) mouvement) // Associate with the required Mouvement
                .build();
        Livreur livreur2 = Livreur.builder()
                .nomLivreur("Livreur 2")
                .mouvements((List<Mouvement>) mouvement) // Associate with the same Mouvement
                .build();

        livreurRepository.save(livreur1);
        livreurRepository.save(livreur2);

        // Retrieve all Livreurs
        List<Livreur> livreurs = livreurRepository.findAll();

        // Assertions
        assertEquals(2, livreurs.size(), "Number of livreurs found did not match.");
        assertTrue(livreurs.stream().anyMatch(l -> l.getNomLivreur().equals("Livreur 1")), "Livreur 1 not found.");
        assertTrue(livreurs.stream().anyMatch(l -> l.getNomLivreur().equals("Livreur 2")), "Livreur 2 not found.");
    }


    @Test
    public void testExistsByCodeLivreur() {
        // Create a Client entity
        Client client = Client.builder()
                .name("Test Client")
                .email("test.client@example.com")
                .build();
        client = clientRepository.save(client);

        // Create a Valise entity associated with the Client
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(client) // Associate with the saved Client
                .build();
        valise = valiseRepository.save(valise);

        // Create a Mouvement entity associated with the Valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .valise(valise) // Associate with the saved Valise
                .build();
        mouvement = mouvementRepository.save(mouvement);

        // Create a Livreur entity associated with the Mouvement
        Livreur livreur = Livreur.builder()
                .codeLivreur("EXIST123")
                .nomLivreur("Existing Livreur")
                .mouvements((List<Mouvement>) mouvement) // Associate with the saved Mouvement
                .build();
        livreur = livreurRepository.save(livreur);

        // Verify that the Livreur can be found by its code
        boolean exists = livreurRepository.existsByCodeLivreur("EXIST123");
        assertTrue(exists, "Expected the codeLivreur to exist, but it does not.");
    }

}


