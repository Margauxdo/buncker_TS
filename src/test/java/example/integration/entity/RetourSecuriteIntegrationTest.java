package example.integration.entity;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class RetourSecuriteIntegrationTest {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testCreateRetourSecurite() {
        // Arrange
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        Client savedClient = clientRepository.saveAndFlush(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(1001L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setCloture(false);
        retourSecurite.setClient(savedClient);

        // Act
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.saveAndFlush(retourSecurite);

        // Assert
        assertNotNull(savedRetourSecurite.getId());
        assertEquals(1001L, savedRetourSecurite.getNumero());
        assertFalse(savedRetourSecurite.getCloture());
        assertEquals(savedClient.getId(), savedRetourSecurite.getClient().getId());
    }

    @Test
    public void testUpdateRetourSecurite() {
        // Arrange
        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("jane.doe@example.com");
        Client savedClient = clientRepository.saveAndFlush(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(2002L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setCloture(false);
        retourSecurite.setClient(savedClient);
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.saveAndFlush(retourSecurite);

        // Act
        savedRetourSecurite.setCloture(true);
        savedRetourSecurite.setDateCloture(new Date());
        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.saveAndFlush(savedRetourSecurite);

        // Assert
        assertTrue(updatedRetourSecurite.getCloture());
        assertNotNull(updatedRetourSecurite.getDateCloture());
    }

    @Test
    public void testDeleteRetourSecurite() {
        // Arrange
        Client client = new Client();
        client.setName("Mark Spencer");
        client.setEmail("mark.spencer@example.com");
        Client savedClient = clientRepository.saveAndFlush(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(3003L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setClient(savedClient);
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.saveAndFlush(retourSecurite);

        // Act
        retourSecuriteRepository.deleteById(savedRetourSecurite.getId());
        Optional<RetourSecurite> deletedRetourSecurite = retourSecuriteRepository.findById(savedRetourSecurite.getId());

        // Assert
        assertFalse(deletedRetourSecurite.isPresent());
    }

    @Test
    public void testRetourSecuriteWithMouvements() {
        // Arrange
        Client client = new Client();
        client.setName("Alice Doe");
        client.setEmail("alice.doe@example.com");
        Client savedClient = clientRepository.saveAndFlush(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(4004L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setClient(savedClient);
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.saveAndFlush(retourSecurite);

        Mouvement mouvement = new Mouvement();
        mouvement.setRetourSecurite(savedRetourSecurite);
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("In Progress");
        savedRetourSecurite.getMouvements().add(mouvement);

        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.saveAndFlush(savedRetourSecurite);

        // Act
        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(updatedRetourSecurite.getId());

        // Assert
        assertTrue(foundRetourSecurite.isPresent());
        assertEquals(1, foundRetourSecurite.get().getMouvements().size());
        assertEquals("In Progress", foundRetourSecurite.get().getMouvements().get(0).getStatutSortie());
    }

    @Test
    public void testSaveRetourSecuriteWithoutNumero() {
        // Arrange
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setDatesecurite(new Date());

        // Assert
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteRepository.saveAndFlush(retourSecurite);
        }, "Expected DataIntegrityViolationException due to missing 'numero'");
    }
}
