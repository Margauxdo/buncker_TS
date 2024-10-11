package example.services;

import example.entities.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class RegleManuelleServiceTest {

    @Mock
    private RegleManuelleRepository regleManuelleRepository;

    @InjectMocks
    private RegleManuelleService regleManuelleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRegleManuelle_Success() {
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleRepository.save(regleManuelle)).thenReturn(regleManuelle);

        RegleManuelle result = regleManuelleService.createRegleManuelle(regleManuelle);

        Assertions.assertNotNull(result, "La règle manuelle ne doit pas être null");
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testCreateRegleManuelle_Failure_Exception() {
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleRepository.save(regleManuelle)).thenThrow(new RuntimeException("Erreur de base de données"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            regleManuelleService.createRegleManuelle(regleManuelle);
        });

        Assertions.assertEquals("Erreur de base de données", exception.getMessage());
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testUpdateRegleManuelle_Success() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(id);

        when(regleManuelleRepository.existsById(id)).thenReturn(true);
        when(regleManuelleRepository.save(any(RegleManuelle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegleManuelle result = regleManuelleService.updateRegleManuelle(id, regleManuelle);

        Assertions.assertNotNull(result, "La règle manuelle ne doit pas être null");
        Assertions.assertEquals(id, result.getId(), "L'ID de la règle manuelle doit correspondre");

        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testUpdateRegleManuelle_Failure_Exception() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(2); // ID différent pour déclencher l'exception

        // Mock la réponse de `existsById`
        when(regleManuelleRepository.existsById(id)).thenReturn(true);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            regleManuelleService.updateRegleManuelle(id, regleManuelle);
        });

        Assertions.assertEquals("L'ID de la règle manuelle ne correspond pas", exception.getMessage());
        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, never()).save(any(RegleManuelle.class));
        verifyNoMoreInteractions(regleManuelleRepository);
    }




    @Test
    public void testDeleteRegleManuelle_Success() {
        int id = 1;
        when(regleManuelleRepository.existsById(id)).thenReturn(true);

        regleManuelleService.deleteRegleManuelle(id);

        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testDeleteRegleManuelle_Failure_Exception() {
        int id = 1;
        when(regleManuelleRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            regleManuelleService.deleteRegleManuelle(id);
        });

        Assertions.assertEquals("La règle manuelle n'existe pas", exception.getMessage());
        verify(regleManuelleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelle_Success() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(id);

        when(regleManuelleRepository.findById(id)).thenReturn(Optional.of(regleManuelle));

        RegleManuelle result = regleManuelleService.getRegleManuelle(id);

        Assertions.assertNotNull(result, "La règle manuelle ne doit pas être null");
        Assertions.assertEquals(id, result.getId(), "L'ID doit correspondre");

        verify(regleManuelleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelle_Failure_Exception() {
        int id = 1;
        when(regleManuelleRepository.findById(id)).thenReturn(Optional.empty());

        RegleManuelle result = regleManuelleService.getRegleManuelle(id);

        Assertions.assertNull(result, "La règle manuelle doit être null si non trouvée");
        verify(regleManuelleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelles_Success() {
        List<RegleManuelle> regles = List.of(new RegleManuelle(), new RegleManuelle());

        when(regleManuelleRepository.findAll()).thenReturn(regles);

        List<RegleManuelle> result = regleManuelleService.getRegleManuelles();

        Assertions.assertEquals(2, result.size(), "La liste des règles doit contenir 2 éléments");
        verify(regleManuelleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelles_Failure_Exception() {
        when(regleManuelleRepository.findAll()).thenThrow(new RuntimeException("Erreur de base de données"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            regleManuelleService.getRegleManuelles();
        });

        Assertions.assertEquals("Erreur de base de données", exception.getMessage());
        verify(regleManuelleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testNoInteractionWithRegleManuelleRepository_Success() {
        verifyNoInteractions(regleManuelleRepository);
    }
}
