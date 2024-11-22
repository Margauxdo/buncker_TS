package example.controller;

import example.entity.Probleme;
import example.repositories.ProblemeRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    // Tests API REST
    @Test
    public void testGetAllProblemes_Success() {
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(new Probleme());
        when(problemeService.getAllProblemes()).thenReturn(problemeList);
        List<Probleme> result = problemeController.getAllProblemesApi();
        Assertions.assertNotNull(result);
        assertEquals(problemeList.size(), result.size());
    }

    @Test
    public void testGetAllProblems_Failure() {
        when(problemeService.getAllProblemes()).thenThrow(new RuntimeException("Database invalid"));
        Assertions.assertThrows(RuntimeException.class, () -> problemeController.getAllProblemesApi());
    }

    @Test
    public void testGetProblemeById_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.getProblemByIdApi(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetProblemeById_Failure() {
        when(problemeService.getProblemeById(1)).thenReturn(null);
        ResponseEntity<Probleme> result = problemeController.getProblemByIdApi(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testCreateProbleme_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(probleme)).thenReturn(probleme);
        ResponseEntity<Probleme> result = problemeController.createProblemeApi(probleme);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(probleme, result.getBody());
    }

    @Test
    public void testCreateProbleme_Failure() {
        Probleme probleme = new Probleme();
        when(problemeService.createProbleme(any(Probleme.class))).thenThrow(new IllegalArgumentException("Invalid problem"));
        ResponseEntity<Probleme> result = problemeController.createProblemeApi(probleme);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testUpdateProbleme_Success() {
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(updatedProbleme);
        ResponseEntity<Probleme> result = problemeController.updateProblemeApi(1, updatedProbleme);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedProbleme, result.getBody());
    }

    @Test
    void testUpdateProbleme_Failure() {
        Probleme updatedProbleme = new Probleme();
        when(problemeService.updateProbleme(1, updatedProbleme)).thenReturn(null);
        ResponseEntity<Probleme> result = problemeController.updateProblemeApi(1, updatedProbleme);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteProbleme_Success() {
        when(problemeRepository.findById(1)).thenReturn(Optional.of(new Probleme()));
        doNothing().when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProblemeApi(1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testDeleteProbleme_NotFound() {
        doThrow(new IllegalArgumentException("Problem not found")).when(problemeService).deleteProbleme(1);
        ResponseEntity<Void> result = problemeController.deleteProblemeApi(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    // Tests Thymeleaf
    @Test
    public void testViewAllProblemes() {
        List<Probleme> problemeList = new ArrayList<>();
        problemeList.add(new Probleme());
        when(problemeService.getAllProblemes()).thenReturn(problemeList);

        Model model = new ConcurrentModel();
        String response = problemeController.viewAllProblemes(model);

        assertEquals("problemes/pb_list", response);
        assertTrue(model.containsAttribute("problemes"));
        assertEquals(problemeList, model.getAttribute("problemes"));
    }

    @Test
    public void testViewProbleme_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);

        Model model = new ConcurrentModel();
        String response = problemeController.viewProbleme(1, model);

        assertEquals("problemes/pb_edit", response);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(probleme, model.getAttribute("probleme"));
    }

    @Test
    public void testViewProbleme_NotFound() {
        when(problemeService.getProblemeById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = problemeController.viewProbleme(1, model);

        assertEquals("problemes/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Probleme ave l'ID1non trouve", model.getAttribute("errorMessage"));
    }

    @Test
    public void testCreateProblemeForm() {
        Model model = new ConcurrentModel();
        String response = problemeController.createProblemeForm(model);

        assertEquals("problemes/pb_create", response);
        assertTrue(model.containsAttribute("probleme"));
    }

    @Test
    public void testUpdatedProblemeForm_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);

        Model model = new ConcurrentModel();
        String response = problemeController.updatedProblemeForm(1, model);

        assertEquals("problemes/pb_edit", response);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(probleme, model.getAttribute("probleme"));
    }

    @Test
    public void testUpdatedProblemeForm_NotFound() {
        when(problemeService.getProblemeById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = problemeController.updatedProblemeForm(1, model);

        assertEquals("problemes/error", response);
    }

    @Test
    public void testDeleteProblemeForm_Success() {
        doNothing().when(problemeService).deleteProbleme(1);
        String response = problemeController.deleteProbleme(1);
        assertEquals("redirect:/problemes/pb_list", response);
    }
}
