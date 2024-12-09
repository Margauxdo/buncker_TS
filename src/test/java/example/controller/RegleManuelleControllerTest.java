package example.controller;

import example.DTO.RegleManuelleDTO;
import example.interfaces.IRegleManuelleService;
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

public class RegleManuelleControllerTest {

    @InjectMocks
    private RegleManuelleController regleManuelleController;

    @Mock
    private IRegleManuelleService regleManuelleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test : Liste des règles manuelles - Succès
    @Test
    public void testListRegleManuelles_Success() {
        // Arrange
        List<RegleManuelleDTO> regleManuelles = new ArrayList<>();
        regleManuelles.add(new RegleManuelleDTO());
        when(regleManuelleService.getRegleManuelles()).thenReturn(regleManuelles);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.listRegleManuelles(model);

        // Assert
        assertEquals("reglesManuelles/RM_list", response);
        assertTrue(model.containsAttribute("regleManuelles"));
        assertEquals(regleManuelles, model.getAttribute("regleManuelles"));
    }

    // Test : Détails d'une règle manuelle par ID - Succès
    @Test
    public void testViewRegleManuelleById_Success() {
        // Arrange
        RegleManuelleDTO regleManuelle = new RegleManuelleDTO();
        regleManuelle.setId(1);
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(regleManuelle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.viewRegleManuelleById(1, model);

        // Assert
        assertEquals("reglesManuelles/RM_details", response);
        assertTrue(model.containsAttribute("regleManuelle"));
        assertEquals(regleManuelle, model.getAttribute("regleManuelle"));
    }

    // Test : Détails d'une règle manuelle par ID - Non trouvé
    @Test
    public void testViewRegleManuelleById_NotFound() {
        // Arrange
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.viewRegleManuelleById(1, model);

        // Assert
        assertEquals("reglesManuelles/error", response);
    }

    // Test : Formulaire de création d'une règle manuelle
    @Test
    public void testCreateRegleManuelleForm() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.createRegleManuelleForm(model);

        // Assert
        assertEquals("reglesManuelles/RM_create", response);
        assertTrue(model.containsAttribute("regleManuelle"));
        assertNotNull(model.getAttribute("regleManuelle"));
    }

    // Test : Formulaire d'édition d'une règle manuelle - Succès
    @Test
    public void testEditRegleManuelleForm_Success() {
        // Arrange
        RegleManuelleDTO regleManuelle = new RegleManuelleDTO();
        regleManuelle.setId(1);
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(regleManuelle);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.editRegleManuelleForm(1, model);

        // Assert
        assertEquals("reglesManuelles/RM_edit", response);
        assertTrue(model.containsAttribute("regleManuelle"));
        assertEquals(regleManuelle, model.getAttribute("regleManuelle"));
    }

    // Test : Formulaire d'édition d'une règle manuelle - Non trouvé
    @Test
    public void testEditRegleManuelleForm_NotFound() {
        // Arrange
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.editRegleManuelleForm(1, model);

        // Assert
        assertEquals("reglesManuelles/error", response);
    }

    // Test : Suppression d'une règle manuelle - Succès
    @Test
    public void testDeleteRegleManuelle_Success() {
        // Act
        String response = regleManuelleController.deleteRegleManuelle(1);

        // Assert
        assertEquals("redirect:/reglesManuelles/RM_list", response);
        verify(regleManuelleService, times(1)).deleteRegleManuelle(1);
    }

    // Test : Suppression d'une règle manuelle - Non trouvé
    @Test
    public void testDeleteRegleManuelle_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("Manual Ruler with ID 1 not found"))
                .when(regleManuelleService).deleteRegleManuelle(1);

        // Act
        String response = regleManuelleController.deleteRegleManuelle(1);

        // Assert
        assertEquals("redirect:/reglesManuelles/error", response);
        verify(regleManuelleService, times(1)).deleteRegleManuelle(1);
    }
}
