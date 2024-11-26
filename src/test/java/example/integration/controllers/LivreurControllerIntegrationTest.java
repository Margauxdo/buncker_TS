
package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        livreurRepository.deleteAll();

    }

    @Test
    public void testListLivreurs() throws Exception {
        Livreur livreur = Livreur.builder().nomLivreur("Test").prenomLivreur("User").build();
        livreurRepository.save(livreur);

        mockMvc.perform(get("/livreurs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_list"))
                .andExpect(model().attributeExists("livreurs"))
                .andExpect(model().attribute("livreurs", hasSize(1)))
                .andExpect(model().attribute("livreurs", hasItem(hasProperty("nomLivreur", is("Test")))));
    }


    @Test
    public void testViewLivreurById() throws Exception {
        Livreur livreur = Livreur.builder().nomLivreur("View Test").prenomLivreur("Test").build();
        livreur = livreurRepository.save(livreur);

        mockMvc.perform(get("/livreurs/view/{id}", livreur.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_details"))
                .andExpect(model().attributeExists("livreur"))
                .andExpect(model().attribute("livreur", hasProperty("nomLivreur", is("View Test"))));
    }

    @Test
    public void testCreateLivreurForm() throws Exception {
        mockMvc.perform(get("/livreurs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_create"))
                .andExpect(model().attributeExists("livreur"));
    }

    @Test
    public void testCreateLivreur() throws Exception {
        mockMvc.perform(post("/livreurs/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomLivreur", "New Livreur")
                        .param("prenomLivreur", "User"))
                .andExpect(status().is3xxRedirection()) // Expecting a redirection
                .andExpect(redirectedUrl("/livreurs/list"));

        // Verify the saved livreur
        Livreur savedLivreur = livreurRepository.findAll().get(0);
        assertNotNull(savedLivreur);
        assertEquals("New Livreur", savedLivreur.getNomLivreur());
        assertEquals("User", savedLivreur.getPrenomLivreur());
    }





    @Test
    public void testUpdateLivreur() throws Exception {
        // Create Mouvement and Livreur
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("Livreur Test")
                .prenomLivreur("Test")
                .mouvement(mouvement)
                .build();
        livreur = livreurRepository.save(livreur);

        // Perform update
        mockMvc.perform(post("/livreurs/edit/{id}", livreur.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nom", "Updated Livreur")
                        .param("prenom", "Updated Test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"));

        // Validate update
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).get();
        assert updatedLivreur.getNomLivreur().equals("Updated Livreur");
    }

    @Test
    public void testDeleteLivreur() throws Exception {
        // Create Mouvement and Livreur
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("Active")
                .build();
        mouvement = mouvementRepository.save(mouvement);

        Livreur livreur = Livreur.builder()
                .nomLivreur("Livreur Test")
                .prenomLivreur("Test")
                .mouvement(mouvement)
                .build();
        livreur = livreurRepository.save(livreur);

        // Perform delete
        mockMvc.perform(post("/livreurs/delete/{id}", livreur.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"));

        // Validate deletion
        assert livreurRepository.findById(livreur.getId()).isEmpty();
    }
}
