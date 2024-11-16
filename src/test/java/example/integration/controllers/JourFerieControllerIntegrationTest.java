package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.JourFerie;
import example.entities.Regle;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
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

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testGetAllHolidays_shouldReturnEmptyList() throws Exception {
        // Act
        mockMvc.perform(get("/api/jourferies")
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetHolidayById_shouldReturnNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/api/jourferies/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateHoliday_shouldReturnCreated() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("TestRuleCode");
        regleRepository.save(regle);

        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setRegle(regle);
        jourFerie.setJoursFerieList(Collections.singletonList(new Date()));

        // Act & Assert
        mockMvc.perform(post("/api/jourferies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jourFerie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regle.coderegle", is("TestRuleCode")))
                .andExpect(jsonPath("$.joursFerieList[0]").exists());
    }


    @Test
    public void testGetHolidayById_shouldReturnBadRequestForInvalidId() throws Exception {
        // Act
        mockMvc.perform(get("/api/jourferies/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetHolidayById_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/jourferies/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isNotFound());
    }




}
