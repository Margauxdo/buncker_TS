package example.integration.controllers;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MouvementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
    }

    @Test
    public void testListAllMouvements() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andExpect(model().attribute("mouvements", hasSize(1)));
    }

    @Test
    public void testViewMouvementById() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/view/{id}", mouvement.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_details"))
                .andExpect(model().attributeExists("mouvement"))
                .andExpect(model().attribute("mouvement", hasProperty("statutSortie", is("Active"))));
    }

    @Test
    public void testCreateMouvementForm() throws Exception {
        mockMvc.perform(get("/mouvements/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_create"))
                .andExpect(model().attributeExists("mouvement"));
    }

    @Test
    public void testCreateMouvement() throws Exception {
        mockMvc.perform(post("/mouvements/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateHeureMouvement", "2024-11-26T10:00:00")
                        .param("statutSortie", "Active"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/mouv_list"));

        Mouvement savedMouvement = mouvementRepository.findAll().get(0);
        assert savedMouvement.getStatutSortie().equals("Active");
    }

    @Test
    public void testEditMouvementForm() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/edit/{id}", mouvement.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_edit"))
                .andExpect(model().attributeExists("mouvement"))
                .andExpect(model().attribute("mouvement", hasProperty("statutSortie", is("Active"))));
    }

    @Test
    public void testUpdateMouvement() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(post("/mouvements/edit/{id}", mouvement.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("statutSortie", "Inactive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/mouv_list"));

        Mouvement updatedMouvement = mouvementRepository.findById(mouvement.getId()).orElse(null);
        assert updatedMouvement != null;
        assert updatedMouvement.getStatutSortie().equals("Inactive");
    }

    @Test
    public void testDeleteMouvement() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(post("/mouvements/delete/{id}", mouvement.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/mouv_list"));

        assert mouvementRepository.findById(mouvement.getId()).isEmpty();
    }}
