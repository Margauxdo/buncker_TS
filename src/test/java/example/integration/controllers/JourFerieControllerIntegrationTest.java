package example.integration.controllers;

import example.entity.JourFerie;
import example.repositories.JourFerieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class JourFerieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
    }

    // Test: View list of public holidays
    @Test
    public void testViewJFDateList() throws Exception {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();
        jourFerieRepository.save(jourFerie);

        // Act & Assert
        mockMvc.perform(get("/jourferies/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("jourFeries/JF_dates"))
                .andExpect(model().attributeExists("datesFerie"))
                .andExpect(model().attribute("datesFerie", hasSize(1)))
                .andExpect(model().attribute("datesFerie", hasItem(jourFerie.getDate())));
    }

    // Test: View a public holiday by ID
    @Test
    public void testViewJFById() throws Exception {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();
        jourFerie = jourFerieRepository.save(jourFerie);

        // Act & Assert
        mockMvc.perform(get("/jourferies/view/{id}", jourFerie.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("jourFeries/JF_details"))
                .andExpect(model().attributeExists("jourFerie"))
                .andExpect(model().attribute("jourFerie", hasProperty("id", is(jourFerie.getId()))))
                .andExpect(model().attribute("jourFerie", hasProperty("date", is(jourFerie.getDate()))));
    }

    // Test: Create public holiday form
    @Test
    public void testCreateJourFerieForm() throws Exception {
        mockMvc.perform(get("/jourferies/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("jourFeries/JF_create"))
                .andExpect(model().attributeExists("jourFerie"));
    }

    // Test: Create a public holiday via form
    @Test
    public void testCreateJourFerieThymeleaf() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/jourferies/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2024-01-01")) // Match the format in @DateTimeFormat
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        // Validate the saved holiday
        JourFerie savedJourFerie = jourFerieRepository.findAll().get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2024-01-01", sdf.format(savedJourFerie.getDate()));
    }


    // Test: Create a public holiday with duplicate date
    @Test
    public void testCreateJourFerieDuplicateDate() throws Exception {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
                .build();
        jourFerieRepository.save(jourFerie);

        // Act & Assert
        mockMvc.perform(post("/jourferies/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("jourFeries/JF_create"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", is("Un jour férié avec cette date existe déjà.")));
    }
}

