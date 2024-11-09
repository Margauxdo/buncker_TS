package example.integration.entities;

import example.entities.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import jakarta.validation.ConstraintViolationException;
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
        Assertions.assertNotNull(savedRegle.getId(), "The rule ID must be generated");
        Assertions.assertEquals("adminA", savedRegle.getCreateurRegle());
        Assertions.assertEquals("This is a manual rule", savedRegle.getDescriptionRegle());
        Assertions.assertEquals("RM001", savedRegle.getCoderegle());
    }


    @Test
    public void testSaveRegleManuelleWithoutCreateur() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setDescriptionRegle("This is a rule without a creator");

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            regleManuelleRepository.save(regleManuelle);
        }, "Save should fail if rule creator is missing");
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
        Assertions.assertFalse(foundRegle.isPresent(), "The deleted rule must no longer be present in the database");
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
                "The rule description needs to be updated");
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

        Assertions.assertEquals(2, regles.size(), "There must be two rules in the database.");
    }

    @Test
    public void testSaveRegleManuelleWithDescription() {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("adminA");
        regleManuelle.setCoderegle("RM001");
        regleManuelle.setDescriptionRegle("Description de test");

        RegleManuelle savedRegle = regleManuelleRepository.save(regleManuelle);

        Assertions.assertNotNull(savedRegle.getId(), "The rule must be saved with a description.");
        Assertions.assertEquals("Description de test", savedRegle.getDescriptionRegle(), "The rule description should match.");
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
        }, "An exception should be thrown if a duplicate rulecode is saved.");
    }





}
