package example.controller;

import example.entity.TypeRegle;
import example.interfaces.ITypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TypeRegleControllerTest {

    @InjectMocks
    private TypeRegleController typeRegleController;

    @Mock
    private ITypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour afficher la liste des TypeRegles via Thymeleaf
    @Test
    public void testViewAllTypeRegles_Success() {
        // Arrange
        when(typeRegleService.getTypeRegles()).thenReturn(java.util.List.of(new TypeRegle()));

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewAllTypeRegles(model);

        // Assert
        assertEquals("typeRegles/TR_list", response, "Expected view name is 'typeRegles/TR_list'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
    }

    // Test pour voir un TypeRegle par ID via Thymeleaf (Succès)
    @Test
    public void testViewTypeRegleById_Success() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleService.getTypeRegle(1)).thenReturn(typeRegle);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewTypeRegleById(1, model);

        // Assert
        assertEquals("typeRegles/TP_details", response, "Expected view name is 'typeRegles/TP_details'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
        assertEquals(typeRegle, model.getAttribute("typeRegle"), "Expected 'typeRegle' in model");
    }

    // Test pour voir un TypeRegle par ID via Thymeleaf (Erreur)
    @Test
    public void testViewTypeRegleById_NotFound() {
        // Arrange
        when(typeRegleService.getTypeRegle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewTypeRegleById(1, model);

        // Assert
        assertEquals("typeRegles/error", response, "Expected view name is 'typeRegles/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
    }

    // Test pour afficher le formulaire de création via Thymeleaf
    @Test
    public void testCreateTypeRegleForm_Success() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.createTypeRegleForm(model);

        // Assert
        assertEquals("typeRegles/TP_create", response, "Expected view name is 'typeRegles/TP_create'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
    }

    // Test pour afficher le formulaire de modification via Thymeleaf (Succès)
    @Test
    public void testEditTypeRegleForm_Success() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleService.getTypeRegle(1)).thenReturn(typeRegle);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.editTypeRegleForm(1, model);

        // Assert
        assertEquals("typeRegles/TR_edit", response, "Expected view name is 'typeRegles/TR_edit'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
        assertEquals(typeRegle, model.getAttribute("typeRegle"), "Expected 'typeRegle' in model");
    }

    // Test pour afficher le formulaire de modification via Thymeleaf (Erreur)
    @Test
    public void testEditTypeRegleForm_NotFound() {
        // Arrange
        when(typeRegleService.getTypeRegle(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.editTypeRegleForm(1, model);

        // Assert
        assertEquals("typeRegles/error", response, "Expected view name is 'typeRegles/error'");
    }




    @Test
    public void testDeleteTypeRegle_Success() {
        // Arrange
        doNothing().when(typeRegleService).deleteTypeRegle(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.deleteTypeRegle(1, model);

        // Assert
        assertEquals("redirect:/typeRegles/TR_list", response, "Expected redirect to 'typeRegles/TR_list'");
        verify(typeRegleService, times(1)).deleteTypeRegle(1);
    }

    @Test
    public void testDeleteTypeRegle_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("TypeRegle not found")).when(typeRegleService).deleteTypeRegle(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.deleteTypeRegle(1, model);

        // Assert
        assertEquals("typeRegles/error", response, "Expected view name is 'typeRegles/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
        assertEquals("TypeRegle avec l'ID 1 non trouvé !", model.getAttribute("errorMessage"));
        verify(typeRegleService, times(1)).deleteTypeRegle(1);
    }


}
