package templates.typeRegles.controller;

import example.DTO.ProblemeDTO;
import example.DTO.ValiseDTO;
import example.controller.ProblemeController;
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
        Mockito.when(problemeService.getAllProblemes()).thenReturn(problemes);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.viewAllProblemes(model);

        // Assert
        Assertions.assertEquals("problemes/pb_list", viewName);
        Assertions.assertTrue(model.containsAttribute("problemes"));
        Assertions.assertEquals(problemes, model.getAttribute("problemes"));
        Mockito.verify(problemeService, Mockito.times(1)).getAllProblemes();
    }



    @Test
    public void testViewProbleme_Success() {
        // Arrange
        ProblemeDTO problemeDTO = ProblemeDTO.builder().id(1).descriptionProbleme("Problème Exemple").build();
        Mockito.when(problemeService.getProblemeById(1)).thenReturn(problemeDTO);

        Model model = new ConcurrentModel();

        // Act
        String viewName = problemeController.viewProbleme(1, model);

        // Assert
        Assertions.assertEquals("problemes/pb_details", viewName);
        Assertions.assertTrue(model.containsAttribute("probleme"));
        Assertions.assertEquals(problemeDTO, model.getAttribute("probleme"));
        Mockito.verify(problemeService, Mockito.times(1)).getProblemeById(1);
    }










}
