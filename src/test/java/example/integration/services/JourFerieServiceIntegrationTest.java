package example.integration.services;

import example.entities.JourFerie;
import example.entities.Regle;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import example.services.JourFerieService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class JourFerieServiceIntegrationTest {

    @Autowired
    private JourFerieService jourFerieService;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    private JourFerie jourFerie;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();

        Regle regle = new Regle();
        regle.setCoderegle("R123");
        regle = regleRepository.save(regle);

        jourFerie = JourFerie.builder()
                .regle(regle)
                .build();
    }



    @Test
    public void testGetJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        JourFerie retrievedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());

        // Assert
        assertNotNull(retrievedJourFerie, "Le jour férié ne devrait pas être null");
        assertEquals(jourFerie.getId(), retrievedJourFerie.getId(), "L'ID du jour férié devrait correspondre");
        assertEquals(jourFerie.getJoursFerieList(), retrievedJourFerie.getJoursFerieList(), "La liste des jours fériés devrait correspondre");
        assertEquals(jourFerie.getRegle().getId(), retrievedJourFerie.getRegle().getId(), "La règle associée devrait correspondre");
    }

    @Test
    public void testGetJourFeries() {
        // Arrange
        jourFerieRepository.save(jourFerie);

        // Act
        List<JourFerie> jourFerieList = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(jourFerieList, "La liste des jours fériés ne devrait pas être null");
        assertEquals(1, jourFerieList.size(), "La taille de la liste des jours fériés devrait être de 1");
        assertEquals(jourFerie.getId(), jourFerieList.get(0).getId(), "L'ID du jour férié dans la liste devrait correspondre");
    }

    @Test
    public void testSaveAndRetrieveJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        JourFerie savedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());

        // Assert
        assertNotNull(savedJourFerie, "Le jour férié sauvegardé ne devrait pas être null");
        assertEquals(jourFerie.getId(), savedJourFerie.getId(), "L'ID du jour férié sauvegardé devrait correspondre");
    }

    @Test
    public void testDeleteJourFerie() {
        // Arrange
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act
        jourFerieRepository.deleteById(jourFerie.getId());

        // Assert
        JourFerie deletedJourFerie = jourFerieService.getJourFerie(jourFerie.getId());
        assertNull(deletedJourFerie, "Le jour férié devrait être null après suppression");
    }
}
