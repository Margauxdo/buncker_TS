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
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

        // Appeler le contrôleur et vérifier le résultat
        mockMvc.perform(get("/valise/list"))
                .andExpect(status().isOk()) // Vérifie le statut HTTP 200
                .andExpect(view().name("valises/valises_list")) // Vérifie le nom de la vue
                .andExpect(model().attributeExists("valises")) // Vérifie que l'attribut 'valises' est présent
                .andExpect(model().attribute("valises", hasSize(1))) // Vérifie la taille de la liste des valises
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
                .client(client)
                .build();
        valiseRepository.save(valise);

        mockMvc.perform(get("/valise/view/{id}", valise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/valise_details"))
                .andExpect(model().attributeExists("valise"))
                .andExpect(model().attribute("valise", hasProperty("client", hasProperty("name", is("Client Test")))));
    }



    @Test
    void testCreateValise() throws Exception {
        // Préparer un client valide
        Client client = Client.builder()
                .name("Test Client")
                .email("client@test.com")
                .build();
        client = clientRepository.save(client);

        // Log pour vérifier le client
        System.out.println("Client ID: " + client.getId());

        // Effectuer la requête POST pour créer une valise
        mockMvc.perform(post("/valise/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "New Valise")
                        .param("numeroValise", "789")
                        .param("client.id", String.valueOf(client.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valise/list"));

        // Vérifier si la valise est bien sauvegardée
        List<Valise> valises = valiseRepository.findAll();
        assertFalse(valises.isEmpty(), "The valise list should not be empty");

        // Vérifier les propriétés de la valise sauvegardée
        Valise savedValise = valises.get(0);
        Assertions.assertEquals("New Valise", savedValise.getDescription());
        Assertions.assertEquals(789L, savedValise.getNumeroValise());
        Assertions.assertEquals(client.getId(), savedValise.getClient().getId());
    }



    @Test
    void testEditValise() throws Exception {
        // Créer un client valide pour l'association
        Client client = Client.builder()
                .name("Test Client")
                .email("client@test.com")
                .build();
        clientRepository.save(client);

        // Créer une valise associée au client
        Valise valise = Valise.builder()
                .description("Old Valise")
                .numeroValise(111L)
                .dateCreation(new Date())
                .client(client) // Associer le client ici
                .build();
        Valise savedValise = valiseRepository.save(valise);

        // Modifier la valise
        mockMvc.perform(post("/valise/edit/{id}", savedValise.getId())
                        .param("description", "Updated Valise")
                        .param("numeroValise", "222")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valise/list"));

        // Vérification de la mise à jour
        Valise updatedValise = valiseRepository.findById(savedValise.getId()).orElse(null);
        Assertions.assertNotNull(updatedValise);
        Assertions.assertEquals("Updated Valise", updatedValise.getDescription());
        Assertions.assertEquals(222L, updatedValise.getNumeroValise());

    }


    @Test
    void testDeleteValise() throws Exception {
        // Créer un client valide pour l'association
        Client client = Client.builder()
                .name("Test Client")
                .email("client@test.com")
                .build();
        clientRepository.save(client);

        // Créer une valise associée au client
        Valise valise = Valise.builder()
                .description("To Be Deleted")
                .numeroValise(333L)
                .dateCreation(new Date())
                .client(client) // Associer le client ici
                .build();
        Valise savedValise = valiseRepository.save(valise);

        // Effectuer la suppression
        mockMvc.perform(post("/valise/delete/{id}", savedValise.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valises/valises_list"));

        // Vérification de la suppression
        Assertions.assertTrue(valiseRepository.findById(savedValise.getId()).isEmpty());
    }


}
