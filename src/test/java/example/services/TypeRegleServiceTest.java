package example.services;

import example.DTO.TypeRegleDTO;
import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
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

public class TypeRegleServiceTest {

    @Mock
    private TypeRegleRepository typeRegleRepository;

    @Mock
    private RegleRepository regleRepository;

    @InjectMocks
    private TypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTypeRegle_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);

        when(regleRepository.findById(1)).thenReturn(Optional.of(regle));

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type A");
        typeRegle.setRegle(regle);

        when(typeRegleRepository.save(any(TypeRegle.class))).thenReturn(typeRegle);

        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Type A")
                .regle(regle)
                .build();

        // Act
        TypeRegleDTO result = typeRegleService.createTypeRegle(typeRegleDTO);

        // Assert
        assertNotNull(result, "The created TypeRegleDTO should not be null");
        assertEquals("Type A", result.getNomTypeRegle(), "NomTypeRegle should match the input");
        assertEquals(1, result.getNomTypeRegle(), "RegleId should match the input");

        verify(typeRegleRepository, times(1)).save(any(TypeRegle.class));
        verify(regleRepository, times(1)).findById(1);
    }

    @Test
    public void testCreateTypeRegle_Failure_NoRegle() {
        // Arrange
        when(regleRepository.findById(1)).thenReturn(Optional.empty());

        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Type A")
                .regle(new Regle())
                .build();

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            typeRegleService.createTypeRegle(typeRegleDTO);
        });

        assertEquals("La règle associée avec l'ID 1 est introuvable", exception.getMessage());

        verify(regleRepository, times(1)).findById(1);
        verifyNoInteractions(typeRegleRepository);
    }

    @Test
    public void testUpdateTypeRegle_Success() {
        // Arrange
        int id = 1;
        Regle regle = new Regle();
        regle.setId(2);

        TypeRegle existingTypeRegle = new TypeRegle();
        existingTypeRegle.setId(id);
        existingTypeRegle.setNomTypeRegle("Type B");
        existingTypeRegle.setRegle(regle);

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(existingTypeRegle));
        when(regleRepository.findById(2)).thenReturn(Optional.of(regle));
        when(typeRegleRepository.save(any(TypeRegle.class))).thenReturn(existingTypeRegle);

        TypeRegleDTO updatedDTO = TypeRegleDTO.builder()
                .id(id)
                .nomTypeRegle("Updated Type B")
                .regle(regle)
                .build();

        // Act
        TypeRegleDTO result = typeRegleService.updateTypeRegle(id, updatedDTO);

        // Assert
        assertNotNull(result, "The updated TypeRegleDTO should not be null");
        assertEquals("Updated Type B", result.getNomTypeRegle(), "NomTypeRegle should be updated");
        assertEquals(2, result.getRegle(), "RegleId should match the updated value");

        verify(typeRegleRepository, times(1)).findById(id);
        verify(regleRepository, times(1)).findById(2);
        verify(typeRegleRepository, times(1)).save(any(TypeRegle.class));
    }

    @Test
    public void testDeleteTypeRegle_Success() {
        // Arrange
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(true);

        // Act
        typeRegleService.deleteTypeRegle(id);

        // Assert
        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetTypeRegle_Success() {
        // Arrange
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);
        typeRegle.setNomTypeRegle("Type C");

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(typeRegle));

        // Act
        TypeRegleDTO result = typeRegleService.getTypeRegle(id);

        // Assert
        assertNotNull(result, "The fetched TypeRegleDTO should not be null");
        assertEquals("Type C", result.getNomTypeRegle(), "NomTypeRegle should match");
    }

    @Test
    public void testGetTypeRegles_Success() {
        // Arrange
        TypeRegle typeRegle1 = new TypeRegle();
        typeRegle1.setId(1);
        typeRegle1.setNomTypeRegle("Type D");

        TypeRegle typeRegle2 = new TypeRegle();
        typeRegle2.setId(2);
        typeRegle2.setNomTypeRegle("Type E");

        when(typeRegleRepository.findAll()).thenReturn(List.of(typeRegle1, typeRegle2));

        // Act
        List<TypeRegleDTO> results = typeRegleService.getTypeRegles();

        // Assert
        assertNotNull(results, "The list of TypeRegleDTOs should not be null");
        assertEquals(2, results.size(), "The list should contain 2 elements");
    }
}
