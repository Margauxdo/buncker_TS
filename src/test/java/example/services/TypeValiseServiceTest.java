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
                .build();

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));

        // Act
        TypeValiseDTO result = typeValiseService.getTypeValise(id);

        // Assert
        assertNotNull(result, "The retrieved TypeValiseDTO should not be null");
        assertEquals("Owner", result.getProprietaire());
        assertEquals("Description", result.getDescription());

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
