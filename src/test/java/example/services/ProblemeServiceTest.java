package example.services;

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
    public void testCreateProbleme_Success() {
        Probleme probleme = new Probleme();
        when(problemeRepository.save(probleme)).thenReturn(probleme);
        Probleme problemeCreated = problemeService.createProbleme(probleme);

        Assertions.assertNotNull(problemeCreated, "The problem must not be null");
        verify(problemeRepository).save(probleme);
    }

    @Test
    public void testCreateProbleme_Failure_Exception() {
        Probleme probleme = new Probleme();
        when(problemeRepository.save(probleme)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> problemeService.createProbleme(probleme));
    }

    @Test
    public void testUpdateProbleme_Success() {
        int id = 1;
        Probleme probleme = new Probleme();
        probleme.setId(id);
        when(problemeRepository.existsById(id)).thenReturn(true);
        when(problemeRepository.save(any(Probleme.class))).thenAnswer(invocation -> {
            Probleme savedProbleme = invocation.getArgument(0);
            savedProbleme.setId(id);
            return savedProbleme;
        });
        Probleme result = problemeService.updateProbleme(id, probleme);
        Assertions.assertNotNull(result, "Problem not updated");
        Assertions.assertEquals(id, result.getId(), "Problem not updated");
        verify(problemeRepository, times(1)).existsById(id);
        verify(problemeRepository, times(1)).save(probleme);
        verifyNoMoreInteractions(problemeRepository);
    }
    @Test
    public void testUpdateProbleme_Failure_Exception() {
        int id = 1;
        Probleme probleme = new Probleme();
        probleme.setId(2);

        when(problemeRepository.existsById(id)).thenReturn(true);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            problemeService.updateProbleme(id, probleme);
        });

        Assertions.assertEquals("Problem ID does not match expected ID", exception.getMessage(),
                "The exception must match the expected message");
        verify(problemeRepository, times(1)).existsById(id);
        verify(problemeRepository, never()).save(any(Probleme.class));
        verifyNoMoreInteractions(problemeRepository);
    }


    @Test
    public void testDeleteProbleme_Success() {
        int id = 1;
        when(problemeRepository.existsById(id)).thenReturn(true);

        problemeService.deleteProbleme(id);

        verify(problemeRepository, times(1)).existsById(id);
        verify(problemeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(problemeRepository);
    }

    @Test
    public void testDeleteProbleme_Failure_Exception() {
        int id = 1;
        when(problemeRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            problemeService.deleteProbleme(id);
        });

        // Modifier l'assertion pour correspondre au message réel
        Assertions.assertEquals("Problem not found with ID: 1", exception.getMessage(),
                "The exception message should match 'Problem not found with ID: 1'");
        verify(problemeRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(problemeRepository);
    }


    @Test
    public void testGetProblemeById_Success() {
        int id = 1;
        Probleme probleme = new Probleme();
        probleme.setId(id);
        when(problemeRepository.findById(id)).thenReturn(Optional.of(probleme));
        Probleme result = problemeService.getProblemeById(id);
        Assertions.assertNotNull(result, "Problem not found");
        Assertions.assertEquals(id, result.getId(), "Problem not found");
        verify(problemeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(problemeRepository);
    }
    @Test
    public void testGetProblemeById_Failure_Exception() {
        int id = 1;
        when(problemeRepository.findById(id)).thenReturn(Optional.empty());

        Probleme result = problemeService.getProblemeById(id);

        Assertions.assertNull(result, "Problem should be null if not found");
        verify(problemeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(problemeRepository);
    }

    @Test
    public void testGetAllProblems_Success() {
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(new Probleme());
        problemeList.add(new Probleme());

        when(problemeRepository.findAll()).thenReturn(problemeList);
        List<Probleme> result = problemeService.getAllProblemes();
        Assertions.assertEquals(2, result.size(), "List of problems");
        verify(problemeRepository, times(1)).findAll();
        verifyNoMoreInteractions(problemeRepository);
    }
    @Test
    public void testGetAllProblems_Failure_Exception() {

        when(problemeRepository.findAll()).thenThrow(new RuntimeException("database error"));
        Exception exception = Assertions.assertThrows(RuntimeException.class,()-> {
            problemeService.getAllProblemes();
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(problemeRepository, times(1)).findAll();
        verifyNoMoreInteractions(problemeRepository);
    }
    @Test
    public void testNoInteractionWithProblemeRepository_Success() {
        verifyNoInteractions(problemeRepository);
    }
    @Test
    public void testNoInteractionWithProblemeRepository_Failure_Exception() {
        int id = 1;
        Probleme probleme = new Probleme();
        probleme.setId(id);

        try {
            problemeService.updateProbleme(id, probleme);
        } catch (Exception e) {
        }

        verify(problemeRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(problemeRepository);
    }

}
