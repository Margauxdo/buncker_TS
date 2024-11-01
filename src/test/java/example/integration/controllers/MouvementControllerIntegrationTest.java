package example.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Mouvement;
import example.repositories.MouvementRepository;
import example.services.MouvementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class MouvementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        mouvementRepository.deleteAll();
    }
    @Test
    public void testGetAllMouvements_ShouldReturnAllMouvements() throws Exception {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        mouvementRepository.save(mouvement1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expectedDate = dateFormat.format(mouvement1.getDateHeureMouvement());

        String valuePattern = expectedDate + "(Z|\\+00:00)";

        mockMvc.perform(get("/api/mouvement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statutSortie").value("close"))
                .andExpect(jsonPath("$[0].dateHeureMouvement").value(org.hamcrest.Matchers.matchesPattern(valuePattern)));
    }






    @Test
    public void testGetMouvementById_ShouldReturnMouvement_WhenMouvementExists() throws Exception {
        // Arrange
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        mouvementRepository.save(mouvement1);

        // Format the date to match the response format (either `Z` or `+00:00`)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expectedDateRegex = dateFormat.format(mouvement1.getDateHeureMouvement()).replace("Z", "(Z|\\+00:00)");

        // Act & Assert
        mockMvc.perform(get("/api/mouvement/{id}", mouvement1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutSortie").value("close"))
                .andExpect(jsonPath("$.dateHeureMouvement").value(org.hamcrest.Matchers.matchesPattern(expectedDateRegex)));
    }





    @Test
    public void testGetMouvementById_ShouldReturnNotFound_WhenMouvementDoesNotExist () throws Exception {
        mockMvc.perform(get("/api/mouvement/{id}", 9999) // Correctly specifying the ID in the path
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMouvement_ShouldReturnCreated_WhenMouvementIsValid() throws Exception {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        String mouvementJson = objectMapper.writeValueAsString(mouvement1);

        mockMvc.perform(post("/api/mouvement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mouvementJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statutSortie").value("close"));
    }
    @Test
    public void testCreateMouvement_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        String invalidMouvementJson = "{\"statutSortie\":\"invalid\"}";
        mockMvc.perform(post("/api/mouvement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMouvementJson))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testUpdateMouvement_ShouldReturnUpdatedMouvement_WhenMouvementExists() throws Exception {
        // Arrange
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        mouvementRepository.save(mouvement1);

        ZonedDateTime updatedDate = ZonedDateTime.now(ZoneOffset.UTC);
        Mouvement updatedMouvement1 = new Mouvement();
        updatedMouvement1.setId(mouvement1.getId());
        updatedMouvement1.setStatutSortie("close");
        updatedMouvement1.setDateHeureMouvement(Date.from(updatedDate.toInstant()));
        String updatedMouvementJson = objectMapper.writeValueAsString(updatedMouvement1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expectedDateString = updatedDate.format(formatter);

        // Act & Assert
        mockMvc.perform(put("/api/mouvement/{id}", mouvement1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMouvementJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutSortie").value("close"))
                .andExpect(jsonPath("$.dateHeureMouvement").value(org.hamcrest.Matchers.startsWith(expectedDateString)));
    }








    @Test
    public void testUpdateMouvement_ShouldReturnNotFound_WhenMouvementDoesNotExist() throws Exception {
        Mouvement updatedMouvement1 = new Mouvement();
        updatedMouvement1.setStatutSortie("invalid");
        updatedMouvement1.setDateHeureMouvement(new Date());
        String updatedMouvementJson = objectMapper.writeValueAsString(updatedMouvement1);

        mockMvc.perform(put("/api/mouvement/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMouvementJson))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateMouvement_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        mouvementRepository.save(mouvement1);

        String invalidMouvementJson = "{\"statutSortie\":\"in\"}";
        mockMvc.perform(put("/api/mouvement/{id}", mouvement1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMouvementJson))
                .andExpect(status().isBadRequest());


    }
    @Test
    public void testDeleteMouvement_ShouldReturnNoContent_WhenMouvementExists() throws Exception {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("close");
        mouvement1.setDateHeureMouvement(new Date());
        mouvementRepository.save(mouvement1);
        mockMvc.perform(delete("/api/mouvement/{id}", mouvement1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testDeleteMouvement_ShouldReturnNotFound_WhenMouvementDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/mouvement/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMouvement_ShouldReturnInternalServerError_WhenExceptionThrown(){

    }














}
