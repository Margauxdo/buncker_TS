package example.controller;

import example.entity.Formule;
import example.interfaces.IFormuleService;
import jakarta.persistence.EntityNotFoundException;
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

public class FormuleControllerTest {

    @InjectMocks
    private FormuleController formuleController;

    @Mock
    private IFormuleService formuleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Affichage de la liste des formules - Succès
    @Test
    public void testViewFormuleList() {
        List<Formule> formules = List.of(new Formule());
        when(formuleService.getAllFormules()).thenReturn(formules);

        Model model = new ConcurrentModel();
        String response = formuleController.viewFormuleList(model);

        assertEquals("formules/formule_list", response);
        assertTrue(model.containsAttribute("formules"));
        assertEquals(formules, model.getAttribute("formules"));
        verify(formuleService, times(1)).getAllFormules();
    }

    // Test: Affichage d'une formule par ID - Succès
    @Test
    public void testViewFormuleById_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model model = new ConcurrentModel();
        String response = formuleController.viewFormuleById(1, model);

        assertEquals("formules/formule_detail", response);
        assertTrue(model.containsAttribute("formule"));
        assertEquals(formule, model.getAttribute("formule"));
        verify(formuleService, times(1)).getFormuleById(1);
    }



    // Test: Formulaire de création d'une formule
    @Test
    public void testCreateFormuleForm() {
        Model model = new ConcurrentModel();
        String response = formuleController.createFormuleForm(model);

        assertEquals("formules/formule_create", response);
        assertTrue(model.containsAttribute("formule"));
        assertNotNull(model.getAttribute("formule"));
    }






    // Test: Formulaire de modification d'une formule - Succès
    @Test
    public void testEditFormuleForm_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model model = new ConcurrentModel();
        String response = formuleController.editFormuleForm(1, model);

        assertEquals("formules/formule_edit", response);
        assertTrue(model.containsAttribute("formule"));
        assertEquals(formule, model.getAttribute("formule"));
        verify(formuleService, times(1)).getFormuleById(1);
    }

    // Test: Formulaire de modification d'une formule - Non trouvée
    @Test
    public void testEditFormuleForm_NotFound() {
        // Arrange: Simulate service throwing an EntityNotFoundException
        when(formuleService.getFormuleById(1)).thenThrow(new jakarta.persistence.EntityNotFoundException("Formule avec l'Id 1 n'existe pas !"));

        // Act & Assert: Expect the exception and verify its message
        jakarta.persistence.EntityNotFoundException exception = assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> formuleController.editFormuleForm(1, new ConcurrentModel())
        );

        // Verify the exception message
        assertEquals("Formule avec l'Id 1 n'existe pas !", exception.getMessage(), "Exception message does not match");

        // Verify the service interaction
        verify(formuleService, times(1)).getFormuleById(1);
    }


    // Test: Mise à jour d'une formule - Succès
    @Test
    public void testUpdateFormule_Success() {
        // Arrange
        Formule formule = new Formule();
        Integer regleId = null; // Ajoutez l'argument pour la règle
        Model model = mock(Model.class); // Mock the Model object
        when(formuleService.updateFormule(eq(1), any(Formule.class))).thenReturn(formule);

        // Act
        String response = formuleController.updateFormule(1, formule, regleId, model);

        // Assert
        assertEquals("redirect:/formules/list", response, "The response should redirect to the formules list.");
        verify(formuleService, times(1)).updateFormule(1, formule);
    }




    // Test: Suppression d'une formule - Succès
    @Test
    public void testDeleteFormule_Success() {
        // Mock the RedirectAttributes
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Simulate successful deletion in the service
        doNothing().when(formuleService).deleteFormule(1);

        // Call the controller method
        ResponseEntity<Void> response = formuleController.deleteFormule(1, redirectAttributes);

        // Assert the response status and location header
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "Expected HTTP status is FOUND (302).");
        assertEquals("/formules/list", response.getHeaders().getLocation().toString(), "Expected redirection to /formules/list.");

        // Verify the service was called exactly once with the correct ID
        verify(formuleService, times(1)).deleteFormule(1);
    }



}
