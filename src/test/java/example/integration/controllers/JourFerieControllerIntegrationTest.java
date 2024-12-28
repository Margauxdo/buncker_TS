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
    public void testCreateJourFerie() throws Exception {
        mockMvc.perform(post("/jourferies/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2024-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        JourFerie savedJourFerie = jourFerieRepository.findAll().get(0);
        assertEquals("2024-01-01", new SimpleDateFormat("yyyy-MM-dd").format(savedJourFerie.getDate()));
    }




}
