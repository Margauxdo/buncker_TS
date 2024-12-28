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
        Regle savedRegle = regleService.createRegle(regleDTO);

        // Assert
        assertNotNull(savedRegle);
        assertNotNull(savedRegle.getId());
        assertEquals("REGLE123", savedRegle.getCoderegle());
        assertEquals("Sortie spéciale", savedRegle.getReglePourSortie());
    }

    @Test
    public void testUpdateRegle_Success() {
        // Arrange
        Regle savedRegle = regleService.createRegle(regleDTO);
        savedRegle.setCoderegle("UPDATED_CODE");

        // Act
        //RegleDTO updatedRegle = regleService.updateRegle(savedRegle.getId());

        // Assert
        //assertNotNull(updatedRegle);
        //assertEquals(savedRegle.getId(), updatedRegle.getId());
        //assertEquals("UPDATED_CODE", updatedRegle.getCoderegle());
    }


}
