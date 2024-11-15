package example.entities;

import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProblemeTest {

    @Autowired
    private EntityManager em;

    private Probleme probleme;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        Client client = new Client();
        client.setName("nomA");
        client.setEmail("nomA@gmail.com");
        client = clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Valise de test");
        valise.setClient(client);
        valise = valiseRepository.save(valise);

        probleme = new Probleme();
        probleme.setDescriptionProbleme("description des problemes");
        probleme.setDetailsProbleme("detail des problemes");
        probleme.setClient(client);
        probleme.setValise(valise);

        em.persist(probleme);
        em.flush();
    }

    @Test
    public void testProblemePersistence() {
        Assertions.assertNotNull(probleme.getId(), "Probleme ID should not be null after persistence");
    }

    @Test
    public void testProblemeDescription() {
        Assertions.assertEquals("description des problemes", probleme.getDescriptionProbleme(), "Description should match");
    }

    @Test
    public void testProblemeDetails() {
        Assertions.assertEquals("detail des problemes", probleme.getDetailsProbleme(), "Details should match");
    }

    @Test
    public void testProblemeValiseAssociation() {
        Assertions.assertNotNull(probleme.getValise(), "Probleme should be associated with a Valise");
        Valise expectedValise = valiseRepository.findAll().get(0);
        Assertions.assertEquals(expectedValise.getId(), probleme.getValise().getId(), "Valise ID should match");
    }

    @Test
    public void testProblemeClientAssociation() {
        Assertions.assertNotNull(probleme.getClient(), "Probleme should be associated with a Client");
        Client expectedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals(expectedClient.getId(), probleme.getClient().getId(), "Client ID should match");
    }

    @Test
    public void testNonNullDescriptionProbleme() {
        Probleme problemeWithoutDescription = new Probleme();
        problemeWithoutDescription.setClient(probleme.getClient());
        problemeWithoutDescription.setValise(probleme.getValise());

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(problemeWithoutDescription);
            em.flush();
        }, "Persistence should fail if descriptionProbleme is null");
    }

    @Test
    public void testUpdateProblemeDetails() {
        probleme.setDetailsProbleme("updated problem details");
        em.merge(probleme);
        em.flush();

        Probleme updatedProbleme = em.find(Probleme.class, probleme.getId());
        Assertions.assertEquals("updated problem details", updatedProbleme.getDetailsProbleme(), "Details should be updated");
    }
}

