package example.integration.services;

import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.JourFerieRepository;
import example.services.JourFerieService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
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

    private JourFerie jourFerie;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();

        jourFerie = JourFerie.builder()
                .date(new Date())
                .regles(new ArrayList<>()) // Start with an empty list of rules
                .build();
    }

    @Test
    public void testCreateJourFerie_Success() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("CODE123")
                .build();
        jourFerie.getRegles().add(regle);

        // Act
        JourFerie savedJourFerie = jourFerieService.saveJourFerie(jourFerie);

        // Assert
        assertNotNull(savedJourFerie);
        assertNotNull(savedJourFerie.getId());
        assertEquals(jourFerie.getDate(), savedJourFerie.getDate());
        assertEquals(1, savedJourFerie.getRegles().size());
        assertEquals("CODE123", savedJourFerie.getRegles().get(0).getCoderegle());
    }


    @Test
    public void testCreateJourFerie_Failure_NoRegle() {
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jourFerieService.saveJourFerie(jourFerie);
        });
        assertEquals("A JourFerie must have at least one associated Regle", exception.getMessage());
    }

    @Test
    public void testGetJourFerie_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("RULE_001");
        jourFerie.getRegles().add(regle);

        JourFerie savedJourFerie = jourFerieService.saveJourFerie(jourFerie);

        // Act
        JourFerie fetchedJourFerie = jourFerieService.getJourFerie(savedJourFerie.getId());

        // Assert
        assertNotNull(fetchedJourFerie);
        assertEquals(savedJourFerie.getId(), fetchedJourFerie.getId());
        assertEquals(savedJourFerie.getDate(), fetchedJourFerie.getDate());
        assertEquals(1, fetchedJourFerie.getRegles().size());
        assertEquals("RULE_001", fetchedJourFerie.getRegles().get(0).getCoderegle());
    }


    @Test
    public void testGetJourFerie_Failure_NotFound() {
        // Act
        JourFerie fetchedJourFerie = jourFerieService.getJourFerie(999);

        // Assert
        assertNull(fetchedJourFerie, "A JourFerie with ID 999 should not exist.");
    }

    @Test
    public void testGetAllJourFeries() {
        // Arrange
        JourFerie jourFerie1 = JourFerie.builder()
                .date(new Date())
                .regles(new ArrayList<>())
                .build();
        Regle regle1 = new Regle();
        regle1.setCoderegle("R001");
        regle1.setJourFerie(jourFerie1);
        jourFerie1.getRegles().add(regle1);

        jourFerieService.saveJourFerie(jourFerie1);

        JourFerie jourFerie2 = JourFerie.builder()
                .date(new Date())
                .regles(new ArrayList<>())
                .build();
        Regle regle2 = new Regle();
        regle2.setCoderegle("R002");
        regle2.setJourFerie(jourFerie2);
        jourFerie2.getRegles().add(regle2);

        jourFerieService.saveJourFerie(jourFerie2);

        // Act
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(jourFeries);
        assertEquals(2, jourFeries.size());
    }



    @Test
    public void testDeleteJourFerie() {
        Regle regle = Regle.builder()
                .coderegle("CODE123")
                .build();

        jourFerie.getRegles().add(regle);

        JourFerie savedJourFerie = jourFerieService.saveJourFerie(jourFerie);

        // Act:
        jourFerieService.deleteJourFerie(savedJourFerie.getId());

        // Assert:
        JourFerie deletedJourFerie = jourFerieService.getJourFerie(savedJourFerie.getId());
        assertNull(deletedJourFerie, "The JourFerie should be deleted and not found again.");
    }



}

