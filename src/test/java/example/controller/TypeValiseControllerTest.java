package example.controller;

import example.entity.TypeValise;
import example.interfaces.ITypeValiseService;
import jakarta.persistence.EntityNotFoundException;
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

public class TypeValiseControllerTest {

    @InjectMocks
    private TypeValiseController typeValiseController;

    @Mock
    private ITypeValiseService typeValiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **Tests Thymeleaf**

    @Test
    public void testViewTypeValises_Success() {
        // Arrange
        List<TypeValise> typeValises = List.of(new TypeValise());
        when(typeValiseService.getTypeValises()).thenReturn(typeValises);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.viewTypeValises(model);

        // Assert
        assertEquals("typeValises/TV_list", response, "Expected view name is 'typeValises/TV_list'");
        assertTrue(model.containsAttribute("typeValises"), "Model should contain 'typeValises' attribute");
        assertEquals(typeValises, model.getAttribute("typeValises"), "Expected 'typeValises' in model");
    }

    @Test
    public void testViewTypeValiseById_Success() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.viewTypeValiseById(1, model);

        // Assert
        assertEquals("typeValises/TV_details", response, "Expected view name is 'typeValises/TV_details'");
        assertTrue(model.containsAttribute("typeValise"), "Model should contain 'typeValise' attribute");
        assertEquals(typeValise, model.getAttribute("typeValise"), "Expected 'typeValise' in model");
    }

    @Test
    public void testViewTypeValiseById_NotFound() {
        // Arrange
        when(typeValiseService.getTypeValise(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.viewTypeValiseById(1, model);

        // Assert
        assertEquals("typeValises/error", response, "Expected view name is 'typeValises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
    }

    @Test
    public void testCreateTypeValiseForm_Success() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.createTypeValiseForm(model);

        // Assert
        assertEquals("typeValises/TV_create", response, "Expected view name is 'typeValises/TV_create'");
        assertTrue(model.containsAttribute("typeValise"), "Model should contain 'typeValise' attribute");
    }

    @Test
    public void testEditTypeValiseForm_Success() {
        // Arrange
        TypeValise typeValise = new TypeValise();
        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.editTypeValiseForm(1, model);

        // Assert
        assertEquals("typeValises/TV_edit", response, "Expected view name is 'typeValises/TV_edit'");
        assertTrue(model.containsAttribute("typeValise"), "Model should contain 'typeValise' attribute");
        assertEquals(typeValise, model.getAttribute("typeValise"), "Expected 'typeValise' in model");
    }

    @Test
    public void testEditTypeValiseForm_NotFound() {
        // Arrange :
        when(typeValiseService.getTypeValise(1)).thenReturn(null);

        // Préparer un modèle concurrent
        Model model = new ConcurrentModel();

        // Act :
        String response = typeValiseController.editTypeValiseForm(1, model);

        // Assert :
        assertEquals("typeValises/error", response, "La vue attendue est 'typeValises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Le modèle doit contenir l'attribut 'errorMessage'");
        assertEquals("Le type de valise avec l'ID 1 n'a pas été trouvé.",
                model.getAttribute("errorMessage"), "Le message d'erreur n'est pas correct.");
    }



    @Test
    public void testDeleteTypeValise_Success() {
        // Arrange
        doNothing().when(typeValiseService).deleteTypeValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.deleteTypeValise(1, model);

        // Assert
        assertEquals("redirect:/typeValises/TV_list", response, "Expected redirection to 'typeValises/TV_list'");
        verify(typeValiseService, times(1)).deleteTypeValise(1);
    }

    @Test
    public void testDeleteTypeValise_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("TypeValise not found")).when(typeValiseService).deleteTypeValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.deleteTypeValise(1, model);

        // Assert
        assertEquals("typeValises/error", response, "Expected view name is 'typeValises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
    }
}
