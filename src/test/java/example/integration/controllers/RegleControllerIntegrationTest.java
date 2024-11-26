package example.integration.controllers;

import example.entity.Regle;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegleRepository regleRepository;

    private Regle testRegle;

    @BeforeEach
    public void setUp() {
        // Initialisation d'une règle de test
        testRegle = Regle.builder()
                .coderegle("CODE123")
                .reglePourSortie("Règle de test")
                .dateRegle(new Date())
                .nombreJours(5)
                .calculCalendaire(2)
                .fermeJS1(false)
                .build();
        testRegle = regleRepository.save(testRegle);
    }

    @Test
    public void testViewAllRegles() throws Exception {
        mockMvc.perform(get("/regles/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_list"))
                .andExpect(model().attributeExists("regles"))
                .andExpect(model().attribute("regles", hasItem(hasProperty("coderegle", is("CODE123")))));
    }

    @Test
    public void testViewRegleById() throws Exception {
        mockMvc.perform(get("/regles/view/{id}", testRegle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_details"))
                .andExpect(model().attributeExists("regle"))
                .andExpect(model().attribute("regle", hasProperty("coderegle", is("CODE123"))));
    }

    @Test
    public void testCreateRegleForm() throws Exception {
        mockMvc.perform(get("/regles/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_create"))
                .andExpect(model().attributeExists("formule"));
    }

    @Test
    public void testCreateRegle() throws Exception {
        mockMvc.perform(post("/regles/create")
                        .param("coderegle", "NEWCODE")
                        .param("reglePourSortie", "Nouvelle règle")
                        .param("nombreJours", "10")
                        .param("calculCalendaire", "3")
                        .param("fermeJS1", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/regle_list"));

        Regle newRegle = regleRepository.findByCoderegle("NEWCODE");
        assert newRegle != null;
        assert newRegle.getReglePourSortie().equals("Nouvelle règle");
    }

    @Test
    public void testEditRegleForm() throws Exception {
        mockMvc.perform(get("/regles/edit/{id}", testRegle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_edit"))
                .andExpect(model().attributeExists("regle"))
                .andExpect(model().attribute("regle", hasProperty("coderegle", is("CODE123"))));
    }

    @Test
    public void testUpdateRegle() throws Exception {
        mockMvc.perform(post("/regles/edit/{id}", testRegle.getId())
                        .param("coderegle", "UPDATEDCODE")
                        .param("reglePourSortie", "Règle mise à jour")
                        .param("nombreJours", "15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/regle_list"));

        Regle updatedRegle = regleRepository.findById(testRegle.getId()).orElse(null);
        assert updatedRegle != null;
        assert updatedRegle.getCoderegle().equals("UPDATEDCODE");
        assert updatedRegle.getNombreJours() == 15;
    }

    @Test
    public void testDeleteRegle() throws Exception {
        mockMvc.perform(post("/regles/delete/{id}", testRegle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/regle_list"));

        assert regleRepository.findById(testRegle.getId()).isEmpty();
    }
}


