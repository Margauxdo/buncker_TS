package example.controller;

import example.entity.RegleManuelle;
import example.exceptions.ConflictException;
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

    // Tests pour les fonctionnalités Thymeleaf

    @Test
    public void testListRegleManuelles_Success() {
        // Arrange
        List<RegleManuelle> regleManuelles = new ArrayList<>();
        regleManuelles.add(new RegleManuelle());
        when(regleManuelleService.getRegleManuelles()).thenReturn(regleManuelles);

        Model model = new ConcurrentModel();

        // Act
        String response = regleManuelleController.listRegleManuelles(model);

        // Assert
        assertEquals("reglesManuelles/RM_list", response);
        assertTrue(model.containsAttribute("regleManuelles"));
        assertEquals(regleManuelles, model.getAttribute("regleManuelles"));
    }

    @Test
    public void testViewRegleManuelleById_Success() {
        // Arrange
        RegleManuelle regleManuelle = new RegleManuelle();
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

    @Test
    public void testEditRegleManuelleForm_Success() {
        // Arrange
        RegleManuelle regleManuelle = new RegleManuelle();
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

    @Test
    public void testDeleteRegleManuelle_Success() {
        // Act
        String response = regleManuelleController.deleteRegleManuelle(1);

        // Assert
        assertEquals("redirect:/reglesManuelles/RM_list", response);
        verify(regleManuelleService, times(1)).deleteRegleManuelle(1);
    }

    @Test
    public void testDeleteRegleManuelle_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("Manual rule not found")).when(regleManuelleService).deleteRegleManuelle(1);

        // Act
        String response = regleManuelleController.deleteRegleManuelle(1);

        // Assert
        assertEquals("redirect:/reglesManuelles/error", response); // Redirection mise à jour
        verify(regleManuelleService, times(1)).deleteRegleManuelle(1);
    }

}
