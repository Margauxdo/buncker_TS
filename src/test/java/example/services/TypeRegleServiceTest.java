package example.services;

import example.entity.TypeRegle;
import example.repositories.TypeRegleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TypeRegleServiceTest {

    @Mock
    private TypeRegleRepository typeRegleRepository;

    @InjectMocks
    private TypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTypeRegle_Success() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleRepository.save(typeRegle)).thenReturn(typeRegle);

        TypeRegle result = typeRegleService.createTypeRegle(typeRegle);

        Assertions.assertNotNull(result, "Rule type must not be null");
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testCreateTypeRegle_Failure_Exception() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleRepository.save(typeRegle)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.createTypeRegle(typeRegle);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testUpdateTypeRegle_Success() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);

        when(typeRegleRepository.existsById(id)).thenReturn(true);
        when(typeRegleRepository.save(any(TypeRegle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TypeRegle result = typeRegleService.updateTypeRegle(id, typeRegle);

        Assertions.assertNotNull(result, "Rule type must not be null");
        Assertions.assertEquals(id, result.getId(), "Rule type ID must match");

        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testUpdateTypeRegle_Failure_Exception() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(2);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            typeRegleService.updateTypeRegle(id, typeRegle);
        });

        Assertions.assertEquals("Rule type ID does not match", exception.getMessage());

        verify(typeRegleRepository, never()).existsById(id);
        verify(typeRegleRepository, never()).save(any(TypeRegle.class));
        verifyNoMoreInteractions(typeRegleRepository);
    }


    @Test
    public void testDeleteTypeRegle_Success() {
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(true);

        typeRegleService.deleteTypeRegle(id);

        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testDeleteTypeRegle_Failure_Exception() {
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.deleteTypeRegle(id);
        });

        Assertions.assertEquals("The rule type does not exist", exception.getMessage());
        verify(typeRegleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }


    @Test
    public void testGetTypeRegle_Success() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(typeRegle));

        TypeRegle result = typeRegleService.getTypeRegle(id);

        Assertions.assertNotNull(result, "Rule type must not be null");
        Assertions.assertEquals(id, result.getId(), "Rule type ID must match");

        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testGetTypeRegle_Failure_Exception() {
        int id = 1;
        when(typeRegleRepository.findById(id)).thenReturn(Optional.empty());

        TypeRegle result = typeRegleService.getTypeRegle(id);

        Assertions.assertNull(result, "Rule type must be null if not found");
        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }
    @Test
    public void testGetTypeRegles_Success(){
        List<TypeRegle> typeRegles = List.of(new TypeRegle(), new TypeRegle());

        when(typeRegleRepository.findAll()).thenReturn(typeRegles);

        List<TypeRegle> result = typeRegleService.getTypeRegles();

        Assertions.assertEquals(2, result.size(), "Rule type list must contain 2 elements");
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }
    @Test
    public void testGetTypeRegles_Failure_Exception(){
        when(typeRegleRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.getTypeRegles();
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testNoInteractionWithTypeRegleRepository_Success() {
        verifyNoInteractions(typeRegleRepository);
    }
}

