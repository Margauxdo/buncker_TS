package example.integration.services;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import example.services.TypeValiseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TypeValiseServiceIntegrationTest {

    @Autowired
    private TypeValiseService typeValiseService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testCreateTypeValise_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client);
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .build();
        valiseRepository.save(valise);

        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valise(valise)
                .build();

        // Act
        TypeValise createdTypeValise = typeValiseService.createTypeValise(typeValise);

        // Assert
        assertNotNull(createdTypeValise.getId());
        assertEquals("John Doe", createdTypeValise.getProprietaire());
        assertEquals("Test Description", createdTypeValise.getDescription());
        assertNotNull(createdTypeValise.getValise());
        assertEquals("Test Valise", createdTypeValise.getValise().getDescription());
    }


    @Test
    void testGetTypeValiseById_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .build();
        valiseRepository.save(valise);

        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valise(valise)
                .build();
        TypeValise savedTypeValise = typeValiseService.createTypeValise(typeValise);

        // Act
        TypeValise foundTypeValise = typeValiseService.getTypeValise(savedTypeValise.getId());

        // Assert
        assertNotNull(foundTypeValise);
        assertEquals("John Doe", foundTypeValise.getProprietaire());
        assertEquals("Test Description", foundTypeValise.getDescription());
        assertNotNull(foundTypeValise.getValise());
        assertEquals(valise.getId(), foundTypeValise.getValise().getId());
    }


    @Test
    void testUpdateTypeValise_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .build();
        valiseRepository.save(valise);

        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valise(valise)
                .build();
        TypeValise savedTypeValise = typeValiseService.createTypeValise(typeValise); // Save the TypeValise

        savedTypeValise.setDescription("Updated Description");

        // Act
        TypeValise updatedTypeValise = typeValiseService.updateTypeValise(savedTypeValise.getId(), savedTypeValise);

        // Assert
        assertNotNull(updatedTypeValise);
        assertEquals("Updated Description", updatedTypeValise.getDescription());
        assertEquals(savedTypeValise.getId(), updatedTypeValise.getId());
        assertEquals("John Doe", updatedTypeValise.getProprietaire());
        assertNotNull(updatedTypeValise.getValise());
    }



    @Test
    void testCreateTypeValise_ValidationFailure() {
        // Arrange
        TypeValise typeValise = TypeValise.builder()
                .proprietaire(null)
                .description(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> typeValiseService.createTypeValise(typeValise));
    }

    @Test
    void testDeleteTypeValise_Success() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .build();
        valiseRepository.save(valise);

        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valise(valise)
                .build();
        TypeValise savedTypeValise = typeValiseService.createTypeValise(typeValise);

        // Act
        typeValiseService.deleteTypeValise(savedTypeValise.getId());
        entityManager.flush();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> typeValiseService.getTypeValise(savedTypeValise.getId()));
    }
    @Test
    void testDeleteTypeValise_NotFound() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> typeValiseService.deleteTypeValise(999));
        assertEquals("The suitcase type does not exist", exception.getMessage());
    }

    @Test
    void testGetTypeValise_NotFound() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> typeValiseService.getTypeValise(999));
        assertEquals("TypeValise with ID 999 not found", exception.getMessage());
    }


    @Test
    void testUpdateTypeValise_NotFound() {
        // Arrange
        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Updated Description")
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> typeValiseService.updateTypeValise(999, typeValise));
    }
    @Test
    void testCreateTypeValise_WithInvalidValise() {
        // Arrange
        TypeValise typeValise = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valise(null) // Valise manquante
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> typeValiseService.createTypeValise(typeValise));
    }
    @Test
    void testCreateTypeValise_DuplicateFields() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .build();
        valiseRepository.save(valise);

        TypeValise typeValise1 = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Duplicate Description")
                .valise(valise)
                .build();
        typeValiseService.createTypeValise(typeValise1);

        TypeValise typeValise2 = TypeValise.builder()
                .proprietaire("John Doe")
                .description("Duplicate Description")
                .valise(valise)
                .build();

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> typeValiseService.createTypeValise(typeValise2));
    }





}
