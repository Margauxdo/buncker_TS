package example.integration.repositories;

import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class JourFerieIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
    }
    @Test
    public void testSaveJourFerieSuccess() {
        Regle regleA = new Regle();

        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerieA);

        assertNotNull(savedJourFerie.getId());

        assertEquals(regleA, savedJourFerie.getRegle());
    }
    @Test
    public void testFindByIdSuccess(){
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);
        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);
        JourFerie savedJF = jourFerieRepository.save(jourFerieA);
        Optional<JourFerie> foundJF = jourFerieRepository.findById(savedJF.getId());
        assertTrue(foundJF.isPresent());
        assertEquals(regleA, foundJF.get().getRegle());
    }
    @Test
    public void testDeleteJourFerieSuccess(){
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);
        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);
        JourFerie savedJF = jourFerieRepository.save(jourFerieA);
        jourFerieRepository.deleteById(jourFerieA.getId());
        Optional<JourFerie> deletedJF = jourFerieRepository.findById(jourFerieA.getId());
        assertFalse(deletedJF.isPresent());
    }
    @Test
    public void testUpdateJourFerieSuccess(){
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);
        Regle regleB = new Regle();
        regleB.setCoderegle("CODE456");
        regleB = regleRepository.save(regleB);
        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);
        JourFerie savedJF = jourFerieRepository.save(jourFerieA);

        savedJF.setRegle(regleB);
        JourFerie updatedJF = jourFerieRepository.save(savedJF);
        Optional<JourFerie> foundJF = jourFerieRepository.findById(updatedJF.getId());
        assertTrue(foundJF.isPresent());
        assertEquals(regleB, foundJF.get().getRegle());
    }
    @Test
    public void testFindAllJourFerieSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        Regle regleB = new Regle();
        regleB.setCoderegle("CODE456");
        regleB = regleRepository.save(regleB);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);
        jourFerieRepository.save(jourFerieA);

        JourFerie jourFerieB = new JourFerie();
        jourFerieB.setRegle(regleB);
        jourFerieRepository.save(jourFerieB);

        List<JourFerie> listJF = jourFerieRepository.findAll();

        assertNotNull(listJF);
        assertTrue(listJF.size() >= 2);

        listJF.forEach(System.out::println);

        assertTrue(listJF.contains(jourFerieA));
        assertTrue(listJF.contains(jourFerieB));
    }

    @Test
    public void deleteByRegleIdSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegle(regleA);
        jourFerieRepository.save(jourFerieA);

        assertTrue(jourFerieRepository.findById(jourFerieA.getId()).isPresent());

        int deletedCount = jourFerieRepository.deleteByRegleId(regleA.getId());

        assertEquals(1, deletedCount);
        assertFalse(jourFerieRepository.findById(jourFerieA.getId()).isPresent());
    }

    @Test
    public void deleteByRegleIdNotFound() {
        int deletedCount = jourFerieRepository.deleteByRegleId(999); // Utiliser un ID qui n'existe pas

        assertEquals(0, deletedCount);
    }






}
