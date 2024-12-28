package templates.typeRegles.controller;

import example.DTO.JourFerieDTO;
import example.controller.JourFerieController;
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
    public void testCreateJourFerieForm() {
        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieForm(model);

        Assertions.assertEquals("joursFeries/JF_create", response);
        Assertions.assertTrue(model.containsAttribute("jourFerie"));
        Assertions.assertNotNull(model.getAttribute("jourFerie"));
    }

}
