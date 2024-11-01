package example.controller;

import example.entities.Probleme;
import example.interfaces.IProblemeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProblemeControllerTest {

    @InjectMocks
    private ProblemeController problemeController;

    @Mock
    private IProblemeService problemeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllProblemes_Success(){
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(new Probleme());
        when(problemeService.getAllProblemes()).thenReturn(problemeList);
        List<Probleme> result = problemeController.getAllProblemes();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(problemeList.size(), result.size());
    }
    @Test
    public void testGetAllProblems_Failure(){
        when(problemeService.getAllProblemes()).thenThrow(new RuntimeException("Erreur de la base de donnee"));
        Assertions.assertThrows(RuntimeException.class, () -> problemeController.getAllProblemes());
    }
    @Test
    public void testGetProblemeById_Success(){
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.getProblemById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetProblemeById_Failure(){
        when(problemeService.getProblemeById(1)).thenReturn(null);
        ResponseEntity<Probleme> result = problemeController.getProblemById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateProbleme_Success(){
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(probleme)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(probleme, result.getBody());
    }
    @Test
    public void testCreateProbleme_Failure(){
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(any(Probleme.class))).thenThrow(new IllegalArgumentException("Probleme invalide"));
        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateProbleme_Success(){
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(updatedProbleme);
        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedProbleme, result.getBody());
    }
    @Test
    public void testUpdateProbleme_Failure(){
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(null);
        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testDeleteProbleme_Success(){
        doNothing().when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProbleme(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    @Test
    public void testDeleteProbleme_Failure(){
        doThrow(new RuntimeException("Internal server Error")).when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProbleme(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(problemeService).deleteProbleme(1);
    }
    @Test
    public void testDeleteProbleme_NotFound(){
        doThrow(new IllegalArgumentException("Problem not found")).when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProbleme(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testProblemeController(){
        Assertions.assertNotNull(problemeController);
        Assertions.assertNotNull(problemeService);
    }
    @Test
    public void testCreateProbleme_Conflict() {
        // Arrange: Prepare a problem object and mock the service to throw an exception
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(any(Probleme.class))).thenThrow(new IllegalStateException("Conflit"));

        // Act: Call the controller method
        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);

        // Assert: Verify that the response status is 409 CONFLICT
        Assertions.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }


    @Test
    public void testUpdateProbleme_InvalidInput(){
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(eq(1), any(Probleme.class))).thenThrow(new IllegalArgumentException("Entrée invalide"));
        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetAllProblemes_Exception(){
        when(problemeService.getAllProblemes()).thenThrow(new RuntimeException("Erreur inattendue"));
        Assertions.assertThrows(RuntimeException.class, () -> problemeController.getAllProblemes());
    }


}
