// Unit Test for LivreurController
package example.controller;

import example.entity.Livreur;
import example.entity.Mouvement;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LivreurControllerTest {

    @InjectMocks
    private LivreurController livreurController;

    @Mock
    private ILivreurService livreurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListLivreurs_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getAllLivreurs()).thenReturn(List.of(livreur));

        Model model = new ConcurrentModel();
        String response = livreurController.listLivreurs(model);

        assertEquals("livreurs/livreur_list", response);
        assertTrue(model.containsAttribute("livreurs"));
        assertEquals(1, ((List<?>) model.getAttribute("livreurs")).size());
        verify(livreurService, times(1)).getAllLivreurs();
    }

    @Test
    public void testViewLivreurById_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getLivreurById(1)).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        assertEquals("livreurs/livreur_details", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreur, model.getAttribute("livreur"));
        verify(livreurService, times(1)).getLivreurById(1);
    }

    @Test
    public void testViewLivreurById_NotFound() {
        when(livreurService.getLivreurById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        assertEquals("livreurs/error", response);
        assertEquals("Livreur avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(livreurService, times(1)).getLivreurById(1);
    }

    @Test
    public void testCreateLivreurForm() {
        Model model = new ConcurrentModel();
        String response = livreurController.createLivreurForm(model);

        assertEquals("livreurs/livreur_create", response);
        assertTrue(model.containsAttribute("livreur"));
        assertNotNull(model.getAttribute("livreur"));
    }

    @Test
    public void testCreateLivreur_Success() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John Doe");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreur, bindingResult, model);

        assertEquals("redirect:/livreurs/list", response);
        verify(livreurService, times(1)).createLivreur(livreur);
        verify(bindingResult, times(1)).hasErrors();
    }


    @Test
    public void testCreateLivreur_Conflict() {
        Livreur livreur = new Livreur();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new ConflictException("Conflict")).when(livreurService).createLivreur(any(Livreur.class));

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreur, bindingResult, model);

        assertEquals("livreurs/livreur_create", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Une erreur inattendue s'est produite.", model.getAttribute("errorMessage"));
        verify(livreurService, times(1)).createLivreur(livreur);
    }


    @Test
    public void testEditLivreurForm_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getLivreurById(1)).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.editLivreurForm(1, model);

        assertEquals("livreurs/livreur_edit", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreur, model.getAttribute("livreur"));
        verify(livreurService, times(1)).getLivreurById(1);
    }

    @Test
    public void testUpdateLivreur_Success() {
        // Arrange: Create a valid Livreur object
        Livreur livreur = new Livreur();
        livreur.setId(1);
        livreur.setNomLivreur("Updated Name");
        livreur.setMouvement(new Mouvement());
        livreur.getMouvement().setId(1);

        when(livreurService.updateLivreur(1, livreur)).thenReturn(livreur);

        // Prepare a Model object to pass to the controller
        Model model = new ConcurrentModel();

        // Act: Call the update method
        String response = livreurController.updateLivreur(1, livreur, model);

        // Assert: Verify the results
        assertEquals("redirect:/livreurs/list", response, "Expected redirection to the livreurs list page.");
        verify(livreurService, times(1)).updateLivreur(1, livreur);
        assertFalse(model.containsAttribute("errorMessage"), "No error message should be present in the model.");
    }


    @Test
    public void testDeleteLivreur_Success() {
        doNothing().when(livreurService).deleteLivreur(1);

        String response = livreurController.deleteLivreur(1, new ConcurrentModel());

        assertEquals("redirect:/livreurs/list", response);
        verify(livreurService, times(1)).deleteLivreur(1);
    }

    @Test
    public void testDeleteLivreur_Error() {
        doThrow(new RuntimeException("Delete Error")).when(livreurService).deleteLivreur(1);

        Model model = new ConcurrentModel();
        String response = livreurController.deleteLivreur(1, model);

        assertEquals("livreurs/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Une erreur inattendue s'est produite lors de la suppression.", model.getAttribute("errorMessage"));
    }
}
