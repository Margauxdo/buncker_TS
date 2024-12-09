package example.integration.services;

import example.DTO.TypeRegleDTO;
import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import example.services.TypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class TypeRegleServiceIntegrationTest {

    @Autowired
    private TypeRegleService typeRegleService;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private RegleRepository regleRepository;

    private Regle regle;

    @BeforeEach
    void setUp() {
        // Clean up repository
        typeRegleRepository.deleteAll();
        regleRepository.deleteAll();

        // Create and save a Regle instance
        regle = Regle.builder()
                .coderegle("REGLE123")
                .build();
        regle = regleRepository.save(regle);
    }

    @Test
    public void testCreateTypeRegle_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Test TypeRegle")
                .regleId(regle.getId())
                .build();

        // Act
        TypeRegleDTO savedTypeRegleDTO = typeRegleService.createTypeRegle(typeRegleDTO);

        // Assert
        assertNotNull(savedTypeRegleDTO);
        assertNotNull(savedTypeRegleDTO.getId());
        assertEquals("Test TypeRegle", savedTypeRegleDTO.getNomTypeRegle());
        assertEquals(regle.getId(), savedTypeRegleDTO.getRegleId());
    }

    @Test
    public void testCreateTypeRegle_Failure_NoRegle() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Invalid TypeRegle")
                .regleId(999) // ID inexistant
                .build();

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            typeRegleService.createTypeRegle(typeRegleDTO);
        });

        assertEquals("La règle associée avec l'ID 999 est introuvable", exception.getMessage());
    }

    @Test
    public void testUpdateTypeRegle_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Initial TypeRegle")
                .regleId(regle.getId())
                .build();
        TypeRegleDTO savedTypeRegleDTO = typeRegleService.createTypeRegle(typeRegleDTO);

        TypeRegleDTO updatedDTO = TypeRegleDTO.builder()
                .id(savedTypeRegleDTO.getId())
                .nomTypeRegle("Updated TypeRegle")
                .regleId(regle.getId())
                .build();

        // Act
        TypeRegleDTO result = typeRegleService.updateTypeRegle(savedTypeRegleDTO.getId(), updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated TypeRegle", result.getNomTypeRegle());
    }

    @Test
    public void testUpdateTypeRegle_Failure_NotFound() {
        // Arrange
        TypeRegleDTO updatedDTO = TypeRegleDTO.builder()
                .id(999)
                .nomTypeRegle("Nonexistent TypeRegle")
                .regleId(regle.getId())
                .build();

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            typeRegleService.updateTypeRegle(999, updatedDTO);
        });

        assertEquals("TypeRegle avec l'ID 999 est introuvable", exception.getMessage());
    }

    @Test
    public void testGetTypeRegle_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("TypeRegle A")
                .regleId(regle.getId())
                .build();
        TypeRegleDTO savedTypeRegleDTO = typeRegleService.createTypeRegle(typeRegleDTO);

        // Act
        TypeRegleDTO fetchedTypeRegleDTO = typeRegleService.getTypeRegle(savedTypeRegleDTO.getId());

        // Assert
        assertNotNull(fetchedTypeRegleDTO);
        assertEquals(savedTypeRegleDTO.getNomTypeRegle(), fetchedTypeRegleDTO.getNomTypeRegle());
    }

    @Test
    public void testGetAllTypeRegles() {
        // Arrange
        TypeRegleDTO typeRegleDTO1 = TypeRegleDTO.builder()
                .nomTypeRegle("TypeRegle 1")
                .regleId(regle.getId())
                .build();
        typeRegleService.createTypeRegle(typeRegleDTO1);

        TypeRegleDTO typeRegleDTO2 = TypeRegleDTO.builder()
                .nomTypeRegle("TypeRegle 2")
                .regleId(regle.getId())
                .build();
        typeRegleService.createTypeRegle(typeRegleDTO2);

        // Act
        List<TypeRegleDTO> typeRegleDTOList = typeRegleService.getTypeRegles();

        // Assert
        assertNotNull(typeRegleDTOList);
        assertEquals(2, typeRegleDTOList.size());
    }

    @Test
    public void testDeleteTypeRegle_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Deletable TypeRegle")
                .regleId(regle.getId())
                .build();
        TypeRegleDTO savedTypeRegleDTO = typeRegleService.createTypeRegle(typeRegleDTO);

        // Act
        typeRegleService.deleteTypeRegle(savedTypeRegleDTO.getId());

        // Assert
        Optional<TypeRegle> deletedTypeRegle = typeRegleRepository.findById(savedTypeRegleDTO.getId());
        assertTrue(deletedTypeRegle.isEmpty());
    }
}
