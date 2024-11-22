package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
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

public class JourFerieControllerTest {

    @InjectMocks
    private JourFerieController jourFerieController;

    @Mock
    private IJourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewJFList() {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(1);
        when(jourFerieService.getJourFeries()).thenReturn(List.of(jourFerie));

        Model model = new ConcurrentModel();
        String response = jourFerieController.viewJF(model);

        assertEquals("jourFeries/JF_list", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertEquals(1, ((List) model.getAttribute("jourFerie")).size());  // Vérifie que la liste contient un élément
    }

    @Test
    public void testViewJFById_Success() {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(1);
        when(jourFerieService.getJourFerie(1)).thenReturn(jourFerie);

        Model model = new ConcurrentModel();
        String response = jourFerieController.viewJFById(1, model);

        assertEquals("jourFeries/JF_details", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertEquals(jourFerie, model.getAttribute("jourFerie"));  // Vérifie que l'attribut est bien l'objet jourFerie
    }

    @Test
    public void testViewJFById_NotFound() {
        when(jourFerieService.getJourFerie(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = jourFerieController.viewJFById(1, model);

        assertEquals("jourFeries/error", response);

        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Jour férié non trouvé pour l'ID 1", model.getAttribute("errorMessage"));

        assertNull(model.getAttribute("jourFerie"));
    }


    @Test
    public void testCreateJourFerieForm() {
        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieForm(model);

        assertEquals("jourFeries/JF_create", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertNotNull(model.getAttribute("jourFerie"));
    }

    @Test
    public void testCreateJourFerie_Success() {
        JourFerie jourFerie = new JourFerie();
        when(jourFerieService.saveJourFerie(jourFerie)).thenReturn(jourFerie);

        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieThymeleaf(jourFerie);

        assertEquals("redirect:/jourferies/JF_list", response);
    }
}
