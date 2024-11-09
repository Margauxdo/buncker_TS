package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.TypeValise;
import example.repositories.TypeValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class TypeValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        typeValiseRepository.deleteAll();
    }


    @Test
    public void testUpdateTypeValise_NotFound() throws Exception {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Jean Veltin");
        typeValise.setDescription("Valise Standard");

        mvc.perform(put("/api/typeValise/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeValise)))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testDeleteTypeValise_Success() throws Exception {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Valise Standard");
        typeValise.setProprietaire("John Doe");

        typeValise = typeValiseRepository.save(typeValise);

        mvc.perform(delete("/api/typeValise/{id}", typeValise.getId()))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testDeleteTypeValise_NotFound() throws Exception {
        mvc.perform(delete("/api/typeValise/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTypeValise_Success() throws Exception {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Valise de Voyage");
        typeValise.setProprietaire("Jane Doe");

        mvc.perform(post("/api/typeValise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeValise)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetTypeValise_Success() throws Exception {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Valise de Luxe");
        typeValise.setProprietaire("Alex Smith");
        typeValise = typeValiseRepository.save(typeValise);

        mvc.perform(get("/api/typeValise/{id}", typeValise.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTypeValise_Success() throws Exception {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Valise Standard");
        typeValise.setProprietaire("John Doe");
        typeValise = typeValiseRepository.save(typeValise);

        typeValise.setDescription("Valise Standard Mise à Jour");

        mvc.perform(put("/api/typeValise/{id}", typeValise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeValise)))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetTypeValise_NotFound() throws Exception {
        mvc.perform(get("/api/typeValise/{id}", 999999))
                .andExpect(status().isNotFound());
    }

}
