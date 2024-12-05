package example.integration.controllers;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.ClientRepository;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private ValiseRepository valiseRepository;

        private Mouvement mouvement;
    @Autowired
    private LivreurRepository livreurRepository;

    @BeforeEach
        public void setUp() {
            mouvementRepository.deleteAll();
            clientRepository.deleteAll();
            valiseRepository.deleteAll();
        }

        // Test : Lister tous les mouvements
        @Test
        public void testListAllMouvements() throws Exception {
            Client client = clientRepository.save(Client.builder().name("Doe").email("doe@test.com").build());
            Valise valise = valiseRepository.save(
                    Valise.builder().description("Valise de test").numeroValise(123L).client(client).build());
            mouvementRepository.save(Mouvement.builder().valise(valise).statutSortie("En cours").build());

            mockMvc.perform(get("/mouvements/list"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("mouvements/mouv_list"))
                    .andExpect(model().attributeExists("mouvements"))
                    .andExpect(model().attribute("mouvements", hasSize(greaterThanOrEqualTo(1))));
        }

    // Test : Voir un mouvement par ID - Succès
    @Test
    public void testViewMouvementById_Success() throws Exception {
        Client client = clientRepository.save(Client.builder().name("Doe").email("doe@test.com").build());
        Valise valise = valiseRepository.save(
                Valise.builder().description("Valise de test").numeroValise(123L).client(client).build());
        Mouvement mouvement = mouvementRepository.save(Mouvement.builder().valise(valise).statutSortie("En cours").build());

        mockMvc.perform(get("/mouvements/view/{id}", mouvement.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_details"))
                .andExpect(model().attributeExists("mouvement"))
                .andExpect(model().attribute("mouvement", hasProperty("id", is(mouvement.getId()))));
    }

    // Test : Voir un mouvement par ID - Non trouvé
        @Test
        public void testViewMouvementById_NotFound() throws Exception {
            mockMvc.perform(get("/mouvements/view/{id}", 9999))
                    .andExpect(status().isOk())
                    .andExpect(view().name("mouvements/error"))
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", containsString("non trouvé")));
        }

        // Test : Créer


    // Test : Modifier un mouvement - Succès
        @Test
        public void testUpdateMouvement_Success() throws Exception {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dateHeureMouvement = dateFormat.parse("2024-12-01 10:30");
            Date dateSortiePrevue = dateFormat.parse("2024-12-05 15:00");
            Date dateRetourPrevue = dateFormat.parse("2024-12-10 18:00");
            Date dateCreation = dateFormat.parse("2024-11-30 08:00");

            Client client = Client.builder()
                    .name("Doe")
                    .email("doe@test.com") // Email unique
                    .build();
            clientRepository.save(client);

            Valise valise = Valise.builder()
                    .description("Test Valise")
                    .numeroValise(123L)
                    .client(client)
                    .build();
            valiseRepository.save(valise);
            Livreur livreur = Livreur.builder()
                    .nomLivreur("Jules")
                    .codeLivreur("AWX58")
                    .build();
            livreurRepository.save(livreur);

            // Création d'un mouvement avec des dates spécifiques
            Mouvement mouvement = Mouvement.builder()
                    .dateHeureMouvement(dateHeureMouvement)
                    .dateSortiePrevue(dateSortiePrevue)
                    .dateRetourPrevue(dateRetourPrevue)
                    .statutSortie("finished")
                    .valise(valise)
                    .livreurs(List.of(livreur))
                    .build();
            mouvement.addLivreur(livreur);


            mouvement = mouvementRepository.save(mouvement); // Sauvegarde et récupération de l'objet

            // Test de mise à jour du mouvement
            mockMvc.perform(post("/mouvements/edit/{id}", mouvement.getId())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("statutSortie", "Finalisé")
                            .param("dateHeureMouvement", "2024-12-06T10:00") // Format ISO-8601 pour datetime-local
                            .param("dateSortiePrevue", "2024-12-06") // Format ISO-8601 pour date
                            .param("dateRetourPrevue", "2024-12-10")) // Format ISO-8601 pour date
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/mouvements/list"));

            // Validation des modifications
            Mouvement updatedMouvement = mouvementRepository.findById(mouvement.getId()).orElse(null);
            assertNotNull(updatedMouvement);
            assertEquals("Finalisé", updatedMouvement.getStatutSortie());
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

}
