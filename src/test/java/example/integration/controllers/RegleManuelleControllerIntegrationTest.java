package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class RegleManuelleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        regleManuelleRepository.deleteAll();
    }

    @Test
    public void testGetAllRegleManuelles_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/regle-manuelle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetRegleManuelleById_ShouldReturnRegleManuelle() throws Exception {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("henri bernard");
        regleManuelle.setDescriptionRegle("Description de test");
        regleManuelle.setCoderegle("CODE123");

        regleManuelle = regleManuelleRepository.save(regleManuelle);

        mockMvc.perform(get("/regle-manuelle/{id}", regleManuelle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createurRegle", is("henri bernard")))
                .andExpect(jsonPath("$.descriptionRegle", is("Description de test")))
                .andExpect(jsonPath("$.coderegle", is("CODE123")));
    }


    @Test
    public void testGetRegleManuelleById_NotFound() throws Exception {
        mockMvc.perform(get("/regle-manuelle/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRegleManuelle_ShouldCreateAndReturnNewRegleManuelle() throws Exception {
        // Création d'une instance avec les champs requis
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("henri bernard");
        regleManuelle.setDescriptionRegle("Description de test");
        regleManuelle.setCoderegle("CODE123");

        mockMvc.perform(post("/regle-manuelle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regleManuelle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createurRegle", is("henri bernard")))
                .andExpect(jsonPath("$.descriptionRegle", is("Description de test")))
                .andExpect(jsonPath("$.coderegle", is("CODE123")));
    }







    @Test
    public void testUpdateRegleManuelle_ShouldUpdateAndReturnRegleManuelle() throws Exception {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("henri bernard");
        regleManuelle.setDescriptionRegle("Description de test");
        regleManuelle.setCoderegle("CODE123");

        regleManuelle = regleManuelleRepository.save(regleManuelle);

        regleManuelle.setCreateurRegle("julien Renard");

        mockMvc.perform(put("/regle-manuelle/{id}", regleManuelle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regleManuelle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createurRegle", is("julien Renard")));
    }


    @Test
    public void testUpdateRegleManuelle_NotFound() throws Exception {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("henri bernard");
        regleManuelle.setDescriptionRegle("Description de test");
        regleManuelle.setCoderegle("CODE123");

        mockMvc.perform(put("/regle-manuelle/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regleManuelle)))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testDeleteRegleManuelle_ShouldReturnNoContent() throws Exception {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("henri bernard");
        regleManuelle.setDescriptionRegle("Description de test");
        regleManuelle.setCoderegle("CODE123");

        regleManuelle = regleManuelleRepository.save(regleManuelle);

        mockMvc.perform(delete("/regle-manuelle/{id}", regleManuelle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testDeleteRegleManuelle_NotFound() throws Exception {
        mockMvc.perform(delete("/regle-manuelle/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
