package example.services;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;
import example.entity.Regle;
import example.repositories.SortieSemaineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SortieSemaineServiceTest {

    @Mock
    private SortieSemaineRepository sortieSemaineRepository;

    @InjectMocks
    private SortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SortieSemaine createSortieSemaine(int id) {
        return SortieSemaine.builder()
                .id(id)
                .dateSortieSemaine(new Date())
                .regles(new ArrayList<>()) // Initialiser la liste des règles
                .build();
    }

    @Test
    public void testDeleteSortieSemaine_Success() {
        // Arrange
        int id = 1;
        when(sortieSemaineRepository.existsById(id)).thenReturn(true);

        // Act
        sortieSemaineService.deleteSortieSemaine(id);

        // Assert
        verify(sortieSemaineRepository, times(1)).existsById(id);
        verify(sortieSemaineRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetSortieSemaine_Success() {
        // Arrange
        int id = 1;
        SortieSemaine sortieSemaine = createSortieSemaine(id);

        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.of(sortieSemaine));

        // Act
        SortieSemaineDTO result = sortieSemaineService.getSortieSemaine(id);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(id, result.getId(), "L'ID doit correspondre");
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetSortieSemaine_NotFound() {
        // Arrange
        int id = 1;
        when(sortieSemaineRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> sortieSemaineService.getSortieSemaine(id)
        );

        assertEquals("SortieSemaine with ID " + id + " not found", exception.getMessage());
        verify(sortieSemaineRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_Success() {
        // Arrange
        List<SortieSemaine> semaines = List.of(
                createSortieSemaine(1),
                createSortieSemaine(2)
        );
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        // Act
        List<SortieSemaineDTO> result = sortieSemaineService.getAllSortieSemaine();

        // Assert
        assertNotNull(result, "La liste des sorties ne doit pas être null");
        assertEquals(2, result.size(), "La liste doit contenir 2 éléments");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_EmptyList() {
        // Arrange
        when(sortieSemaineRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<SortieSemaineDTO> result = sortieSemaineService.getAllSortieSemaine();

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertTrue(result.isEmpty(), "La liste doit être vide");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testGetAllSortieSemaine_LargeDataSet() {
        // Arrange
        List<SortieSemaine> semaines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            semaines.add(createSortieSemaine(i + 1));
        }
        when(sortieSemaineRepository.findAll()).thenReturn(semaines);

        // Act
        List<SortieSemaineDTO> result = sortieSemaineService.getAllSortieSemaine();

        // Assert
        assertEquals(1000, result.size(), "La taille de la liste doit être 1000");
        verify(sortieSemaineRepository, times(1)).findAll();
        verifyNoMoreInteractions(sortieSemaineRepository);
    }

    @Test
    public void testNoInteractionsWithSortieSemaineRepository_Success() {
        verifyNoInteractions(sortieSemaineRepository);
    }
}
