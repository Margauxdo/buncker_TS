package example.controller;

import example.DTO.SortieSemaineDTO;
import example.interfaces.ISortieSemaineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SortieSemaineControllerTest {

    @InjectMocks
    private SortieSemaineController sortieSemaineController;

    @Mock
    private ISortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Lister toutes les semaines - Succès
    @Test
    public void testViewSortieSemaine_Success() {
        // Arrange
        List<SortieSemaineDTO> sortieSemaines = List.of(new SortieSemaineDTO());
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(sortieSemaines);
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.viewSortieSemaine(model);

        // Assert
        assertEquals("sortieSemaines/SS_list", response);
        assertTrue(model.containsAttribute("sortieSemaine"));
        assertEquals(sortieSemaines, model.getAttribute("sortieSemaine"));
        verify(sortieSemaineService, times(1)).getAllSortieSemaine();
    }

    // Test: Lister toutes les semaines - Liste vide
    @Test
    public void testViewSortieSemaine_EmptyList() {
        // Arrange
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(Collections.emptyList());
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.viewSortieSemaine(model);

        // Assert
        assertEquals("sortieSemaines/SS_list", response);
        assertTrue(model.containsAttribute("sortieSemaine"));
        assertTrue(((List<?>) model.getAttribute("sortieSemaine")).isEmpty());
    }

    // Test: Voir une semaine spécifique - Succès
    @Test
    public void testViewSortieSemaineById_Success() {
        // Arrange
        SortieSemaineDTO sortieSemaine = new SortieSemaineDTO();
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.viewSortieSemaineById(1, model);

        // Assert
        assertEquals("sortieSemaines/SS_details", response);
        assertTrue(model.containsAttribute("sortieSemaine"));
        assertEquals(sortieSemaine, model.getAttribute("sortieSemaine"));
        verify(sortieSemaineService, times(1)).getSortieSemaine(1);
    }

    // Test: Voir une semaine spécifique - Non trouvé
    @Test
    public void testViewSortieSemaineById_NotFound() {
        // Arrange
        int id = 1;
        when(sortieSemaineService.getSortieSemaine(id)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.viewSortieSemaineById(id, model);

        // Assert
        assertEquals("sortieSemaines/error", response);
        assertTrue(model.containsAttribute("errormessage"));
        assertEquals("SortieSemaine avec l'Id " + id + " non trouvée", model.getAttribute("errormessage"));
        verify(sortieSemaineService, times(1)).getSortieSemaine(id);
    }

    // Test: Formulaire de création d'une semaine
    @Test
    public void testCreateSortieSemaineForm() {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.createSortieSemaineForm(model);

        // Assert
        assertEquals("sortieSemaines/SS_create", response);
        assertTrue(model.containsAttribute("sortieSemaine"));
        assertNotNull(model.getAttribute("sortieSemaine"));
    }

    // Test: Créer une semaine - Succès
    @Test
    public void testCreateSortieSemaine_Success() {
        // Arrange
        SortieSemaineDTO sortieSemaineDTO = new SortieSemaineDTO();

        // Act
        String response = sortieSemaineController.createSortieSemaine(sortieSemaineDTO);

        // Assert
        assertEquals("redirect:/sortieSemaines/SS_list", response);
        verify(sortieSemaineService, times(1)).createSortieSemaine(sortieSemaineDTO);
    }

    // Test: Modifier une semaine - Succès
    @Test
    public void testEditSortieSemaineForm_Success() {
        // Arrange
        SortieSemaineDTO sortieSemaine = new SortieSemaineDTO();
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.editSortieSemaineForm(1, model);

        // Assert
        assertEquals("sortieSemaines/SS_edit", response);
        assertTrue(model.containsAttribute("sortieSemaine"));
        assertEquals(sortieSemaine, model.getAttribute("sortieSemaine"));
    }

    // Test: Supprimer une semaine - Succès
    @Test
    public void testDeleteSortieSemaine_Success() {
        // Arrange
        doNothing().when(sortieSemaineService).deleteSortieSemaine(1);

        // Act
        String response = sortieSemaineController.deleteSortieSemaine(1);

        // Assert
        assertEquals("redirect:/sortieSemaines/SS_list", response);
        verify(sortieSemaineService, times(1)).deleteSortieSemaine(1);
    }
}
