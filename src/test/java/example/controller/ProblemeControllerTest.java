package example.controller;

import example.entities.Probleme;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProblemeControllerTest {

    @InjectMocks
    private ProblemeController problemeController;
    @Mock
    private ProblemeRepository problemeRepository;
    @Mock
    private ProblemeService problemeService;



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
        assertEquals(problemeList.size(), result.size());
    }
    @Test
    public void testGetAllProblems_Failure(){
        when(problemeService.getAllProblemes()).thenThrow(new RuntimeException("Database invalid"));
        Assertions.assertThrows(RuntimeException.class, () -> problemeController.getAllProblemes());
    }
    @Test
    public void testGetProblemeById_Success(){
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.getProblemById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetProblemeById_Failure(){
        when(problemeService.getProblemeById(1)).thenReturn(null);
        ResponseEntity<Probleme> result = problemeController.getProblemById(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateProbleme_Success(){
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(probleme)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(probleme, result.getBody());
    }
    @Test
    public void testCreateProbleme_Failure(){
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(any(Probleme.class))).thenThrow(new IllegalArgumentException("Invalid problem"));
        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateProbleme_Success(){
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(updatedProbleme);
        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedProbleme, result.getBody());
    }
    @Test
    void testUpdateProbleme_Failure() {
        Probleme updatedProbleme = new Probleme();

        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(null);

        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testDeleteProbleme_Success() {
        when(problemeRepository.findById(1)).thenReturn(Optional.of(new Probleme()));

        doNothing().when(problemeService).deleteProbleme(1);

        ResponseEntity<Void> result = problemeController.deleteProbleme(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }


    @Test
    public void testDeleteProbleme_NotFound(){
        doThrow(new IllegalArgumentException("Problem not found")).when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProbleme(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testProblemeController(){
        Assertions.assertNotNull(problemeController);
        Assertions.assertNotNull(problemeService);
    }
    @Test
    public void testCreateProbleme_Conflict() {
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(any(Probleme.class))).thenThrow(new IllegalStateException("Conflit"));

        ResponseEntity<Probleme> result = problemeController.createProbleme(probleme);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }


    @Test
    void testUpdateProbleme_InvalidInput() {
        Probleme updatedProbleme = new Probleme();

        when(problemeService.updateProbleme(eq(1), any(Probleme.class)))
                .thenThrow(new IllegalArgumentException("Entrée invalide"));

        ResponseEntity<Probleme> result = problemeController.updateProbleme(1, updatedProbleme);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }



    @Test
    public void testGetAllProblemes_Exception(){
        when(problemeService.getAllProblemes()).thenThrow(new RuntimeException("Unexpected error"));
        Assertions.assertThrows(RuntimeException.class, () -> problemeController.getAllProblemes());
    }


}
