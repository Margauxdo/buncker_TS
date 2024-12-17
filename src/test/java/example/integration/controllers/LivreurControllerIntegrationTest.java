
package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.LivreurService;
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

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
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
    @Autowired
    private LivreurService livreurService;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        livreurRepository.deleteAll();

    }



    @Test
    public void testListLivreurs_Success() throws Exception {
        mockMvc.perform(get("/livreurs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_list"))
                .andExpect(model().attributeExists("livreurs"))
                .andDo(print());
    }



    @Test
    public void testViewLivreurById_Failure_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/livreurs/view/999")) // ID non existant
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Livreur avec l'ID 999 non trouvé.")))
                .andDo(print());
    }




    @Test
    public void testCreateLivreur_Success() throws Exception {
        // Arrange
        Livreur livreur = Livreur.builder()
                .nomLivreur("Dupont")
                .prenomLivreur("Jean")
                .codeLivreur("CODE123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/livreurs/livreurs/create")
                        .flashAttr("livreur", livreur))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"))
                .andDo(print());

        // Vérifier la présence en base de données
        Livreur savedLivreur = livreurRepository.findAll().stream()
                .filter(l -> l.getNomLivreur().equals("Dupont"))
                .findFirst()
                .orElse(null);
        assertNotNull(savedLivreur);
        assertEquals("Jean", savedLivreur.getPrenomLivreur());
    }



    @Test
    public void testUpdateLivreur_Success() throws Exception {
        // Arrange: Create a Client
        Client client = Client.builder()
                .name("Test Client")
                .email("test@example.com")
                .build();
        client = clientRepository.save(client);

        // Create a Valise associated with the Client
        Valise valise = Valise.builder()
                .refClient("VAL-123")
                .description("Test Valise")
                .numeroValise(123L)
                .client(client) // Associate the Client
                .build();
        valise = valiseRepository.save(valise);

        // Create a Mouvement associated with the Valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .valise(valise)
                .build();
        mouvement = mouvementRepository.save(mouvement);

        // Create a Livreur associated with the Mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("Dupont")
                .prenomLivreur("Jean")
                .mouvements((List<Mouvement>) mouvement) // Associate the Mouvement
                .build();
        livreur = livreurRepository.save(livreur);

        // Update the Livreur's details
        Livreur updatedLivreur = Livreur.builder()
                .id(livreur.getId())
                .nomLivreur("Martin")
                .prenomLivreur("Paul")
                .mouvements((List<Mouvement>) mouvement) // Associate the Mouvement

                .build();

        // Act & Assert
        mockMvc.perform(post("/livreurs/edit/" + livreur.getId())
                        .flashAttr("livreur", updatedLivreur))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"))
                .andDo(print());

        Livreur savedLivreur = livreurRepository.findById(livreur.getId()).orElse(null);
        assertNotNull(savedLivreur, "The updated Livreur should exist in the database.");
        assertEquals("Martin", savedLivreur.getNomLivreur(), "The Livreur's name should be updated.");
        assertEquals("Paul", savedLivreur.getPrenomLivreur(), "The Livreur's first name should be updated.");
    }



    @Test
    public void testDeleteLivreur_Success() throws Exception {
        // Arrange: Create a mock client
        Client client = Client.builder()
                .name("Test Client")
                .email("test@example.com")
                .build();
        client = clientRepository.save(client);

        Valise valise = Valise.builder()
                .refClient("VAL-123")
                .description("Valise de test")
                .numeroValise(123L)
                .client(client) // Associate the client
                .build();
        valise = valiseRepository.save(valise);

        // Create a mouvement with the associated valise
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .valise(valise) // Associate the valise
                .build();
        mouvement = mouvementRepository.save(mouvement);

        // Create a livreur with the mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("Dupont")
                .prenomLivreur("Jean")
                .mouvements((List<Mouvement>) mouvement) // Associate the Mouvement

                .build();
        livreur = livreurRepository.save(livreur);

        // Act & Assert
        mockMvc.perform(post("/livreurs/delete/" + livreur.getId()))
                .andExpect(status().is3xxRedirection()) // Expect a redirection (302)
                .andExpect(redirectedUrl("/livreurs/list")) // Verify redirection URL
                .andDo(print());

        // Verify: The livreur should be deleted from the database
        boolean exists = livreurRepository.existsById(livreur.getId());
        assertEquals(false, exists, "The livreur should be deleted from the database.");
    }



    @Test
    public void testDeleteLivreur_Failure_NotFound() throws Exception {
        mockMvc.perform(post("/livreurs/delete/999")) // ID inexistant
                .andExpect(status().isOk()) // Vue d'erreur retournée
                .andExpect(view().name("livreurs/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andDo(print());
    }












}
