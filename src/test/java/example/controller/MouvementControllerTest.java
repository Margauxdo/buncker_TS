package example.controller;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
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
    private IMouvementService mouvementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAllMouvements() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.getAllMouvements()).thenReturn(List.of(mouvement));

        Model model = new ConcurrentModel();
        String response = mouvementController.listAllMouvements(model);

        assertEquals("mouvements/mouv_list", response);
        assertTrue(model.containsAttribute("mouvements"));
        assertEquals(1, ((List) model.getAttribute("mouvements")).size());  // Vérifie que la liste contient un élément
    }

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
    }


    @Test
    public void testViewMouvementById_NotFound() {
        when(mouvementService.getMouvementById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = mouvementController.viewMouvementById(1, model);

        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
    }


    @Test
    public void testCreateMouvementForm() {
        Model model = new ConcurrentModel();
        String response = mouvementController.createMouvementForm(model);

        assertEquals("mouvements/mouv_create", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertNotNull(model.getAttribute("mouvement"));
    }

    @Test
    public void testCreateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new java.util.Date());
        when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);

        Model model = new ConcurrentModel();
        String response = mouvementController.createMouvementThymeleaf(mouvement);

        assertEquals("redirect:/mouvements/mouv_list", response);
    }

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
    }

    @Test
    public void testEditMouvementForm_NotFound() {
        when(mouvementService.getMouvementById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = mouvementController.editMouvementForm(1, model);

        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
    }


    @Test
    public void testUpdateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        mouvement.setStatutSortie("Finalisé");

        when(mouvementService.updateMouvement(1, mouvement)).thenReturn(mouvement);

        String response = mouvementController.updateMouvement(1, mouvement);

        assertEquals("redirect:/mouvements/mouv_list", response);
    }

    @Test
    public void testDeleteMouvement_Success() {
        doNothing().when(mouvementService).deleteMouvement(1);

        String response = mouvementController.deleteMouvement(1);

        assertEquals("redirect:/mouvements/mouv_list", response);
    }
}
