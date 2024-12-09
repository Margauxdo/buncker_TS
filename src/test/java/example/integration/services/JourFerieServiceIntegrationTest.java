package example.integration.services;

import example.DTO.JourFerieDTO;
import example.entity.JourFerie;
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

    private JourFerieDTO jourFerieDTO;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();

        jourFerieDTO = JourFerieDTO.builder()
                .date(new Date())
                .regleIds(new ArrayList<>()) // Start with an empty list of rule IDs
                .build();
    }

    @Test
    public void testCreateJourFerie_Success() {
        // Arrange
        jourFerieDTO.setRegleIds(List.of(1)); // Ajouter des IDs fictifs pour les règles

        // Act
        JourFerieDTO savedJourFerie = jourFerieService.saveJourFerie(jourFerieDTO);

        // Assert
        assertNotNull(savedJourFerie);
        assertNotNull(savedJourFerie.getId());
        assertEquals(jourFerieDTO.getDate(), savedJourFerie.getDate());
        assertEquals(1, savedJourFerie.getRegleIds().size());
    }

    @Test
    public void testCreateJourFerie_Failure_NoRegle() {
        // Arrange: Aucun ID de règle n'est ajouté
        jourFerieDTO.setRegleIds(new ArrayList<>());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jourFerieService.saveJourFerie(jourFerieDTO);
        });
        assertEquals("A JourFerie must have at least one associated Regle", exception.getMessage());
    }

    @Test
    public void testGetJourFerie_Success() {
        // Arrange
        jourFerieDTO.setRegleIds(List.of(1)); // Ajouter un ID fictif
        JourFerieDTO savedJourFerie = jourFerieService.saveJourFerie(jourFerieDTO);

        // Act
        JourFerieDTO fetchedJourFerie = jourFerieService.getJourFerie(savedJourFerie.getId());

        // Assert
        assertNotNull(fetchedJourFerie);
        assertEquals(savedJourFerie.getId(), fetchedJourFerie.getId());
        assertEquals(savedJourFerie.getDate(), fetchedJourFerie.getDate());
        assertEquals(1, fetchedJourFerie.getRegleIds().size());
    }

    @Test
    public void testGetJourFerie_Failure_NotFound() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            jourFerieService.getJourFerie(999);
        });

        assertEquals("JourFerie not found with ID: 999", exception.getMessage());
    }

    @Test
    public void testGetAllJourFeries() {
        // Arrange
        JourFerieDTO jourFerie1 = JourFerieDTO.builder()
                .date(new Date())
                .regleIds(List.of(1))
                .build();
        JourFerieDTO jourFerie2 = JourFerieDTO.builder()
                .date(new Date())
                .regleIds(List.of(2))
                .build();

        jourFerieService.saveJourFerie(jourFerie1);
        jourFerieService.saveJourFerie(jourFerie2);

        // Act
        List<JourFerieDTO> jourFeries = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(jourFeries);
        assertEquals(2, jourFeries.size());
    }

    @Test
    public void testDeleteJourFerie_Success() {
        // Arrange
        jourFerieDTO.setRegleIds(List.of(1)); // Ajouter un ID fictif
        JourFerieDTO savedJourFerie = jourFerieService.saveJourFerie(jourFerieDTO);

        // Act
        jourFerieService.deleteJourFerie(savedJourFerie.getId());

        // Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            jourFerieService.getJourFerie(savedJourFerie.getId());
        });
        assertEquals("JourFerie not found with ID: " + savedJourFerie.getId(), exception.getMessage());
    }
}
