package example.integration.repositories;

import example.entities.Client;
import example.entities.RetourSecurite;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
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

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveRetourSecurite() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient.getId());

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(savedClient);

        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecu);

        assertNotNull(savedRs.getId());
        assertEquals(2546L, savedRs.getNumero());
        assertEquals(savedClient.getId(), savedRs.getClient().getId());
    }

    @Test
    public void testFindByIdRetourSecurite() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        clientRepository.save(client);

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(client);

        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecu);
        Optional<RetourSecurite> foundRs = retourSecuriteRepository.findById(savedRs.getId());

        assertTrue(foundRs.isPresent());
        assertEquals(2546L, foundRs.get().getNumero());
    }


    @Test
    public void testDeleteRetourSecurite() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        Client savedClient = clientRepository.save(client);

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(savedClient);
        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecu);

        retourSecuriteRepository.deleteById(savedRs.getId());

        Optional<RetourSecurite> deleteRS = retourSecuriteRepository.findById(savedRs.getId());
        assertFalse(deleteRS.isPresent());
    }


    @Test
    public void testUpdateRetourSecurite() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        Client savedClient = clientRepository.save(client);

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(savedClient);
        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecu);

        savedRs.setNumero(1254L);
        savedRs.setDatesecurite(new Date());
        RetourSecurite updatedRs = retourSecuriteRepository.save(savedRs);

        Optional<RetourSecurite> foundRs = retourSecuriteRepository.findById(updatedRs.getId());
        assertTrue(foundRs.isPresent());
        assertEquals(1254L, foundRs.get().getNumero());
        assertEquals(savedClient.getId(), foundRs.get().getClient().getId());
    }


    @Test
    public void testFindAllRetourSecurite() {
        Client client1 = new Client();
        client1.setName("Dutois");
        client1.setEmail("h.dutois@gmail.com");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Bernard");
        client2.setEmail("bernard@gmail.com");
        clientRepository.save(client2);

        RetourSecurite retourSecu1 = new RetourSecurite();
        retourSecu1.setNumero(2546L);
        retourSecu1.setDatesecurite(new Date());
        retourSecu1.setClient(client1);

        RetourSecurite retourSecu2 = new RetourSecurite();
        retourSecu2.setNumero(1254L);
        retourSecu2.setDatesecurite(new Date());
        retourSecu2.setClient(client2);
        retourSecuriteRepository.save(retourSecu1);
        retourSecuriteRepository.save(retourSecu2);

        List<RetourSecurite> retourSecurites = retourSecuriteRepository.findAll();
        assertNotNull(retourSecurites);
        assertEquals(2, retourSecurites.size());
    }


    @Test
    public void testDeleteRetourSecuriteWithRelation() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        clientRepository.save(client);

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(client);

        RetourSecurite savedRs = retourSecuriteRepository.save(retourSecu);
        retourSecuriteRepository.deleteById(savedRs.getId());

        Optional<RetourSecurite> deleteRS = retourSecuriteRepository.findById(savedRs.getId());
        assertFalse(deleteRS.isPresent());
    }

    @Test
    public void testFindByNumero() {
        Client client = new Client();
        client.setName("Dutois");
        client.setEmail("h.dutois@gmail.com");
        clientRepository.save(client);

        RetourSecurite retourSecu = new RetourSecurite();
        retourSecu.setNumero(2546L);
        retourSecu.setDatesecurite(new Date());
        retourSecu.setClient(client);
        retourSecuriteRepository.save(retourSecu);

        Optional<RetourSecurite> foundRs = retourSecuriteRepository.findByNumero(2546L);

        assertTrue(foundRs.isPresent());
        assertEquals(2546L, foundRs.get().getNumero());
        assertEquals(client.getId(), foundRs.get().getClient().getId());
    }

}
