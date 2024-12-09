package example.integration.services;

import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeValise;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import example.services.ValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("testintegration")
@Transactional
class ValiseServiceIntegrationTest {

    @Autowired
    private ValiseService valiseService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private RegleRepository regleRepository;

    private Client client;
    private TypeValise typeValise;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .telephoneExploitation("123456789")
                .build();
        client = clientRepository.save(client);

        typeValise = TypeValise.builder()
                .proprietaire("Propriétaire 1")
                .description("Description TypeValise")
                .build();
        typeValise = typeValiseRepository.save(typeValise);
    }
    @Test
    void testCreateValise_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .clientId(client.getId())
                .typeValiseId(typeValise.getId())
                .build();

        // Act
        ValiseDTO savedValise = valiseService.createValise(valiseDTO);

        // Assert
        assertNotNull(savedValise);
        assertNotNull(savedValise.getId());
        assertEquals("Test Valise", savedValise.getDescription());
        assertEquals(client.getId(), savedValise.getClientId());
        assertEquals(typeValise.getId(), savedValise.getTypeValiseId());
    }

    @Test
    void testGetValiseById_Success() {
        // Arrange
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();
        valise = valiseRepository.save(valise);

        // Act
        ValiseDTO fetchedValise = valiseService.getValiseById(valise.getId());

        // Assert
        assertNotNull(fetchedValise);
        assertEquals(valise.getId(), fetchedValise.getId());
        assertEquals("Test Valise", fetchedValise.getDescription());
    }

    @Test
    void testCreateValise_WithRegles() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("R123")
                .dateRegle(new Date())
                .build();
        regle = regleRepository.save(regle);

        List<Integer> regleIds = List.of(regle.getId());

        ValiseDTO valiseDTO = ValiseDTO.builder()
                .description("Valise avec règles")
                .numeroValise(789012L)
                .clientId(client.getId())
                .typeValiseId(typeValise.getId())
                .regleSortieIds(regleIds)
                .build();

        // Act
        ValiseDTO savedValise = valiseService.createValise(valiseDTO);

        // Assert
        assertNotNull(savedValise);
        assertEquals(1, savedValise.getRegleSortieIds().size());
        assertEquals(regle.getId(), savedValise.getRegleSortieIds().get(0));
    }




    @Test
    void testGetValiseById_NotFound() {
        int invalidId = 999;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.getValiseById(invalidId);
        });

        assertEquals("Valise not found with ID: " + invalidId, exception.getMessage());
    }

    @Test
    void testUpdateValise_Success() {
        // Arrange
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();
        valise = valiseRepository.save(valise);

        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(valise.getId())
                .description("Updated Description")
                .numeroValise(valise.getNumeroValise())
                .clientId(client.getId())
                .typeValiseId(typeValise.getId())
                .build();

        // Act
        ValiseDTO updatedValise = valiseService.updateValise(valise.getId(), valiseDTO);

        // Assert
        assertNotNull(updatedValise);
        assertEquals("Updated Description", updatedValise.getDescription());
    }

    @Test
    void testDeleteValise_Success() {
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();
        valise = valiseRepository.save(valise);

        valiseService.deleteValise(valise.getId());

        assertFalse(valiseRepository.existsById(valise.getId()));
    }

    @Test
    void testDeleteValise_NotFound() {
        int invalidId = 999;

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.deleteValise(invalidId);
        });

        assertEquals("The suitcase does not exist", exception.getMessage());
    }



    @Test
    void testCreateValise_InvalidClient() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .description("Valise sans client")
                .numeroValise(123456L)
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            valiseService.createValise(valiseDTO);
        });

        // Vérifiez que la cause est une IllegalArgumentException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Client ID is required", exception.getCause().getMessage());
    }


    @Test
    void testGetAllValises() {
        Valise valise1 = Valise.builder()
                .description("Valise 1")
                .numeroValise(111111L)
                .client(client)
                .typeValise(typeValise)
                .build();
        valiseRepository.save(valise1);

        Valise valise2 = Valise.builder()
                .description("Valise 2")
                .numeroValise(222222L)
                .client(client)
                .typeValise(typeValise)
                .build();
        valiseRepository.save(valise2);

        List<ValiseDTO> valises = valiseService.getAllValises();

        assertNotNull(valises);
        assertEquals(2, valises.size());
    }










}
