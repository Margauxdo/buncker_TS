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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSortieSemaine_Success() {
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.save(semaine)).thenReturn(semaine);

        SortieSemaine result = sortieSemaineService.createSortieSemaine(semaine);

        assertNotNull(result, "Le résultat ne doit pas être nul après la création.");
        verify(sortieSemaineRepository, times(1)).save(semaine);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testUpdateSortieSemaine_Success() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.existsById(id)).thenReturn(true);
        when(sortieSemaineRepository.save(semaine)).thenReturn(semaine);

        SortieSemaine result = sortieSemaineService.updateSortieSemaine(id, semaine);

        assertNotNull(result, "La mise à jour doit retourner un objet non nul.");
        verify(sortieSemaineRepository, times(1)).existsById(id);
        verify(sortieSemaineRepository, times(1)).save(semaine);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testUpdateSortieSemaine_Failure() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                        sortieSemaineService.updateSortieSemaine(id, semaine),
                "Une exception est attendue lorsque l'ID n'existe pas."
        );

        assertEquals("week outing is not possible", exception.getMessage(),
                "Le message d'erreur doit être 'week outing is not possible'.");
        verify(sortieSemaineRepository, times(1)).existsById(id);
        verify(sortieSemaineRepository, never()).save(semaine);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testDeleteSortieSemaine_Success() {
        int id = 1;
        sortieSemaineService.deleteSortieSemaine(id);

        verify(sortieSemaineRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetSortieSemaine_Success() {
        int id = 1;
        SortieSemaine semaine = new SortieSemaine();
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.of(semaine));

        SortieSemaine result = sortieSemaineService.getSortieSemaine(id);

        assertNotNull(result, "La récupération doit retourner un objet non nul.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetSortieSemaine_NotFound() {
        int id = 1;
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.empty());

        SortieSemaine result = sortieSemaineService.getSortieSemaine(id);

        assertNull(result, "La récupération d'un ID inexistant doit retourner null.");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_Success() {
        List<SortieSemaine> semaines = new ArrayList<>();
        semaines.add(new SortieSemaine());
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaine> result = sortieSemaineService.getAllSortieSemaine();

        assertNotNull(result, "La liste des semaines ne doit pas être nulle.");
        assertEquals(1, result.size(), "La taille de la liste doit être égale à 1.");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testDeleteSortieSemaine_NotExistingId() {
        int id = 999;
        doThrow(new RuntimeException("Item not found")).when(sortieSemaineRepository).deleteById(id);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sortieSemaineService.deleteSortieSemaine(id),
                "Une exception est attendue pour un ID inexistant.");

        assertEquals("Item not found", exception.getMessage(), "Le message d'erreur doit être 'Item not found'.");
        verify(sortieSemaineRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_LargeDataSet() {
        List<SortieSemaine> semaines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            semaines.add(new SortieSemaine());
        }
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        List<SortieSemaine> result = sortieSemaineService.getAllSortieSemaine();

        assertEquals(1000, result.size(), "La taille de la liste doit être de 1000.");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testNoInteractionsWithSortieSemaineRepository_Success() {
        verifyNoInteractions(sortieSemaineRepository);
    }
    @Test
    public void testNoInteractionsWithSortieSemaineRepository_Failure_Exception() {
        int id =1;
        SortieSemaine sortieSemaine =  new SortieSemaine();
        sortieSemaine.setId(id);
        try{
            sortieSemaineService.updateSortieSemaine(id, sortieSemaine);
        }catch(RuntimeException e){

        }
        verify(sortieSemaineRepository , times(1)).existsById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }
}
