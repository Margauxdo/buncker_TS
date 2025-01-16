package example.services;

import example.DTO.ProblemeDTO;
import example.entity.Probleme;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ProblemeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProblemeServiceTest {
    @Mock
    private ProblemeRepository problemeRepository;

    @InjectMocks
    private ProblemeService problemeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteProbleme_Success() {
        // Arrange
        int id = 1;
        when(problemeRepository.existsById(id)).thenReturn(true);
        doNothing().when(problemeRepository).deleteById(id);

        // Act
        problemeService.deleteProbleme(id);

        // Assert
        verify(problemeRepository, times(1)).existsById(id);
        verify(problemeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(problemeRepository);
    }

    @Test
    public void testDeleteProbleme_NotFound() {
        // Arrange
        int id = 1;
        when(problemeRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> problemeService.deleteProbleme(id)
        );

        Assertions.assertEquals("Problème introuvable avec l'ID : 1", exception.getMessage());
        verify(problemeRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(problemeRepository);
    }

    @Test
    public void testGetProblemeById_Success() {
        // Arrange
        int id = 1;
        Probleme probleme = Probleme.builder()
                .id(id)
                .descriptionProbleme("Test description")
                .detailsProbleme("Test details")
                .clients(new ArrayList<>()) // Initialiser la liste des clients
                .build();

        when(problemeRepository.findById(id)).thenReturn(Optional.of(probleme));

        // Act
        ProblemeDTO result = problemeService.getProblemeById(id);

        // Assert
        Assertions.assertNotNull(result, "Le problème ne doit pas être null");
        Assertions.assertEquals(id, result.getId(), "L'ID du problème doit correspondre");
        Assertions.assertEquals("Test description", result.getDescriptionProbleme(), "La description doit correspondre");
        verify(problemeRepository, times(1)).findById(id);
    }


    @Test
    public void testGetProblemeById_NotFound() {
        // Arrange
        int id = 1;
        when(problemeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> problemeService.getProblemeById(id)
        );

        Assertions.assertEquals("Probleme not found", exception.getMessage());
        verify(problemeRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllProblemes_Success() {
        // Arrange
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(Probleme.builder().id(1).descriptionProbleme("Desc 1").detailsProbleme("Details 1").build());
        problemeList.add(Probleme.builder().id(2).descriptionProbleme("Desc 2").detailsProbleme("Details 2").build());

        when(problemeRepository.findAll()).thenReturn(problemeList);

        // Act
        List<ProblemeDTO> result = problemeService.getAllProblemes();

        // Assert
        Assertions.assertEquals(2, result.size(), "La liste des problèmes doit contenir 2 éléments");
        verify(problemeRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllProblemes_EmptyList() {
        // Arrange
        when(problemeRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ProblemeDTO> result = problemeService.getAllProblemes();

        // Assert
        Assertions.assertNotNull(result, "La liste des problèmes ne doit pas être null");
        Assertions.assertTrue(result.isEmpty(), "La liste des problèmes doit être vide");
        verify(problemeRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllProblemes_Failure_Exception() {
        // Arrange
        when(problemeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> problemeService.getAllProblemes()
        );

        Assertions.assertEquals("Database error", exception.getMessage(), "Le message d'exception doit correspondre");
        verify(problemeRepository, times(1)).findAll();
    }

    @Test
    public void testNoInteractionWithProblemeRepository_Success() {
        verifyNoInteractions(problemeRepository);
    }
}
