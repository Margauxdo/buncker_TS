package example.services;

import example.DTO.SortieSemaineDTO;
import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.SortieSemaineRepository;
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

class SortieSemaineServiceTest {

    @Mock
    private SortieSemaineRepository sortieSemaineRepository;

    @InjectMocks
    private SortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSortieSemaine_Success() {
        SortieSemaineDTO sortieSemaineDTO = new SortieSemaineDTO();
        sortieSemaineDTO.setDateSortieSemaine(new Date());
        sortieSemaineDTO.setRegleId(1);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setId(1);

        when(sortieSemaineRepository.save(any(SortieSemaine.class))).thenReturn(sortieSemaine);

        SortieSemaineDTO result = sortieSemaineService.createSortieSemaine(sortieSemaineDTO);

        assertNotNull(result, "The result must not be null after creation.");
        verify(sortieSemaineRepository, times(1)).save(any(SortieSemaine.class));
        verifyNoMoreInteractions(sortieSemaineRepository);
    }




    @Test
    public void testDeleteSortieSemaine_Success() {
        int id = 1;

        // Mock existsById to return true, so deleteById can proceed
        when(sortieSemaineRepository.existsById(id)).thenReturn(true);

        // Call the service method to delete
        sortieSemaineService.deleteSortieSemaine(id);

        // Verify interactions
        verify(sortieSemaineRepository, times(1)).deleteById(id);
        verify(sortieSemaineRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }


    @Test
    public void testGetSortieSemaine_Success() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.of(semaine));

        SortieSemaineDTO result = sortieSemaineService.getSortieSemaine(id);

        assertNotNull(result, "he retrieval should return a non-null object.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetSortieSemaine_NotFound() {
        int id = 1;
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.empty());

        SortieSemaineDTO result = sortieSemaineService.getSortieSemaine(id);

        assertNull(result, "Retrieval of a non-existent ID should return null.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_Success() {
        List<SortieSemaine> semaines = new ArrayList<>();
        semaines.add(new SortieSemaine());
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaineDTO> result = sortieSemaineService.getAllSortieSemaine();

        assertNotNull(result, "The list of weeks must not be null");
        assertEquals(1, result.size(), "The list of weeks must not be null. The size of the list must be 1.");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testDeleteSortieSemaine_NotExistingId() {
        int id = 999;

        when(sortieSemaineRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sortieSemaineService.deleteSortieSemaine(id),
                "An exception is expected for a non-existent ID.");

        assertEquals("Week Output Not Found for ID " + id, exception.getMessage(),
                "The error message should be 'WeekOutput not found for ID 999'.");
        verify(sortieSemaineRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }




    @Test
    public void testGetAllSortieSemaine_LargeDataSet() {
        List<SortieSemaine> semaines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            semaines.add(new SortieSemaine());
        }
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaineDTO> result = sortieSemaineService.getAllSortieSemaine();

        assertEquals(1000, result.size(), "The list size should be 1000.");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testNoInteractionsWithSortieSemaineRepository_Success() {
        verifyNoInteractions(sortieSemaineRepository);
    }



    @Test
    public void testUpdateSortieSemaine_Success() {
        int id = 1;
        SortieSemaineDTO sortieSemaineDTO = new SortieSemaineDTO();
        sortieSemaineDTO.setId(id);
        sortieSemaineDTO.setDateSortieSemaine(new Date());
        sortieSemaineDTO.setRegleId(1);

        SortieSemaine existingSortieSemaine = new SortieSemaine();
        existingSortieSemaine.setId(id);

        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.of(existingSortieSemaine));
        when(sortieSemaineRepository.save(any(SortieSemaine.class))).thenReturn(existingSortieSemaine);

        SortieSemaineDTO result = sortieSemaineService.updateSortieSemaine(id, sortieSemaineDTO);

        assertNotNull(result, "The result should not be null after a successful update.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verify(sortieSemaineRepository, times(1)).save(any(SortieSemaine.class));
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testUpdateSortieSemaine_Failure() {
        int id = 1;
        SortieSemaineDTO sortieSemaineDTO = new SortieSemaineDTO();
        sortieSemaineDTO.setId(id);
        sortieSemaineDTO.setDateSortieSemaine(new Date());

        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sortieSemaineService.updateSortieSemaine(id, sortieSemaineDTO),
                "An exception is expected when the ID does not exist."
        );

        assertEquals("Week Output Not Found for ID " + id, exception.getMessage(),
                "The error message should match the expected value.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

}
