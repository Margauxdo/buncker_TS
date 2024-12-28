package templates.typeRegles.controller;

import example.DTO.ValiseDTO;
import example.controller.ValiseController;
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







    // Test pour afficher le formulaire de modification (succès)
    @Test
    public void testEditValiseForm_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        Mockito.when(valiseService.getValiseById(1)).thenReturn(valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.updateValise(1, valiseDTO, model);

        // Assert
        Assertions.assertEquals("redirect:/valise/list", response, "Expected redirect to '/valise/list'");
        Mockito.verify(valiseService, Mockito.times(1)).updateValise(1, valiseDTO);
    }


    // Test pour afficher le formulaire de modification (erreur)
    @Test
    public void testEditValiseForm_NotFound() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        Mockito.doThrow(new ResourceNotFoundException("Valise not found")).when(valiseService).updateValise(1, valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.updateValise(1, valiseDTO, model);

        // Assert
        Assertions.assertEquals("valises/error", response, "Expected view name is 'valises/error'");
        Assertions.assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
        Mockito.verify(valiseService, Mockito.times(1)).updateValise(1, valiseDTO);
    }



    @Test
    public void testViewValiseById_Success() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(1)
                .description("Test Valise")
                .build();
        Mockito.when(valiseService.getValiseById(1)).thenReturn(valiseDTO);

        Model model = new ConcurrentModel();

        // Act
        String response = valiseController.getValiseDetails(1, model);

        // Assert
        Assertions.assertEquals("valises/valise_details", response, "Expected view name is 'valises/valise_details'");
        Assertions.assertTrue(model.containsAttribute("valise"), "Model should contain 'valise' attribute");
        ValiseDTO valise = (ValiseDTO) model.getAttribute("valise");
        Assertions.assertNotNull(valise, "Valise should not be null");
        Assertions.assertEquals("Test Valise", valise.getDescription(), "Description should match");
    }



}
