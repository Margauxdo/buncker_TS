package example.integration.entities;

import example.entity.*;
import example.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
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

    @Autowired
    private JourFerieRepository jourFerieRepository;


    @BeforeEach
    public void setUp() {

        regleRepository.deleteAll();
    }

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
        assertFalse(deletedSortieSemaine.isPresent());
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
    @Transactional
    public void testSaveEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Règle test");
        regle.setDateRegle(new Date());
        regle.setNombreJours(7);

        Regle savedRegle = regleRepository.save(regle);

        Assertions.assertNotNull(savedRegle.getId(), "L'ID de la règle doit être généré.");
        Assertions.assertEquals("R001", savedRegle.getCoderegle(), "Le code de la règle doit correspondre.");
    }
    @Test
    @Transactional
    public void testSaveEntityFailureDueToDuplicateCode() {
        Regle regle1 = new Regle();
        regle1.setCoderegle("R002");
        regle1.setReglePourSortie("Règle 1");
        regleRepository.saveAndFlush(regle1);

        Regle regle2 = new Regle();
        regle2.setCoderegle("R002");
        regle2.setReglePourSortie("Règle 2");

        assertThrows(DataIntegrityViolationException.class, () -> {
            regleRepository.saveAndFlush(regle2);
        }, "Une DataIntegrityViolationException aurait dû être levée pour le code duplicata.");
    }
    @Test
    @Transactional
    public void testCascadeDeleteWithSortieSemaine() {
        Regle regle = new Regle();
        regle.setCoderegle("R003");
        regle.setReglePourSortie("Règle avec sorties");

        SortieSemaine sortie1 = new SortieSemaine();
        sortie1.setRegle(regle);

        SortieSemaine sortie2 = new SortieSemaine();
        sortie2.setRegle(regle);

        regle.setSortieSemaine(List.of(sortie1, sortie2));

        regleRepository.saveAndFlush(regle);
        regleRepository.deleteById(regle.getId());

        Assertions.assertFalse(sortieSemaineRepository.existsById(sortie1.getId()), "La sortie 1 doit être supprimée en cascade.");
        Assertions.assertFalse(sortieSemaineRepository.existsById(sortie2.getId()), "La sortie 2 doit être supprimée en cascade.");
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
    public void testManyToOneRelationshipWithTypeRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("R010");
        regle.setReglePourSortie("Règle test");
        regleRepository.saveAndFlush(regle);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type 1");
        typeRegle.setRegle(regle); // Relation ManyToOne définie
        typeRegleRepository.saveAndFlush(typeRegle);

        assertNotNull(typeRegle.getRegle());
        assertEquals("R010", typeRegle.getRegle().getCoderegle());

        regle.setTypeRegle(typeRegle);
        regleRepository.saveAndFlush(regle);

        Regle fetchedRegle = regleRepository.findById(regle.getId()).orElseThrow();
        assertNotNull(fetchedRegle.getTypeRegle());
        assertEquals("Type 1", fetchedRegle.getTypeRegle().getNomTypeRegle());
    }








}
