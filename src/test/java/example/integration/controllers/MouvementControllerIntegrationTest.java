package example.integration.controllers;

import example.entity.*;
import example.interfaces.IMouvementService;
import example.repositories.*;

import example.services.MouvementService;
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
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class MouvementControllerIntegrationTest {


        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private MouvementRepository mouvementRepository;
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private ValiseRepository valiseRepository;

        private Mouvement mouvement;
    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private MouvementService mouvementService;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {

        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testCreateMouvement_Success() throws Exception {
        Client client = clientRepository.save(Client.builder().name("Doe").email("doe@test.com").build());
        Valise valise = valiseRepository.save(Valise.builder().description("Valise de test").numeroValise(123L).client(client).build());

        mockMvc.perform(post("/mouvements/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("statutSortie", "En cours")
                        .param("valise.id", String.valueOf(valise.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        List<Mouvement> mouvements = mouvementRepository.findAll();
        assertEquals(1, mouvements.size());
        assertEquals("En cours", mouvements.get(0).getStatutSortie());
    }



    // Test : Supprimer un mouvement - Succès
    @Test
    public void testDeleteMouvement_Success() throws Exception {
        Client client = clientRepository.save(Client.builder().name("Doe").email("doe@test.com").build());
        Valise valise = valiseRepository.save(
                Valise.builder().description("Valise de test").numeroValise(123L).client(client).build());
        Mouvement mouvement = mouvementRepository.save(Mouvement.builder().valise(valise).statutSortie("En cours").build());

        mockMvc.perform(post("/mouvements/delete/{id}", mouvement.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        assertFalse(mouvementRepository.existsById(mouvement.getId()));
    }


    @Test
    public void testUpdateMouvement() throws Exception {
        Client client = clientRepository.save(Client.builder().name("Doe").email("doe@test.com").build());
        Valise valise = valiseRepository.save(Valise.builder().description("Valise de test").numeroValise(123L).client(client).build());
        Mouvement mouvement = mouvementRepository.save(Mouvement.builder().valise(valise).statutSortie("En cours").build());

        mockMvc.perform(post("/mouvements/edit/" + mouvement.getId())
                        .param("statutSortie", "Terminé")
                        .param("valise.id", String.valueOf(valise.getId()))) // Transmettez l'ID de la valise
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"));

        Mouvement updatedMouvement = mouvementRepository.findById(mouvement.getId()).orElse(null);
        assertNotNull(updatedMouvement);
        assertEquals("Terminé", updatedMouvement.getStatutSortie());
    }


    @Test
    public void testListMouvement() throws Exception {

        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andDo(print());
    }

    @Test
    @Transactional
    public void testListMouvements_Success() throws Exception {
        // Arrange - Créer des entités nécessaires
        Client client = clientRepository.save(Client.builder().name("Doe").email("unique" + System.currentTimeMillis() + "@test.com").build());
        Valise valise1 = valiseRepository.save(Valise.builder().description("Valise 1").client(client).build());
        Valise valise2 = valiseRepository.save(Valise.builder().description("Valise 2").client(client).build());

        Mouvement mouvement1 = mouvementRepository.save(
                Mouvement.builder()
                        .valise(valise1)
                        .statutSortie("En cours")
                        .dateSortiePrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-07"))
                        .build()
        );

        Mouvement mouvement2 = mouvementRepository.save(
                Mouvement.builder()
                        .valise(valise2)
                        .statutSortie("Terminé")
                        .dateSortiePrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-08"))
                        .build()
        );

        // Act & Assert - Effectuer une requête GET et vérifier le résultat
        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andExpect(model().attribute("mouvements", hasSize(2)))
                .andExpect(model().attribute("mouvements", hasItem(
                        hasProperty("statutSortie", is("En cours"))
                )))
                .andExpect(model().attribute("mouvements", hasItem(
                        hasProperty("statutSortie", is("Terminé"))
                )))
                .andDo(print());
    }




}
