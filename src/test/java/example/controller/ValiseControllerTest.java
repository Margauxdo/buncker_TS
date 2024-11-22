package example.controller;

import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValiseControllerTest {

    @InjectMocks
    private ValiseController valiseController;

    @Mock
    private IValiseService valiseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewValises_Success() {
        // Arrange
        when(valiseService.getAllValises()).thenReturn(java.util.List.of(new Valise()));

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.viewValises(model);

        // Assert
        assertEquals("valises/valises_list", response, "Expected view name is 'valises/valises_list'");
        assertTrue(model.containsAttribute("valises"), "Model should contain 'valises' attribute");
    }

    @Test
    public void testViewValiseById_Success() {
        // Arrange
        Valise valise = new Valise();
        when(valiseService.getValiseById(1)).thenReturn(valise);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.viewValise(1, model);

        // Assert
        assertEquals("valises/valise_details", response, "Expected view name is 'valises/valise_details'");
        assertTrue(model.containsAttribute("valise"), "Model should contain 'valise' attribute");
        assertEquals(valise, model.getAttribute("valise"), "Expected 'valise' in model");
    }

    @Test
    public void testViewValiseById_NotFound() {
        // Arrange
        when(valiseService.getValiseById(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.viewValise(1, model);

        // Assert
        assertEquals("valises/error", response, "Expected view name is 'valises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
    }

    @Test
    public void testCreateValiseForm_Success() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.createValiseForm(model);

        // Assert
        assertEquals("valises/valise_create", response, "Expected view name is 'valises/valise_create'");
        assertTrue(model.containsAttribute("valise"), "Model should contain 'valise' attribute");
    }

    @Test
    public void testEditValiseForm_Success() {
        // Arrange
        Valise valise = new Valise();
        when(valiseService.getValiseById(1)).thenReturn(valise);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.editValiseForm(1, model);

        // Assert
        assertEquals("valises/valise_edit", response, "Expected view name is 'valises/valise_edit'");
        assertTrue(model.containsAttribute("valise"), "Model should contain 'valise' attribute");
        assertEquals(valise, model.getAttribute("valise"), "Expected 'valise' in model");
    }

    @Test
    public void testEditValiseForm_NotFound() {
        // Arrange
        when(valiseService.getValiseById(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.editValiseForm(1, model);

        // Assert
        assertEquals("valises/error", response, "Expected view name is 'valises/error'");
    }

    @Test
    public void testDeleteValise_Success_Thymeleaf() {
        // Arrange
        doNothing().when(valiseService).deleteValise(1);

        Model model = new ConcurrentModel(); // Create a mock model

        // Act
        String response = valiseController.deleteValise(1, model);

        // Assert
        assertEquals("redirect:/valises/valises_list", response, "Expected redirect to 'valises/valises_list'");
        verify(valiseService, times(1)).deleteValise(1);
    }

    @Test
    public void testDeleteValise_NotFound_Thymeleaf() {
        // Arrange
        doThrow(new ResourceNotFoundException("Valise not found")).when(valiseService).deleteValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.deleteValise(1, model);

        // Assert
        assertEquals("valises/error", response, "Expected view name is 'valises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
        assertEquals("Valise with ID 1 not found!", model.getAttribute("errorMessage"));
        verify(valiseService, times(1)).deleteValise(1);
    }

}
