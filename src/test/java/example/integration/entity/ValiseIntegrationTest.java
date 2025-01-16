package example.integration.entity;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.Regle;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.RegleRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ValiseIntegrationTest {

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        mouvementRepository.deleteAll();
        regleRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveValiseSuccess() {
        // Arrange
        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise("V123");
        valise.setDateCreation(new Date());

        // Act
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Assert
        assertNotNull(savedValise.getId());
        assertEquals("Test Valise", savedValise.getDescription());
        assertEquals("V123", savedValise.getNumeroValise());
    }

    @Test
    public void testSaveValiseWithClient() {
        // Arrange
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("testclient@example.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Valise with Client");
        valise.setNumeroValise("V456");
        valise.setClient(client);

        // Act
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Assert
        assertNotNull(savedValise.getId());
        assertEquals(client.getId(), savedValise.getClient().getId());
    }

    @Test
    public void testSaveValiseWithRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle = regleRepository.saveAndFlush(regle);

        Valise valise = new Valise();
        valise.setDescription("Valise with Regle");
        valise.setNumeroValise("V789");
        valise.setReglesSortie(regle);

        // Act
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Assert
        assertNotNull(savedValise.getId());
        assertEquals(regle.getId(), savedValise.getReglesSortie().getId());
    }

    @Test
    public void testSaveValiseFailure() {
        // Arrange
        Valise valise = new Valise(); // Missing required fields

        // Act & Assert
        assertThrows(Exception.class, () -> valiseRepository.saveAndFlush(valise));
    }

    @Test
    public void testUpdateValise() {
        // Arrange
        Valise valise = new Valise();
        valise.setDescription("Initial Description");
        valise.setNumeroValise("V001");
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Act
        savedValise.setDescription("Updated Description");
        savedValise.setNumeroValise("V002");
        Valise updatedValise = valiseRepository.saveAndFlush(savedValise);

        // Assert
        assertEquals("Updated Description", updatedValise.getDescription());
        assertEquals("V002", updatedValise.getNumeroValise());
    }

    @Test
    public void testDeleteValise() {
        // Arrange
        Valise valise = new Valise();
        valise.setDescription("To Be Deleted");
        valise.setNumeroValise("V999");
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Act
        valiseRepository.deleteById(savedValise.getId());
        Optional<Valise> deletedValise = valiseRepository.findById(savedValise.getId());

        // Assert
        assertFalse(deletedValise.isPresent());
    }

    @Test
    public void testFindValiseById() {
        // Arrange
        Valise valise = new Valise();
        valise.setDescription("Find Valise");
        valise.setNumeroValise("V111");
        Valise savedValise = valiseRepository.saveAndFlush(valise);

        // Act
        Optional<Valise> foundValise = valiseRepository.findById(savedValise.getId());

        // Assert
        assertTrue(foundValise.isPresent());
        assertEquals("Find Valise", foundValise.get().getDescription());
    }

    @Test
    public void testFindAllValises() {
        // Arrange
        Valise valise1 = new Valise();
        valise1.setDescription("Valise 1");
        valise1.setNumeroValise("V001");

        Valise valise2 = new Valise();
        valise2.setDescription("Valise 2");
        valise2.setNumeroValise("V002");

        valiseRepository.saveAndFlush(valise1);
        valiseRepository.saveAndFlush(valise2);

        // Act
        List<Valise> valises = valiseRepository.findAll();

        // Assert
        assertEquals(2, valises.size());
    }
}
