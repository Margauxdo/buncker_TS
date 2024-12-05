package example.controller;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
import example.services.LivreurService;
import example.services.ValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MouvementControllerTest {

    @InjectMocks
    private MouvementController mouvementController;

    @Mock
    private ValiseService valiseService;

    @Mock
    private LivreurService livreurService;

    @Mock
    private IMouvementService mouvementService;
    private Model model;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        model = new ConcurrentModel();
    }

    // Test : Lister tous les mouvements - Succès
    @Test
    public void testListAllMouvements_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.getAllMouvements()).thenReturn(List.of(mouvement));

        Model model = new ConcurrentModel();
        String response = mouvementController.listAllMouvements(model);

        assertEquals("mouvements/mouv_list", response);
        assertTrue(model.containsAttribute("mouvements"));
        assertEquals(1, ((List<?>) model.getAttribute("mouvements")).size());
        verify(mouvementService, times(1)).getAllMouvements();
    }

    // Test : Voir un mouvement par ID - Succès
    @Test
    public void testViewMouvementById_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.getMouvementById(1)).thenReturn(mouvement);

        Model model = new ConcurrentModel();
        String response = mouvementController.viewMouvementById(1, model);

        assertEquals("mouvements/mouv_details", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertEquals(mouvement, model.getAttribute("mouvement"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }

    // Test : Voir un mouvement par ID - Non trouvé
    @Test
    public void testViewMouvementById_NotFound() {
        // Arrange
        when(mouvementService.getMouvementById(1)).thenReturn(null);

        // Act
        Model model = new ConcurrentModel();
        String response = mouvementController.viewMouvementById(1, model);

        // Assert
        assertEquals("mouvements/error", response); // Verify error page is returned
        assertTrue(model.containsAttribute("errorMessage")); // Verify error message is added to model
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(mouvementService, times(1)).getMouvementById(1); // Verify service interaction
    }


    // Test : Formulaire de création de mouvement
    @Test
    public void testCreateMouvementForm() {
        // Arrange
        when(valiseService.getAllValises()).thenReturn(List.of());
        when(livreurService.getAllLivreurs()).thenReturn(List.of());
        Model model = new ConcurrentModel();

        // Act
        String response = mouvementController.createMouvementForm(model);

        // Assert
        assertEquals("mouvements/mouv_create", response, "The view name should be 'mouvements/mouv_create'");
        assertTrue(model.containsAttribute("mouvement"), "Model should contain attribute 'mouvement'");
        assertTrue(model.containsAttribute("valises"), "Model should contain attribute 'valises'");
        assertTrue(model.containsAttribute("allLivreurs"), "Model should contain attribute 'allLivreurs'");
        assertNotNull(model.getAttribute("mouvement"), "Attribute 'mouvement' should not be null");

        // Verify interactions
        verify(valiseService, times(1)).getAllValises();
        verify(livreurService, times(1)).getAllLivreurs();
        verifyNoMoreInteractions(valiseService, livreurService);
    }


    // Test : Création de mouvement - Succès
    @Test
    public void testCreateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new java.util.Date());

        // Mocking dependencies
        when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = mock(Model.class);

        // Act
        String response = mouvementController.createMouvementThymeleaf(mouvement, bindingResult, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Assert redirection
        verify(mouvementService, times(1)).createMouvement(any(Mouvement.class)); // Verify service call
        verify(bindingResult, times(1)).hasErrors(); // Verify validation check
    }



    // Test : Formulaire de modification de mouvement - Succès
    @Test
    public void testEditMouvementForm_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.getMouvementById(1)).thenReturn(mouvement);

        Model model = new ConcurrentModel();
        String response = mouvementController.editMouvementForm(1, model);

        assertEquals("mouvements/mouv_edit", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertEquals(mouvement, model.getAttribute("mouvement"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }

    // Test : Formulaire de modification de mouvement - Non trouvé
    @Test
    public void testEditMouvementForm_NotFound() {
        // Arrange
        when(mouvementService.getMouvementById(1)).thenReturn(null);
        Model model = new ConcurrentModel();

        // Act
        String response = mouvementController.editMouvementForm(1, model);

        // Assert
        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(mouvementService, times(1)).getMouvementById(1);
        verifyNoMoreInteractions(mouvementService);
    }


    // Test : Mise à jour de mouvement - Succès
    @Test
    public void testUpdateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        mouvement.setStatutSortie("Finalisé");

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);

        // Mocking BindingResult to indicate no validation errors
        when(result.hasErrors()).thenReturn(false);
        when(mouvementService.updateMouvement(1, mouvement)).thenReturn(mouvement);

        // Act
        String response = mouvementController.updateMouvement(1, mouvement, result, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Corrected the redirect URL
        verify(mouvementService, times(1)).updateMouvement(1, mouvement);
        verify(result, times(1)).hasErrors();
    }



    // Test : Suppression de mouvement - Succès
    @Test
    public void testDeleteMouvement_Success() {
        doNothing().when(mouvementService).deleteMouvement(1);

        String response = mouvementController.deleteMouvement(1, new ConcurrentModel());

        assertEquals("redirect:/mouvements/list", response);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

}
