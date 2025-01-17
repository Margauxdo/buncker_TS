package example.integration.repositories;

import example.entity.*;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import example.repositories.TypeRegleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class RegleRepositoryIntegrationTest {

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @BeforeEach
    public void setUp() {
        regleRepository.deleteAll();
        sortieSemaineRepository.deleteAll();
        typeRegleRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R123");
        regle.setDateRegle(new Date());
        regle.setNombreJours(5);

        // Act
        Regle savedRegle = regleRepository.save(regle);
        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());

        // Assert
        assertTrue(foundRegle.isPresent());
        assertEquals("R123", foundRegle.get().getCoderegle());
        assertEquals(5, foundRegle.get().getNombreJours());
    }

    @Test
    public void testUpdateRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R456");
        regle = regleRepository.save(regle);

        // Act
        regle.setCoderegle("R789");
        Regle updatedRegle = regleRepository.save(regle);

        // Assert
        assertEquals("R789", updatedRegle.getCoderegle());
    }

    @Test
    public void testDeleteRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R321");
        regle = regleRepository.save(regle);

        // Act
        regleRepository.delete(regle);
        Optional<Regle> deletedRegle = regleRepository.findById(regle.getId());

        // Assert
        assertFalse(deletedRegle.isPresent());
    }

    @Test
    public void testFindAllRegles() {
        // Arrange
        Regle regle1 = new Regle();
        regle1.setCoderegle("R101");
        regleRepository.save(regle1);

        Regle regle2 = new Regle();
        regle2.setCoderegle("R102");
        regleRepository.save(regle2);

        // Act
        List<Regle> regles = regleRepository.findAll();

        // Assert
        assertEquals(2, regles.size());
        regles.sort(Comparator.comparing(Regle::getCoderegle));
        assertEquals("R101", regles.get(0).getCoderegle());
        assertEquals("R102", regles.get(1).getCoderegle());
    }

    @Test
    public void testSaveInvalidRegle() {
        // Arrange
        Regle regle = new Regle(); // Missing required fields

        // Act & Assert
        assertThrows(Exception.class, () -> regleRepository.saveAndFlush(regle));
    }

    @Test
    public void testCascadeDeleteWithFormule() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("Test Formule");

        Regle regle = new Regle();
        regle.setCoderegle("R800");
        regle.setFormule(formule);
        regleRepository.save(regle);

        // Act
        regleRepository.delete(regle);

        // Assert
        List<Regle> remainingRegles = regleRepository.findAll();
        assertTrue(remainingRegles.isEmpty());
    }

    @Test
    public void testFindRegleByNonExistentId() {
        // Act
        Optional<Regle> foundRegle = regleRepository.findById(-1);

        // Assert
        assertFalse(foundRegle.isPresent());
    }
}
