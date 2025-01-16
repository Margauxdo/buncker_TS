package example.integration.entity;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class TypeRegleIntegrationTest {

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        typeRegleRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testCreateTypeRegleSuccess() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type 1");
        typeRegle.setDescription("Description for Type 1");

        // Act
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Assert
        assertNotNull(savedTypeRegle.getId());
        assertEquals("Type 1", savedTypeRegle.getNomTypeRegle());
        assertEquals("Description for Type 1", savedTypeRegle.getDescription());
    }

    @Test
    public void testUpdateTypeRegleSuccess() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Old Name");
        typeRegle.setDescription("Old Description");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Act
        savedTypeRegle.setNomTypeRegle("Updated Name");
        savedTypeRegle.setDescription("Updated Description");
        TypeRegle updatedTypeRegle = typeRegleRepository.saveAndFlush(savedTypeRegle);

        // Assert
        assertEquals("Updated Name", updatedTypeRegle.getNomTypeRegle());
        assertEquals("Updated Description", updatedTypeRegle.getDescription());
    }

    @Test
    public void testDeleteTypeRegleSuccess() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("To Be Deleted");
        typeRegle.setDescription("Description for deletion");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Act
        typeRegleRepository.deleteById(savedTypeRegle.getId());
        Optional<TypeRegle> deletedTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());

        // Assert
        assertFalse(deletedTypeRegle.isPresent());
    }

    @Test
    public void testFindTypeRegleByIdSuccess() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Find Me");
        typeRegle.setDescription("Description to be found");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Act
        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());

        // Assert
        assertTrue(foundTypeRegle.isPresent());
        assertEquals("Find Me", foundTypeRegle.get().getNomTypeRegle());
    }

    @Test
    public void testFindTypeRegleByIdNotFound() {
        // Act
        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(-1);

        // Assert
        assertFalse(foundTypeRegle.isPresent());
    }

    @Test
    public void testSaveTypeRegleFailure() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle(); // Missing required field `nomTypeRegle`

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle);
        });
    }

    @Test
    public void testTypeRegleWithRegles() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type with Rules");
        typeRegle.setDescription("Description for Type with Rules");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setTypeRegle(savedTypeRegle);
        regleRepository.saveAndFlush(regle);

        // Act
        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());

        // Assert
        assertTrue(foundTypeRegle.isPresent());
        assertEquals("Type with Rules", foundTypeRegle.get().getNomTypeRegle());
    }
}
