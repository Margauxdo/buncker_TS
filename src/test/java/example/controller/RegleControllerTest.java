package example.controller;

import example.entity.Regle;
import example.interfaces.IRegleService;
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

public class RegleControllerTest {

    @InjectMocks
    private RegleController regleController;

    @Mock
    private IRegleService regleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests Thymeleaf

    @Test
    public void testViewAllRegles_Success() {
        // Arrange
        List<Regle> regles = new ArrayList<>();
        regles.add(new Regle());
        when(regleService.readAllRegles()).thenReturn(regles);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.viewAllRegles(model);

        // Assert
        assertEquals("regles/regle_list", response);
        assertTrue(model.containsAttribute("regles"));
        assertEquals(regles, model.getAttribute("regles"));
    }

    @Test
    public void testViewRegle_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);
        when(regleService.readRegle(1)).thenReturn(regle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.viewRegle(1, model);

        // Assert
        assertEquals("regles/regle_details", response);
        assertTrue(model.containsAttribute("regle"));
        assertEquals(regle, model.getAttribute("regle"));
    }

    @Test
    public void testViewRegle_NotFound() {
        // Arrange
        when(regleService.readRegle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.viewRegle(1, model);

        // Assert
        assertEquals("regles/error", response);
    }

    @Test
    public void testCreateRegleForm() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = regleController.createRegleForm(model);

        // Assert
        assertEquals("regles/regle_create", response);
        assertTrue(model.containsAttribute("formule"));
        assertNotNull(model.getAttribute("formule"));
    }

    @Test
    public void testEditRegleForm_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);
        when(regleService.readRegle(1)).thenReturn(regle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.editRegleForm(1, model);

        // Assert
        assertEquals("regles/regle_edit", response);
        assertTrue(model.containsAttribute("regle"));
        assertEquals(regle, model.getAttribute("regle"));
    }

    @Test
    public void testEditRegleForm_NotFound() {
        // Arrange
        when(regleService.readRegle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.editRegleForm(1, model);

        // Assert
        assertEquals("regles/error", response);
    }

    @Test
    public void testDeleteRegle_Success() {
        // Act
        String response = regleController.deleteRegle(1);

        // Assert
        assertEquals("redirect:/regles/regle_list", response);
        verify(regleService, times(1)).deleteRegle(1);
    }
}
