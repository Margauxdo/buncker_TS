package example.integration.entities;

import example.entity.Client;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
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
    public void testSaveRetourSecurite() {
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);
        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecurite);
        Assertions.assertNotNull(savedRs.getId(), "The safety return id must be generated");
        Assertions.assertEquals(client.getId(), savedRs.getClients().get(0), "The associated client must match");
    }
    @Test
    public void testUpdateRetourSecurite() {
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);
        RetourSecurite savedRS = retourSecuriteRepository.save(retourSecurite);

        Date newDate = new Date();
        savedRS.setDatesecurite(newDate);
        RetourSecurite updatedRs = retourSecuriteRepository.save(savedRS);
        Assertions.assertEquals(newDate, updatedRs.getDatesecurite(), "The new date must match");
    }
    @Test
    public void testSaveRetourSecuriteWithoutClient() {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteRepository.save(retourSecurite);
        }, "A DataIntegrityViolationException is expected due to the client not being present.");
    }




    @Test
    public void testSaveRetourSecuriteWithoutNumero() {
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());


        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteRepository.save(retourSecurite);
        }, "Expected DataIntegrityViolationException due to missing 'number'");
    }


    @Test
    public void testDeleteRetourSecurite() {
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);
        RetourSecurite savedRS = retourSecuriteRepository.save(retourSecurite);
        retourSecuriteRepository.deleteById(savedRS.getId());

        Optional<RetourSecurite> foundRS = retourSecuriteRepository.findById(savedRS.getId());
        Assertions.assertFalse(foundRS.isPresent(), "The safety return id must be generated");
    }
    @Test
    public void testFindAllRetourSecurite() {
        Client clientA = new Client();
        clientA.setEmail("clientA@test.com");
        clientA.setName("Martin");
        clientRepository.save(clientA);

        Client clientB = new Client();
        clientB.setEmail("clientB@test.com");
        clientB.setName("Bob");
        clientRepository.save(clientB);

        RetourSecurite retourSecuriteC = new RetourSecurite();
        retourSecuriteC.setClients(new ArrayList<>());
        retourSecuriteC.setDatesecurite(new Date());
        retourSecuriteC.setNumero(1001L);
        retourSecuriteRepository.save(retourSecuriteC);

        RetourSecurite retourSecuriteD = new RetourSecurite();
        retourSecuriteD.setClients(new ArrayList<>());
        retourSecuriteD.setDatesecurite(new Date());
        retourSecuriteD.setNumero(1002L);
        retourSecuriteRepository.save(retourSecuriteD);

        List<RetourSecurite> retourSecurites = retourSecuriteRepository.findAll();
        Assertions.assertEquals(2, retourSecurites.size());
    }

    @Test
    public void testFindRetourSecuriteByIdNotFound(){
        Optional<RetourSecurite> foundRS = retourSecuriteRepository.findById(9999);
        Assertions.assertFalse(foundRS.isPresent(), "The safety return id must be generated");
    }
    @Test
    public void testCascadeDeleteWithoutclient(){
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);
        retourSecuriteRepository.save(retourSecurite);
        retourSecuriteRepository.deleteById(retourSecurite.getId());
        clientRepository.deleteById(client.getId());
        Assertions.assertFalse(clientRepository.existsById(retourSecurite.getId()));
        Assertions.assertFalse(retourSecuriteRepository.existsById(retourSecurite.getId()));
    }

    @Test
    public void testFindById_ReturnSecuriteExists() {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setName("John Doe");
        clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(12345L);
        retourSecuriteRepository.save(retourSecurite);

        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(retourSecurite.getId());

        Assertions.assertTrue(foundRetourSecurite.isPresent(), "safety return should be found");
        Assertions.assertEquals(retourSecurite.getId(), foundRetourSecurite.get().getId(), "IDs should match");
    }

    @Test
    public void testFindById_ReturnSecuriteDoesNotExist() {
        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(99999);

        Assertions.assertFalse(foundRetourSecurite.isPresent(), "safety return should not be found");
    }
    @Test
    public void testCascadeDeleteClients() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        em.persist(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setNumero(12345L);
        em.persist(retourSecurite);
        em.flush();

        em.remove(retourSecurite);
        em.flush();

        List<Client> clients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
        Assertions.assertTrue(clients.isEmpty(), "Clients should be deleted due to cascade.");
    }


}
