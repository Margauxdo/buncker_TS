package example.integration.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.controller.ValiseController;
import example.entities.Client;
import example.entities.Probleme;
import example.entities.Regle;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.RegleRepository;
import example.repositories.ValiseRepository;
import example.services.ValiseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ValiseIntegrationTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private ValiseRepository valiseRepository;

        @Autowired
        private ProblemeRepository problemeRepository;

        @Autowired
        private RegleRepository regleRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        public void setUp() {
            problemeRepository.deleteAll();
            valiseRepository.deleteAll();
            clientRepository.deleteAll();
        }

        // Méthode d'assistance pour créer un client
        private Client createClient() {
            Client client = new Client();
            client.setName("Client Test");
            client.setEmail("client@test.com");
            return clientRepository.save(client);
        }

        // Méthode d'assistance pour créer une valise
        private Valise createValise(Client client) {
            Valise valise = new Valise();
            valise.setDescription("Valise Test");
            valise.setNumeroValise(12345L);
            valise.setClient(client);
            return valiseRepository.save(valise);
        }

        @Test
        public void testGetAllValises_Success() throws Exception {
            Client client = createClient();
            createValise(client);

            mvc.perform(get("/api/valise"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].description").value("Valise Test"))
                    .andExpect(jsonPath("$[0].numeroValise").value(12345L));
        }

        @Test
        public void testCreateValise_Success() throws Exception {
            Client client = createClient();

            Valise valise = new Valise();
            valise.setDescription("Nouvelle Valise");
            valise.setNumeroValise(67890L);
            valise.setClient(client);

            String jsonContent = objectMapper.writeValueAsString(valise);

            mvc.perform(post("/api/valise")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.description").value("Nouvelle Valise"))
                    .andExpect(jsonPath("$.numeroValise").value(67890L));
        }

        @Test
        public void testDeleteValise_Success() {
            Client client = createClient();
            Valise valise = createValise(client);

            valiseRepository.delete(valise);

            assertFalse(valiseRepository.findById(valise.getId()).isPresent());
        }

        @Test
        public void testUpdateValise_Success() {
            Client client = createClient();
            Valise valise = createValise(client);

            valise.setDescription("Valise mise à jour");
            valiseRepository.save(valise);

            Valise updatedValise = valiseRepository.findById(valise.getId()).orElseThrow();
            assertEquals("Valise mise à jour", updatedValise.getDescription());
        }

        @Test
        void testValiseRelationWithRegle() {
            Client client = createClient();
            Regle regle = new Regle();
            regle.setCoderegle("CODE123");
            regleRepository.save(regle);

            Valise valise = createValise(client);
            valise.setRegleSortie(regle);
            valiseRepository.save(valise);

            Optional<Valise> found = valiseRepository.findById(valise.getId());
            assertTrue(found.isPresent());
            assertEquals("CODE123", found.get().getRegleSortie().getCoderegle());
        }

        @Test
        void testValiseRelationWithProblemes() {
            Client client = createClient();
            Valise valise = createValise(client);

            Probleme probleme = new Probleme();
            probleme.setValise(valise);
            probleme.setDescriptionProbleme("Problème Test");
            problemeRepository.save(probleme);

            List<Probleme> problemes = problemeRepository.findByValiseId(valise.getId());
            assertFalse(problemes.isEmpty());
            assertEquals("Problème Test", problemes.get(0).getDescriptionProbleme());
        }

        @Test
        void testValiseRelationWithClient() {
            Client client = createClient();
            Valise valise = createValise(client);

            Optional<Valise> found = valiseRepository.findById(valise.getId());
            assertTrue(found.isPresent());
            assertEquals(client.getName(), found.get().getClient().getName());
            assertEquals(client.getEmail(), found.get().getClient().getEmail());
        }
    }




