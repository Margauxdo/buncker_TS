package example.services;

import example.entities.JourFerie;
import example.repositories.JourFerieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class JourFerieServiceTest {

    @Mock
    private JourFerieRepository jourFerieRepository;

    @InjectMocks
    private JourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test pour récupérer un jour férié existant
    @Test
    public void testGetJourFerie_Success() {
        int id = 1;
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(id);

        when(jourFerieRepository.findById(id)).thenReturn(Optional.of(jourFerie));

        JourFerie result = jourFerieService.getJourFerie(id);

        Assertions.assertNotNull(result, "JourFerie should not be null");
        Assertions.assertEquals(id, result.getId(), "JourFerie ID should match");

        verify(jourFerieRepository, times(1)).findById(id);
        verifyNoMoreInteractions(jourFerieRepository);
    }

    // Test pour récupérer un jour férié inexistant
    @Test
    public void testGetJourFerie_Failure() {
        int id = 1;

        when(jourFerieRepository.findById(id)).thenReturn(Optional.empty());

        JourFerie result = jourFerieService.getJourFerie(id);

        Assertions.assertNull(result, "JourFerie should be null if not found");

        verify(jourFerieRepository, times(1)).findById(id);
        verifyNoMoreInteractions(jourFerieRepository);
    }

    // Test pour récupérer tous les jours fériés avec succès
    @Test
    public void testGetJourFeries_Success() {
        List<JourFerie> jourFeries = new ArrayList<>();
        jourFeries.add(new JourFerie());
        jourFeries.add(new JourFerie());

        when(jourFerieRepository.findAll()).thenReturn(jourFeries);

        List<JourFerie> result = jourFerieService.getJourFeries();

        Assertions.assertEquals(2, result.size(), "The list of jourFeries should contain 2 elements");

        verify(jourFerieRepository, times(1)).findAll();
        verifyNoMoreInteractions(jourFerieRepository);
    }

    // Test pour récupérer une liste vide de jours fériés
    @Test
    public void testGetJourFeries_EmptyList() {
        when(jourFerieRepository.findAll()).thenReturn(new ArrayList<>());

        List<JourFerie> result = jourFerieService.getJourFeries();

        Assertions.assertTrue(result.isEmpty(), "The list of jourFeries should be empty");

        verify(jourFerieRepository, times(1)).findAll();
        verifyNoMoreInteractions(jourFerieRepository);
    }

    @Test
    public void testNoInteractionWithJourFerieRepository_Success() {
        verifyNoInteractions(jourFerieRepository);
    }

    @Test
    public void testNoInteractionWithJourFerieRepository_Failure_Exception() {
        jourFerieService.getJourFerie(1); // Provoque une interaction avec le dépôt
        Assertions.assertThrows(AssertionError.class, () -> {
            verifyNoInteractions(jourFerieRepository); // Ce test échouera car il y a eu une interaction
        });
    }
}

