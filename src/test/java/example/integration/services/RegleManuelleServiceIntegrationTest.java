package example.integration.services;

import example.entity.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import example.services.RegleManuelleService;
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
    private RegleManuelle regleManuelle;

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
}
