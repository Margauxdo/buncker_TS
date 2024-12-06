package example.integration.controllers;

import example.controller.JourFerieController;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.JourFerieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class JourFerieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JourFerieRepository jourFerieRepository;
    @Autowired
    private JourFerieController jourFerieController;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
    }

    // Test: Show the create form
    @Test
    public void testCreateJourFerieForm() {
        Model model = new ConcurrentModel();
        String response = jourFerieController.createJourFerieForm(model);

        // Assertions
        assertEquals("jourFeries/JF_create", response);
        assertTrue(model.containsAttribute("jourFerie"));
        assertNotNull(model.getAttribute("jourFerie"));
    }


    @Test
    public void testCreateJourFerie() throws Exception {
        mockMvc.perform(post("/jourferies/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2024-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        JourFerie savedJourFerie = jourFerieRepository.findAll().get(0);
        assertEquals("2024-01-01", new SimpleDateFormat("yyyy-MM-dd").format(savedJourFerie.getDate()));
    }

    @Test
    public void testViewJourFerieList() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 24); // Utilisation d'un format valide
        JourFerie jf = JourFerie.builder()
                .date(new Date())
                .build();
        jourFerieRepository.save(jf);

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Act & Assert
        mockMvc.perform(get("/jourferies/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("joursFeries/JF_list"))

                .andExpect(model().attributeExists("jourFeries"))
                .andExpect(model().attribute("jourFeries", hasItem(
                        allOf(
                                hasProperty("id", is(jf.getId())),
                                hasProperty("date", is(jf.getDate()))
                        )
                )));

    }


    @Test
    public void testViewJourFerieDetails() throws Exception {
        // Préparation des données
        LocalDate date = LocalDate.of(2024, 12, 24);
        JourFerie jf = JourFerie.builder()
                .date(new Date())
                .build();
        JourFerie savedJf = jourFerieRepository.save(jf);

        mockMvc.perform(get("/jourferies/view/" + savedJf.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("joursFeries/JF_details"))
                .andExpect(model().attributeExists("jourFerie"));


    }




}
