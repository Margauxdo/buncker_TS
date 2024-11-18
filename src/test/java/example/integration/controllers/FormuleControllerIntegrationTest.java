package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Formule;
import example.entities.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class FormuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();
    }

    @Test
    public void testGetAllFormules_shouldReturnEmptyList() throws Exception {
        // Act
        mockMvc.perform(get("/api/formules")
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetFormuleById_shouldReturnNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/api/formules/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFormule_shouldReturnCreatedFormule() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Sortie spéciale");
        regle.setDateRegle(new Date());
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("Libelle Test")
                .formule("Formule Test")
                .regle(regle)
                .build();

        // Act
        mockMvc.perform(post("/api/formules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formule)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libelle", is("Libelle Test")))
                .andExpect(jsonPath("$.formule", is("Formule Test")))
                .andExpect(jsonPath("$.regle.coderegle", is("R001")));
    }



    @Test
    public void testCreateFormule_shouldReturnBadRequestForInvalidData() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .formule("Formule Test sans libelle")
                .build();

        // Act et Assert
        mockMvc.perform(post("/api/formules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formule)))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testUpdateFormule_shouldUpdateFormuleWhenFormuleExists() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Libelle Initial")
                .formule("Formule Initiale")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        Formule updatedFormule = Formule.builder()
                .libelle("Libelle Mis à Jour")
                .formule("Formule Mise à Jour")
                .build();

        // Act
        mockMvc.perform(put("/api/formules/{id}", savedFormule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFormule)))
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle", is("Libelle Mis à Jour")))
                .andExpect(jsonPath("$.formule", is("Formule Mise à Jour")));
    }

    @Test
    public void testUpdateFormule_shouldReturnNotFoundWhenFormuleDoesNotExist() throws Exception {
        // Arrange
        Formule updatedFormule = Formule.builder()
                .libelle("Libelle Inexistant")
                .formule("Formule Inexistante")
                .build();

        // Act
        mockMvc.perform(put("/api/formules/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFormule)))
        // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFormule_shouldReturnNoContentWhenFormuleExists() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Libelle à Supprimer")
                .formule("Formule à Supprimer")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        // Act
        mockMvc.perform(delete("/api/formules/{id}", savedFormule.getId())
             .contentType(MediaType.APPLICATION_JSON))
        // Assert
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFormule_shouldReturnNotFoundWhenFormuleDoesNotExist() throws Exception {
        // Act
        mockMvc.perform(delete("/api/formules/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
           .andExpect(status().isNotFound());
    }
}
