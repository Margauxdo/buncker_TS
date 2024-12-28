/*package example.integration.services;

import example.DTO.RegleManuelleDTO;
import example.entity.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import example.services.RegleManuelleService;
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
public class RegleManuelleServiceIntegrationTest {

    @Autowired
    private RegleManuelleService regleManuelleService;

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    private RegleManuelleDTO regleManuelle;

    @BeforeEach
    void setUp() {
        // Nettoyer les données avant chaque test
        regleManuelleRepository.deleteAll();

        // Initialisation de l'objet DTO pour les tests
        regleManuelle = RegleManuelleDTO.builder()
                .coderegle("A256896lm")
                .createurRegle("John Doe")
                .descriptionRegle("Description règle de John Doe")
                .build();
    }

    @Test
    public void testCreateRegleManuelle() {
        // Act
        RegleManuelleDTO savedRM = regleManuelleService.createRegleManuelle(regleManuelle);

        // Assert
        assertNotNull(savedRM);
        assertNotNull(savedRM.getId());
        assertEquals("A256896lm", savedRM.getCoderegle());
        assertEquals("John Doe", savedRM.getCreateurRegle());
        assertEquals("Description règle de John Doe", savedRM.getDescriptionRegle());
    }

    @Test
    public void testUpdateRegleManuelle() {
        // Arrange
        RegleManuelleDTO savedRM = regleManuelleService.createRegleManuelle(regleManuelle);

        // Modifier les champs pour le test
        savedRM.setCoderegle("UPDATED_CODE");
        savedRM.setCreateurRegle("Jane Doe");
        savedRM.setDescriptionRegle("Description mise à jour");

        // Act
        RegleManuelleDTO updatedRM = regleManuelleService.updateRegleManuelle(savedRM.getId(), savedRM);

        // Assert
        assertNotNull(updatedRM);
        assertEquals(savedRM.getId(), updatedRM.getId());
        assertEquals("UPDATED_CODE", updatedRM.getCoderegle());
        assertEquals("Jane Doe", updatedRM.getCreateurRegle());
        assertEquals("Description mise à jour", updatedRM.getDescriptionRegle());
    }

    @Test
    public void testDeleteRegleManuelle() {
        // Arrange
        RegleManuelleDTO savedRM = regleManuelleService.createRegleManuelle(regleManuelle);

        // Act
        regleManuelleService.deleteRegleManuelle(savedRM.getId());

        // Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            regleManuelleService.getRegleManuelle(savedRM.getId());
        });
        assertEquals("Manual Ruler not found with ID " + savedRM.getId(), exception.getMessage());
    }

    @Test
    public void testGetRegleManuelle() {
        // Arrange
        RegleManuelleDTO savedRM = regleManuelleService.createRegleManuelle(regleManuelle);

        // Act
        RegleManuelleDTO fetchedRM = regleManuelleService.getRegleManuelle(savedRM.getId());

        // Assert
        assertNotNull(fetchedRM);
        assertEquals(savedRM.getId(), fetchedRM.getId());
        assertEquals("A256896lm", fetchedRM.getCoderegle());
        assertEquals("John Doe", fetchedRM.getCreateurRegle());
        assertEquals("Description règle de John Doe", fetchedRM.getDescriptionRegle());
    }

    @Test
    public void testGetRegleManuelles() {
        // Arrange
        RegleManuelleDTO rm1 = RegleManuelleDTO.builder()
                .coderegle("CODE1")
                .createurRegle("Creator 1")
                .descriptionRegle("Description 1")
                .build();

        RegleManuelleDTO rm2 = RegleManuelleDTO.builder()
                .coderegle("CODE2")
                .createurRegle("Creator 2")
                .descriptionRegle("Description 2")
                .build();

        regleManuelleService.createRegleManuelle(rm1);
        regleManuelleService.createRegleManuelle(rm2);

        // Act
        List<RegleManuelleDTO> regleManuelleList = regleManuelleService.getRegleManuelles();

        // Assert
        assertNotNull(regleManuelleList);
        assertEquals(2, regleManuelleList.size());
    }

    @Test
    public void testDeleteRegleManuelle_NotFound() {
        // Arrange
        int nonExistentId = 9999;

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            regleManuelleService.deleteRegleManuelle(nonExistentId);
        });
        assertEquals("Manual Ruler not found with ID " + nonExistentId, exception.getMessage());
    }
}*/
