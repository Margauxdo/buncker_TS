package example.services;

import example.DTO.TypeValiseDTO;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Business Valise")
                .valiseId(valise.getId())
                .build();

        when(valiseRepository.findById(typeValiseDTO.getValiseId())).thenReturn(Optional.of(valise));
        when(typeValiseRepository.save(any(TypeValise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TypeValiseDTO result = typeValiseService.createTypeValise(typeValiseDTO);

        // Assert
        assertNotNull(result, "The created TypeValiseDTO should not be null");
        assertEquals("John Doe", result.getProprietaire());
        assertEquals("Business Valise", result.getDescription());
        assertEquals(1, result.getValiseId());

        verify(valiseRepository, times(1)).findById(typeValiseDTO.getValiseId());
        verify(typeValiseRepository, times(1)).save(any(TypeValise.class));
        verifyNoMoreInteractions(valiseRepository, typeValiseRepository);
    }

    @Test
    public void testCreateTypeValise_Failure_NoValise() {
        // Arrange
        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("John Doe")
                .description("Business Valise")
                .valiseId(1)
                .build();

        when(valiseRepository.findById(typeValiseDTO.getValiseId())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            typeValiseService.createTypeValise(typeValiseDTO);
        });

        assertEquals("Valise not found with ID: 1", exception.getMessage());

        verify(valiseRepository, times(1)).findById(typeValiseDTO.getValiseId());
        verifyNoInteractions(typeValiseRepository);
    }

    @Test
    public void testUpdateTypeValise_Success() {
        // Arrange
        int id = 1;
        Valise valise = new Valise();
        valise.setId(1);

        TypeValise existingTypeValise = TypeValise.builder()
                .id(id)
                .proprietaire("Old Owner")
                .description("Old Description")
                .valise(valise)
                .build();

        TypeValiseDTO updatedTypeValiseDTO = TypeValiseDTO.builder()
                .id(id)
                .proprietaire("New Owner")
                .description("New Description")
                .valiseId(1)
                .build();

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(existingTypeValise));
        when(valiseRepository.findById(updatedTypeValiseDTO.getValiseId())).thenReturn(Optional.of(valise));
        when(typeValiseRepository.save(any(TypeValise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TypeValiseDTO result = typeValiseService.updateTypeValise(id, updatedTypeValiseDTO);

        // Assert
        assertNotNull(result, "The updated TypeValiseDTO should not be null");
        assertEquals("New Owner", result.getProprietaire());
        assertEquals("New Description", result.getDescription());
        assertEquals(1, result.getValiseId());

        verify(typeValiseRepository, times(1)).findById(id);
        verify(valiseRepository, times(1)).findById(updatedTypeValiseDTO.getValiseId());
        verify(typeValiseRepository, times(1)).save(any(TypeValise.class));
    }

    @Test
    public void testDeleteTypeValise_Success() {
        // Arrange
        int id = 1;
        TypeValise typeValise = new TypeValise();
        typeValise.setId(id);

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));

        // Act
        typeValiseService.deleteTypeValise(id);

        // Assert
        verify(typeValiseRepository, times(1)).findById(id);
        verify(typeValiseRepository, times(1)).delete(typeValise);
    }

    @Test
    public void testGetTypeValise_Success() {
        // Arrange
        int id = 1;
        Valise valise = new Valise();
        valise.setId(1);

        TypeValise typeValise = TypeValise.builder()
                .id(id)
                .proprietaire("Owner")
                .description("Description")
                .valise(valise)
                .build();

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));

        // Act
        TypeValiseDTO result = typeValiseService.getTypeValise(id);

        // Assert
        assertNotNull(result, "The retrieved TypeValiseDTO should not be null");
        assertEquals("Owner", result.getProprietaire());
        assertEquals("Description", result.getDescription());
        assertEquals(1, result.getValiseId());

        verify(typeValiseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTypeValises_Success() {
        // Arrange
        List<TypeValise> typeValises = List.of(
                TypeValise.builder().id(1).proprietaire("Owner1").description("Description1").build(),
                TypeValise.builder().id(2).proprietaire("Owner2").description("Description2").build()
        );

        when(typeValiseRepository.findAll()).thenReturn(typeValises);

        // Act
        List<TypeValiseDTO> result = typeValiseService.getTypeValises();

        // Assert
        assertNotNull(result, "The retrieved list should not be null");
        assertEquals(2, result.size(), "The retrieved list size should be 2");
        assertEquals("Owner1", result.get(0).getProprietaire());
        assertEquals("Owner2", result.get(1).getProprietaire());

        verify(typeValiseRepository, times(1)).findAll();
    }
}
