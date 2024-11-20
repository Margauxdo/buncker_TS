package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.FormuleRepository;
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
    private FormuleRepository formuleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();
        formuleRepository.deleteAll();
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
        regle = regleRepository.save(regle);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setRegles(Collections.singletonList(regle)); // Correction ici
        jourFerie.setJoursFerieList(Collections.singletonList(new Date()));

        // Act & Assert
        mockMvc.perform(post("/api/jourferies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jourFerie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regles[0].coderegle", is("TestRuleCode")))
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
        // Act
        mockMvc.perform(get("/api/jourferies/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateHolidayWithFormule() throws Exception {
        // Arrange
        Formule formule = new Formule();
        formule.setFormule("Formule Test");
        formule = formuleRepository.save(formule);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList(Collections.singletonList(new Date()));
        jourFerie.setRegles(new ArrayList<>());
        jourFerieRepository.save(jourFerie);

        // Act & Assert
        mockMvc.perform(post("/api/jourferies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jourFerie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.formule.nom", is("Formule Test")));
    }
}
