package example.integration.entities;

import example.entities.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RegleManuelleIntegrationTest {

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    @BeforeEach
    public void setUp() {
        regleManuelleRepository.deleteAll();
    }

    @Test
    public void testSaveRegleManuelle() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("adminA");
        regleManuelle.setDescriptionRegle("This is a manual rule");

        regleManuelle.setCoderegle("RM001");

        RegleManuelle savedRegle = regleManuelleRepository.save(regleManuelle);
        Assertions.assertNotNull(savedRegle.getId(), "L'ID de la règle doit être généré");
        Assertions.assertEquals("adminA", savedRegle.getCreateurRegle());
        Assertions.assertEquals("This is a manual rule", savedRegle.getDescriptionRegle());
        Assertions.assertEquals("RM001", savedRegle.getCoderegle());
    }


    @Test
    public void testSaveRegleManuelleWithoutCreateur() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setDescriptionRegle("This is a rule without a creator");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            regleManuelleRepository.save(regleManuelle);
        }, "La sauvegarde devrait échouer si le créateur de la règle est manquant");
    }

    @Test
    public void testDeleteRegleManuelle() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("adminA");
        regleManuelle.setDescriptionRegle("This rule will be deleted");

        regleManuelle.setCoderegle("RM001");

        RegleManuelle savedRegle = regleManuelleRepository.save(regleManuelle);
        regleManuelleRepository.deleteById(savedRegle.getId());

        Optional<RegleManuelle> foundRegle = regleManuelleRepository.findById(savedRegle.getId());
        Assertions.assertFalse(foundRegle.isPresent(), "La règle supprimée ne doit plus être présente dans la base");
    }

    @Test
    public void testUpdateRegleManuelle() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("adminA");
        regleManuelle.setDescriptionRegle("Initial rule description");
        regleManuelle.setCoderegle("RM001");

        RegleManuelle savedRegle = regleManuelleRepository.save(regleManuelle);

        savedRegle.setDescriptionRegle("Updated rule description");
        RegleManuelle updatedRegle = regleManuelleRepository.save(savedRegle);

        Assertions.assertEquals("Updated rule description", updatedRegle.getDescriptionRegle(),
                "La description de la règle doit être mise à jour");
    }

    @Test
    public void testFindAllReglesManuelles() {
        RegleManuelle regle1 = new RegleManuelle();
        regle1.setCreateurRegle("adminA");
        regle1.setDescriptionRegle("First rule");
        regle1.setCoderegle("RM001");

        RegleManuelle regle2 = new RegleManuelle();
        regle2.setCreateurRegle("adminB");
        regle2.setDescriptionRegle("Second rule");
        regle2.setCoderegle("RM002");

        regleManuelleRepository.save(regle1);
        regleManuelleRepository.save(regle2);

        List<RegleManuelle> regles = regleManuelleRepository.findAll();

        Assertions.assertEquals(2, regles.size(), "Il doit y avoir deux règles dans la base de données.");
    }

    @Test
    public void testSaveRegleManuelleWithNullDescription() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("adminA");
        regleManuelle.setCoderegle("RM001");

        RegleManuelle savedRegle = regleManuelleRepository.save(regleManuelle);

        Assertions.assertNotNull(savedRegle.getId(), "La règle doit être sauvegardée même sans description.");
        Assertions.assertNull(savedRegle.getDescriptionRegle(), "La description de la règle doit être nulle.");
    }

    @Test
    public void testSaveDuplicateCoderegle() {
        RegleManuelle regle1 = new RegleManuelle();
        regle1.setCreateurRegle("adminA");
        regle1.setDescriptionRegle("First rule");
        regle1.setCoderegle("RM001");
        regleManuelleRepository.save(regle1);

        RegleManuelle regle2 = new RegleManuelle();
        regle2.setCreateurRegle("adminB");
        regle2.setDescriptionRegle("Second rule");
        regle2.setCoderegle("RM001");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            regleManuelleRepository.save(regle2);
        }, "Une exception doit être levée si un coderegle en double est sauvegardé.");
    }





}
