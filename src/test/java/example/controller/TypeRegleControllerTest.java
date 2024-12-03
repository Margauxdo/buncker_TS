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

import java.util.List;
import java.util.Optional;

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

    // **Test: Lister tous les TypeRegles (Thymeleaf)**
    @Test
    public void testViewAllTypeRegles_Success() {
        // Arrange
        List<TypeRegle> expectedTypeRegles = List.of(
                new TypeRegle(1, "TypeRegle1", null),
                new TypeRegle(2, "TypeRegle2", null)
        );
        when(typeRegleService.getTypeRegles()).thenReturn(expectedTypeRegles);
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewAllTypeRegles(model);

        // Assert
        assertEquals("typeRegles/TR_list", response, "Expected view name is 'typeRegles/TR_list'");
        assertTrue(model.containsAttribute("typeRegles"), "Model should contain 'typeRegles' attribute");
        assertEquals(expectedTypeRegles, model.getAttribute("typeRegles"), "Expected 'typeRegles' to match the returned list");
        verify(typeRegleService, times(1)).getTypeRegles();
    }


    // **Test: Voir un TypeRegle par ID (Succès)**
    @Test
    public void testViewTypeRegleById_Success() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(1); // Ajout d'un ID pour valider le contenu
        when(typeRegleService.getTypeRegle(1)).thenReturn(Optional.of(typeRegle));
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewTypeRegleById(1, model);

        // Assert
        assertEquals("typeRegles/TP_details", response, "Expected view name is 'typeRegles/TP_details'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
        assertEquals(Optional.of(typeRegle), model.getAttribute("typeRegle"), "Expected 'typeRegle' in model as Optional");
    }


    // **Test: Voir un TypeRegle par ID (Non trouvé)**
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
        assertEquals("typeRegle avec l'Id1 non trouve !", model.getAttribute("errorMessage"));
    }

    // **Test: Afficher le formulaire de création (Thymeleaf)**
    @Test
    public void testCreateTypeRegleForm_Success() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.createTypeRegleForm(model);

        // Assert
        assertEquals("typeRegles/TP_create", response, "Expected view name is 'typeRegles/TP_create'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
        assertNotNull(model.getAttribute("typeRegle"), "Expected 'typeRegle' in model");
    }

    // **Test: Afficher le formulaire de modification (Succès)**
    @Test
    public void testEditTypeRegleForm_Success() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(1); // Ajout d'un ID pour valider le contenu
        when(typeRegleService.getTypeRegle(1)).thenReturn(Optional.of(typeRegle));
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.editTypeRegleForm(1, model);

        // Assert
        assertEquals("typeRegles/TR_edit", response, "Expected view name is 'typeRegles/TR_edit'");
        assertTrue(model.containsAttribute("typeRegle"), "Model should contain 'typeRegle' attribute");
        assertEquals(Optional.of(typeRegle), model.getAttribute("typeRegle"), "Expected 'typeRegle' in model as Optional");
    }

    // **Test: Afficher le formulaire de modification (Non trouvé)**
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

    // **Test: Supprimer un TypeRegle (Succès)**
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

    // **Test: Supprimer un TypeRegle (Non trouvé)**
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
