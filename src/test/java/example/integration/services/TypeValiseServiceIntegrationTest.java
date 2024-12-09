package example.integration.services;

import example.DTO.TypeValiseDTO;
import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import example.services.TypeValiseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager entityManager;

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

        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valiseId(valise.getId())
                .build();

        // Act
        TypeValiseDTO createdTypeValise = typeValiseService.createTypeValise(typeValiseDTO);

        // Assert
        assertNotNull(createdTypeValise.getId());
        assertEquals("John Doe", createdTypeValise.getProprietaire());
        assertEquals("Test Description", createdTypeValise.getDescription());
        assertEquals(valise.getId(), createdTypeValise.getValiseId());
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

        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valiseId(valise.getId())
                .build();
        TypeValiseDTO savedTypeValise = typeValiseService.createTypeValise(typeValiseDTO);

        // Act
        TypeValiseDTO foundTypeValise = typeValiseService.getTypeValise(savedTypeValise.getId());

        // Assert
        assertNotNull(foundTypeValise);
        assertEquals("John Doe", foundTypeValise.getProprietaire());
        assertEquals("Test Description", foundTypeValise.getDescription());
        assertEquals(valise.getId(), foundTypeValise.getValiseId());
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

        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valiseId(valise.getId())
                .build();
        TypeValiseDTO savedTypeValise = typeValiseService.createTypeValise(typeValiseDTO);

        savedTypeValise.setDescription("Updated Description");

        // Act
        TypeValiseDTO updatedTypeValise = typeValiseService.updateTypeValise(savedTypeValise.getId(), savedTypeValise);

        // Assert
        assertNotNull(updatedTypeValise);
        assertEquals("Updated Description", updatedTypeValise.getDescription());
        assertEquals(savedTypeValise.getId(), updatedTypeValise.getId());
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

        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Test Description")
                .valiseId(valise.getId())
                .build();
        TypeValiseDTO savedTypeValise = typeValiseService.createTypeValise(typeValiseDTO);

        // Act
        typeValiseService.deleteTypeValise(savedTypeValise.getId());
        entityManager.flush();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> typeValiseService.getTypeValise(savedTypeValise.getId()));
    }
}
