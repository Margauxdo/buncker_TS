package example.integration.services;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import example.services.TypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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

    private Regle regle;
    private TypeRegle typeRegle;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        // Clean up repository
        typeRegleRepository.deleteAll();

        // Prepare a Regle instance
        regle = Regle.builder()
                .coderegle("REGLE123")
                .build();

        // Prepare a TypeRegle instance
        typeRegle = TypeRegle.builder()
                .nomTypeRegle("Test TypeRegle")
                .regle(regle)
                .build();
    }

    @Test
    public void testCreateTypeRegle_Success() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("REGLE123")
                .build();
        regle = regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Test TypeRegle")
                .regle(regle)
                .build();

        // Act
        TypeRegle savedTypeRegle = typeRegleService.createTypeRegle(typeRegle);

        // Assert
        assertNotNull(savedTypeRegle);
        assertNotNull(savedTypeRegle.getId());
        assertEquals("Test TypeRegle", savedTypeRegle.getNomTypeRegle());
        assertNotNull(savedTypeRegle.getRegle());
        assertEquals("REGLE123", savedTypeRegle.getRegle().getCoderegle());
    }



    @Test
    public void testCreateTypeRegle_Failure_NoRegle() {
        // Arrange
        typeRegle.setRegle(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            typeRegleService.createTypeRegle(typeRegle);
        });

        assertEquals("La Regle associée ne peut pas être null", exception.getMessage(),
                "Expected exception message does not match the actual message.");
    }


    @Test
    public void testUpdateTypeRegle_Success() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("REGLE123")
                .build();
        regle = regleRepository.save(regle);
        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Initial TypeRegle")
                .regle(regle)
                .build();
        TypeRegle savedTypeRegle = typeRegleService.createTypeRegle(typeRegle);

        savedTypeRegle.setNomTypeRegle("Updated TypeRegle");

        // Act
        TypeRegle updatedTypeRegle = typeRegleService.updateTypeRegle(savedTypeRegle.getId(), savedTypeRegle);

        // Assert
        assertNotNull(updatedTypeRegle);
        assertEquals(savedTypeRegle.getId(), updatedTypeRegle.getId());
        assertEquals("Updated TypeRegle", updatedTypeRegle.getNomTypeRegle());
        assertNotNull(updatedTypeRegle.getRegle());
        assertEquals("REGLE123", updatedTypeRegle.getRegle().getCoderegle());
    }




    @Test
    public void testGetTypeRegle_Failure_NotFound() {
        // Act
        Optional<TypeRegle> fetchedTypeRegle = typeRegleService.getTypeRegle(999);

        // Assert
        Assertions.assertTrue(fetchedTypeRegle.isEmpty(), "TypeRegle with ID 999 should not exist.");
    }


    @Test
    public void testGetAllTypeRegles() {
        // Arrange
        Regle regle1 = Regle.builder()
                .coderegle("REGLE123")
                .build();
        regle1 = regleRepository.save(regle1);

        Regle regle2 = Regle.builder()
                .coderegle("REGLE456")
                .build();
        regle2 = regleRepository.save(regle2);

        TypeRegle typeRegle1 = TypeRegle.builder()
                .nomTypeRegle("TypeRegle 1")
                .regle(regle1)
                .build();
        typeRegleService.createTypeRegle(typeRegle1);

        TypeRegle typeRegle2 = TypeRegle.builder()
                .nomTypeRegle("TypeRegle 2")
                .regle(regle2)
                .build();
        typeRegleService.createTypeRegle(typeRegle2);

        // Act
        List<TypeRegle> typeRegles = typeRegleService.getTypeRegles();

        // Assert
        assertNotNull(typeRegles, "La liste des TypeRegle ne devrait pas être nulle.");
        assertEquals(2, typeRegles.size(), "La liste des TypeRegle devrait contenir 2 éléments.");
        assertEquals("TypeRegle 1", typeRegles.get(0).getNomTypeRegle());
        assertEquals("TypeRegle 2", typeRegles.get(1).getNomTypeRegle());
    }




    @Test
    public void testDeleteTypeRegle_Failure_NotFound() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            typeRegleService.deleteTypeRegle(999);
        });

        assertEquals("TypeRegle avec l'ID 999 est introuvable", exception.getMessage(),
                "Expected exception message does not match the actual message.");
    }



    @Test
    public void testDeleteTypeRegle_Success() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("REGLE123")
                .build();
        regle = regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Deletable TypeRegle")
                .regle(regle)
                .build();
        typeRegle = typeRegleService.createTypeRegle(typeRegle);

        // Act
        typeRegleService.deleteTypeRegle(typeRegle.getId());

        // Assert
        assertFalse(typeRegleRepository.existsById(typeRegle.getId()));
    }


}
