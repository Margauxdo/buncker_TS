package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Livreur;
import example.repositories.LivreurRepository;
import example.services.LivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class LivreurControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private LivreurRepository livreurRepository;
    @SpyBean
    private LivreurService livreurService;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() throws Exception {
        livreurRepository.deleteAll();
    }

    @Test
    public void testGetAllLivreurs_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetAllLivreurs_shouldReturnListOfLivreurs() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreurRepository.save(livreur);

        mockMvc.perform(get("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomLivreur").value("Doe"))
                .andExpect(jsonPath("$[0].codeLivreur").value("12345M"));
    }

    @Test
    public void testGetLivreurById_shouldReturnLivreur() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreur = livreurRepository.save(livreur);

        mockMvc.perform(get("/api/livreur/{id}", livreur.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomLivreur").value("Doe"))
                .andExpect(jsonPath("$.codeLivreur").value("12345M"));
    }

    @Test
    public void testGetLivreurById_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/livreur/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetLivreurById_shouldReturnBadRequestForInvalidId() throws Exception {
        mockMvc.perform(get("/api/livreur/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateLivreur_shouldReturnCreated() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setPrenomLivreur("John");
        livreur.setCodeLivreur("25658AW");
        livreur.setNumeroCartePro("125896");
        String livreurJson = objectMapper.writeValueAsString(livreur);

        mockMvc.perform(post("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livreurJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomLivreur").value("Doe"));
    }

    @Test
    public void testCreateLivreur_shouldReturnBadRequestForInvalidData() throws Exception {
        String invalidLivreurJson = "{\"nomLivreur\":\"\"}";

        mockMvc.perform(post("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLivreurJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateLivreur_shouldReturnConflictForDuplicateLivreur() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreurRepository.save(livreur);

        String livreurJson = objectMapper.writeValueAsString(livreur);

        mockMvc.perform(post("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livreurJson))
                .andExpect(status().isConflict());
    }


    @Test
    public void testCreateLivreur_shouldReturnServerErrorForException() throws Exception {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");

        doThrow(new RuntimeException("Simulated server error"))
                .when(livreurService).createLivreur(livreur);

        String livreurJson = objectMapper.writeValueAsString(livreur);

        // Act & Assert
        mockMvc.perform(post("/api/livreur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livreurJson))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testUpdateLivreur_shouldReturnUpdatedLivreur() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreur = livreurRepository.save(livreur);

        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setId(livreur.getId());
        updatedLivreur.setNomLivreur("Updated Doe");
        updatedLivreur.setCodeLivreur("54321M");
        String updatedLivreurJson = objectMapper.writeValueAsString(updatedLivreur);

        mockMvc.perform(put("/api/livreur/{id}", livreur.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLivreurJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomLivreur").value("Updated Doe"))
                .andExpect(jsonPath("$.codeLivreur").value("54321M"));
    }


    @Test
    public void testUpdateLivreur_shouldReturnNotFoundForNonExistentId() throws Exception {
        // Créer un Livreur avec des données fictives
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setNomLivreur("Non-existent");
        updatedLivreur.setCodeLivreur("99999M");
        String updatedLivreurJson = objectMapper.writeValueAsString(updatedLivreur);

        // Effectuer une requête PUT pour un ID inexistant
        mockMvc.perform(put("/api/livreur/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLivreurJson))
                .andExpect(status().isNotFound()); // Vérifie que la réponse est bien 404
    }




    @Test
    public void testUpdateLivreur_shouldReturnBadRequestForInvalidData() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreur = livreurRepository.save(livreur);

        String invalidLivreurJson = "{\"nomLivreur\":\"\"}";

        mockMvc.perform(put("/api/livreur/{id}", livreur.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLivreurJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteLivreur_shouldReturnNoContent() throws Exception {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Doe");
        livreur.setCodeLivreur("12345M");
        livreur = livreurRepository.save(livreur);

        mockMvc.perform(delete("/api/livreur/{id}", livreur.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteLivreur_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(delete("/api/livreur/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleteLivreur_shouldReturnServerErrorForException() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Simulated server error"))
                .when(livreurService).deleteLivreur(-1);

        // Act & Assert
        mockMvc.perform(delete("/api/livreur/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
