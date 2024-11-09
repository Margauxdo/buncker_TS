package example.services;

import example.entities.RegleManuelle;
import example.entities.SortieSemaine;
import example.repositories.RegleManuelleRepository;
import jakarta.persistence.EntityNotFoundException;
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

        Assertions.assertNotNull(result, "Manual rule must not be null");
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testCreateRegleManuelle_Failure_Exception() {
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleRepository.save(regleManuelle)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            regleManuelleService.createRegleManuelle(regleManuelle);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
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

        Assertions.assertNotNull(result, "Manual rule must not be null");
        Assertions.assertEquals(id, result.getId(), "Manual rule ID must match");

        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testUpdateRegleManuelle_IdIsSetProperly() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(2);

        when(regleManuelleRepository.existsById(id)).thenReturn(true);

        regleManuelleService.updateRegleManuelle(id, regleManuelle);

        Assertions.assertEquals(id, regleManuelle.getId(), "The ID should be updated to match the provided ID.");
        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).save(regleManuelle);
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
    public void testGetRegleManuelle_Success() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(id);

        when(regleManuelleRepository.findById(id)).thenReturn(Optional.of(regleManuelle));

        RegleManuelle result = regleManuelleService.getRegleManuelle(id);

        Assertions.assertNotNull(result, "Manual rule must not be null");
        Assertions.assertEquals(id, result.getId(), "ID must match");

        verify(regleManuelleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelle_Failure_Exception() {
        int id = 1;
        when(regleManuelleRepository.findById(id)).thenReturn(Optional.empty());

        RegleManuelle result = regleManuelleService.getRegleManuelle(id);

        Assertions.assertNull(result, "Manual rule must be null if not found");
        verify(regleManuelleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelles_Success() {
        List<RegleManuelle> regles = List.of(new RegleManuelle(), new RegleManuelle());

        when(regleManuelleRepository.findAll()).thenReturn(regles);

        List<RegleManuelle> result = regleManuelleService.getRegleManuelles();

        Assertions.assertEquals(2, result.size(), "Rule list must contain 2 elements");
        verify(regleManuelleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testGetRegleManuelles_Failure_Exception() {
        when(regleManuelleRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            regleManuelleService.getRegleManuelles();
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(regleManuelleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleManuelleRepository);
    }

    @Test
    public void testNoInteractionWithRegleManuelleRepository_Success() {
        verifyNoInteractions(regleManuelleRepository);
    }
    @Test
    public void testNoInteractionWithRegleManuelleRepository_Failure_Exception() {
        int id =1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(id);
        try{
            regleManuelleService.updateRegleManuelle(id, regleManuelle);
        }catch(RuntimeException e){

        }
        verify(regleManuelleRepository , times(1)).existsById(id);
        verifyNoMoreInteractions(regleManuelleRepository);
    }

}
