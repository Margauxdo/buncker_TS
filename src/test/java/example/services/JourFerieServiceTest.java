package example.services;

import example.DTO.JourFerieDTO;
import example.entity.JourFerie;
import example.repositories.JourFerieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JourFerieServiceTest {

    @Mock
    private JourFerieRepository jourFerieRepository;

    @InjectMocks
    private JourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private JourFerie createJourFerie() {
        return JourFerie.builder()
                .id(1)
                .date(new Date())
                .build();
    }

    private JourFerieDTO createJourFerieDTO() {
        return JourFerieDTO.builder()
                .id(1)
                .date(new Date())
                .build();
    }

    @Test
    public void testGetJourFerie_Success() {
        // Arrange
        int id = 1;
        JourFerie jourFerie = createJourFerie();

        when(jourFerieRepository.findById(id)).thenReturn(Optional.of(jourFerie));

        // Act
        JourFerieDTO result = jourFerieService.getJourFerie(id);

        // Assert
        assertNotNull(result, "JourFerie should not be null");
        assertEquals(id, result.getId(), "JourFerie ID should match");
        verify(jourFerieRepository, times(1)).findById(id);
    }

    @Test
    public void testGetJourFerie_NotFound() {
        // Arrange
        int id = 1;

        when(jourFerieRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> jourFerieService.getJourFerie(id)
        );

        assertEquals("JourFerie introuvable avec l'ID : " + id, exception.getMessage());
        verify(jourFerieRepository, times(1)).findById(id);
    }

    @Test
    public void testGetJourFeries_Success() {
        // Arrange
        List<JourFerie> jourFeries = new ArrayList<>();
        jourFeries.add(createJourFerie());
        jourFeries.add(createJourFerie());

        when(jourFerieRepository.findAll()).thenReturn(jourFeries);

        // Act
        List<JourFerieDTO> result = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "The list of jourFeries should contain 2 elements");
        verify(jourFerieRepository, times(1)).findAll();
    }

    @Test
    public void testGetJourFeries_EmptyList() {
        // Arrange
        when(jourFerieRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<JourFerieDTO> result = jourFerieService.getJourFeries();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertTrue(result.isEmpty(), "The list of jourFeries should be empty");
        verify(jourFerieRepository, times(1)).findAll();
    }

    @Test
    public void testSaveJourFerie_Success() {
        // Arrange
        JourFerieDTO jourFerieDTO = createJourFerieDTO();
        JourFerie jourFerie = createJourFerie();

        when(jourFerieRepository.save(any(JourFerie.class))).thenReturn(jourFerie);

        // Act
        JourFerieDTO result = jourFerieService.saveJourFerie(jourFerieDTO);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(jourFerieDTO.getDate(), result.getDate(), "Dates should match");
        verify(jourFerieRepository, times(1)).save(any(JourFerie.class));
    }




    @Test
    public void testDeleteJourFerie_NotFound() {
        // Arrange
        int id = 1;

        when(jourFerieRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> jourFerieService.deleteJourFerie(id)
        );

        assertEquals("JourFerie introuvable avec l'ID : " + id, exception.getMessage());
        verify(jourFerieRepository, times(1)).findById(id);
    }
}
