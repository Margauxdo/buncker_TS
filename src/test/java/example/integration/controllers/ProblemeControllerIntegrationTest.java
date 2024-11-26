package example.integration.controllers;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ProblemeRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProblemeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProblemeRepository problemeRepository;

    @Autowired
    private ProblemeService problemeService;

    private Probleme existingProbleme;

    @BeforeEach
    public void setUp() {
        // Create mock data
        Valise valise = Valise.builder().build();
        Client client = Client.builder().build();
        existingProbleme = Probleme.builder()
                .descriptionProbleme("Mock Description")
                .detailsProbleme("Mock Details")
                .valise(valise)
                .client(client)
                .build();
        problemeRepository.save(existingProbleme);
    }

    @Test
    public void testViewAllProblemes() throws Exception {
        mockMvc.perform(get("/pb/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_list"))
                .andExpect(model().attributeExists("problemes"))
                .andExpect(model().attribute("problemes", hasItem(
                        hasProperty("descriptionProbleme", is(existingProbleme.getDescriptionProbleme()))
                )));
    }

    @Test
    public void testViewProbleme() throws Exception {
        mockMvc.perform(get("/pb/view/{id}", existingProbleme.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_edit"))
                .andExpect(model().attributeExists("probleme"))
                .andExpect(model().attribute("probleme", hasProperty("descriptionProbleme", is(existingProbleme.getDescriptionProbleme()))));
    }

    @Test
    public void testViewProblemeNotFound() throws Exception {
        mockMvc.perform(get("/pb/view/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("non trouve")));
    }

    @Test
    public void testCreateProblemeForm() throws Exception {
        mockMvc.perform(get("/pb/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_create"))
                .andExpect(model().attributeExists("probleme"));
    }

    @Test
    public void testCreateProbleme() throws Exception {
        mockMvc.perform(post("/pb/create")
                        .param("descriptionProbleme", "New Description")
                        .param("detailsProbleme", "New Details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problemes/pb_list"));

        Optional<Probleme> savedProbleme = problemeRepository.findAll().stream()
                .filter(pb -> "New Description".equals(pb.getDescriptionProbleme()))
                .findFirst();
        assert savedProbleme.isPresent();
    }

    @Test
    public void testEditProblemeForm() throws Exception {
        mockMvc.perform(get("/pb/edit/{id}", existingProbleme.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_edit"))
                .andExpect(model().attributeExists("probleme"))
                .andExpect(model().attribute("probleme", hasProperty("descriptionProbleme", is(existingProbleme.getDescriptionProbleme()))));
    }

    @Test
    public void testEditProbleme() throws Exception {
        mockMvc.perform(post("/pb/edit/{id}", existingProbleme.getId())
                        .param("descriptionProbleme", "Updated Description")
                        .param("detailsProbleme", "Updated Details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problemes/pb_list"));

        Optional<Probleme> updatedProbleme = problemeRepository.findById(existingProbleme.getId());
        assert updatedProbleme.isPresent();
        assert "Updated Description".equals(updatedProbleme.get().getDescriptionProbleme());
    }

    @Test
    public void testDeleteProbleme() throws Exception {
        mockMvc.perform(post("/pb/delete/{id}", existingProbleme.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problemes/pb_list"));

        Optional<Probleme> deletedProbleme = problemeRepository.findById(existingProbleme.getId());
        assert deletedProbleme.isEmpty();
    }
}
