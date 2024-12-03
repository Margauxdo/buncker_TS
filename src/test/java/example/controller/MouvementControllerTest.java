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
        when(mouvementService.getMouvementById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = mouvementController.viewMouvementById(1, model);

        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }

    // Test : Formulaire de création de mouvement
    @Test
    public void testCreateMouvementForm() {
        // Mock the behavior of dependencies
        when(valiseService.getAllValises()).thenReturn(List.of());
        when(livreurService.getAllLivreurs()).thenReturn(List.of());

        // Execute the controller method
        String response = mouvementController.createMouvementForm(model);

        // Assert the results
        assertEquals("mouvements/mouv_create", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertTrue(model.containsAttribute("valises"));
        assertTrue(model.containsAttribute("allLivreurs"));
        assertNotNull(model.getAttribute("mouvement"));
    }

    // Test : Création de mouvement - Succès
    @Test
    public void testCreateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new java.util.Date());
        when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);

        // Mocking the Model object
        Model model = mock(Model.class);

        // Act
        String response = mouvementController.createMouvementThymeleaf(mouvement, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Corrected the redirect URL
        verify(mouvementService, times(1)).createMouvement(mouvement);
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
        when(mouvementService.getMouvementById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = mouvementController.editMouvementForm(1, model);

        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }

    // Test : Mise à jour de mouvement - Succès
    @Test
    public void testUpdateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        mouvement.setStatutSortie("Finalisé");

        // Mocking the service and model
        when(mouvementService.updateMouvement(1, mouvement)).thenReturn(mouvement);
        Model model = mock(Model.class);

        // Act
        String response = mouvementController.updateMouvement(1, mouvement, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Corrected the redirect URL
        verify(mouvementService, times(1)).updateMouvement(1, mouvement);
    }


    // Test : Suppression de mouvement - Succès
    @Test
    public void testDeleteMouvement_Success() {
        doNothing().when(mouvementService).deleteMouvement(1);

        String response = mouvementController.deleteMouvement(1);

        assertEquals("redirect:/mouvements/mouv_list", response);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }
}
