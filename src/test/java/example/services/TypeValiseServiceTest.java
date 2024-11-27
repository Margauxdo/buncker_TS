package example.services;

import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
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

public class TypeValiseServiceTest {

    @Mock
    private TypeValiseRepository typeValiseRepository;
    @Mock
    private ValiseRepository valiseRepository;

    @InjectMocks
    private TypeValiseService typeValiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTypeValise_Success() {
        // Arrange
        Valise valise = new Valise();
        valise.setId(1);
        TypeValise typeValise = new TypeValise();
        typeValise.setValise(valise);

        when(valiseRepository.findById(valise.getId())).thenReturn(Optional.of(valise));
        when(typeValiseRepository.save(typeValise)).thenReturn(typeValise);

        // Act
        TypeValise result = typeValiseService.createTypeValise(typeValise);

        // Assert
        Assertions.assertNotNull(result, "Suitcase type must not be null");
        verify(valiseRepository, times(1)).findById(valise.getId());
        verify(typeValiseRepository, times(1)).save(typeValise);
        verifyNoMoreInteractions(valiseRepository, typeValiseRepository);
    }

    @Test
    public void testCreateTypeValise_Failure_Exception() {
        TypeValise typeValise = new TypeValise();

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            typeValiseService.createTypeValise(typeValise);
        });

        Assertions.assertEquals("TypeValise or its associated Valise cannot be null", exception.getMessage(),
                "Exception message should match the expected error message.");

        // Verify interactions
        verify(typeValiseRepository, never()).save(any(TypeValise.class));
        verifyNoInteractions(valiseRepository);
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
        // Arrange: Prepare test data and mocks
        int id = 1;
        TypeValise mockTypeValise = TypeValise.builder()
                .id(id)
                .proprietaire("John Doe")
                .description("Business Valise")
                .build();

        // Mock `findById` to return the mockTypeValise
        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(mockTypeValise));

        // Act: Call the method to test
        typeValiseService.deleteTypeValise(id);

        // Assert: Verify interactions with the repository
        verify(typeValiseRepository, times(1)).findById(id);
        verify(typeValiseRepository, times(1)).delete(mockTypeValise);
        verifyNoMoreInteractions(typeValiseRepository);
    }



    @Test
    public void testDeleteTypeValise_Failure_Exception() {
        // Arrange: Mock `findById` to return an empty Optional
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert: Expect RuntimeException
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeValiseService.deleteTypeValise(id);
        });

        // Assert: Verify exception message
        Assertions.assertEquals("The suitcase type does not exist", exception.getMessage());

        // Verify repository interactions
        verify(typeValiseRepository, times(1)).findById(id);
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
    public void testGetTypeValise_Failure_Exception() {
        // Arrange: Mock repository to return an empty Optional
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert: Expect EntityNotFoundException
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> typeValiseService.getTypeValise(id),
                "Expected EntityNotFoundException when TypeValise is not found"
        );

        Assertions.assertEquals(
                "TypeValise with ID 1 not found",
                exception.getMessage(),
                "Exception message did not match"
        );

        // Verify interactions with the repository
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
        // Arrange: Mock `findById` to return an empty Optional
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert: Expect EntityNotFoundException
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            typeValiseService.getTypeValise(id);
        });

        // Assert: Verify exception message
        Assertions.assertEquals("TypeValise with ID " + id + " not found", exception.getMessage());

        // Verify repository interactions
        verify(typeValiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeValiseRepository);
    }




}


