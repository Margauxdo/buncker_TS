package example.controller;

import example.entity.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SortieSemaineControllerTest {

    @InjectMocks
    private SortieSemaineController sortieSemaineController;

    @Mock
    private ISortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test Thymeleaf: Afficher toutes les semaines
    @Test
    public void testViewSortieSemaine_Success() {
        List<SortieSemaine> sortieSemaines = new ArrayList<>();
        sortieSemaines.add(new SortieSemaine());
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(sortieSemaines);

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.viewSortieSemaine(model);

        assertEquals("sortieSemaines/SS_list", response, "Expected view name is 'sortieSemaines/SS_list'");
        assertTrue(model.containsAttribute("sortieSemaine"), "Model should contain 'sortieSemaine' attribute");
        assertEquals(sortieSemaines, model.getAttribute("sortieSemaine"), "Expected list of 'sortieSemaine' in model");
    }

    @Test
    public void testViewSortieSemaine_EmptyList() {
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(Collections.emptyList());

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.viewSortieSemaine(model);

        assertEquals("sortieSemaines/SS_list", response, "Expected view name is 'sortieSemaines/SS_list'");
        assertTrue(model.containsAttribute("sortieSemaine"), "Model should contain 'sortieSemaine' attribute");
        assertTrue(((List<?>) model.getAttribute("sortieSemaine")).isEmpty(), "Expected empty list in model");
    }

    // Test Thymeleaf: Voir une semaine spécifique par ID
    @Test
    public void testViewSortieSemaineById_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.viewSortieSemaineById(1, model);

        assertEquals("sortieSemaines/SS_details", response, "Expected view name is 'sortieSemaines/SS_details'");
        assertTrue(model.containsAttribute("sortieSemaine"), "Model should contain 'sortieSemaine' attribute");
        assertEquals(sortieSemaine, model.getAttribute("sortieSemaine"), "Expected 'sortieSemaine' in model");
    }

    @Test
    public void testViewSortieSemaineById_NotFound() {
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.viewSortieSemaineById(1, model);

        assertEquals("sortieSemaines/error", response, "Expected view name is 'sortieSemaines/error'");
        assertTrue(model.containsAttribute("errormessage"), "Model should contain 'errormessage' attribute");
        assertEquals("sortieSemaine avec l'Id1non trouvé", model.getAttribute("errormessage"));
    }

    // Test Thymeleaf: Créer une semaine
    @Test
    public void testCreateSortieSemaineForm() {
        Model model = new ConcurrentModel();
        String response = sortieSemaineController.createSortieSemaineForm(model);

        assertEquals("sortieSemaines/SS_create", response, "Expected view name is 'sortieSemaines/SS_create'");
        assertTrue(model.containsAttribute("sortieSemaine"), "Model should contain 'sortieSemaine' attribute");
        assertNotNull(model.getAttribute("sortieSemaine"), "Expected 'sortieSemaine' attribute in model");
    }

    @Test
    public void testCreateSortieSemaine_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();

        String response = sortieSemaineController.createSortieSemaine(sortieSemaine);

        assertEquals("redirect:/sortieSemaines/SS_list", response, "Expected redirection to 'SS_list'");
        verify(sortieSemaineService, times(1)).createSortieSemaine(sortieSemaine);
    }

    // Test Thymeleaf: Modifier une semaine
    @Test
    public void testEditSortieSemaineForm_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.editSortieSemaineForm(1, model);

        assertEquals("sortieSemaines/SS_edit", response, "Expected view name is 'sortieSemaines/SS_edit'");
        assertTrue(model.containsAttribute("sortieSemaine"), "Model should contain 'sortieSemaine' attribute");
        assertEquals(sortieSemaine, model.getAttribute("sortieSemaine"), "Expected 'sortieSemaine' in model");
    }

    @Test
    public void testEditSortieSemaineForm_NotFound() {
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = sortieSemaineController.editSortieSemaineForm(1, model);

        assertEquals("sortieSemaines/error", response, "Expected view name is 'sortieSemaines/error'");
    }

    @Test
    public void testUpdateSortieSemaine_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();

        String response = sortieSemaineController.updateSortieSemaine(1, sortieSemaine);

        assertEquals("redirect:/sortieSemaines/SS_list", response, "Expected redirection to 'SS_list'");
        verify(sortieSemaineService, times(1)).updateSortieSemaine(1, sortieSemaine);
    }

    // Test Thymeleaf: Supprimer une semaine
    @Test
    public void testDeleteSortieSemaine_Success() {
        String response = sortieSemaineController.deleteSortieSemaine(1);

        assertEquals("redirect:/sortieSemaines/SS_list", response, "Expected redirection to 'SS_list'");
        verify(sortieSemaineService, times(1)).deleteSortieSemaine(1);
    }
}
