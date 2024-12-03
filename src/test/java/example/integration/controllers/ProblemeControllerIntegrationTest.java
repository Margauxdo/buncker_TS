package example.integration.controllers;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
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
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
    }

    @Test
    public void testViewAllProblemes() throws Exception {
        // Arrange
        Client client = Client.builder()
                .name("Jean")
                .email("j.j@gamil.com")
                .build();
        clientRepository.save(client);

        Valise val = Valise.builder()
                .refClient("AWD526")
                .numeroValise(2563L)
                .description("description du type de valise")
                .client(client)
                .build();
        valiseRepository.save(val);

        Probleme pbtest = Probleme.builder()
                .descriptionProbleme("description detaillé du soucis")
                .client(client)
                .valise(val)
                .detailsProbleme("detail du pb")
                .build();

        problemeRepository.save(pbtest);

        // Act & Assert
        mockMvc.perform(get("/pb/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_list"))
                .andExpect(content().string(containsString("description detaillé du soucis")))  // Expected description
                .andExpect(content().string(containsString("detail du pb")))  // Expected details
                .andExpect(content().string(containsString("description du type de valise")));  // Expected valise description
    }


    @Test
    @Transactional
    public void testViewProbleme_Success() throws Exception {
        // Étape 1 : Créer et sauvegarder un Client
        Client testClient = clientRepository.save(Client.builder()
                .name("Test Client")
                .email("testclient@example.com")
                .build());
        assertNotNull(testClient.getId(), "L'ID du Client ne doit pas être nul après la sauvegarde.");

        // Étape 2 : Créer et sauvegarder une Valise liée au Client
        Valise testValise = valiseRepository.save(Valise.builder()
                .description("Test Valise Description")
                .numeroValise(123L)
                .client(testClient)
                .build());
        assertNotNull(testValise.getId(), "L'ID de la Valise ne doit pas être nul après la sauvegarde.");

        // Étape 3 : Créer et sauvegarder un Problème lié à la Valise et au Client
        Probleme probleme = problemeRepository.save(Probleme.builder()
                .descriptionProbleme("Problem to View")
                .detailsProbleme("Details to View")
                .valise(testValise)
                .client(testClient) // Ajout du client pour résoudre le problème de liaison
                .build());
        assertNotNull(probleme.getId(), "L'ID du Problème ne doit pas être nul après la sauvegarde.");

        // Étape 4 : Effectuer une requête GET pour afficher le problème
        mockMvc.perform(get("/pb/view/" + probleme.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_details"))
                .andExpect(content().string(containsString("Problem to View")))
                .andExpect(content().string(containsString("Details to View")))
                .andExpect(content().string(containsString("Test Client"))); // Vérifie la liaison avec le client
    }


    @Test
    public void testViewProbleme_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/pb/view/999")) // ID inexistant
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("problemes/error"));
    }


    @Test
    @Transactional
    public void testCreateProbleme_Success() throws Exception {
        // Étape 1 : Créer et sauvegarder un Client
        Client testClient = clientRepository.save(Client.builder()
                .name("Test Client")
                .email("testclient@example.com")
                .build());
        assertNotNull(testClient.getId(), "L'ID du Client ne doit pas être nul après la sauvegarde.");

        // Étape 2 : Créer et sauvegarder une Valise liée au Client
        Valise testValise = valiseRepository.save(Valise.builder()
                .description("Test Valise Description")
                .numeroValise(123L)
                .client(testClient)
                .build());
        assertNotNull(testValise.getId(), "L'ID de la Valise ne doit pas être nul après la sauvegarde.");

        // Étape 3 : Exécuter la requête POST pour créer un problème
        mockMvc.perform(post("/pb/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("descriptionProbleme", "Problème exemple")
                        .param("detailsProbleme", "Détails du problème")
                        .param("valise.id", String.valueOf(testValise.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pb/list"));

        // Étape 4 : Vérification des données sauvegardées
        List<Probleme> problemes = problemeRepository.findAll();
        assertFalse(problemes.isEmpty(), "La liste des Problèmes ne doit pas être vide.");

        Probleme savedProbleme = problemes.get(0);
        assertEquals("Problème exemple", savedProbleme.getDescriptionProbleme(), "La description du problème doit correspondre.");
        assertEquals("Détails du problème", savedProbleme.getDetailsProbleme(), "Les détails du problème doivent correspondre.");
        assertEquals(testValise.getId(), savedProbleme.getValise().getId(), "L'ID de la Valise doit correspondre.");
    }


    @Test
    public void testUpdateProblemeForm_Success() throws Exception {
        // Arrange: Create a client
        Client client = clientRepository.save(Client.builder()
                .name("Jean")
                .email("j.j@gamil.com")
                .build()
        );

        Valise valise = valiseRepository.save(Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(client)
                .build());

        Probleme probleme = problemeRepository.save(Probleme.builder()
                .detailsProbleme("details")
                .descriptionProbleme("Description")
                .valise(valise)
                .build());

        // Act & Assert: Perform the update and check if the form is displayed with the correct data
        mockMvc.perform(get("/pb/edit/" + probleme.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_edit"))
                .andExpect(content().string(containsString("Description")))  // updated from "Old Problem"
                .andExpect(content().string(containsString("details")));      // updated from "Old Details"
    }


    @Test
    public void testUpdateProbleme_Success() throws Exception {
        // Step 1: Create and save a Client
        Client testClient = clientRepository.save(Client.builder()
                .name("Test Client")
                .email("test@example.com")
                .build());
        assertNotNull(testClient.getId(), "The Client ID should not be null after saving.");

        // Step 2: Create and save a Valise associated with the Client
        Valise testValise = valiseRepository.save(Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(testClient) // Associate with the Client
                .build());
        assertNotNull(testValise.getId(), "The Valise ID should not be null after saving.");

        // Step 3: Create and save a Problema
        Probleme savedPb = Probleme.builder()
                .valise(testValise)
                .detailsProbleme("details a modifié")
                .descriptionProbleme("description pb a modifie")
                .client(testClient)
                .build();
        problemeRepository.save(savedPb);

        // Step 4: Perform the update
        mockMvc.perform(post("/pb/edit/" + savedPb.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("descriptionProbleme", "Updated Problem")
                        .param("detailsProbleme", "Updated Details")
                        .param("valise.id", String.valueOf(savedPb.getValise().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pb/list"));

        // Step 5: Verify the update
        Optional<Probleme> updatedProbleme = problemeRepository.findById(savedPb.getId());
        assertTrue(updatedProbleme.isPresent(), "The updated Probleme should exist.");
        assertEquals("Updated Problem", updatedProbleme.get().getDescriptionProbleme(), "The description should match the updated value.");
        assertEquals("Updated Details", updatedProbleme.get().getDetailsProbleme(), "The details should match the updated value.");
        assertEquals(testValise.getId(), updatedProbleme.get().getValise().getId(), "The Valise ID should remain unchanged.");
    }


    @Test
    public void testDeleteProbleme_Success() throws Exception {
        // Arrange: Create and save a Client
        Client client = Client.builder()
                .name("jean")
                .email("jean@gmail.com")
                .build();
        clientRepository.save(client);

        Valise val = Valise.builder()
                .description("Test Valise")
                .numeroValise(123L)
                .client(client)
                .build();
        valiseRepository.save(val);

        Probleme pb = Probleme.builder()
                .descriptionProbleme("Original Problem")
                .detailsProbleme("Original Details")
                .valise(val)
                .build();
        problemeRepository.save(pb);

        // Act & Assert
        mockMvc.perform(post("/pb/delete/" + pb.getId()))  // Use the correct path with "/pb"
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pb/list"));

        // Verification: Check that the Problema has been deleted
        Optional<Probleme> deletedProbleme = problemeRepository.findById(pb.getId());
        assertFalse(deletedProbleme.isPresent());
    }






}
