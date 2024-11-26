package example.integration.controllers;

import example.entity.TypeValise;
import example.repositories.TypeValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TypeValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    private TypeValise testTypeValise;

    @BeforeEach
    void setUp() {
        // Set up a sample TypeValise
        testTypeValise = TypeValise.builder()
                .proprietaire("Propriétaire Test")
                .description("Description Test")
                .build();
        typeValiseRepository.save(testTypeValise);
    }

    @Test
    void testViewTypeValises() throws Exception {
        mockMvc.perform(get("/typeValise/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_list"))
                .andExpect(model().attributeExists("typeValises"))
                .andExpect(model().attribute("typeValises", hasItem(
                        allOf(
                                hasProperty("proprietaire", is("Propriétaire Test")),
                                hasProperty("description", is("Description Test"))
                        )
                )));
    }

    @Test
    void testViewTypeValiseById_Success() throws Exception {
        mockMvc.perform(get("/typeValise/view/{id}", testTypeValise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_details"))
                .andExpect(model().attributeExists("typeValise"))
                .andExpect(model().attribute("typeValise", hasProperty("proprietaire", is("Propriétaire Test"))));
    }

    @Test
    void testViewTypeValiseById_NotFound() throws Exception {
        mockMvc.perform(get("/typeValise/view/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Type de valise avec l'Id999 non trouvé")));
    }

    @Test
    void testCreateTypeValiseForm() throws Exception {
        mockMvc.perform(get("/typeValise/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_create"))
                .andExpect(model().attributeExists("typeValise"));
    }

    @Test
    void testCreateTypeValise() throws Exception {
        mockMvc.perform(post("/typeValise/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("proprietaire", "New Propriétaire")
                        .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeValises/TV_list"));

        TypeValise createdTypeValise = typeValiseRepository.findAll().stream()
                .filter(tv -> "New Propriétaire".equals(tv.getProprietaire()))
                .findFirst()
                .orElse(null);

        assert createdTypeValise != null;
        assert "New Description".equals(createdTypeValise.getDescription());
    }

    @Test
    void testEditTypeValiseForm_Success() throws Exception {
        mockMvc.perform(get("/typeValise/edit/{id}", testTypeValise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_edit"))
                .andExpect(model().attributeExists("typeValise"))
                .andExpect(model().attribute("typeValise", hasProperty("proprietaire", is("Propriétaire Test"))));
    }

    @Test
    void testEditTypeValiseForm_NotFound() throws Exception {
        mockMvc.perform(get("/typeValise/edit/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Le type de valise avec l'ID 999 n'a pas été trouvé.")));
    }

    @Test
    void testEditTypeValise() throws Exception {
        mockMvc.perform(post("/typeValise/edit/{id}", testTypeValise.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("proprietaire", "Updated Propriétaire")
                        .param("description", "Updated Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeValises/TV_list"));

        TypeValise updatedTypeValise = typeValiseRepository.findById(testTypeValise.getId()).orElse(null);

        assert updatedTypeValise != null;
        assert "Updated Propriétaire".equals(updatedTypeValise.getProprietaire());
        assert "Updated Description".equals(updatedTypeValise.getDescription());
    }

    @Test
    void testDeleteTypeValise_Success() throws Exception {
        mockMvc.perform(post("/typeValise/delete/{id}", testTypeValise.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeValises/TV_list"));

        assert typeValiseRepository.findById(testTypeValise.getId()).isEmpty();
    }

    @Test
    void testDeleteTypeValise_NotFound() throws Exception {
        mockMvc.perform(post("/typeValise/delete/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("TypeValise avec l'ID 999 non trouvé !")));
    }
}

