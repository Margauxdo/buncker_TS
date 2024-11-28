
package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
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
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    void setUp() {
        livreurRepository.deleteAll();

    }




    @Test
    public void testCreateLivreurForm() throws Exception {
        mockMvc.perform(get("/livreurs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_create"))
                .andExpect(model().attributeExists("livreur"));
    }

    @Test
    public void testListLivreurs() throws Exception {
        // Ajouter un livreur à la base de données
        Livreur livreur = Livreur.builder()
                .nomLivreur("Test")
                .prenomLivreur("User")
                .mouvement(null) // Gérer la contrainte si applicable
                .build();
        livreurRepository.save(livreur);

        // Vérification de la liste
        mockMvc.perform(get("/livreurs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_list"))
                .andExpect(model().attributeExists("livreurs"))
                .andExpect(model().attribute("livreurs", hasSize(1)))
                .andExpect(model().attribute("livreurs", hasItem(hasProperty("nomLivreur", is("Test")))));
    }

    @Test
    public void testViewLivreurById() throws Exception {
        // Ajouter un livreur à la base de données
        Livreur livreur = Livreur.builder()
                .nomLivreur("Test")
                .prenomLivreur("User")
                .mouvement(null)
                .build();
        livreur = livreurRepository.save(livreur);

        // Vérification de la vue par ID
        mockMvc.perform(get("/livreurs/view/" + livreur.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_details"))
                .andExpect(model().attributeExists("livreur"))
                .andExpect(model().attribute("livreur", hasProperty("nomLivreur", is("Test"))));
    }

    @Test
    public void testCreateLivreur() throws Exception {
        mockMvc.perform(post("/livreurs/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomLivreur", "New Livreur")
                        .param("prenomLivreur", "User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"));

        // Vérification de l'enregistrement dans la base
        Livreur savedLivreur = livreurRepository.findAll().get(0);
        assertNotNull(savedLivreur);
        assertEquals("New Livreur", savedLivreur.getNomLivreur());
    }

    @Test
    public void testEditLivreur() throws Exception {
        // Ajouter un livreur à la base de données
        Livreur livreur = Livreur.builder()
                .nomLivreur("Test")
                .prenomLivreur("User")
                .mouvement(null)
                .build();
        livreur = livreurRepository.save(livreur);

        // Modification du livreur
        mockMvc.perform(post("/livreurs/edit/" + livreur.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomLivreur", "Updated Livreur")
                        .param("prenomLivreur", "Updated User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"));

        // Vérification de la mise à jour
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).orElse(null);
        assertNotNull(updatedLivreur);
        assertEquals("Updated Livreur", updatedLivreur.getNomLivreur());
    }

    @Test
    public void testDeleteLivreur() throws Exception {
        // Ajouter un livreur à la base de données
        Livreur livreur = Livreur.builder()
                .nomLivreur("Test")
                .prenomLivreur("User")
                .mouvement(null)
                .build();
        livreur = livreurRepository.save(livreur);

        // Supprimer le livreur
        mockMvc.perform(post("/livreurs/delete/" + livreur.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"));

        // Vérification de la suppression
        assertEquals(0, livreurRepository.count());
    }




}
