package example.entities;

import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        valise.setClient(client); // Lien bidirectionnel
        client.setValises(new ArrayList<>());
        client.getValises().add(valise);

        em.persist(client);
        em.flush();
    }

    @Test
    public void testClientPersistence() {
        assertNotNull(client.getId(), "L'ID du client ne doit pas être null après la persistance.");
    }

    @Test
    public void testClientDataSavedCorrectly() {
        Client persistedClient = em.find(Client.class, client.getId());
        assertNotNull(persistedClient, "Le client doit être récupéré depuis la base de données.");
        assertEquals("John Doe", persistedClient.getName());
        assertEquals("john.doe@example.com", persistedClient.getEmail());
        assertEquals("123 Main St", persistedClient.getAdresse());
        assertEquals("123456789", persistedClient.getTelephoneExploitation());
        assertEquals("Sampleville", persistedClient.getVille());
        assertEquals("Manager", persistedClient.getPersonnelEtFonction());
    }

    @Test
    public void testClientRelationsPersistence() {
        Client persistedClient = em.find(Client.class, client.getId());
        assertNotNull(persistedClient, "Le client doit être récupéré depuis la base de données.");
        assertEquals(1, persistedClient.getValises().size(), "Le client doit avoir une valise associée.");
    }

    @Test
    public void testCascadePersistValises() {
        Client foundClient = em.find(Client.class, client.getId());
        Assertions.assertNotNull(foundClient, "Le client devrait être trouvé.");
        Assertions.assertEquals(1, foundClient.getValises().size(), "Une valise devrait être persistée en cascade.");
    }

    @Test
    public void testCascadeDeleteValises() {
        em.remove(client);
        em.flush();

        Client deletedClient = em.find(Client.class, client.getId());
        Assertions.assertNull(deletedClient, "Le client devrait être supprimé.");

        List<Valise> remainingValises = em.createQuery("SELECT v FROM Valise v", Valise.class).getResultList();
        Assertions.assertTrue(remainingValises.isEmpty(), "Les valises associées devraient également être supprimées.");
    }

    @Test
    public void testNonNullName() {
        Client clientWithoutName = new Client();
        clientWithoutName.setAdresse("Adresse sans nom");
        clientWithoutName.setEmail("nullname@example.com");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            em.persist(clientWithoutName);
            em.flush();
        }, "Le nom du client ne devrait pas être nul.");
    }


}



