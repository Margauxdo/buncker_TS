package example.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Probleme;
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
        // Arrange: Create and save an initial Probleme instance
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        // Prepare the updated probleme data
        Probleme updatedPb = new Probleme();
        updatedPb.setId(probleme.getId());
        updatedPb.setDetailsProbleme("updated details Probleme");
        updatedPb.setDescriptionProbleme("updated description probleme");
        String updatedPbJson = objectMapper.writeValueAsString(updatedPb);

        // Act & Assert: Perform PUT request on the correct endpoint
        mockMvc.perform(put("/api/pb/{id}", probleme.getId())  // Corrected endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPbJson))
                .andExpect(status().isOk())  // Expecting 200 OK status
                .andExpect(jsonPath("$.descriptionProbleme").value("updated description probleme"))
                .andExpect(jsonPath("$.detailsProbleme").value("updated details Probleme"));  // Corrected assertion
    }


    @Test
    public void testCreateProbleme_shouldCreateAndReturnTheNewProblem() throws Exception {
        // Arrange: Create a new probleme object and convert it to JSON
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme("descriptionProbleme");
        probleme.setDetailsProbleme("detailsProbleme");
        String problemJSON = objectMapper.writeValueAsString(probleme);

        // Act & Assert: Perform POST request on the correct endpoint
        mockMvc.perform(post("/api/pb")  // Corrected endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(problemJSON))
                .andExpect(status().isCreated())  // Expecting 201 CREATED
                .andExpect(jsonPath("$.descriptionProbleme").value("descriptionProbleme"));  // Verify the response content
    }

    @Test
    public void testCreateProbleme_shouldShouldReturnForInvalidProblemData() throws Exception {
        String problemJSON = "{\"descriptionProbleme\":\"descriptionProbleme\"}";
        mockMvc.perform(post("/api/mouvement")
        .contentType(MediaType.APPLICATION_JSON)
                .content(problemJSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testGetAllProblems_ShouldReturnAllProblems() throws Exception {
        // Arrange: Create and save a Probleme instance
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        // Act & Assert: Perform GET request on the correct endpoint
        mockMvc.perform(get("/api/pb")  // Corrected endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descriptionProbleme").value("description probleme"))
                .andExpect(jsonPath("$[0].detailsProbleme").value("details Probleme"));  // Corrected JSON path for the list
    }

    @Test
    public void testGetAllProblems_shouldReturnEmptyList() throws Exception {
        // Act & Assert: Perform GET request on the correct endpoint for an empty list
        mockMvc.perform(get("/api/pb")  // Corrected endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());  // Check if the returned list is empty
    }

    @Test
    public void testGetProblemById_shouldReturnProblemWhenItExists() throws Exception {
        // Arrange: Create and save a Probleme instance
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        problemeRepository.save(probleme);

        // Act & Assert: Perform GET request on the correct endpoint
        mockMvc.perform(get("/api/pb/{id}", probleme.getId())  // Corrected endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expecting 200 OK status
                .andExpect(jsonPath("$.descriptionProbleme").value("description probleme"))
                .andExpect(jsonPath("$.detailsProbleme").value("details Probleme"));  // Verifying the detailsProbleme
    }

    @Test
    public void testGetProblemById_ShouldReturnIfTheProblemIDDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/mouvement/{id}",9999)
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
    public void testupdateProbleme_ShouldReturnIfTheProblemID() throws Exception {
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        String updatedPbJson = objectMapper.writeValueAsString(probleme);
        mockMvc.perform(put("/api/mouvement/{id}", 99999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedPbJson))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeleteProbleme_ShouldSuccessfullyDeleteTheSpecifiedProblem() throws Exception {
        // Arrange: Create and save a probleme instance in the repository
        Probleme probleme = new Probleme();
        probleme.setDetailsProbleme("details Probleme");
        probleme.setDescriptionProbleme("description probleme");
        probleme = problemeRepository.save(probleme);

        // Act & Assert: Perform a DELETE request on the correct endpoint
        mockMvc.perform(delete("/api/pb/{id}", probleme.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Expect 204 NO_CONTENT on successful deletion
    }


    @Test
    public void testdeleteProblemee_ShouldReturnIfProblemIddoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/mouvement/{id}",99999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
