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

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClientTest {

    @Autowired
    private EntityManager em;

    private Client client;

    @BeforeEach
    public void setUp() {

        Valise valise = new Valise();
        valise.setDescription("Valise Test");
        valise.setClient(client);
        client.getValises().add(valise);

        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("Problème Test");
        probleme.setClient(client);
        client.getProblemes().add(probleme);

        client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setAdresse("123 Main St");


    }

    @Test
    public void testSaveClient() {
        em.persist(client);
        em.flush();

        Client savedClient = em.find(Client.class, client.getId());
        Assertions.assertNotNull(savedClient);
        Assertions.assertEquals("John Doe", savedClient.getName());
        Assertions.assertEquals("john.doe@example.com", savedClient.getEmail());
    }

    @Test
    public void testLazyLoading() {
        em.persist(client);
        em.flush();

        Client savedClient = em.find(Client.class, client.getId());
        Assertions.assertNotNull(savedClient);

        // Lazy loading
        Assertions.assertNotNull(savedClient.getRetourSecurite());
        Assertions.assertNotNull(savedClient.getRegle());
    }

    @Test
    public void testValidationConstraints() {
        Client invalidClient = new Client();
        invalidClient.setEmail("invalid-email");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            em.persist(invalidClient);
            em.flush();
        });
    }

    @Test
    public void testDataIntegrityViolation() {
        em.persist(client);
        em.flush();

        Client duplicateClient = new Client();
        duplicateClient.setName("John Doe");
        duplicateClient.setEmail("john.doe@example.com");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            em.persist(duplicateClient);
            em.flush();
        });
    }
}

