package example.integration.entities;

import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();
        formuleRepository.deleteAll();
    }

    @Test
    public void testSaveJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegles(Collections.singletonList(regleA)); // Initialisation correcte

        JourFerie savedJF = jourFerieRepository.save(jourFerie);

        assertNotNull(savedJF.getId(), "Generated ID should not be null");
        assertEquals(regleA.getId(), savedJF.getRegles().get(0).getId(), "The associated rule ID should match");
    }

    @Test
    public void testSaveJourFerieWithoutRegle() {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> jourFerieRepository.save(jourFerie),
                "A DataIntegrityViolationException is expected if there is no rule");
    }

    @Test
    public void testUpdateJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegles(Collections.singletonList(regleA));
        JourFerie savedJF = jourFerieRepository.save(jourFerie);

        savedJF.setJoursFerieList(Collections.singletonList(new java.util.Date()));
        JourFerie updatedJF = jourFerieRepository.save(savedJF);

        assertEquals(1, updatedJF.getJoursFerieList().size(), "The holiday list size should match");
        assertEquals(regleA.getCoderegle(), updatedJF.getRegles().get(0).getCoderegle(), "The associated rule should remain unchanged");
    }

    @Test
    public void testDeleteJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");
        regleRepository.save(regleA);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegles(Collections.singletonList(regleA));
        jourFerieRepository.save(jourFerie);

        jourFerieRepository.deleteById(jourFerie.getId());
        Optional<JourFerie> foundJf = jourFerieRepository.findById(jourFerie.getId());

        assertFalse(foundJf.isPresent(), "The holiday should be deleted");
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
        jourFerie1.setRegles(Collections.singletonList(regle1));
        jourFerieRepository.save(jourFerie1);

        JourFerie jourFerie2 = new JourFerie();
        jourFerie2.setJoursFerieList(new ArrayList<>());
        jourFerie2.setRegles(Collections.singletonList(regle2));
        jourFerieRepository.save(jourFerie2);

        List<JourFerie> listJF = jourFerieRepository.findAll();
        assertEquals(2, listJF.size(), "The size of the holiday list should be 2");
    }

    @Test
    public void testCascadeDeleteWithFormule() {
        Formule formule = new Formule();
        formule.setFormule("Formule Test");
        formule = formuleRepository.save(formule);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(new ArrayList<>());
        jourFerie.setRegles(Collections.emptyList());
        jourFerieRepository.save(jourFerie);

        jourFerieRepository.deleteById(jourFerie.getId());

        assertTrue(formuleRepository.findById(formule.getId()).isPresent(),
                "The formula should still exist after the holiday is removed");
    }
}
