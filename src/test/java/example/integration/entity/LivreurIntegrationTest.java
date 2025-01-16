package example.integration.entity;

import example.entity.Livreur;
import example.entity.Mouvement;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LivreurIntegrationTest {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        livreurRepository.deleteAll();
    }

    @Test
    void testCreateLivreur() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setPrenomLivreur("John");
        livreur.setCodeLivreur("CODE123");
        livreur.setTelephonePortable("1234567890");

        // Act
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        // Assert
        assertNotNull(savedLivreur.getId());
        assertEquals("Doe", savedLivreur.getNomLivreur());
        assertEquals("John", savedLivreur.getPrenomLivreur());
        assertEquals("CODE123", savedLivreur.getCodeLivreur());
    }

    @Test
    void testReadLivreur() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Smith");
        livreur.setPrenomLivreur("Anna");
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        // Act
        Optional<Livreur> foundLivreur = livreurRepository.findById(savedLivreur.getId());

        // Assert
        assertTrue(foundLivreur.isPresent());
        assertEquals(savedLivreur.getId(), foundLivreur.get().getId());
        assertEquals("Smith", foundLivreur.get().getNomLivreur());
        assertEquals("Anna", foundLivreur.get().getPrenomLivreur());
    }

    @Test
    void testUpdateLivreur() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Brown");
        livreur.setPrenomLivreur("Lucy");
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        // Act
        savedLivreur.setNomLivreur("Brown Updated");
        savedLivreur.setPrenomLivreur("Lucy Updated");
        Livreur updatedLivreur = livreurRepository.saveAndFlush(savedLivreur);

        // Assert
        assertEquals("Brown Updated", updatedLivreur.getNomLivreur());
        assertEquals("Lucy Updated", updatedLivreur.getPrenomLivreur());
    }

    @Test
    void testDeleteLivreur() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("White");
        livreur.setPrenomLivreur("Charlie");
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        // Act
        livreurRepository.delete(savedLivreur);
        Optional<Livreur> deletedLivreur = livreurRepository.findById(savedLivreur.getId());

        // Assert
        assertTrue(deletedLivreur.isEmpty());
    }



    @Test
    void testDeleteLivreurWithMouvements() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Blue");
        livreur.setPrenomLivreur("Tom");
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        Mouvement mouvement = new Mouvement();
        mouvement.setLivreur(savedLivreur); // Associate the movement with the livreur
        mouvementRepository.saveAndFlush(mouvement);

        // Act
        mouvementRepository.deleteAll(); // Delete all movements first
        livreurRepository.delete(savedLivreur);
        Optional<Livreur> deletedLivreur = livreurRepository.findById(savedLivreur.getId());

        // Assert
        assertTrue(deletedLivreur.isEmpty());
    }
}
