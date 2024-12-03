package example.integration.controllers;

import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integrationtest")
public class ValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        // Nettoyer la base avant chaque test
        valiseRepository.deleteAll();
    }

    @Test
    void testViewValisesList() throws Exception {
        // Créer et sauvegarder un client pour le test
        Client client = Client.builder()
                .name("Client Test")
                .email("client@test.com")
                .build();
        clientRepository.save(client);

        // Créer et sauvegarder une valise pour le test
        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .dateCreation(new Date())
                .client(client)
                .build();
        valiseRepository.save(valise);

        // Effectuer la requête GET et valider la réponse
        mockMvc.perform(get("/valise/list"))
                .andExpect(status().isOk()) // Statut 200 attendu
                .andExpect(content().contentType("text/html;charset=UTF-8")) // Vérifie le type de contenu
                .andExpect(view().name("valises/valises_list")) // Vérifie le nom de la vue
                .andExpect(model().attributeExists("valises")) // Vérifie que le modèle contient "valises"
                .andExpect(model().attribute("valises", hasSize(1))) // Vérifie la taille de la liste "valises"
                .andExpect(model().attribute("valises", hasItem(
                        allOf(
                                hasProperty("description", is(valise.getDescription())),
                                hasProperty("numeroValise", is(valise.getNumeroValise()))
                        )
                )));
    }



    @Test
    void testViewValiseDetails() throws Exception {
        Client client = Client.builder()
                .name("Client Test")
                .email("client@test.com")
                .build();
        clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .dateCreation(new Date())
                .client(client)
                .build();
        Valise savedValise = valiseRepository.save(valise);

        mockMvc.perform(get("/valise/view/{id}" + savedValise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/valise_details"))
                .andExpect(model().attributeExists("valise"));


    }

    @Test
    void testCreateValise() throws Exception {
        mockMvc.perform(post("/valise/create")
                        .param("description", "New Valise")
                        .param("numeroValise", "789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valise/list"));

        // Vérifier si la valise est créée dans la base
        Valise valise = valiseRepository.findAll().get(0);
        Assertions.assertEquals("New Valise", valise.getDescription());
        Assertions.assertEquals("789", valise.getNumeroValise());

    }

    @Test
    void testEditValise() throws Exception {
        Valise valise = Valise.builder()
                .description("Old Valise")
                .numeroValise(111L)
                .dateCreation(new Date())
                .build();
        Valise savedValise = valiseRepository.save(valise);

        mockMvc.perform(post("/valise/edit/{id}" + savedValise.getId())
                        .param("description", "Updated Valise")
                        .param("numeroValise", "222"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valise/list"));

        // Vérification de la mise à jour
        Valise updatedValise = valiseRepository.findById(savedValise.getId()).orElse(null);
        assert updatedValise != null;
        assert updatedValise.getDescription().equals("Updated Valise");
    }

    @Test
    void testDeleteValise() throws Exception {
        Valise valise = Valise.builder()
                .description("To Be Deleted")
                .numeroValise(333L)
                .dateCreation(new Date())
                .build();
        Valise savedValise = valiseRepository.save(valise);

        mockMvc.perform(post("/valise/delete/{id}" + savedValise.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valises/valises_list"));

        // Vérification de la suppression
        assert valiseRepository.findById(savedValise.getId()).isEmpty();
    }

    @Test
    void testViewErrorPage() throws Exception {
        mockMvc.perform(get("/valise/view/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Valise avec l'Id")));
    }
}
