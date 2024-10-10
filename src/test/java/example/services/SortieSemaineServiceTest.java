package example.services;

import example.entities.SortieSemaine;
import example.repositories.SortieSemaineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SortieSemaineServiceTest {

    @Mock
    private SortieSemaineRepository sortieSemaineRepository;

    @InjectMocks
    private SortieSemaineService sortieSemaineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSortieSemaine_Success() {
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.save(semaine)).thenReturn(semaine);

        SortieSemaine result = sortieSemaineService.createSortieSemaine(semaine);

        assertNotNull(result);
        verify(sortieSemaineRepository, times(1)).save(semaine);
    }

    @Test
    void testUpdateSortieSemaine_Success() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.existsById(id)).thenReturn(true);
        when(sortieSemaineRepository.save(semaine)).thenReturn(semaine);

        SortieSemaine result = sortieSemaineService.updateSortieSemaine(id, semaine);

        assertNotNull(result);
        verify(sortieSemaineRepository, times(1)).save(semaine);
    }

    @Test
    void testUpdateSortieSemaine_Failure() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            sortieSemaineService.updateSortieSemaine(id, semaine);
        });

        assertEquals("week outing is not possible", exception.getMessage());
        verify(sortieSemaineRepository, never()).save(semaine);
    }

    @Test
    void testDeleteSortieSemaine_Success() {
        int id = 1;
        sortieSemaineService.deleteSortieSemaine(id);

        verify(sortieSemaineRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetSortieSemaine_Success() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.of(semaine));

        SortieSemaine result = sortieSemaineService.getSortieSemaine(id);

        assertNotNull(result);
        verify(sortieSemaineRepository, times(1)).findById(id);
    }

    @Test
    void testGetSortieSemaine_NotFound() {
        int id = 1;
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.empty());

        SortieSemaine result = sortieSemaineService.getSortieSemaine(id);

        assertNull(result);
        verify(sortieSemaineRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllSortieSemaine_Success() {
        List<SortieSemaine> semaines = new ArrayList<>();
        semaines.add(new SortieSemaine());
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaine> result = sortieSemaineService.getAllSortieSemaine();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sortieSemaineRepository, times(1)).findAll();
    }
    @Test
    void testDeleteSortieSemaine_NotExistingId() {
        int id = 999;
        doThrow(new RuntimeException("Item not found")).when(sortieSemaineRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> sortieSemaineService.deleteSortieSemaine(id));
        verify(sortieSemaineRepository, times(1)).deleteById(id);
    }
    @Test
    void testGetAllSortieSemaine_LargeDataSet() {
        List<SortieSemaine> semaines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            semaines.add(new SortieSemaine());
        }
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaine> result = sortieSemaineService.getAllSortieSemaine();

        assertEquals(1000, result.size());
        verify(sortieSemaineRepository, times(1)).findAll();
    }
    @Test
    void testNoInteractionsWithRepository() {
        verifyNoInteractions(sortieSemaineRepository);
    }




}

