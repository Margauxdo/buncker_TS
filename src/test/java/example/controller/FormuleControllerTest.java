package example.controller;

import example.entity.Formule;
import example.interfaces.IFormuleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
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

    @Test
    public void testViewFormuleList() {
        List<Formule> formules = new ArrayList<>();
        formules.add(new Formule());
        when(formuleService.getAllFormules()).thenReturn(formules);

        Model model = new ConcurrentModel();
        String response = formuleController.viewFormuleList(model);

        assertEquals("formules/formule_list", response);
        assertTrue(model.containsAttribute("formules"));
        assertEquals(formules, model.getAttribute("formules"));
    }

    @Test
    public void testViewFormuleById_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model model = new ConcurrentModel();
        String response = formuleController.viewFormuleById(1, model);

        assertEquals("formules/formule_detail", response);
        assertTrue(model.containsAttribute("formule"));
        assertEquals(formule, model.getAttribute("formule"));
    }


    @Test
    public void testViewFormuleById_NotFound() {
        when(formuleService.getFormuleById(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            formuleController.viewFormuleById(1, model);
        });

        // Vérifiez le message d'exception (si nécessaire)
        assertEquals("Formule avec l'Id 1 n'existe pas !", exception.getMessage());
    }


    @Test
    public void testCreateFormuleForm() {
        Model model = new ConcurrentModel();
        String response = formuleController.createFormuleForm(model);

        assertEquals("formules/formule_create", response);
        assertTrue(model.containsAttribute("formule"));
        assertNotNull(model.getAttribute("formule"));
    }

    @Test
    public void testCreateFormule_Success() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenReturn(formule); // Utilisation de when() pour les méthodes non-void

        Model model = new ConcurrentModel();
        String response = formuleController.createFormule(formule, model);

        assertEquals("redirect:/formules/list", response);
    }


    @Test
    public void testCreateFormule_Failure() {
        doThrow(new IllegalArgumentException("Invalid formula")).when(formuleService).createFormule(any(Formule.class));

        Model model = new ConcurrentModel();
        String response = formuleController.createFormule(new Formule(), model);

        assertEquals("formules/formule_create", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Invalid formula", model.getAttribute("errorMessage"));
    }

    @Test
    public void testEditFormuleForm_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model model = new ConcurrentModel();
        String response = formuleController.editFormuleForm(1, model);

        assertEquals("formules/formule_edit", response);
        assertTrue(model.containsAttribute("formule"));
        assertEquals(formule, model.getAttribute("formule"));
    }

    @Test
    public void testEditFormuleForm_NotFound() {
        when(formuleService.getFormuleById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = formuleController.editFormuleForm(1, model);

        assertEquals("formules/error", response);
    }

    @Test
    public void testUpdateFormule_Success() {
        Formule formule = new Formule();
        when(formuleService.updateFormule(eq(1), any(Formule.class))).thenReturn(formule); // Utilisez when() pour simuler le retour d'un objet

        String response = formuleController.updateFormule(1, formule);

        assertEquals("redirect:/formules/formules_list", response);  }


    @Test
    public void testDeleteFormule_Success() {
        doNothing().when(formuleService).deleteFormule(1);

        String response = formuleController.deleteFormule(1);

        assertEquals("redirect:/formules/formules_list", response);  }
}
