package example.integration.services;

import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeRegle;
import example.entity.Valise;
import example.exceptions.RegleNotFoundException;
import example.repositories.RegleRepository;
import example.services.RegleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class RegleServiceIntegrationTest {

    @Autowired
    private RegleService regleService;

    @Autowired
    private RegleRepository regleRepository;

    private Regle regle;

    @BeforeEach
    void setUp() {
        // Clean up the repository
        regleRepository.deleteAll();

        // Initialize a Regle instance
        regle = Regle.builder()
                .coderegle("REGLE123")
                .build();
    }

    @Test
    public void testCreateRegle_Success() {
        // Arrange
        Regle newRegle = Regle.builder()
                .coderegle("REGLE001")
                .build();

        // Act
        Regle savedRegle = regleService.createRegle(newRegle);

        // Assert
        assertNotNull(savedRegle);
        assertNotNull(savedRegle.getId());
        assertEquals("REGLE001", savedRegle.getCoderegle());
    }

    @Test
    public void testCreateRegle_Failure_NullCoderegle() {
        // Arrange
        Regle invalidRegle = Regle.builder()
                .coderegle(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            regleService.createRegle(invalidRegle);
        });

        assertEquals("Coderegle cannot be null or empty", exception.getMessage());
    }


    @Test
    public void testUpdateRegle_Success() {
        // Arrange
        Regle savedRegle = regleService.createRegle(regle);
        savedRegle.setCoderegle("UPDATED_CODE");

        // Act
        Regle updatedRegle = regleService.updateRegle(savedRegle.getId(), savedRegle);

        // Assert
        assertNotNull(updatedRegle);
        assertEquals(savedRegle.getId(), updatedRegle.getId());
        assertEquals("UPDATED_CODE", updatedRegle.getCoderegle());
    }

    @Test
    public void testUpdateRegle_Failure_NotFound() {
        // Arrange
        Regle updatedRegle = Regle.builder()
                .coderegle("NON_EXISTENT_CODE")
                .build();

        // Act & Assert
        RegleNotFoundException exception = assertThrows(RegleNotFoundException.class, () -> {
            regleService.updateRegle(999, updatedRegle);
        });

        assertEquals("Règle non trouvée avec l'ID 999", exception.getMessage());
    }


    @Test
    public void testGetRegle_Success() {
        // Arrange
        Regle savedRegle = regleService.createRegle(regle);

        // Act
        Regle fetchedRegle = regleService.readRegle(savedRegle.getId()); // Use the appropriate method to fetch by ID

        // Assert
        assertNotNull(fetchedRegle, "Fetched Regle should not be null.");
        assertEquals(savedRegle.getId(), fetchedRegle.getId(), "The fetched Regle ID should match the saved Regle ID.");
        assertEquals("REGLE123", fetchedRegle.getCoderegle(), "The coderegle of the fetched Regle should match the saved one.");
    }


    @Test
    public void testGetRegle_Failure_NotFound() {
        // Act & Assert
        RegleNotFoundException exception = assertThrows(RegleNotFoundException.class, () -> {
            regleService.readRegleById(999L);
        });

        assertEquals("Regle not found with ID 999", exception.getMessage());
    }


    @Test
    public void testGetAllRegles() {
        // Arrange
        Regle regle1 = Regle.builder()
                .coderegle("REGLE001")
                .build();

        Regle regle2 = Regle.builder()
                .coderegle("REGLE002")
                .build();

        regleService.createRegle(regle1);
        regleService.createRegle(regle2);

        // Act
        List<Regle> regles = regleService.readAllRegles();

        // Assert
        assertNotNull(regles, "The list of Regles should not be null.");
        assertEquals(2, regles.size(), "The list of Regles should contain 2 items.");
        assertEquals("REGLE001", regles.get(0).getCoderegle());
        assertEquals("REGLE002", regles.get(1).getCoderegle());
    }


    @Test
    public void testDeleteRegle_Success() {
        // Arrange
        Regle savedRegle = regleService.createRegle(regle);

        // Act
        regleService.deleteRegle(savedRegle.getId());

        // Assert
        assertThrows(RegleNotFoundException.class, () -> regleService.readRegle(savedRegle.getId()),
                "Expected RegleNotFoundException when trying to read a deleted Regle.");
    }


    @Test
    public void testDeleteRegle_Failure_NotFound() {
        // Act & Assert
        RegleNotFoundException exception = assertThrows(RegleNotFoundException.class, () -> {
            regleService.deleteRegle(999);
        });

        assertEquals("Ruler with id 999 not found, cannot delete", exception.getMessage());
    }

}
