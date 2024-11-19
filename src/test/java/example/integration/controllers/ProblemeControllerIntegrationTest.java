package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Probleme;
import example.repositories.ProblemeRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class ProblemeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProblemeRepository problemeRepository;
    @SpyBean
    private ProblemeService problemeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        problemeRepository.deleteAll();

    }


    @Test
    public void testUpdateProbleme_ShouldUpdateReturnSpecifiedProblem() throws Exception {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        Probleme updatedPb = new Probleme();
        updatedPb.setId(probleme.getId());
        updatedPb.setDetailsProbleme("updated details Probleme");
        updatedPb.setDescriptionProbleme("updated description probleme");
        String updatedPbJson = objectMapper.writeValueAsString(updatedPb);

        // Act & Assert
        mockMvc.perform(put("/api/pb/{id}", probleme.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPbJson))
                .andExpect(status().isOk())  // 200 code ok
                .andExpect(jsonPath("$.descriptionProbleme").value("updated description probleme"))
                .andExpect(jsonPath("$.detailsProbleme").value("updated details Probleme"));
    }


    @Test
    public void testCreateProbleme_shouldCreateAndReturnTheNewProblem() throws Exception {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("descriptionProbleme");
        probleme.setDetailsProbleme("detailsProbleme");
        String problemJSON = objectMapper.writeValueAsString(probleme);

        // Act & Assert
        mockMvc.perform(post("/api/pb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(problemJSON))
                .andExpect(status().isCreated())  //201 created
                .andExpect(jsonPath("$.descriptionProbleme").value("descriptionProbleme"));
    }

    @Test
    public void testCreateProbleme_shouldReturnBadRequestForInvalidProblemData() throws Exception {
        // Exemple de données avec un champ obligatoire manquant
        String problemJSON = "{\"descriptionProbleme\":\"descriptionProbleme\"}"; // detailsProbleme est manquant ici

        mockMvc.perform(post("/api/pb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(problemJSON))
                .andExpect(status().isBadRequest()); // S'attend à une réponse 400
    }




    @Test
    public void testGetAllProblems_ShouldReturnAllProblems() throws Exception {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        // Act & Assert
        mockMvc.perform(get("/api/pb")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descriptionProbleme").value("description probleme"))
                .andExpect(jsonPath("$[0].detailsProbleme").value("details Probleme"));
    }

    @Test
    public void testGetAllProblems_shouldReturnEmptyList() throws Exception {
        // Act & Assert:
        mockMvc.perform(get("/api/pb")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetProblemById_shouldReturnProblemWhenItExists() throws Exception {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        // Act & Assert
        mockMvc.perform(get("/api/pb/{id}", probleme.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expecting 200 OK status
                .andExpect(jsonPath("$.descriptionProbleme").value("description probleme"))
                .andExpect(jsonPath("$.detailsProbleme").value("details Probleme"));
    }

    @Test
    public void testGetProblemById_ShouldReturnIfTheProblemIDDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/pb/{id}",999999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testCreateProbleme_ShouldReturnCONFLICTDuplicateProblemCreated() throws Exception {
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");

        problemeRepository.save(probleme);

        String pbJson = objectMapper.writeValueAsString(probleme);

        mockMvc.perform(post("/api/pb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pbJson))
                .andExpect(status().isConflict());
    }




    @Test
    public void testUpdateProbleme_ShouldReturnNotFoundIfTheProblemIDDoesNotExist() throws Exception {
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("description probleme");
        probleme.setDetailsProbleme("details Probleme");

        String updatedPbJson = objectMapper.writeValueAsString(probleme);

        mockMvc.perform(put("/api/pb/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPbJson))
                .andExpect(status().isNotFound());  // Attendez un code de statut 404
    }



    @Test
    public void testDeleteProbleme_ShouldSuccessfullyDeleteTheSpecifiedProblem() throws Exception {
        // Arrange
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        probleme = problemeRepository.save(probleme);

        // Act & Assert
        mockMvc.perform(delete("/api/pb/{id}", probleme.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testDeleteProbleme_ShouldReturnNotFoundIfProblemIdDoesNotExist() throws Exception {
        // Test de suppression avec un ID qui n'existe pas
        mockMvc.perform(delete("/api/pb/{id}", 99999)  // ID invalide
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Vérifie que le code de réponse est 404
    }


}
