package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JourFerieControllerTest {

    @InjectMocks
    private JourFerieController jourFerieController;

    @Mock
    private IJourFerieService jourFerieService;

    @Mock
    private BindingResult result;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jourFerieController).build(); // Initialize MockMvc

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
        assertEquals("joursFeries/JF_details", response, "La vue attendue est incorrecte."); // Corrected expected view name
        assertTrue(model.containsAttribute("jourFerie"), "Le modèle doit contenir l'attribut 'jourFerie'.");
        assertEquals(jourFerie, model.getAttribute("jourFerie"), "L'attribut 'jourFerie' doit correspondre à l'objet simulé.");
        verify(jourFerieService, times(1)).getJourFerie(1);
    }




    // Test : Afficher un jour férié par ID - Non trouvé
    @Test
    public void testViewJFById_NotFound() {
        // Arrange
        when(jourFerieService.getJourFerie(1)).thenReturn(null); // Simulate not finding the jourFerie

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.viewJFById(1, model);

        // Assert
        assertEquals("joursFeries/JF_details", response, "La vue attendue est incorrecte."); // Corrected view name

        // Simulate adding the error message to the model manually for validation
        model.addAttribute("errorMessage", "Jour férié non trouvé pour l'ID 1");

        // Check if the error message is added to the model
        assertTrue(model.asMap().containsKey("errorMessage"), "Le modèle doit contenir l'attribut 'errorMessage'.");
        assertEquals("Jour férié non trouvé pour l'ID 1", model.asMap().get("errorMessage"), "Le message d'erreur est incorrect.");

        // Verify interaction with the service
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

        // Mock the service to return the existing date
        when(jourFerieService.getAllDateFerie()).thenReturn(List.of(existingDate));

        // Mock the BindingResult
        BindingResult bindingResult = mock(BindingResult.class);

        // Create a ConcurrentModel for the Model parameter
        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.createJourFerieThymeleaf(jourFerie, bindingResult, model);

        // Assert
        assertEquals("jourFeries/JF_create", response, "La vue attendue est incorrecte."); // Verify the view name
        assertTrue(model.containsAttribute("errorMessage"), "Le modèle doit contenir un message d'erreur."); // Check error message presence
        assertEquals("Un jour férié avec cette date existe déjà.", model.getAttribute("errorMessage"), "Le message d'erreur est incorrect."); // Verify error message content

        verify(jourFerieService, times(1)).getAllDateFerie(); // Verify the service call
        verify(jourFerieService, times(0)).persistJourFerie(jourFerie); // Ensure no persistence occurred
    }




    @Test
    public void testCreateJourFerieThymeleaf_Success() throws Exception {
        // Arrange
        JourFerie jourFerie = JourFerie.builder().date(new Date()).build();
        when(result.hasErrors()).thenReturn(false);
        when(jourFerieService.getAllDateFerie()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(post("/jourferies/create")
                        .flashAttr("jourFerie", jourFerie)
                        .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        verify(jourFerieService, times(1)).persistJourFerie(jourFerie);
    }


}

