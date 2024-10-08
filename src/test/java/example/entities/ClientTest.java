package example.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityManager;

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
    }

    @Test
    public void testClientPersistence() {
        em.persist(client);
        em.flush();
        assertNotNull(client.getId(), "L'ID du client ne doit pas être null après la persistance.");
    }

    @Test
    public void testClientDataSavedCorrectly() {
        // Persiste le client dans la base de données
        em.persist(client);
        em.flush();

        // Récupère le client depuis la base de données
        Client persistedClient = em.find(Client.class, client.getId());
        assertNotNull(persistedClient, "Le client doit être récupéré depuis la base de données.");

        // Vérifie que les données enregistrées sont correctes
        assertEquals("John Doe", persistedClient.getName());
        assertEquals("john.doe@example.com", persistedClient.getEmail());
        assertEquals("123 Main St", persistedClient.getAdresse());
        assertEquals("123456789", persistedClient.getTelephoneExploitation());
        assertEquals("Sampleville", persistedClient.getVille());
        assertEquals("Manager", persistedClient.getPersonnelEtFonction());
    }


    @Test
    public void testClientRelationsPersistence() {
        // Crée une nouvelle valise et la configure
        Valise valise = new Valise();
        valise.setDescription("Valise Test");

        // Associe la valise au client
        client.getValises().add(valise);
        valise.setClient(client); // Associe également le client à la valise

        // Persiste le client (la valise est automatiquement persistée grâce à CascadeType.ALL)
        em.persist(client);
        em.flush();

        // Récupère le client pour vérifier les relations
        Client persistedClient = em.find(Client.class, client.getId());
        assertNotNull(persistedClient, "Le client doit être récupéré depuis la base de données.");
        assertEquals(1, persistedClient.getValises().size(), "Le client doit avoir une valise associée.");
    }



}



