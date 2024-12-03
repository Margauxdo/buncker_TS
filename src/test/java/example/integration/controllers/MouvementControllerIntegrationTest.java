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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class MouvementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    void setUp() {
        mouvementRepository.deleteAll();
    }

    @Test
    public void testListAllMouvements() throws Exception {
        // Define a specific date and time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date specificDate = formatter.parse("2023-12-15 10:30:00");

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(specificDate)
                .statutSortie("Prévu")
                .build();
        mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andExpect(model().attribute("mouvements", hasSize(1)))
                .andExpect(model().attribute("mouvements", hasItem(
                        hasProperty("statutSortie", is("Prévu"))
                )));
    }


    @Test
    public void testViewMouvementById() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date specificDate = formatter.parse("2023-12-24 10:30:00");

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(specificDate)
                .statutSortie("Confirmé")
                .build();
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/view/{id}", savedMouvement.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_details"))
                .andExpect(model().attribute("mouvement", hasProperty("statutSortie", is("Confirmé"))));
    }

    @Test
    public void testCreateMouvementForm() throws Exception {
        mockMvc.perform(get("/mouvements/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_create"))
                .andExpect(model().attributeExists("mouvement"))
                .andExpect(model().attributeExists("valises"))
                .andExpect(model().attributeExists("allLivreurs"));
    }

    @Test
    public void testCreateMouvement() throws Exception {
        mockMvc.perform(post("/mouvements/create")
                        .param("statutSortie", "Créé")
                        .param("dateHeureMouvement", "2023-12-15 10:30:00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        List<Mouvement> mouvements = mouvementRepository.findAll();
        assertEquals(1, mouvements.size());
        assertEquals("Créé", mouvements.get(0).getStatutSortie());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-12-15 10:30:00"), mouvements.get(0).getDateHeureMouvement());
    }


    @Test
    public void testEditMouvementForm() throws Exception {
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-12-15 10:30:00"))
                .statutSortie("Initial")
                .build();
        mouvementRepository.save(mouvement);

        mockMvc.perform(get("/mouvements/edit/{id}", mouvement.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_edit"))
                .andExpect(model().attributeExists("mouvement"))
                .andExpect(model().attribute("mouvement", hasProperty("statutSortie", is("Initial"))));
    }


    @Test
    public void testUpdateMouvement() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date specificDate = formatter.parse("2024-12-15 10:30:00");

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(specificDate)
                .statutSortie("Prévu")
                .build();
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(post("/mouvements/edit/{id}", savedMouvement.getId())
                        .param("statutSortie", "Modifié"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        Mouvement updatedMouvement = mouvementRepository.findById(savedMouvement.getId()).orElseThrow();
        assertEquals("Modifié", updatedMouvement.getStatutSortie());
    }

    @Test
    public void testDeleteMouvement() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date specificDate = formatter.parse("2023-12-15 10:30:00");

        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(specificDate)
                .statutSortie("Supprimé")
                .build();
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        mockMvc.perform(post("/mouvements/delete/{id}", savedMouvement.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        assertTrue(mouvementRepository.findById(savedMouvement.getId()).isEmpty());
    }
}
