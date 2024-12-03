package example.controller;

import example.entity.Probleme;
import example.entity.Valise;
import example.interfaces.IProblemeService;
import example.services.ProblemeService;
import example.services.ValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProblemeControllerTest {

    @InjectMocks
    private ProblemeController problemeController;


    @Mock
    private ValiseService valiseService;
    @Mock
    private IProblemeService problemeService;

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
        // Arrange
        Probleme probleme = new Probleme();
        when(problemeService.getProblemeById(1)).thenReturn(probleme);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.viewProbleme(1, model);

        // Assert
        assertEquals("problemes/pb_details", viewName); // Updated to match the actual view name
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(probleme, model.getAttribute("probleme"));
        verify(problemeService, times(1)).getProblemeById(1);
    }



    @Test
    public void testCreateProblemeForm() {
        // Arrange
        List<Valise> mockValises = List.of(new Valise(), new Valise()); // Mocked list of Valises
        when(valiseService.getAllValises()).thenReturn(mockValises); // Simulate service behavior

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.createProblemeForm(model);

        // Assert
        assertEquals("problemes/pb_create", viewName, "The expected view name is incorrect.");
        assertTrue(model.containsAttribute("probleme"), "The model should contain the 'probleme' attribute.");
        assertTrue(model.containsAttribute("valises"), "The model should contain the 'valises' attribute.");
        assertEquals(mockValises, model.getAttribute("valises"), "The list of valises does not match.");
        verify(valiseService, times(1)).getAllValises(); // Verify service interaction
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




}
