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

import java.util.ArrayList;
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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private TypeValise createTypeValise(Integer id, String proprietaire, String description) {
        return TypeValise.builder()
                .id(id)
                .proprietaire(proprietaire)
                .description(description)
                .build();
    }

    @Test
    public void testCreateTypeValise_Success() {
        // Arrange
        TypeValiseDTO typeValiseDTO = TypeValiseDTO.builder()
                .proprietaire("Owner1")
                .description("Description1")
                .build();

        TypeValise savedTypeValise = createTypeValise(1, "Owner1", "Description1");

        when(typeValiseRepository.save(any(TypeValise.class))).thenReturn(savedTypeValise);

        // Act
        TypeValiseDTO result = typeValiseService.createTypeValise(typeValiseDTO);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("Owner1", result.getProprietaire(), "Le propriétaire doit correspondre");
        verify(typeValiseRepository, times(1)).save(any(TypeValise.class));
    }

    @Test
    public void testGetTypeValise_Success() {
        // Arrange
        int id = 1;
        TypeValise typeValise = createTypeValise(id, "Owner1", "Description1");

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));

        // Act
        TypeValiseDTO result = typeValiseService.getTypeValise(id);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("Owner1", result.getProprietaire(), "Le propriétaire doit correspondre");
        verify(typeValiseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTypeValise_NotFound() {
        // Arrange
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> typeValiseService.getTypeValise(id)
        );

        assertEquals("TypeValise with ID " + id + " not found", exception.getMessage());
        verify(typeValiseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTypeValises_Success() {
        // Arrange
        List<TypeValise> typeValises = List.of(
                createTypeValise(1, "Owner1", "Description1"),
                createTypeValise(2, "Owner2", "Description2")
        );

        when(typeValiseRepository.findAll()).thenReturn(typeValises);

        // Act
        List<TypeValiseDTO> result = typeValiseService.getTypeValises();

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(2, result.size(), "La taille de la liste doit être 2");
        verify(typeValiseRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteTypeValise_Success() {
        // Arrange
        int id = 1;
        TypeValise typeValise = createTypeValise(id, "Owner1", "Description1");
        List<Valise> associatedValises = new ArrayList<>();
        Valise valise = new Valise();
        valise.setId(1);
        associatedValises.add(valise);

        when(typeValiseRepository.findById(id)).thenReturn(Optional.of(typeValise));
        when(valiseRepository.findByTypeValise(typeValise)).thenReturn(associatedValises);

        // Act
        typeValiseService.deleteTypeValise(id);

        // Assert
        verify(typeValiseRepository, times(1)).findById(id);
        verify(valiseRepository, times(1)).findByTypeValise(typeValise);
        verify(valiseRepository, times(1)).save(valise); // Vérifie que chaque valise est dissociée individuellement
        verify(typeValiseRepository, times(1)).delete(typeValise);
    }


    @Test
    public void testDeleteTypeValise_NotFound() {
        // Arrange
        int id = 1;
        when(typeValiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> typeValiseService.deleteTypeValise(id)
        );

        assertEquals("TypeValise avec l'ID " + id + " introuvable", exception.getMessage());
        verify(typeValiseRepository, times(1)).findById(id);
    }
}
