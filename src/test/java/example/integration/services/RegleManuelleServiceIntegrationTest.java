package example.integration.services;

import example.entity.Regle;
import example.entity.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import example.repositories.RegleRepository;
import example.services.RegleManuelleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class RegleManuelleServiceIntegrationTest {

    @Autowired
    private RegleManuelleService regleManuelleService;
    @Autowired
    private RegleManuelleRepository regleManuelleRepository;
    private RegleManuelle regleManuelle;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        regleManuelleRepository.deleteAll();

        regleManuelle = RegleManuelle.builder()
                .coderegle("A256896lm")
                .createurRegle("John does")
                .descriptionRegle("description regle de John does")
                .build();
    }

    @Test
    public void testCreateRegleManuelle() {
        // Act
        RegleManuelle savedRM = regleManuelleService.createRegleManuelle(regleManuelle);
        // Assert
        assertNotNull(savedRM);
        assertNotNull(savedRM.getId());
        assertEquals("A256896lm", savedRM.getCoderegle());
        assertEquals("John does", savedRM.getCreateurRegle());
        assertEquals("description regle de John does", savedRM.getDescriptionRegle());
    }

    @Test
    public void testUpdateRegleManuelle() {
        // Arrange
        RegleManuelle savedRM = regleManuelleService.createRegleManuelle(regleManuelle);
        // Act
        savedRM.setCoderegle("A2568958no");
        savedRM.setCreateurRegle("Julies Martin");
        savedRM.setDescriptionRegle("description regle de Julie Martin");
        RegleManuelle updatedRM = regleManuelleService.updateRegleManuelle(savedRM.getId(), savedRM);
        // Assert
        assertNotNull(updatedRM);
        assertEquals(savedRM.getId(), updatedRM.getId());
        assertEquals("A2568958no", updatedRM.getCoderegle());
        assertEquals("Julies Martin", updatedRM.getCreateurRegle());
        assertEquals("description regle de Julie Martin", updatedRM.getDescriptionRegle());
    }

    @Test
    public void testDeleteRegleManuelle() {
        // Arrange
        RegleManuelle savedRM = regleManuelleService.createRegleManuelle(regleManuelle);

        // Act
        regleManuelleService.deleteRegleManuelle(savedRM.getId());

        // Assert
        RegleManuelle deletedRM = regleManuelleService.getRegleManuelle(savedRM.getId());
        assertNull(deletedRM, "The manual rule should be deleted and not found again.");
    }


    @Test
    public void testGetRegleManuelle() {
        // Arrange
        RegleManuelle savedRM = regleManuelleService.createRegleManuelle(regleManuelle);
        // Act
        RegleManuelle rMById = regleManuelleService.getRegleManuelle(savedRM.getId());
        // Assert
        assertNotNull(rMById);
        assertEquals(savedRM.getId(), rMById.getId());
        assertEquals("A256896lm", rMById.getCoderegle());
        assertEquals("John does", rMById.getCreateurRegle());
        assertEquals("description regle de John does", rMById.getDescriptionRegle());
    }

    @Test
    public void testGetRegleManuelles() {
        // Arrange
        RegleManuelle rm1 = RegleManuelle.builder()
                .coderegle("A56589M")
                .createurRegle("Marie Delrue")
                .descriptionRegle("description regle de Marie Delrue")
                .build();
        RegleManuelle rm2 = RegleManuelle.builder()
                .coderegle("A55698lM")
                .createurRegle("Arthur Martin")
                .descriptionRegle("description regle de Arthur Martin")
                .build();
        regleManuelleRepository.save(rm1);
        regleManuelleRepository.save(rm2);
        // Act
        List<RegleManuelle> regleManuelleList = regleManuelleService.getRegleManuelles();
        // Assert
        assertNotNull(regleManuelleList);
        assertEquals(2, regleManuelleList.size());
    }

    @Test
    public void testCascadeDeleteRegleManuelle() {
        RegleManuelle savedRegle = regleManuelleService.createRegleManuelle(regleManuelle);
        regleManuelleService.deleteRegleManuelle(savedRegle.getId());

        RegleManuelle deletedRegle = regleManuelleService.getRegleManuelle(savedRegle.getId());
        Assertions.assertNull(deletedRegle, "Manual rule should be deleted and not found again");
    }




    @Test
    public void testUpdateRegleManuelle_NotFound() {
        // Arrange
        int nonExistentId = 9999;
        RegleManuelle regleManuelleToUpdate = RegleManuelle.builder()
                .coderegle("A2568958no")
                .createurRegle("Julie Martin")
                .descriptionRegle("Description mise à jour")
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            regleManuelleService.updateRegleManuelle(nonExistentId, regleManuelleToUpdate);
        }, "La mise à jour devrait échouer car l'ID n'existe pas");
    }
    @Test
    public void testDeleteRegleManuelle_NotFound() {
        // Arrange
        int nonExistentId = 9999;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            regleManuelleService.deleteRegleManuelle(nonExistentId);
        }, "La suppression devrait échouer car l'ID n'existe pas");
    }





}
