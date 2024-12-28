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
                .build();
    }

    @Test
    public void testCreateJourFerie_Success() {
        // Arrange

        // Act
        JourFerieDTO savedJourFerie = jourFerieService.saveJourFerie(jourFerieDTO);

        // Assert
        assertNotNull(savedJourFerie);
        assertNotNull(savedJourFerie.getId());
        assertEquals(jourFerieDTO.getDate(), savedJourFerie.getDate());
    }


    @Test
    public void testGetJourFerie_Success() {
        // Arrange
        JourFerieDTO savedJourFerie = jourFerieService.saveJourFerie(jourFerieDTO);

        // Act
        JourFerieDTO fetchedJourFerie = jourFerieService.getJourFerie(savedJourFerie.getId());

        // Assert
        assertNotNull(fetchedJourFerie);
        assertEquals(savedJourFerie.getId(), fetchedJourFerie.getId());
        assertEquals(savedJourFerie.getDate(), fetchedJourFerie.getDate());
    }


    @Test
    public void testGetAllJourFeries() {
        // Arrange
        JourFerieDTO jourFerie1 = JourFerieDTO.builder()
                .date(new Date())
                .build();
        JourFerieDTO jourFerie2 = JourFerieDTO.builder()
                .date(new Date())
                .build();

        jourFerieService.saveJourFerie(jourFerie1);
        jourFerieService.saveJourFerie(jourFerie2);

        // Act
        List<JourFerieDTO> jourFeries = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(jourFeries);
        assertEquals(2, jourFeries.size());
    }


}
