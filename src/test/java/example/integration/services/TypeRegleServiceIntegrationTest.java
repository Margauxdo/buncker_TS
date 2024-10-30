package example.integration.services;

import example.entities.TypeRegle;
import example.repositories.TypeRegleRepository;
import example.services.TypeRegleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class TypeRegleServiceIntegrationTest {

    @Autowired
    private TypeRegleService typeRegleService;
    @Autowired
    private TypeRegleRepository typeRegleRepository;

    private TypeRegle typeRegle;

    @BeforeEach
    void setUp() {
        typeRegleRepository.deleteAll();

        typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type test")
                .build();
        typeRegle = typeRegleService.createTypeRegle(typeRegle);
    }

    @Test
    public void testCreateTypeRegle() {
        // Act
        TypeRegle savedTR = typeRegleService.createTypeRegle(typeRegle);

        // Assert
        assertNotNull(savedTR);
        assertEquals("Type test", savedTR.getNomTypeRegle());
    }

    @Test
    public void testUpdateTypeRegle() {
        // Arrange
        TypeRegle savedTR = typeRegleService.createTypeRegle(typeRegle);
        // Act
        savedTR.setNomTypeRegle("new type test");
        TypeRegle updatedTR = typeRegleService.updateTypeRegle(savedTR.getId(),savedTR);
        // Assert
        assertNotNull(updatedTR);
        assertEquals("new type test", updatedTR.getNomTypeRegle());

    }
    @Test
    public void testDeleteTypeRegle() {
        // Arrange
        TypeRegle savedTR = typeRegleService.createTypeRegle(typeRegle);

        // Act
        typeRegleService.deleteTypeRegle(savedTR.getId());

        // Assert
        boolean isDeleted = typeRegleRepository.findById(savedTR.getId()).isEmpty();
        assertTrue(isDeleted, "L'entité TypeRegle devrait être supprimée de la base de données");
    }

    @Test
    public void testGetTypeRegle() {
        // Arrange
        TypeRegle savedTR = typeRegleService.createTypeRegle(typeRegle);
        //Act
        TypeRegle retrievedTR = typeRegleService.getTypeRegle(savedTR.getId());
        //Assert
        assertNotNull(retrievedTR);
        assertEquals("Type test", retrievedTR.getNomTypeRegle());
    }
    @Test
    public void testGetTypeRegles() {
        // Arrange
        typeRegleRepository.deleteAll();
        TypeRegle typeRegle1 = TypeRegle.builder()
                .nomTypeRegle("Type commerce")
                .build();
        TypeRegle typeRegle2 = TypeRegle.builder()
                .nomTypeRegle("type banque")
                .build();
        typeRegleService.createTypeRegle(typeRegle1);
        typeRegleService.createTypeRegle(typeRegle2);
        // Act
        List<TypeRegle> typeRegles = typeRegleService.getTypeRegles();
        //Assert
        assertNotNull(typeRegles);
        assertEquals(2, typeRegles.size());
    }

}

