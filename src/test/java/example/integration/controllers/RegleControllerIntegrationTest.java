package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Regle;
import example.repositories.RegleRepository;
import example.services.RegleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class RegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegleRepository regleRepository;

    @SpyBean
    private RegleService regleService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        regleRepository.deleteAll();
    }
    @Test
    @Transactional
    public void testReadAllRegles_ShouldReturnListOfRegles() throws Exception {
        // Création et sauvegarde de la règle
        Regle regle1 = new Regle();
        regle1.setCoderegle("2568L");
        regle1.setDateRegle(new Date());
        regle1.setNombreJours(25);
        regleRepository.save(regle1);

        // Exécuter la requête
        mockMvc.perform(get("/api/regles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].coderegle").value("2568L"));
    }



    @Test
    @Transactional
    public void testReadRegleById_ShouldReturnRegleWhenExists() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        regleRepository.save(regle);

        mockMvc.perform(get("/api/regles/{id}", regle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coderegle").value("2568L"))
                .andExpect(jsonPath("$.nombreJours").value(25));
    }

    @Test
    public void testReadRegleById_ShouldReturnNotFoundWhenRegleDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/regles/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void testCreateRegle_ShouldCreateAndReturnNewRegle() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        String regleJson = objectMapper.writeValueAsString(regle);

        mockMvc.perform(post("/api/regles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.coderegle").value("2568L"))
                .andExpect(jsonPath("$.nombreJours").value(25));
    }

    @Test
    public void testCreateRegle_ShouldReturnConflictWhenDuplicateRegle() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        regleRepository.save(regle);
        String regleJson = objectMapper.writeValueAsString(regle);
        mockMvc.perform(post("/api/regles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(regleJson))
                .andExpect(status().isConflict());
    }
    @Test
    public void testCreateRegle_ShouldReturnBadRequestForInvalidData() throws Exception {
        String invalidRegleJson = "{\"coderegle\":\"\"}";

        mockMvc.perform(post("/api/regles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRegleJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.coderegle").value("Le code de la règle est obligatoire"));

    }









    @Test
    public void testUpdateRegle_ShouldUpdateAndReturnUpdatedRegle() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        regleRepository.save(regle);

        Regle updatedRegle = new Regle();
        updatedRegle.setCoderegle("5688L");
        updatedRegle.setDateRegle(new Date());
        updatedRegle.setNombreJours(32);

        String updatedRegleJson = objectMapper.writeValueAsString(updatedRegle);

        mockMvc.perform(put("/api/regles/{id}", regle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRegleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coderegle").value("5688L"))
                .andExpect(jsonPath("$.nombreJours").value(32));
    }

    @Test
    public void testUpdateRegle_ShouldReturnNotFoundWhenRegleDoesNotExist() throws Exception {
        Regle updatedRegle = new Regle();
        updatedRegle.setCoderegle("invalid");
        updatedRegle.setDateRegle(new Date());
        updatedRegle.setNombreJours(258);
        String updatedRegleJson = objectMapper.writeValueAsString(updatedRegle);

        mockMvc.perform(put("/api/regles/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRegleJson))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testUpdateRegle_ShouldReturnBadRequestForInvalidData() throws Exception {
        // Set up a valid entity in the database
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        regleRepository.save(regle);

        String invalidRegleJson = "{\"coderegle\":\"\"}";

        mockMvc.perform(put("/api/regles/{id}", regle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRegleJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.coderegle").value("Le code de la règle est obligatoire"));
    }


    @Test
    public void testDeleteRegle_ShouldSuccessfullyDeleteSpecifiedRegle() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("2568L");
        regle.setDateRegle(new Date());
        regle.setNombreJours(25);
        regleRepository.save(regle);
        mockMvc.perform(delete("/api/regles/{id}", regle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }



}

