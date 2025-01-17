package example.integration.repositories;

import example.entity.Client;
import example.entity.RetourSecurite;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class RetourSecuriteRepositoryIntegrationTest {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();

        client = Client.builder()
                .name("Test Client")
                .email("client@example.com")
                .build();
        client = clientRepository.save(client);
    }

    @Test
    public void testSaveRetourSecurite() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);

        assertNotNull(savedRetourSecurite.getId());
        assertEquals(12345L, savedRetourSecurite.getNumero());
        assertFalse(savedRetourSecurite.getCloture());
    }

    @Test
    public void testFindByNumero() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        retourSecuriteRepository.save(retourSecurite);

        Optional<RetourSecurite> foundRetour = retourSecuriteRepository.findByNumero(12345L);
        assertTrue(foundRetour.isPresent());
        assertEquals(12345L, foundRetour.get().getNumero());
    }

    @Test
    public void testFindByClientId() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        retourSecuriteRepository.save(retourSecurite);

        List<RetourSecurite> foundRetours = retourSecuriteRepository.findByClientId(client.getId());
        assertEquals(1, foundRetours.size());
        assertEquals(client.getId(), foundRetours.get(0).getClient().getId());
    }

    @Test
    public void testFindByDatesecuriteBetween() {
        Date now = new Date();
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(now)
                .cloture(false)
                .client(client)
                .build();

        retourSecuriteRepository.save(retourSecurite);

        List<RetourSecurite> foundRetours = retourSecuriteRepository.findByDatesecuriteBetween(
                new Date(now.getTime() - 1000), new Date(now.getTime() + 1000));

        assertEquals(1, foundRetours.size());
        assertEquals(now, foundRetours.get(0).getDatesecurite());
    }

    @Test
    public void testFindByClotureFalse() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        retourSecuriteRepository.save(retourSecurite);

        List<RetourSecurite> nonCloturedRetours = retourSecuriteRepository.findByClotureFalse();
        assertEquals(1, nonCloturedRetours.size());
        assertFalse(nonCloturedRetours.get(0).getCloture());
    }

    @Test
    public void testDeleteRetourSecurite() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
        retourSecuriteRepository.delete(savedRetourSecurite);

        Optional<RetourSecurite> foundRetour = retourSecuriteRepository.findById(savedRetourSecurite.getId());
        assertFalse(foundRetour.isPresent());
    }
}
