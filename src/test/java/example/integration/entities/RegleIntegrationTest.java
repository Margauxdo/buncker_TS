package example.integration.entities;

import example.entities.*;
import example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RegleIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private TypeRegleRepository typeRegleRepository;
    ;
    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        regleRepository.deleteAll();
    }

    // Test for saving a valid Regle entity
    @Test
    public void testSaveRegleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setNombreJours(5);

        Regle savedRegle = regleRepository.saveAndFlush(regle);

        assertNotNull(savedRegle.getId());
        assertEquals("R001", savedRegle.getCoderegle());
    }

    @Test
    public void testUpdateEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        savedRegle.setCoderegle("R002");
        Regle updatedRegle = regleRepository.saveAndFlush(savedRegle);

        assertEquals("R002", updatedRegle.getCoderegle());
    }

    @Test
    public void testDeleteEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R003");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        regleRepository.deleteById(savedRegle.getId());
        regleRepository.flush();

        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());
        assertFalse(foundRegle.isPresent());
    }

    @Test
    public void testCascadeDeleteWithChildEntities() {
        Regle regle = new Regle();
        regle.setCoderegle("R004");

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);

        regle.getSortieSemaine().add(sortieSemaine);
        regleRepository.saveAndFlush(regle);

        regleRepository.deleteById(regle.getId());
        regleRepository.flush();

        Optional<SortieSemaine> deletedSortieSemaine = sortieSemaineRepository.findById(sortieSemaine.getId());
        assertFalse(deletedSortieSemaine.isPresent());  // Assurez-vous que l'enfant a été supprimé en cascade
    }


    @Test
    public void testFindEntityByIdSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R005");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());
        assertTrue(foundRegle.isPresent());
    }

    @Test
    public void testFindEntityByIdNotFound() {
        Optional<Regle> foundRegle = regleRepository.findById(-1);
        assertFalse(foundRegle.isPresent());
    }

    @Test
    public void testUniqueConstraintOnEntityField() {
        Regle regle1 = new Regle();
        regle1.setCoderegle("R006");
        regleRepository.saveAndFlush(regle1);

        Regle regle2 = new Regle();
        regle2.setCoderegle("R006");

        assertThrows(DataIntegrityViolationException.class, () -> {
            regleRepository.saveAndFlush(regle2);
        });
    }

    @Test
    public void testSaveEntityFailureDueToNullField() {
        Regle regle = new Regle();


        assertThrows(DataIntegrityViolationException.class, () -> {
            regleRepository.saveAndFlush(regle);
        });
    }

    @Test
    public void testOneToManyRelationshipWithCascade() {
        Regle regle = new Regle();
        regle.setCoderegle("R007");

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);

        regle.getSortieSemaine().add(sortieSemaine);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        assertEquals(1, savedRegle.getSortieSemaine().size());
    }

    @Test
    public void testManyToOneRelationship() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type A");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        Regle regle = new Regle();
        regle.setCoderegle("R008");
        regle.setTypeRegle(savedTypeRegle);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        assertEquals("Type A", savedRegle.getTypeRegle().getNomTypeRegle());
    }

    @Test
    public void testAddAndRemoveChildEntitiesInCollection() {
        Regle regle = new Regle();
        regle.setCoderegle("R009");

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);

        regle.getSortieSemaine().add(sortieSemaine);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        savedRegle.getSortieSemaine().remove(0);
        regleRepository.saveAndFlush(savedRegle);

        assertEquals(0, savedRegle.getSortieSemaine().size());
    }

    @Test
    public void testOneToOneRelationship() {
        // Création d'un client
        Client client = new Client();
        client.setName("ClientTest");
        client.setEmail("client@test.com");
        clientRepository.saveAndFlush(client);

        // Création d'une règle
        Regle regle = new Regle();
        regle.setCoderegle("R010");

        // Création d'une valise et association avec la règle et le client
        Valise valise = new Valise();
        valise.setDescription("ValiseTest");
        valise.setClient(client);
        valise.setRegleSortie(regle);

        // Associer la valise à la règle pour la bidirectionnalité
        regle.setValise(valise);

        // Sauvegarde de la valise et de la règle
        valiseRepository.saveAndFlush(valise);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Vérifications
        assertNotNull(savedRegle.getValise());
        assertEquals("ValiseTest", savedRegle.getValise().getDescription());
        assertEquals("ClientTest", savedRegle.getValise().getClient().getName());
    }



}