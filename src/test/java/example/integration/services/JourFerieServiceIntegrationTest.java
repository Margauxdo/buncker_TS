package example.integration.services;

import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import example.services.JourFerieService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class JourFerieServiceIntegrationTest {

    @Autowired
    private JourFerieService jourFerieService;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private FormuleRepository formuleRepository;

    private JourFerie jourFerie;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();

        Regle regle = new Regle();
        regle.setCoderegle("R123");
        regle = regleRepository.save(regle);

        jourFerie = JourFerie.builder()
                .regles(List.of(regle)) // Fixed invalid cast
                .build();
    }

    @Test
    public void testGetJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        JourFerie retrievedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());

        // Assert
        assertNotNull(retrievedJourFerie, "The jour férié should not be null");
        assertEquals(jourFerie.getId(), retrievedJourFerie.getId(), "The IDs should match");
        assertEquals(1, retrievedJourFerie.getRegles().size(), "There should be one associated rule");
        assertEquals("R123", retrievedJourFerie.getRegles().get(0).getCoderegle(), "The associated rule code should match");
    }

    @Test
    public void testGetJourFeries() {
        // Arrange
        jourFerieRepository.save(jourFerie);

        // Act
        List<JourFerie> jourFerieList = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(jourFerieList, "The list of jour fériés should not be null");
        assertEquals(1, jourFerieList.size(), "There should be one jour férié in the list");
        assertEquals(jourFerie.getId(), jourFerieList.get(0).getId(), "The IDs should match");
    }

    @Test
    public void testSaveAndRetrieveJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        JourFerie savedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());

        // Assert
        assertNotNull(savedJourFerie, "The saved jour férié should not be null");
        assertEquals(jourFerie.getId(), savedJourFerie.getId(), "The IDs should match");
    }

    @Test
    public void testDeleteJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        jourFerieService.getAllDateFerie();

        // Assert
        JourFerie deletedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());
        assertNull(deletedJourFerie, "The jour férié should be null after deletion");
    }

    @Test
    public void testSaveJourFerieWithFormule() {
        // Arrange
        Formule formule = new Formule();
        formule.setFormule("Formule Test");
        formuleRepository.save(formule);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date()); // Properly set a date
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        JourFerie savedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());

        // Assert
        assertNotNull(savedJourFerie, "The jour férié should not be null");
        assertNotNull(savedJourFerie.getDate(), "The date should not be null");
        assertEquals(jourFerie.getDate(), savedJourFerie.getDate(), "The dates should match");
    }
}
