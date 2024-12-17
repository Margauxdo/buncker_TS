package example.controller;

import example.DTO.RegleDTO;
import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
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

    // Test: Affichage de la liste des règles
    @Test
    public void testViewAllRegles_Success() {
        // Arrange
        List<RegleDTO> regles = new ArrayList<>();
        regles.add(RegleDTO.builder().id(1).coderegle("REGLE1").build());
        when(regleService.readAllRegles()).thenReturn(regles);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.listRegles(model);

        // Assert
        assertEquals("regles/regle_list", response);
        assertTrue(model.containsAttribute("regles"));
        assertEquals(regles, model.getAttribute("regles"));
        verify(regleService, times(1)).readAllRegles();
    }

    // Test: Affichage des détails d'une règle (succès)
    @Test
    public void testViewRegle_Success() {
        // Arrange
        RegleDTO regle = RegleDTO.builder().id(1).coderegle("REGLE1").build();
        when(regleService.readRegle(1)).thenReturn(regle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.viewRegle(1, model);

        // Assert
        assertEquals("regles/regle_details", response);
        assertTrue(model.containsAttribute("regle"));
        assertEquals(regle, model.getAttribute("regle"));
        verify(regleService, times(1)).readRegle(1);
    }

    // Test: Affichage des détails d'une règle (non trouvée)
    @Test
    public void testViewRegle_NotFound() {
        // Arrange
        when(regleService.readRegle(1)).thenThrow(new RegleNotFoundException("Regle with ID 1 not found."));

        Model model = new ConcurrentModel();

        // Act & Assert
        Exception exception = assertThrows(RegleNotFoundException.class, () -> regleController.viewRegle(1, model));

        assertEquals("Regle with ID 1 not found.", exception.getMessage());
        verify(regleService, times(1)).readRegle(1);
    }

    // Test: Formulaire de création de règle
    @Test
    public void testCreateRegleForm() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = regleController.createRegleForm(model);

        // Assert
        assertEquals("regles/regle_create", response);
        assertTrue(model.containsAttribute("regle"));
        assertNotNull(model.getAttribute("regle"));
    }

    // Test: Formulaire d'édition de règle (succès)
    /*@Test
    public void testEditRegleForm_Success() {
        // Arrange
        RegleDTO regle = RegleDTO.builder().id(1).coderegle("REGLE1").build();
        when(regleService.readRegle(1)).thenReturn(regle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleController.updateRegle(1, (RegleDTO) model);

        // Assert
        assertEquals("regles/regle_edit", response);
        assertTrue(model.containsAttribute("regle"));
        assertEquals(regle, model.getAttribute("regle"));
        verify(regleService, times(1)).readRegle(1);
    }*/

    // Test: Formulaire d'édition de règle (non trouvée)
   /* @Test
    public void testEditRegleForm_NotFound() {
        // Arrange
        when(regleService.readRegle(1)).thenThrow(new RegleNotFoundException("Regle with ID 1 not found."));

        Model model = new ConcurrentModel();

        // Act & Assert
        Exception exception = assertThrows(RegleNotFoundException.class, () -> regleController.updateRegle(1, (RegleDTO) model));

        assertEquals("Regle with ID 1 not found.", exception.getMessage());
        verify(regleService, times(1)).readRegle(1);
    }*/

    // Test: Suppression d'une règle
    @Test
    public void testDeleteRegle_Success() {
        // Act
        String response = regleController.deleteRegle(1);

        // Assert
        assertEquals("redirect:/regles/list", response);
        verify(regleService, times(1)).deleteRegle(1);
    }
}
