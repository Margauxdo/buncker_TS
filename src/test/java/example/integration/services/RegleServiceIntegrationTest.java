package example.integration.services;

import example.DTO.RegleDTO;
import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.repositories.RegleRepository;
import example.services.RegleService;
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

    private RegleDTO regleDTO;

    @BeforeEach
    void setUp() {
        // Clean up the repository
        regleRepository.deleteAll();

        // Initialize a RegleDTO instance
        regleDTO = RegleDTO.builder()
                .coderegle("REGLE123")
                .reglePourSortie("Sortie spéciale")
                .build();
    }

    @Test
    public void testCreateRegle_Success() {
        // Act
        RegleDTO savedRegle = regleService.createRegle(regleDTO);

        // Assert
        assertNotNull(savedRegle);
        assertNotNull(savedRegle.getId());
        assertEquals("REGLE123", savedRegle.getCoderegle());
        assertEquals("Sortie spéciale", savedRegle.getReglePourSortie());
    }

    @Test
    public void testCreateRegle_Failure_NullCoderegle() {
        // Arrange
        RegleDTO invalidRegle = RegleDTO.builder()
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
        RegleDTO savedRegle = regleService.createRegle(regleDTO);
        savedRegle.setCoderegle("UPDATED_CODE");

        // Act
        RegleDTO updatedRegle = regleService.updateRegle(savedRegle.getId(), savedRegle);

        // Assert
        assertNotNull(updatedRegle);
        assertEquals(savedRegle.getId(), updatedRegle.getId());
        assertEquals("UPDATED_CODE", updatedRegle.getCoderegle());
    }

    @Test
    public void testUpdateRegle_Failure_NotFound() {
        // Arrange
        RegleDTO updatedRegle = RegleDTO.builder()
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
        RegleDTO savedRegle = regleService.createRegle(regleDTO);

        // Act
        RegleDTO fetchedRegle = regleService.readRegle(savedRegle.getId());

        // Assert
        assertNotNull(fetchedRegle);
        assertEquals(savedRegle.getId(), fetchedRegle.getId());
        assertEquals("REGLE123", fetchedRegle.getCoderegle());
    }

    @Test
    public void testGetRegle_Failure_NotFound() {
        // Act & Assert
        RegleNotFoundException exception = assertThrows(RegleNotFoundException.class, () -> {
            regleService.readRegle(999);
        });

        assertEquals("Regle not found with ID 999", exception.getMessage());
    }

    @Test
    public void testGetAllRegles() {
        // Arrange
        RegleDTO regle1 = RegleDTO.builder()
                .coderegle("REGLE001")
                .build();

        RegleDTO regle2 = RegleDTO.builder()
                .coderegle("REGLE002")
                .build();

        regleService.createRegle(regle1);
        regleService.createRegle(regle2);

        // Act
        List<RegleDTO> regles = regleService.readAllRegles();

        // Assert
        assertNotNull(regles);
        assertEquals(2, regles.size());

        assertTrue(regles.stream().anyMatch(r -> "REGLE001".equals(r.getCoderegle())));
        assertTrue(regles.stream().anyMatch(r -> "REGLE002".equals(r.getCoderegle())));
    }

    @Test
    public void testDeleteRegle_Success() {
        // Arrange
        RegleDTO savedRegle = regleService.createRegle(regleDTO);

        // Act
        regleService.deleteRegle(savedRegle.getId());

        // Assert
        assertThrows(RegleNotFoundException.class, () -> regleService.readRegle(savedRegle.getId()));
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
