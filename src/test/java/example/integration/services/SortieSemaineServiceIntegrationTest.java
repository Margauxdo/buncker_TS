package example.integration.services;

import example.entities.Regle;
import example.entities.SortieSemaine;
import example.entities.Valise;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import example.services.SortieSemaineService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class SortieSemaineServiceIntegrationTest {

    @Autowired
    private SortieSemaineService sortieSemaineService;
    @Autowired
    private SortieSemaineRepository semaineRepository;
    private SortieSemaine semaine;
    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
        Regle regle1 = new Regle();
        regle1.setCoderegle("25463AL");
        regle1 = regleRepository.save(regle1);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d1 = sdf.parse("01/01/2016");


            semaine = SortieSemaine.builder()
                    .dateSortieSemaine(d1)
                    .regle(regle1)
                    .build();
            semaine = sortieSemaineService.createSortieSemaine(semaine);

        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateSortieSemaine() {
        // Act
        SortieSemaine savedSem = sortieSemaineService.createSortieSemaine(semaine);
        //Assert
        assertNotNull(savedSem);
        assertNotNull(savedSem.getDateSortieSemaine());
        assertNotNull(savedSem.getRegle());
        assertEquals("25463AL",savedSem.getRegle().getCoderegle());
    }
    @Test
    public void testupdateSortieSemaine() {
        // Arrange
        SortieSemaine savedSem = sortieSemaineService.createSortieSemaine(semaine);
        Regle newRegle = new Regle();
        newRegle.setCoderegle("569823ZZ");
        newRegle = regleRepository.save(newRegle);

        // Act -
        savedSem.setRegle(newRegle);
        SortieSemaine updatedSem = sortieSemaineService.updateSortieSemaine(savedSem.getId(), savedSem);

        // Assert
        assertNotNull(updatedSem, "The update of SortieSemaine failed, the entity is nulll");
        assertEquals("569823ZZ", updatedSem.getRegle().getCoderegle(),
                "Rule code was not updated correctly.");
    }



    @Test
    public void testDeleteSortieSemaine() {
        // Arrange
        SortieSemaine savedSem = sortieSemaineService.createSortieSemaine(semaine);

        // Act
        sortieSemaineService.deleteSortieSemaine(savedSem.getId());

        // Assert
        boolean exists = semaineRepository.findById(savedSem.getId()).isPresent();
        assertFalse(exists, "The SortieSemaine entity should no longer exist after deletion.");
    }

    @Test
    public void testGetSortieSemaine(){
        //Arrange
        SortieSemaine savedSem = sortieSemaineService.createSortieSemaine(semaine);
        //Act
        SortieSemaine semById = sortieSemaineService.getSortieSemaine(savedSem.getId());
        //Assert
        assertNotNull(semById);
        assertEquals("25463AL",semById.getRegle().getCoderegle());

    }
    @Test
    public void testGetAllSortieSemaine() {
        sortieSemaineRepository.deleteAll();
        regleRepository.deleteAll();

        // Arrange
        Regle regle2 = new Regle();
        regle2.setCoderegle("25445ML");
        regle2 = regleRepository.save(regle2);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d1 = sdf.parse("25/11/2024");

            SortieSemaine sem1 = SortieSemaine.builder()
                    .dateSortieSemaine(d1)
                    .regle(regle2)
                    .build();
            sortieSemaineService.createSortieSemaine(sem1);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Regle regle3 = new Regle();
        regle3.setCoderegle("569823AL");
        regle3 = regleRepository.save(regle3);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d1 = sdf.parse("20/11/2024");

            SortieSemaine sem2 = SortieSemaine.builder()
                    .dateSortieSemaine(d1)
                    .regle(regle3)
                    .build();
            sortieSemaineService.createSortieSemaine(sem2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Act
        List<SortieSemaine> listSem = sortieSemaineService.getAllSortieSemaine();

        // Assert
        assertNotNull(listSem);
        assertEquals(2, listSem.size(), "Expected exactly 2 SortieSemaine entries in the list");
    }

}
