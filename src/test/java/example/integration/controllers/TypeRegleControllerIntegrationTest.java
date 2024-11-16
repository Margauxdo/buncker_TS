package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.TypeRegle;
import example.repositories.TypeRegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class TypeRegleControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        typeRegleRepository.deleteAll();
    }

    @Test
    public void testCreateTypeRegle_Success() throws Exception {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type A");

        mvc.perform(post("/api/typeRegle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeRegle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomTypeRegle").value("Type A"));
    }


    @Test
    @Transactional
    public void testGetTypeRegle_Success() throws Exception {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type C");
        typeRegle = typeRegleRepository.save(typeRegle);

        mvc.perform(get("/api/typeRegle/" + typeRegle.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomTypeRegle").value("Type C"));
    }


    @Test
    public void testGetTypeRegle_NotFound() throws Exception {
        mvc.perform(get("/api/typeRegle/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTypeRegle_Success() throws Exception {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type D");
        typeRegle = typeRegleRepository.save(typeRegle);

        typeRegle.setNomTypeRegle("Type D Updated");

        mvc.perform(put("/api/typeRegle/" + typeRegle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeRegle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomTypeRegle").value("Type D Updated"));
    }

    @Test
    public void testUpdateTypeRegle_NotFound() throws Exception {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(999999);
        typeRegle.setNomTypeRegle("Type E");

        mvc.perform(put("/api/typeRegle/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeRegle)))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testDeleteTypeRegle_Success() throws Exception {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type F");
        typeRegle = typeRegleRepository.save(typeRegle);

        mvc.perform(delete("/api/typeRegle/" + typeRegle.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/typeRegle/" + typeRegle.getId()))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeleteTypeRegle_NotFound() throws Exception {
        mvc.perform(delete("/api/typeRegle/999"))
                .andExpect(status().isNotFound());
    }

}
