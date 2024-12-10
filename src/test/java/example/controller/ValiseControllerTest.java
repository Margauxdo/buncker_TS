package example.controller;

import example.DTO.ValiseDTO;
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

import java.util.List;

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

    // **Tests Thymeleaf**






    // Test pour afficher le formulaire de création
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

    // Test pour afficher le formulaire de modification (succès)
    @Test
    public void testEditValiseForm_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        when(valiseService.getValiseById(1)).thenReturn(valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.updateValise(1, valiseDTO, model);

        // Assert
        assertEquals("redirect:/valise/list", response, "Expected redirect to '/valise/list'");
        verify(valiseService, times(1)).updateValise(1, valiseDTO);
    }


    // Test pour afficher le formulaire de modification (erreur)
    @Test
    public void testEditValiseForm_NotFound() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        doThrow(new ResourceNotFoundException("Valise not found")).when(valiseService).updateValise(1, valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.updateValise(1, valiseDTO, model);

        // Assert
        assertEquals("valises/error", response, "Expected view name is 'valises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
        verify(valiseService, times(1)).updateValise(1, valiseDTO);
    }


    // Test pour supprimer une valise (succès)
    @Test
    public void testDeleteValise_Success() {
        // Arrange
        doNothing().when(valiseService).deleteValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.deleteValise(1, model);

        // Assert
        assertEquals("redirect:/valises/valises_list", response, "Expected redirect to 'valises/valises_list'");
        verify(valiseService, times(1)).deleteValise(1);
    }



    @Test
    public void testViewValises_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        when(valiseService.getAllValises()).thenReturn(List.of(valiseDTO));

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.viewValises(model);

        // Assert
        assertEquals("valises/valises_list", response, "Expected view name is 'valises/valises_list'");
        assertTrue(model.containsAttribute("valises"), "Model should contain 'valises' attribute");
        List<ValiseDTO> valises = (List<ValiseDTO>) model.getAttribute("valises");
        assertNotNull(valises, "Valises should not be null");
        assertEquals(1, valises.size(), "Valises list should contain one item");
        assertEquals("Test Valise", valises.get(0).getDescription(), "Description should match");
    }

    @Test
    public void testViewValiseById_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        when(valiseService.getValiseById(1)).thenReturn(valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.getValiseDetails(1, model);

        // Assert
        assertEquals("valises/valise_details", response, "Expected view name is 'valises/valise_details'");
        assertTrue(model.containsAttribute("valise"), "Model should contain 'valise' attribute");
        ValiseDTO valise = (ValiseDTO) model.getAttribute("valise");
        assertNotNull(valise, "Valise should not be null");
        assertEquals("Test Valise", valise.getDescription(), "Description should match");
    }

    @Test
    public void testDeleteValise_NotFound() {
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
