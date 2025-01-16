package example.services;

import example.DTO.TypeRegleDTO;
import example.entity.TypeRegle;
import example.exceptions.ResourceNotFoundException;
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

    @InjectMocks
    private TypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private TypeRegle createTypeRegle(Integer id, String nom, String description) {
        return TypeRegle.builder()
                .id(id)
                .nomTypeRegle(nom)
                .description(description)
                .build();
    }

    @Test
    public void testCreateTypeRegle_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Règle 1")
                .description("Description 1")
                .build();

        TypeRegle savedTypeRegle = createTypeRegle(1, "Règle 1", "Description 1");

        when(typeRegleRepository.save(any(TypeRegle.class))).thenReturn(savedTypeRegle);

        // Act
        TypeRegleDTO result = typeRegleService.createTypeRegle(typeRegleDTO);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("Règle 1", result.getNomTypeRegle(), "Le nom doit correspondre");
        verify(typeRegleRepository, times(1)).save(any(TypeRegle.class));
    }

    @Test
    public void testGetTypeRegleById_Success() {
        // Arrange
        int id = 1;
        TypeRegle typeRegle = createTypeRegle(id, "Règle 1", "Description 1");

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(typeRegle));

        // Act
        TypeRegleDTO result = typeRegleService.getTypeRegle(id);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("Règle 1", result.getNomTypeRegle(), "Le nom doit correspondre");
        verify(typeRegleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTypeRegleById_NotFound() {
        // Arrange
        int id = 1;
        when(typeRegleRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> typeRegleService.getTypeRegle(id)
        );

        assertEquals("TypeRegle avec l'ID " + id + " est introuvable", exception.getMessage());
        verify(typeRegleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllTypeRegles_Success() {
        // Arrange
        List<TypeRegle> typeRegles = List.of(
                createTypeRegle(1, "Règle 1", "Description 1"),
                createTypeRegle(2, "Règle 2", "Description 2")
        );

        when(typeRegleRepository.findAll()).thenReturn(typeRegles);

        // Act
        List<TypeRegleDTO> result = typeRegleService.getTypeRegles();

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(2, result.size(), "La liste doit contenir 2 éléments");
        verify(typeRegleRepository, times(1)).findAll();
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
    public void testDeleteTypeRegle_NotFound() {
        // Arrange
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> typeRegleService.deleteTypeRegle(id)
        );

        assertEquals("TypeRegle avec l'ID " + id + " est introuvable", exception.getMessage());
        verify(typeRegleRepository, times(1)).existsById(id);
    }
}
