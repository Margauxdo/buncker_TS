package example.controller.entities;

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
import org.springframework.transaction.support.TransactionTemplate;

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

    @Autowired
    private TransactionTemplate transactionTemplate;


    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();
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
        // Create a RetourSecurite instance without clients or mouvement
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setNumero(1234L);

        // Attempt to save the entity and verify it's persisted successfully
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
        Assertions.assertNotNull(savedRetourSecurite.getId(), "The RetourSecurite should be saved successfully even without clients.");
        Assertions.assertEquals(1234L, savedRetourSecurite.getNumero());
        Assertions.assertNull(savedRetourSecurite.getClients(), "Clients should be null since none were associated.");
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
    public void testClotureAndDateCloture() {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(5678L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setCloture(true);
        retourSecurite.setDateCloture(new Date());
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);

        Assertions.assertNotNull(savedRetourSecurite.getDateCloture(), "La date de clôture doit être renseignée lorsque 'cloture' est true.");
        Assertions.assertTrue(savedRetourSecurite.getCloture(), "Le champ 'cloture' doit être true.");
    }



    @Test
    public void testCascadeDeleteClients() {
        Client client1 = new Client();
        client1.setEmail("client1@test.com");
        client1.setName("Client1");

        Client client2 = new Client();
        client2.setEmail("client2@test.com");
        client2.setName("Client2");

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(1234L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setClients(List.of(client1, client2));

        client1.setRetourSecurite(retourSecurite);
        client2.setRetourSecurite(retourSecurite);

        retourSecuriteRepository.save(retourSecurite);

        retourSecuriteRepository.deleteById(retourSecurite.getId());

        Assertions.assertFalse(clientRepository.existsById(client1.getId()), "Le client 1 doit être supprimé en cascade.");
        Assertions.assertFalse(clientRepository.existsById(client2.getId()), "Le client 2 doit être supprimé en cascade.");
    }



    @Test
    public void testSaveRetourSecuriteWithEmptyClients() {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(3456L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setClients(new ArrayList<>());

        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
        Assertions.assertTrue(savedRetourSecurite.getClients().isEmpty(), "La liste des clients doit être vide.");
    }





}
