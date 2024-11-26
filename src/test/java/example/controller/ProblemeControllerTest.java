package example.controller;

import example.entity.Probleme;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProblemeControllerTest {

    @InjectMocks
    private ProblemeController problemeController;

    @Mock
    private ProblemeService problemeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }





    // Tests pour Thymeleaf

    @Test
    public void testViewAllProblemes_Success() {
        List<Probleme> problemes = List.of(new Probleme(), new Probleme());
        when(problemeService.getAllProblemes()).thenReturn(problemes);

        Model model = new ConcurrentModel();
        String viewName = problemeController.viewAllProblemes(model);

        assertEquals("problemes/pb_list", viewName);
        assertTrue(model.containsAttribute("problemes"));
        assertEquals(problemes, model.getAttribute("problemes"));
        verify(problemeService, times(1)).getAllProblemes();
    }

    @Test
    public void testViewProbleme_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);

        Model model = new ConcurrentModel();
        String viewName = problemeController.viewProbleme(1, model);

        assertEquals("problemes/pb_edit", viewName);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(probleme, model.getAttribute("probleme"));
        verify(problemeService, times(1)).getProblemeById(1);
    }

    @Test
    public void testViewProbleme_NotFound() {
        when(problemeService.getProblemeById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String viewName = problemeController.viewProbleme(1, model);

        assertEquals("problemes/error", viewName);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Probleme ave l'ID1non trouve", model.getAttribute("errorMessage"));
        verify(problemeService, times(1)).getProblemeById(1);
    }

    @Test
    public void testCreateProblemeForm() {
        Model model = new ConcurrentModel();
        String viewName = problemeController.createProblemeForm(model);

        assertEquals("problemes/pb_create", viewName);
        assertTrue(model.containsAttribute("probleme"));
    }

    @Test
    public void testUpdatedProblemeForm_Success() {
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);

        Model model = new ConcurrentModel();
        String viewName = problemeController.updatedProblemeForm(1, model);

        assertEquals("problemes/pb_edit", viewName);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(probleme, model.getAttribute("probleme"));
        verify(problemeService, times(1)).getProblemeById(1);
    }

    @Test
    public void testDeleteProblemeForm_Success() {
        doNothing().when(problemeService).deleteProbleme(1);

        String viewName = problemeController.deleteProbleme(1);

        assertEquals("redirect:/problemes/pb_list", viewName);
        verify(problemeService, times(1)).deleteProbleme(1);
    }
}
