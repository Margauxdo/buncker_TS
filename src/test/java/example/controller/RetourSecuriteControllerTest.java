package example.controller;

import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
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

public class RetourSecuriteControllerTest {

    @InjectMocks
    private RetourSecuriteController RScontroller;

    @Mock
    private IRetourSecuriteService retourSecuriteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test : Lister tous les retours sécurité - Succès
    @Test
    public void testViewAllRetourSecurites_Success() {
        // Arrange
        List<RetourSecurite> retourSecurites = List.of(new RetourSecurite());
        when(retourSecuriteService.getAllRetourSecurites()).thenReturn(retourSecurites);
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.viewAllRetourSecurites(model);

        // Assert
        assertEquals("retourSecurites/RS_list", response);
        assertTrue(model.containsAttribute("retourSecurites"));
        assertEquals(retourSecurites, model.getAttribute("retourSecurites"));
        verify(retourSecuriteService, times(1)).getAllRetourSecurites();
    }

    // Test : Voir un retour sécurité par ID - Succès
    @Test
    public void testViewRetourSecuriteById_Success() {
        // Arrange
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(1);
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(retourSecurite);
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.viewRetourSecuriteById(1, model);

        // Assert
        assertEquals("retourSecurites/RS_details", response);
        assertTrue(model.containsAttribute("retourSecurite"));
        assertEquals(retourSecurite, model.getAttribute("retourSecurite"));
        verify(retourSecuriteService, times(1)).getRetourSecurite(1);
    }

    // Test : Voir un retour sécurité par ID - Non trouvé
    @Test
    public void testViewRetourSecuriteById_NotFound() {
        // Arrange
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(null);
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.viewRetourSecuriteById(1, model);

        // Assert
        assertEquals("retourSecurites/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Retour securité avec l'ID1non trouve", model.getAttribute("errorMessage"));
        verify(retourSecuriteService, times(1)).getRetourSecurite(1);
    }

    // Test : Formulaire de création d'un retour sécurité
    @Test
    public void testCreateRetourSecuriteForm() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.createRetourSecuriteForm(model);

        // Assert
        assertEquals("retourSecurites/RS_create", response);
        assertTrue(model.containsAttribute("retourSecurite"));
        assertNotNull(model.getAttribute("retourSecurite"));
    }

    // Test : Formulaire de modification d'un retour sécurité - Succès
    @Test
    public void testEditRetourSecuriteForm_Success() {
        // Arrange
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(1);
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(retourSecurite);
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.editRetourSecuriteForm(1, model);

        // Assert
        assertEquals("retourSecurites/RS_edit", response);
        assertTrue(model.containsAttribute("retourSecurite"));
        assertEquals(retourSecurite, model.getAttribute("retourSecurite"));
        verify(retourSecuriteService, times(1)).getRetourSecurite(1);
    }

    // Test : Formulaire de modification d'un retour sécurité - Non trouvé
    @Test
    public void testEditRetourSecuriteForm_NotFound() {
        // Arrange
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(null);
        Model model = new ConcurrentModel();

        // Act
        String response = RScontroller.editRetourSecuriteForm(1, model);

        // Assert
        assertEquals("retourSecurites/error", response);
    }

    // Test : Supprimer un retour sécurité - Succès
    @Test
    public void testDeleteRetourSecurite_Success() {
        // Act
        String response = RScontroller.deleteRetourSecurite(1);

        // Assert
        assertEquals("redirect:/retourSecurites/RS_list", response);
        verify(retourSecuriteService, times(1)).deleteRetourSecurite(1);
    }

    // Test : Supprimer un retour sécurité - Non trouvé
    @Test
    public void testDeleteRetourSecurite_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("Safety return not found")).when(retourSecuriteService).deleteRetourSecurite(1);

        // Act
        String response = RScontroller.deleteRetourSecurite(1);

        // Assert
        assertEquals("redirect:/retourSecurites/RS_list", response);
        verify(retourSecuriteService, times(1)).deleteRetourSecurite(1);
    }
}
