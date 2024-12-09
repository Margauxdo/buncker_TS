package example.integration.entity;

import example.DTO.JourFerieDTO;
import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import example.services.JourFerieService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

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

    @Autowired
    private JourFerieService jourFerieService;

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

        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());
        regleA.setJourFerie(jourFerie); // Bidirectional relationship
        jourFerie.setRegles(Collections.singletonList(regleA));

        JourFerie savedJF = jourFerieRepository.save(jourFerie);

        assertNotNull(savedJF.getId(), "Generated ID should not be null");
        assertEquals("R0002", savedJF.getRegles().get(0).getCoderegle(), "The associated rule ID should match");
    }

    @Test
    public void testSaveJourFerieWithoutRegle() {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jourFerieService.saveJourFerie(new JourFerieDTO());
        });

        assertEquals("A JourFerie must have at least one associated Regle", exception.getMessage());
    }



    @Test
    public void testDeleteJourFerie() {
        Regle regleA = new Regle();
        regleA.setCoderegle("R0002");

        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());
        regleA.setJourFerie(jourFerie);
        jourFerie.setRegles(Collections.singletonList(regleA));
        jourFerieRepository.save(jourFerie);

        jourFerieRepository.deleteById(jourFerie.getId());
        Optional<JourFerie> foundJF = jourFerieRepository.findById(jourFerie.getId());

        assertFalse(foundJF.isPresent(), "The holiday should be deleted");
        assertFalse(regleRepository.existsById(regleA.getId()), "Associated rules should also be deleted");
    }

    @Test
    public void testFindAllJourFerie() {
        Regle regle1 = new Regle();
        regle1.setCoderegle("R0002");
        Regle regle2 = new Regle();
        regle2.setCoderegle("R0001");

        JourFerie jourFerie1 = new JourFerie();
        jourFerie1.setDate(new Date());
        regle1.setJourFerie(jourFerie1);
        jourFerie1.setRegles(Collections.singletonList(regle1));
        jourFerieRepository.save(jourFerie1);

        JourFerie jourFerie2 = new JourFerie();
        jourFerie2.setDate(new Date());
        regle2.setJourFerie(jourFerie2);
        jourFerie2.setRegles(Collections.singletonList(regle2));
        jourFerieRepository.save(jourFerie2);

        List<JourFerie> listJF = jourFerieRepository.findAll();
        assertEquals(2, listJF.size(), "The size of the holiday list should be 2");
    }

    @Test
    public void testCascadeDeleteJourFerieWithRegles() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");

        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());
        regle.setJourFerie(jourFerie);
        jourFerie.setRegles(Collections.singletonList(regle));
        jourFerie = jourFerieRepository.save(jourFerie);

        jourFerieRepository.deleteById(jourFerie.getId());
        assertFalse(jourFerieRepository.existsById(jourFerie.getId()), "The holiday should be deleted");
        assertFalse(regleRepository.existsById(regle.getId()), "Associated rules should also be deleted");
    }

    @Test
    public void testOrphanRemovalForRegle() {
        // Create a new Regle and associate it with a JourFerie
        Regle regle = new Regle();
        regle.setCoderegle("R0003");

        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());
        jourFerie.getRegles().add(regle);
        regle.setJourFerie(jourFerie);

        // Save the JourFerie, which also saves the Regle
        jourFerie = jourFerieRepository.save(jourFerie);

        // Remove the Regle from the JourFerie's regles list
        jourFerie.getRegles().removeIf(r -> r.getCoderegle().equals("R0003"));

        // Save the JourFerie after the removal
        jourFerieRepository.save(jourFerie);

        // Verify that the Regle has been removed from the database
        assertFalse(regleRepository.existsById(regle.getId()), "The orphaned Regle should be removed from the database");
    }

}
