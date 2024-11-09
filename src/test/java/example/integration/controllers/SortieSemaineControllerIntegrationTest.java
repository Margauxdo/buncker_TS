package example.integration.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import example.entities.Regle;
import example.entities.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class SortieSemaineControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private ObjectMapper objectMapper;




    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
        regleRepository.deleteAll();

        objectMapper.getFactory().setStreamWriteConstraints(
                StreamWriteConstraints.builder().maxNestingDepth(100).build()
        );

        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }



    @Test
    public void testGetSortieSemaineById_NotFound() throws Exception {
        mvc.perform(get("/api/sortieSemaine/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testCreateSortieSemaine_Success() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("CodeExemple");
        regle = regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);

        mvc.perform(post("/api/sortieSemaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sortieSemaine)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }



    @Test
    public void testCreateSortieSemaine_InvalidData() throws Exception {
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(null);

        mvc.perform(post("/api/sortieSemaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sortieSemaine)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteSortieSemaine_Success() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("CodeExemple");
        regle = regleRepository.save(regle);
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        SortieSemaine savedSortie = sortieSemaineRepository.save(sortieSemaine);

        mvc.perform(delete("/api/sortieSemaine/" + savedSortie.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testDeleteSortieSemaine_NotFound() throws Exception {
        mvc.perform(delete("/api/sortieSemaine/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllSortieSemaine_Success() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("CodeExemple");
        regle = regleRepository.save(regle);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        sortieSemaineRepository.save(sortieSemaine);
        // Act & Assert
        mvc.perform(get("/api/sortieSemaine")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }



}
