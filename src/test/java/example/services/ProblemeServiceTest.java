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
        int id = 1;
        when(problemeRepository.existsById(id)).thenReturn(true);

        problemeService.deleteProbleme(id);

        verify(problemeRepository, times(1)).existsById(id);
        verify(problemeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(problemeRepository);
    }




    @Test
    public void testGetProblemeById_Success() {
        int id = 1;
        Probleme probleme = new Probleme();
        probleme.setId(id);
        when(problemeRepository.findById(id)).thenReturn(Optional.of(probleme));
        ProblemeDTO result = problemeService.getProblemeById(id);
        Assertions.assertNotNull(result, "Problem not found");
        Assertions.assertEquals(id, result.getId(), "Problem not found");
        verify(problemeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(problemeRepository);
    }


    @Test
    public void testGetAllProblems_Success() {
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(new Probleme());
        problemeList.add(new Probleme());

        when(problemeRepository.findAll()).thenReturn(problemeList);
        List<ProblemeDTO> result = problemeService.getAllProblemes();
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


}
