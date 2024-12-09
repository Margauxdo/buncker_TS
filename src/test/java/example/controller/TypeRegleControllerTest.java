package example.controller;

import example.DTO.TypeRegleDTO;
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
        List<TypeRegleDTO> expectedTypeRegles = List.of(
                TypeRegleDTO.builder().id(1).nomTypeRegle("TypeRegle1").build(),
                TypeRegleDTO.builder().id(2).nomTypeRegle("TypeRegle2").build()
        );
        when(typeRegleService.getTypeRegles()).thenReturn(expectedTypeRegles);
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewAllTypeRegles(model);

        // Assert
        assertEquals("typeRegles/TR_list", response);
        assertTrue(model.containsAttribute("typeRegles"));
        assertEquals(expectedTypeRegles, model.getAttribute("typeRegles"));
        verify(typeRegleService, times(1)).getTypeRegles();
    }

    // **Test: Voir un TypeRegle par ID (Succès)**
    @Test
    public void testViewTypeRegleById_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder().id(1).nomTypeRegle("TypeRegle1").build();
        when(typeRegleService.getTypeRegle(1)).thenReturn(typeRegleDTO);
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewTypeRegleById(1, model);

        // Assert
        assertEquals("typeRegles/TP_details", response);
        assertTrue(model.containsAttribute("typeRegle"));
        assertEquals(typeRegleDTO, model.getAttribute("typeRegle"));
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
        assertEquals("typeRegles/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
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
        assertEquals("typeRegles/TP_create", response);
        assertTrue(model.containsAttribute("typeRegle"));
        assertNotNull(model.getAttribute("typeRegle"));
    }

    // **Test: Afficher le formulaire de modification (Succès)**
    @Test
    public void testEditTypeRegleForm_Success() {
        // Arrange
        TypeRegleDTO typeRegleDTO = TypeRegleDTO.builder().id(1).nomTypeRegle("TypeRegle1").build();
        when(typeRegleService.getTypeRegle(1)).thenReturn(typeRegleDTO);
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.editTypeRegleForm(1, model);

        // Assert
        assertEquals("typeRegles/TR_edit", response);
        assertTrue(model.containsAttribute("typeRegle"));
        assertEquals(typeRegleDTO, model.getAttribute("typeRegle"));
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
        assertEquals("typeRegles/error", response);
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
        assertEquals("redirect:/typeRegles/TR_list", response);
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
        assertEquals("typeRegles/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("TypeRegle avec l'ID 1 non trouvé !", model.getAttribute("errorMessage"));
        verify(typeRegleService, times(1)).deleteTypeRegle(1);
    }
}
