package example.services;

import example.entities.RegleManuelle;
import example.entities.TypeRegle;
import example.entities.TypeValise;
import example.repositories.TypeValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TypeValiseServiceTest {

    @Mock
    private TypeValiseRepository typeValiseRepository;

    @InjectMocks
    private TypeValiseService typeValiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTypeValise_Success(){
        TypeValise typeValise = new TypeValise();
        when(typeValiseRepository.save(typeValise)).thenReturn(typeValise);

        TypeValise result = typeValiseService.createTypeValise(typeValise);

        Assertions.assertNotNull(result, "Suitcase type must not be null");
        verify(typeValiseRepository, times(1)).save(typeValise);
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testCreateTypeValise_Failure_Exception(){
        TypeValise typeValise = new TypeValise();
        when(typeValiseRepository.save(typeValise)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeValiseService.createTypeValise(typeValise);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(typeValiseRepository, times(1)).save(typeValise);
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testUpdateTypeValise_Success(){
        int id = 1;
        TypeValise typeValise = new TypeValise();
        typeValise.setId(id);

        when(typeValiseRepository.existsById(id)).thenReturn(true);
        when(typeValiseRepository.save(any(TypeValise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TypeValise result = typeValiseService.updateTypeValise(id, typeValise);

        Assertions.assertNotNull(result, "Suitcase type must not be null");
        Assertions.assertEquals(id, result.getId(), "Suitcase type ID must match");

        verify(typeValiseRepository, times(1)).existsById(id);
        verify(typeValiseRepository, times(1)).save(typeValise);
        verifyNoMoreInteractions(typeValiseRepository);
    }


    @Test
    public void testDeleteTypeValise_Success() {
        int id = 1;
        when(typeValiseRepository.existsById(id)).thenReturn(true);

        typeValiseService.deleteTypeValise(id);

        verify(typeValiseRepository, times(1)).existsById(id);
        verify(typeValiseRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }

    @Test
    public void testDeleteTypeValise_Failure_Exception() {
        int id = 1;
        when(typeValiseRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeValiseService.deleteTypeValise(id);
        });

        Assertions.assertEquals("The suitcase type does not exist", exception.getMessage());
        verify(typeValiseRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }

    @Test
    public void testGetTypeValise_Success(){
        int id = 1;
        TypeValise typeValise = new TypeValise();
        typeValise.setId(id);

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));

        TypeValise result = typeValiseService.getTypeValise(id);

        Assertions.assertNotNull(result, "Suitcase type must not be null");
        Assertions.assertEquals(id, result.getId(), "ID must match");

        verify(typeValiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testGetTypeValise_Failure_Exception(){
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        TypeValise result = typeValiseService.getTypeValise(id);

        Assertions.assertNull(result, "Suitcase type must be null if not found");
        verify(typeValiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testGetTypeValises_Success(){
        List<TypeValise> typeValises = List.of(new TypeValise(), new TypeValise());

        when(typeValiseRepository.findAll()).thenReturn(typeValises);

        List<TypeValise> result = typeValiseService.getTypeValises();

        Assertions.assertEquals(2, result.size(), "Suitcase type list must contain 2 elements");
        verify(typeValiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testGetTypeValises_Failure_Exception(){
        when(typeValiseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeValiseService.getTypeValises();
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(typeValiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeValiseRepository);
    }
    @Test
    public void testNoInteractionWithTypeValiseRepository_Success() {
        verifyNoInteractions(typeValiseRepository);
    }
    @Test
    public void testNoInteractionWithTypeValiseRepository_Failure_Exception() {
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        TypeValise result = typeValiseService.getTypeValise(id);

        Assertions.assertNull(result, "Suitcase type must be null if not found");
        verify(typeValiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }



}


