package example.controller;

import example.DTO.ProblemeDTO;
import example.DTO.ValiseDTO;
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
        // Arrange
        List<ProblemeDTO> problemes = List.of(
                ProblemeDTO.builder().id(1).descriptionProbleme("Problème 1").build(),
                ProblemeDTO.builder().id(2).descriptionProbleme("Problème 2").build()
        );
        when(problemeService.getAllProblemes()).thenReturn(problemes);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.viewAllProblemes(model);

        // Assert
        assertEquals("problemes/pb_list", viewName);
        assertTrue(model.containsAttribute("problemes"));
        assertEquals(problemes, model.getAttribute("problemes"));
        verify(problemeService, times(1)).getAllProblemes();
    }



    @Test
    public void testViewProbleme_Success() {
        // Arrange
        ProblemeDTO problemeDTO = ProblemeDTO.builder().id(1).descriptionProbleme("Problème Exemple").build();
        when(problemeService.getProblemeById(1)).thenReturn(problemeDTO);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.viewProbleme(1, model);

        // Assert
        assertEquals("problemes/pb_details", viewName);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(problemeDTO, model.getAttribute("probleme"));
        verify(problemeService, times(1)).getProblemeById(1);
    }




    @Test
    public void testCreateProblemeForm() {
        // Arrange
        List<ValiseDTO> mockValises = List.of(
                ValiseDTO.builder().id(1).description("Valise 1").build(),
                ValiseDTO.builder().id(2).description("Valise 2").build()
        );
        when(valiseService.getAllValises()).thenReturn(mockValises);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.createProblemeForm(model);

        // Assert
        assertEquals("problemes/pb_create", viewName);
        assertTrue(model.containsAttribute("probleme"), "The model should contain a 'probleme' attribute");
        assertTrue(model.containsAttribute("valises"), "The model should contain a 'valises' attribute");
        assertEquals(mockValises, model.getAttribute("valises"), "The valises attribute should match the mock valises");
        verify(valiseService, times(1)).getAllValises();
    }




    @Test
    public void testUpdatedProblemeForm_Success() {
        // Arrange
        ProblemeDTO problemeDTO = ProblemeDTO.builder().id(1).descriptionProbleme("Problème Exemple").build();
        when(problemeService.getProblemeById(1)).thenReturn(problemeDTO);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.updatedProblemeForm(1, model);

        // Assert
        assertEquals("problemes/pb_edit", viewName);
        assertTrue(model.containsAttribute("probleme"));
        assertEquals(problemeDTO, model.getAttribute("probleme"));
        verify(problemeService, times(1)).getProblemeById(1);
    }




}
