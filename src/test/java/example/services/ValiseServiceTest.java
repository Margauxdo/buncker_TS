package example.services;

import example.entities.TypeValise;
import example.entities.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ValiseServiceTest {

    @Mock
    private ValiseRepository valiseRepository;

    @InjectMocks
    private ValiseService valiseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateValise_Success(){
        Valise valise = new Valise();
        when(valiseRepository.save(valise)).thenReturn(valise);

        Valise result = valiseService.createValise(valise);

        Assertions.assertNotNull(result, "The suitcase must not be null");
        verify(valiseRepository, times(1)).save(valise);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testCreateValise_Failure_Exception(){
        Valise valise = new Valise();
        when(valiseRepository.save(valise)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.createValise(valise);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(valiseRepository, times(1)).save(valise);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testUpdateValise_Success(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(id);

        when(valiseRepository.existsById(id)).thenReturn(true);
        when(valiseRepository.save(any(Valise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Valise result = valiseService.updateValise( id, valise);

        Assertions.assertNotNull(result, "The suitcase must not be null");
        Assertions.assertEquals(id, result.getId(), "Suitcase ID must match");

        verify(valiseRepository, times(1)).existsById(id);
        verify(valiseRepository, times(1)).save(valise);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testUpdateValise_Failure_Exception(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(2);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            valiseService.updateValise( id, valise);
        });

        Assertions.assertEquals("Suitcase ID does not match", exception.getMessage());

        verify(valiseRepository, never()).existsById(id);
        verify(valiseRepository, never()).save(any(Valise.class));
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testDeleteValise_Success(){
        int id = 1;
        when(valiseRepository.existsById(id)).thenReturn(true);

        valiseService.deleteValise(id);

        verify(valiseRepository, times(1)).existsById(id);
        verify(valiseRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testDeleteValise_Failure_Exception(){
        int id = 1;
        when(valiseRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.deleteValise(id);
        });

        Assertions.assertEquals("The suitcase does not exist", exception.getMessage());
        verify(valiseRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetValiseById_Success(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(id);

        when(valiseRepository.findById(id)).thenReturn(Optional.of(valise));

        Valise result = valiseService.getValiseById(id);

        Assertions.assertNotNull(result, "The suitcase must not be null");
        Assertions.assertEquals(id, result.getId(), "ID must match");

        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetValiseById_Failure_Exception() {
        // Arrange
        int id = 1;
        when(valiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.getValiseById(id);
        }, "Expected ResourceNotFoundException to be thrown");

        // Vérifications
        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }

    @Test
    public void testGetAllValises_Success(){
        List<Valise> valiseList = List.of(new Valise(), new Valise());

        when(valiseRepository.findAll()).thenReturn(valiseList);

        List<Valise> result = valiseService.getAllValises();

        Assertions.assertEquals(2, result.size(), "The suitcase list must contain 2 items");
        verify(valiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetAllValises_Failure_Exception(){
        when(valiseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.getAllValises();
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(valiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testNoInteractionWithValiseRepository_Success() {
        verifyNoInteractions(valiseRepository);
    }
    @Test
    public void testNoInteractionTypeValiseRepository_Failure_Exception() {
        // Arrange
        int id = 1;
        when(valiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.getValiseById(id);
        }, "Expected ResourceNotFoundException to be thrown when the suitcase is not found");

        // Vérifications
        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }





}
