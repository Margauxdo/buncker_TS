package example.entity;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ClientTest {

    @Autowired
    private EntityManager em;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setAdresse("123 Main St");
        client.setTelephoneExploitation("123456789");
        client.setVille("Sampleville");
        client.setPersonnelEtFonction("Manager");

        Valise valise = new Valise();
        valise.setDescription("Valise Test");
        valise.setClient(client);
        client.getValises().add(valise);
    }

    @Test
    public void testSaveClient() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");

        em.persist(client);
        em.flush();

        Client savedClient = em.find(Client.class, client.getId());
        assertNotNull(savedClient);
        assertEquals("Test Client", savedClient.getName());
        assertEquals("test@example.com", savedClient.getEmail());
    }

}
