package example.integration.entities;

import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class JourFerieIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();
    }
    @Test
    public void testSaveJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);
        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegle(regleA);

        JourFerie savedJF = jourFerieRepository.save(jourFerie);
        assertNotNull(savedJF.getId(), "generated ID should not be null");
        assertEquals(regleA.getId(), savedJF.getRegle().getId());

    }
    @Test
    public void testSaveJourFerieWithoutRegle() {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            jourFerieRepository.save(jourFerie);
        }, "A DataIntegrityViolationException is expected if there is no rule");
    }



    @Test
    public void testUpdateJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegle(regleA);
        JourFerie savedJF = jourFerieRepository.save(jourFerie);

        List<JourFerie> listJF = new ArrayList<>();
        savedJF.setJoursFerieList(new ArrayList<>());
        JourFerie updatedJF = jourFerieRepository.save(savedJF);

        assertEquals(regleA.getCoderegle(), updatedJF.getRegle().getCoderegle(), "La règle doit rester inchangée");
    }

    @Test
    public void testDeleteJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);
        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegle(regleA);
        JourFerie savedJD = jourFerieRepository.save(jourFerie);

        jourFerieRepository.delete(jourFerie);
        Optional<JourFerie> foundJf = jourFerieRepository.findById(jourFerie.getId());
        Assertions.assertFalse(foundJf.isPresent());
    }
    @Test
    public void testFindAllJourFerie() {
        Regle regle1 = new Regle();
        regle1.setCoderegle("R0002");
        regleRepository.save(regle1);
        Regle regle2 = new Regle();
        regle2.setCoderegle("R0001");
        regleRepository.save(regle2);

        JourFerie jourFerie1 = new JourFerie();
        jourFerie1.setJoursFerieList(new ArrayList<>());
        jourFerie1.setRegle(regle1);

        JourFerie jourFerie2 = new JourFerie();
        jourFerie2.setJoursFerieList(new ArrayList<>());
        jourFerie2.setRegle(regle2);
        jourFerieRepository.save(jourFerie1);
        jourFerieRepository.save(jourFerie2);
        List<JourFerie> listJF = jourFerieRepository.findAll();
        assertEquals(2, listJF.size());

    }

    @Test
    public void testFindJourFerieByIdNotFound() {
        Optional<JourFerie> foundJF = jourFerieRepository.findById(9999);

        Assertions.assertFalse(foundJF.isPresent(), "No holidays should be found with this ID");
    }

    @Test
    public void testCascadeDeleteWithRegle() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegle(regleA);
        jourFerieRepository.save(jourFerie);

        jourFerieRepository.deleteById(jourFerie.getId());

        assertTrue(regleRepository.findById(regleA.getId()).isPresent(), "The rule must still exist after the holiday is removed");
    }
    @Test
    public void testUpdateJourFerieWithDifferentRegle() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        Regle regleB = new Regle();
        regleB.setCoderegle("R0003");
        regleRepository.save(regleB);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegle(regleA);
        jourFerieRepository.save(jourFerie);

        jourFerie.setRegle(regleB);
        JourFerie updatedJF = jourFerieRepository.save(jourFerie);

        assertEquals(regleB.getId(), updatedJF.getRegle().getId(), "The rule should be updated correctly.");
    }







}
