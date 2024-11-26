package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JourFerieControllerTest {

    @InjectMocks
    private JourFerieController jourFerieController;

    @Mock
    private IJourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test
    @Test
    public void testViewJFDateList_Success() {
        List<Date> expectedDates = List.of(new Date(), new Date());
        when(jourFerieService.getAllDateFerie()).thenReturn(expectedDates);

        Model model = new ConcurrentModel();
        String response = jourFerieController.viewJFDateList(model);

        assertEquals("jourFeries/JF_dates", response);
        assertTrue(model.containsAttribute("datesFerie"));
        assertEquals(expectedDates, model.getAttribute("datesFerie"));
        verify(jourFerieService, times(1)).getAllDateFerie();
    }

    // Test : Récupération de toutes les dates des jours fériés - API


    // Test : Afficher un jour férié par ID - Succès
    @Test
    public void testViewJFById_Success() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(1);
        when(jourFerieService.getJourFerie(1)).thenReturn(jourFerie);

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.viewJFById(1, model);

        // Assert
        assertEquals("jourFeries/JF_details", response, "La vue attendue est incorrecte.");
        assertTrue(model.containsAttribute("jourFerie"), "Le modèle doit contenir l'attribut 'jourFerie'.");
        assertEquals(jourFerie, model.getAttribute("jourFerie"), "L'attribut 'jourFerie' doit correspondre à l'objet simulé.");
        verify(jourFerieService, times(1)).getJourFerie(1);
    }



    // Test : Afficher un jour férié par ID - Non trouvé
    @Test
    public void testViewJFById_NotFound() {
        // Arrange
        when(jourFerieService.getJourFerie(1)).thenReturn(null);

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.viewJFById(1, model);

        // Assert
        assertEquals("jourFeries/error", response, "La vue attendue est incorrecte.");
        assertTrue(model.containsAttribute("errorMessage"), "Le modèle doit contenir un message d'erreur.");
        assertEquals("Jour férié non trouvé pour l'ID 1", model.getAttribute("errorMessage"), "Le message d'erreur est incorrect.");
        verify(jourFerieService, times(1)).getJourFerie(1);
    }



    // Test : Formulaire de création d'un jour férié
    @Test
    public void testCreateJourFerieForm() {
        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieForm(model);

        assertEquals("jourFeries/JF_create", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertNotNull(model.getAttribute("jourFerie"));
    }



    @Test
    public void testCreateJourFerieThymeleaf_DuplicateDate() {
        // Arrange
        Date existingDate = new Date();
        JourFerie jourFerie = JourFerie.builder().date(existingDate).build();
        when(jourFerieService.getAllDateFerie()).thenReturn(List.of(existingDate));

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.createJourFerieThymeleaf(jourFerie, model);

        // Assert : Vérifier les résultats
        assertEquals("jourFeries/JF_create", response, "La vue attendue est incorrecte.");
        assertTrue(model.containsAttribute("errorMessage"), "Le modèle doit contenir un message d'erreur.");
        assertEquals("Un jour férié avec cette date existe déjà.", model.getAttribute("errorMessage"), "Le message d'erreur est incorrect.");
        verify(jourFerieService, times(1)).getAllDateFerie();
        verify(jourFerieService, times(0)).persistJourFerie(jourFerie);
    }




    @Test
    public void testCreateJourFerieThymeleaf_Success() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder().date(new Date()).build();
        when(jourFerieService.getAllDateFerie()).thenReturn(List.of());
        doNothing().when(jourFerieService).persistJourFerie(jourFerie);

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.createJourFerieThymeleaf(jourFerie, model);

        // Assert
        assertEquals("redirect:/jourferies/list", response, "La redirection attendue est incorrecte.");
        verify(jourFerieService, times(1)).getAllDateFerie();
        verify(jourFerieService, times(1)).persistJourFerie(jourFerie);
    }

}

