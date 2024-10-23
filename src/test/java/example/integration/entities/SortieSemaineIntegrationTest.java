package example.integration.entities;

import example.entities.Regle;
import example.entities.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class SortieSemaineIntegrationTest {

    @Autowired
    private SortieSemaineRepository semaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        semaineRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testSaveSortieSemaine() {
        Regle regle = new Regle();
        regle.setCoderegle("RG001");
        regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);

        SortieSemaine savedSortie = semaineRepository.save(sortieSemaine);
        Assertions.assertNotNull(savedSortie.getId(), "The output ID must be generated");
        Assertions.assertEquals(regle.getId(), savedSortie.getRegle().getId(), "The associated rule must match");
    }

    @Test
    public void testSaveSortieSemaineWithoutRegle() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            semaineRepository.save(sortieSemaine);
        }, "expected DataIntegrityViolationException");
    }


    @Test
    public void testUpdateSortieSemaine() {
        Regle regle = new Regle();
        regle.setCoderegle("RG001");
        regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        SortieSemaine savedSortie = semaineRepository.save(sortieSemaine);

        Date newDate = new Date();
        savedSortie.setDateSortieSemaine(newDate);
        SortieSemaine updatedSortie = semaineRepository.save(savedSortie);

        Assertions.assertEquals(newDate, updatedSortie.getDateSortieSemaine(), "Release date needs to be updated");
    }

    @Test
    public void testDeleteSortieSemaine() {
        Regle regle = new Regle();
        regle.setCoderegle("RG001");
        regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        SortieSemaine savedSortie = semaineRepository.save(sortieSemaine);

        semaineRepository.deleteById(savedSortie.getId());

        Optional<SortieSemaine> foundSortie = semaineRepository.findById(savedSortie.getId());
        Assertions.assertFalse(foundSortie.isPresent(), "La sortie supprimée ne doit plus être présente dans la base");
    }

    @Test
    public void testFindAllSortiesSemaine() {
        Regle regle1 = new Regle();
        regle1.setCoderegle("RG001");
        regleRepository.save(regle1);

        Regle regle2 = new Regle();
        regle2.setCoderegle("RG002");
        regleRepository.save(regle2);

        SortieSemaine sortie1 = new SortieSemaine();
        sortie1.setDateSortieSemaine(new Date());
        sortie1.setRegle(regle1);

        SortieSemaine sortie2 = new SortieSemaine();
        sortie2.setDateSortieSemaine(new Date());
        sortie2.setRegle(regle2);

        semaineRepository.save(sortie1);
        semaineRepository.save(sortie2);

        List<SortieSemaine> sorties = semaineRepository.findAll();
        Assertions.assertEquals(2, sorties.size(), "There must be two outputs in the database.");
    }

    @Test
    public void testFindSortieSemaineByIdNotFound() {
        Optional<SortieSemaine> foundSortie = semaineRepository.findById(9999);
        Assertions.assertFalse(foundSortie.isPresent(), "No output should be found");
    }

    @Test
    public void testCascadeDeleteWithRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("RG001");
        regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        semaineRepository.save(sortieSemaine);

        semaineRepository.deleteById(sortieSemaine.getId());

        regleRepository.deleteById(regle.getId());

        Assertions.assertFalse(regleRepository.findById(regle.getId()).isPresent(), "The rule must be deleted");
        Assertions.assertFalse(semaineRepository.findById(sortieSemaine.getId()).isPresent(), "the week outing should be deleted");
    }

}
