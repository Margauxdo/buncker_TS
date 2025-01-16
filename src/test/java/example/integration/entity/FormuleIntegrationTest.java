package example.integration.entity;

import example.entity.Formule;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class FormuleIntegrationTest {

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        regleRepository.findAll().forEach(regle -> {
            regle.setFormule(null);
            regleRepository.save(regle);
        });
        formuleRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    void testUpdateFormuleSuccess() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("libelle 4");
        formule.setFormule("formule4");
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        // Act
        savedFormule.setLibelle("libelle 4 modifié");
        savedFormule.setFormule("formule4 modifiée");
        Formule updatedFormule = formuleRepository.saveAndFlush(savedFormule);

        // Assert
        assertEquals("libelle 4 modifié", updatedFormule.getLibelle());
        assertEquals("formule4 modifiée", updatedFormule.getFormule());
    }

    @Test
    void testFindFormuleNotFound() {
        // Act
        Optional<Formule> formule = formuleRepository.findById(999);

        // Assert
        assertTrue(formule.isEmpty());
    }

    @Test
    void testDeleteFormuleSuccess() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("libelle 5");
        formule.setFormule("formule5");
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        // Act
        formuleRepository.delete(savedFormule);

        // Assert
        Optional<Formule> deletedFormule = formuleRepository.findById(savedFormule.getId());
        assertTrue(deletedFormule.isEmpty());
    }

    @Test
    void testCreateFormule() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");
        formule.setFormule("formule-test");

        // Act
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        // Assert
        Optional<Formule> found = formuleRepository.findById(savedFormule.getId());
        assertTrue(found.isPresent());
        assertEquals("Formule Test", found.get().getLibelle());
        assertEquals("formule-test", found.get().getFormule());
    }

    @Test
    void testFormuleRelationWithRegle() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle.setDateRegle(new Date());
        regle.setFormule(savedFormule);

        // Act
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Assert
        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());
        assertTrue(foundRegle.isPresent());
        assertEquals(savedFormule.getId(), foundRegle.get().getFormule().getId());
    }

    @Test
    void testDeleteFormuleWithRegle() {
        // Arrange
        Formule formule = new Formule();
        formule.setLibelle("libelle 6");
        formule.setFormule("formule6");
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        Regle regle = new Regle();
        regle.setCoderegle("R127");
        regle.setDateRegle(new Date());
        regle.setFormule(savedFormule);
        regleRepository.saveAndFlush(regle);

        regle.setFormule(null); // Detach Regle from Formule
        regleRepository.saveAndFlush(regle);

        // Act
        formuleRepository.delete(savedFormule);

        // Assert
        Optional<Formule> deletedFormule = formuleRepository.findById(savedFormule.getId());
        assertTrue(deletedFormule.isEmpty());
    }
}
