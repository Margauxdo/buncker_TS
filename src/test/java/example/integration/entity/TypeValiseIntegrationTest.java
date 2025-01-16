package example.integration.entity;

import example.entity.TypeValise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class TypeValiseIntegrationTest {

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        typeValiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveTypeValiseSuccess() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("This is a test");
        typeValise.setProprietaire("Jean Bernard");

        // Act
        TypeValise savedTypeValise = typeValiseRepository.saveAndFlush(typeValise);

        // Assert
        assertNotNull(savedTypeValise.getId());
        assertEquals("This is a test", savedTypeValise.getDescription());
        assertEquals("Jean Bernard", savedTypeValise.getProprietaire());
    }

    @Test
    public void testSaveTypeValiseFailure() {
        // Arrange
        TypeValise typeValise = new TypeValise(); // Missing required fields

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> typeValiseRepository.saveAndFlush(typeValise));
    }

    @Test
    public void testFindTypeValiseByIdSuccess() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Find Test");
        typeValise.setProprietaire("Owner");
        TypeValise savedTypeValise = typeValiseRepository.saveAndFlush(typeValise);

        // Act
        Optional<TypeValise> foundTypeValise = typeValiseRepository.findById(savedTypeValise.getId());

        // Assert
        assertTrue(foundTypeValise.isPresent());
        assertEquals("Find Test", foundTypeValise.get().getDescription());
        assertEquals("Owner", foundTypeValise.get().getProprietaire());
    }

    @Test
    public void testFindTypeValiseByIdNotFound() {
        // Act
        Optional<TypeValise> foundTypeValise = typeValiseRepository.findById(-1);

        // Assert
        assertFalse(foundTypeValise.isPresent());
    }

    @Test
    public void testUpdateTypeValiseSuccess() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Initial Description");
        typeValise.setProprietaire("Initial Owner");
        TypeValise savedTypeValise = typeValiseRepository.saveAndFlush(typeValise);

        // Act
        savedTypeValise.setDescription("Updated Description");
        savedTypeValise.setProprietaire("Updated Owner");
        TypeValise updatedTypeValise = typeValiseRepository.saveAndFlush(savedTypeValise);

        // Assert
        assertEquals("Updated Description", updatedTypeValise.getDescription());
        assertEquals("Updated Owner", updatedTypeValise.getProprietaire());
    }

    @Test
    public void testDeleteTypeValiseSuccess() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("To Be Deleted");
        typeValise.setProprietaire("Owner");
        TypeValise savedTypeValise = typeValiseRepository.saveAndFlush(typeValise);

        // Act
        typeValiseRepository.deleteById(savedTypeValise.getId());
        Optional<TypeValise> deletedTypeValise = typeValiseRepository.findById(savedTypeValise.getId());

        // Assert
        assertFalse(deletedTypeValise.isPresent());
    }

    @Test
    public void testUniqueConstraintOnProprietaireAndDescription() {
        // Arrange
        TypeValise typeValise1 = new TypeValise();
        typeValise1.setDescription("Unique Description");
        typeValise1.setProprietaire("Owner");
        typeValiseRepository.saveAndFlush(typeValise1);

        TypeValise typeValise2 = new TypeValise();
        typeValise2.setDescription("Unique Description"); // Same description
        typeValise2.setProprietaire("Owner"); // Same owner

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> typeValiseRepository.saveAndFlush(typeValise2));
    }

    @Test
    public void testSaveTypeValiseWithoutProprietaireShouldFail() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Description without owner");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> typeValiseRepository.saveAndFlush(typeValise));
    }

    @Test
    public void testSaveTypeValiseWithoutDescriptionShouldFail() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Owner without description");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> typeValiseRepository.saveAndFlush(typeValise));
    }
}
