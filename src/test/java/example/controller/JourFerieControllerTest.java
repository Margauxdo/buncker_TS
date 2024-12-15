package example.controller;

import example.DTO.JourFerieDTO;
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

    @Test
    public void testViewJFById_Success() {
        // Arrange
        JourFerieDTO jourFerie = JourFerieDTO.builder().id(1).date(new Date()).build();
        when(jourFerieService.getJourFerie(1)).thenReturn(jourFerie);

        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.viewJFById(1, model);

        // Assert
        assertEquals("joursFeries/JF_details", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertEquals(jourFerie, model.getAttribute("jourFerie"));
        verify(jourFerieService, times(1)).getJourFerie(1);
    }

    @Test
    public void testCreateJourFerieForm() {
        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieForm(model);

        assertEquals("joursFeries/JF_create", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertNotNull(model.getAttribute("jourFerie"));
    }

    @Test
    public void testCreateJourFerieThymeleaf_DuplicateDate() {
        // Arrange
        Date existingDate = new Date();
        JourFerieDTO jourFerieDTO = JourFerieDTO.builder().date(existingDate).build();

        when(jourFerieService.getAllDateFerie()).thenReturn(List.of(existingDate));

        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new ConcurrentModel();

        // Act
        String response = jourFerieController.createJourFerie(jourFerieDTO, bindingResult, model);

        // Assert
        assertEquals("joursFeries/JF_create", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Un jour férié avec cette date existe déjà.", model.getAttribute("errorMessage"));

        verify(jourFerieService, times(1)).getAllDateFerie();
        verify(jourFerieService, times(0)).persistJourFerie(jourFerieDTO);
    }

    @Test
    public void testCreateJourFerieThymeleaf_Success() throws Exception {
        // Arrange
        JourFerieDTO jourFerieDTO = JourFerieDTO.builder().date(new Date()).build();
        when(result.hasErrors()).thenReturn(false);
        when(jourFerieService.getAllDateFerie()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(post("/jourferies/create")
                        .flashAttr("jourFerie", jourFerieDTO)
                        .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        verify(jourFerieService, times(1)).persistJourFerie(jourFerieDTO);
    }
}
