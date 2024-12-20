package example.integration.services;

import example.DTO.LivreurDTO;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.exceptions.RegleNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.LivreurService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LivreurServiceIntegrationTest {

    @Autowired
    private LivreurService livreurService;

    private Mouvement mouvement;
    private Livreur livreur;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date(System.currentTimeMillis() + 86400000L)) // +1 day
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 172800000L)) // +2 days
                .build();

        livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .motDePasse("password")
                .mouvements((List<Mouvement>) mouvement)
                .build();
    }

    @Test
    void testCreateLivreur_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Valise de Test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("En cours")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV001")
                .mouvements((List<Mouvement>) mouvement)
                .build();

        // Act
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Assert
        assertNotNull(createdLivreur.getId());
        assertEquals(livreur.getNomLivreur(), createdLivreur.getNomLivreur());
        assertEquals(livreur.getCodeLivreur(), createdLivreur.getCodeLivreur());
        assertNotNull(createdLivreur.getMouvements());
        assertEquals(mouvement.getId(), createdLivreur.getMouvements().get(1));
        assertEquals(valise.getId(), createdLivreur.getMouvements().get(1).getId());
    }



    @Test
    void testCreateLivreur_Failure_DuplicateCode() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Valise Test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date(System.currentTimeMillis() + 86400000L))
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 172800000L))
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .mouvements((List<Mouvement>) mouvement)
                .build();
        livreurService.createLivreur(livreur);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> livreurService.createLivreur(livreur)); // Deuxième ajout avec le même code
        assertTrue(exception.getMessage().contains("Livreur avec ce code existe déjà."));
    }


    @Test
    void testUpdateLivreur_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);
        Valise valise = Valise.builder()
                .description("Valise Test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("En cours")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV001")
                .mouvements((List<Mouvement>) mouvement) // Associer le mouvement
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Modifier le nom du livreur
        createdLivreur.setNomLivreur("Updated Name");

        // Act
        Livreur updatedLivreur = livreurService.updateLivreur(createdLivreur.getId(), createdLivreur);

        // Assert
        assertNotNull(updatedLivreur);
        assertEquals("Updated Name", updatedLivreur.getNomLivreur());
        assertNotNull(updatedLivreur.getMouvements());
        assertEquals(mouvement.getId(), updatedLivreur.getMouvements().get(1));
    }



    @Test
    void testUpdateLivreur_Failure_NotFound() {
        // Act & Assert
        RegleNotFoundException exception = assertThrows(RegleNotFoundException.class, () -> {
            livreur.setId(999);
            livreurService.updateLivreur(999, livreur);
        });

        assertTrue(exception.getMessage().contains("Delivery person not found with ID"));
    }

    @Test
    void testDeleteLivreur_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Valise Test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date(System.currentTimeMillis() + 86400000L))
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 172800000L))
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .mouvements((List<Mouvement>) mouvement)
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        assertNotNull(livreurService.getLivreurById(createdLivreur.getId()));

        // Act
        livreurService.deleteLivreur((int) createdLivreur.getId());

        entityManager.flush();
        entityManager.clear();

        // Assert:
        Livreur deletedLivreur = entityManager.find(Livreur.class, createdLivreur.getId());
        assertNull(deletedLivreur, "Livreur should be null after deletion");
    }


    @Test
    void testDeleteLivreur_Failure_NotFound() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> livreurService.deleteLivreur(999));
        assertTrue(exception.getMessage().contains("delivery person not found with ID"));
    }

    @Test
    @Transactional // Ensures database state rollback after the test
    void testGetAllLivreurs() {
        // Clear previous entries if any
        livreurRepository.deleteAll();
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();

        // Arrange: Add a new client
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        // Arrange: Add a valise linked to the client
        Valise valise = Valise.builder()
                .description("Valise de test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        // Arrange: Add a mouvement linked to the valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("En cours")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        // Arrange: Add a livreur linked to the mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .mouvements((List<Mouvement>) mouvement)
                .build();
        livreurService.createLivreur(livreur);

        // Act: Retrieve all livreurs
        List<LivreurDTO> livreurs = livreurService.getAllLivreurs();

        // Assert: Verify the expected data
        assertFalse(livreurs.isEmpty(), "The list of livreurs should not be empty");
        assertEquals(1, livreurs.size(), "There should be exactly one livreur in the database");
        assertEquals("John Doe", livreurs.get(0).getNomLivreur(), "The livreur's name should match");
        assertEquals("LIV123", livreurs.get(0).getCodeLivreur(), "The livreur's code should match");
    }





    @Test
    void testGetLivreurById_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Valise Test")
                .client(client)
                .build();
        valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date(System.currentTimeMillis() + 86400000L))
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 172800000L))
                .valise(valise)
                .build();
        mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .mouvements((List<Mouvement>) mouvement) // Associer un mouvement valide
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Act
        LivreurDTO fetchedLivreur = livreurService.getLivreurById(createdLivreur.getId());

        // Assert
        assertNotNull(fetchedLivreur);
        assertEquals(createdLivreur.getNomLivreur(), fetchedLivreur.getNomLivreur());
    }


    @Test
    void testGetLivreurById_Failure_NotFound() {
        // Act
        LivreurDTO fetchedLivreur = livreurService.getLivreurById(999);

        // Assert
        assertNull(fetchedLivreur);
    }
}
