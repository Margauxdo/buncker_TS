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
        regleRepository.deleteAll();
    }

    @Test
    public void testSaveJourFerieSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegles(List.of(regleA));
        jourFerieA = jourFerieRepository.save(jourFerieA);

        assertNotNull(jourFerieA.getId());
        assertEquals(1, jourFerieA.getRegles().size());
        assertEquals("CODE123", jourFerieA.getRegles().get(0).getCoderegle());
    }

    @Test
    public void testFindByIdSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegles(List.of(regleA));
        jourFerieA = jourFerieRepository.save(jourFerieA);

        Optional<JourFerie> foundJF = jourFerieRepository.findById(jourFerieA.getId());
        assertTrue(foundJF.isPresent());
        assertEquals(1, foundJF.get().getRegles().size());
        assertEquals("CODE123", foundJF.get().getRegles().get(0).getCoderegle());
    }

    @Test
    public void testDeleteJourFerieSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegles(List.of(regleA));
        jourFerieA = jourFerieRepository.save(jourFerieA);

        jourFerieRepository.deleteById(jourFerieA.getId());
        Optional<JourFerie> deletedJF = jourFerieRepository.findById(jourFerieA.getId());
        assertFalse(deletedJF.isPresent());
    }

    @Test
    public void testUpdateJourFerieSuccess() {
        Regle regleA = new Regle();
        regleA.setCoderegle("CODE123");
        regleA = regleRepository.save(regleA);

        Regle regleB = new Regle();
        regleB.setCoderegle("CODE456");
        regleB = regleRepository.save(regleB);

        JourFerie jourFerieA = new JourFerie();
        jourFerieA.setRegles(List.of(regleA));
        jourFerieA = jourFerieRepository.save(jourFerieA);

        jourFerieA.setRegles(List.of(regleB));
        JourFerie updatedJF = jourFerieRepository.save(jourFerieA);

        assertEquals(1, updatedJF.getRegles().size());
        assertEquals("CODE456", updatedJF.getRegles().get(0).getCoderegle());
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
        jourFerieA.setRegles(List.of(regleA));
        jourFerieRepository.save(jourFerieA);

        JourFerie jourFerieB = new JourFerie();
        jourFerieB.setRegles(List.of(regleB));
        jourFerieRepository.save(jourFerieB);

        List<JourFerie> listJF = jourFerieRepository.findAll();

        assertNotNull(listJF);
        assertEquals(2, listJF.size());
    }

    @Test
    public void testDeleteJourFerieByInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> jourFerieRepository.deleteById(9999));
    }
}
