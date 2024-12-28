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

import java.util.*;

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





}
