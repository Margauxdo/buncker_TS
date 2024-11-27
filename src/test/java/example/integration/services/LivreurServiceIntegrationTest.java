package example.integration.services;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.exceptions.RegleNotFoundException;
import example.repositories.ClientRepository;
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
                .mouvement(mouvement)
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
                .mouvement(mouvement)
                .build();

        // Act
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Assert
        assertNotNull(createdLivreur.getId());
        assertEquals(livreur.getNomLivreur(), createdLivreur.getNomLivreur());
        assertEquals(livreur.getCodeLivreur(), createdLivreur.getCodeLivreur());
        assertNotNull(createdLivreur.getMouvement());
        assertEquals(mouvement.getId(), createdLivreur.getMouvement().getId());
        assertEquals(valise.getId(), createdLivreur.getMouvement().getValise().getId());
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
                .mouvement(mouvement)
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
                .mouvement(mouvement) // Associer le mouvement
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Modifier le nom du livreur
        createdLivreur.setNomLivreur("Updated Name");

        // Act
        Livreur updatedLivreur = livreurService.updateLivreur(createdLivreur.getId(), createdLivreur);

        // Assert
        assertNotNull(updatedLivreur);
        assertEquals("Updated Name", updatedLivreur.getNomLivreur());
        assertNotNull(updatedLivreur.getMouvement());
        assertEquals(mouvement.getId(), updatedLivreur.getMouvement().getId());
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
                .mouvement(mouvement)
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        assertNotNull(livreurService.getLivreurById(createdLivreur.getId()));

        // Act
        livreurService.deleteLivreur((long) createdLivreur.getId());

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
    void testGetAllLivreurs() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john@doe.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Valise de test")
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
                .codeLivreur("LIV123")
                .mouvement(mouvement)
                .build();
        livreurService.createLivreur(livreur);

        // Act
        List<Livreur> livreurs = livreurService.getAllLivreurs();

        // Assert
        assertFalse(livreurs.isEmpty());
        assertEquals(1, livreurs.size());
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
                .mouvement(mouvement) // Associer un mouvement valide
                .build();
        Livreur createdLivreur = livreurService.createLivreur(livreur);

        // Act
        Livreur fetchedLivreur = livreurService.getLivreurById(createdLivreur.getId());

        // Assert
        assertNotNull(fetchedLivreur);
        assertEquals(createdLivreur.getNomLivreur(), fetchedLivreur.getNomLivreur());
    }


    @Test
    void testGetLivreurById_Failure_NotFound() {
        // Act
        Livreur fetchedLivreur = livreurService.getLivreurById(999);

        // Assert
        assertNull(fetchedLivreur);
    }
}
