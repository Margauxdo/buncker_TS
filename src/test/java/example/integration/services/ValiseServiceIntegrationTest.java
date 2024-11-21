package example.integration.services;

import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import example.services.ValiseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ValiseServiceIntegrationTest {

    @Autowired
    private ValiseService valiseService;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;

    private Valise valise;
    private Client client;
    private TypeValise typeValise;
    private Regle regle;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("Test Client")
                .email("testclient@example.com")
                .build();
        clientRepository.save(client);

        typeValise = TypeValise.builder()
                .description("Test Type Valise")
                .proprietaire("Test Proprietaire")
                .build();
        typeValiseRepository.save(typeValise);

        regle = Regle.builder()
                .coderegle("TEST_CODE")
                .build();
        regleRepository.save(regle);

        valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(12345L)
                .client(client)
                .regleSortie((List<Regle>) regle)
                .typeValise(typeValise)
                .dateCreation(new Date())
                .build();
    }



    @Test
    public void testCreateValise() {
        Valise createdValise = valiseService.createValise(valise);
        assertNotNull(createdValise);
        assertEquals("Test Valise", createdValise.getDescription());
        assertEquals(client.getId(), createdValise.getClient().getId());
        assertEquals(typeValise.getId(), createdValise.getTypeValise().getId());
    }

    @Test
    public void testUpdateValise() {
        Valise createdValise = valiseService.createValise(valise);
        createdValise.setDescription("Updated Valise Description");

        Valise updatedValise = valiseService.updateValise(createdValise.getId(), createdValise);
        assertNotNull(updatedValise);
        assertEquals("Updated Valise Description", updatedValise.getDescription());
    }

    @Test
    public void testDeleteValise() {
        Valise createdValise = valiseService.createValise(valise);
        int valiseId = createdValise.getId();

        valiseService.deleteValise(valiseId);
        assertFalse(valiseRepository.existsById(valiseId));
    }

    @Test
    public void testGetValiseById() {
        Valise createdValise = valiseService.createValise(valise);

        Valise foundValise = valiseService.getValiseById(createdValise.getId());
        assertNotNull(foundValise);
        assertEquals("Test Valise", foundValise.getDescription());
    }

    @Test
    public void testGetAllValises() {
        valiseService.createValise(valise);

        List<Valise> allValises = valiseService.getAllValises();
        assertNotNull(allValises);
        assertFalse(allValises.isEmpty());
    }
}
