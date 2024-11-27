package example.integration.services;

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
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();

        Valise savedValise = valiseService.createValise(valise);

        assertNotNull(savedValise);
        assertNotNull(savedValise.getId());
        assertEquals("Test Valise", savedValise.getDescription());
        assertEquals(client.getId(), savedValise.getClient().getId());
        assertEquals(typeValise.getId(), savedValise.getTypeValise().getId());
    }

    @Test
    void testGetValiseById_Success() {
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();
        valise = valiseRepository.save(valise);

        Valise fetchedValise = valiseService.getValiseById(valise.getId());

        assertNotNull(fetchedValise);
        assertEquals(valise.getId(), fetchedValise.getId());
        assertEquals("Test Valise", fetchedValise.getDescription());
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
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123456L)
                .dateCreation(new Date())
                .client(client)
                .typeValise(typeValise)
                .build();
        valise = valiseRepository.save(valise);

        valise.setDescription("Updated Description");
        Valise updatedValise = valiseService.updateValise(valise.getId(), valise);

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
    void testCreateValise_WithRegles() {
        Regle regle = Regle.builder()
                .coderegle("R123")
                .dateRegle(new Date())
                .build();
        regle = regleRepository.save(regle);

        List<Regle> regleList = new ArrayList<>();
        regleList.add(regle);

        Valise valise = Valise.builder()
                .description("Valise avec règles")
                .numeroValise(789012L)
                .client(client)
                .typeValise(typeValise)
                .regleSortie(regleList)
                .build();

        Valise savedValise = valiseService.createValise(valise);

        assertNotNull(savedValise);
        assertEquals(1, savedValise.getRegleSortie().size());
        assertEquals("R123", savedValise.getRegleSortie().get(0).getCoderegle());
    }

    @Test
    void testCreateValise_InvalidClient() {
        Valise valise = Valise.builder()
                .description("Valise sans client")
                .numeroValise(123456L)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            valiseService.createValise(valise);
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

        List<Valise> valises = valiseService.getAllValises();

        assertNotNull(valises);
        assertEquals(2, valises.size());
    }



}
