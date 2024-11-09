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
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private ValiseController valiseController;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testGetAllValises_Success() throws Exception {
        Client client = new Client();
        client.setName("Client1");
        client.setEmail("client1@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Valise Test");
        valise.setNumeroValise(12345L);
        valise.setClient(client);
        valiseRepository.save(valise);

        mvc.perform(get("/api/valise"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description").value("Valise Test"))
                .andExpect(jsonPath("$[0].numeroValise").value(12345L));
    }

    @Test
    public void testCreateValise_Success() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        client = clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Nouvelle Valise");
        valise.setNumeroValise(67890L);
        valise.setClient(client);

        Valise createdValise = valiseService.createValise(valise);

        assertEquals("Nouvelle Valise", createdValise.getDescription());
        assertEquals(67890L, createdValise.getNumeroValise());
        assertEquals(client.getId(), createdValise.getClient().getId());
    }




    @Test
    public void testDeleteValise_Success() throws Exception {
        Client client = new Client();
        client.setName("Client1");
        client.setEmail("client1@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Valise à supprimer");
        valise.setNumeroValise(54321L);
        valise.setClient(client);
        valiseRepository.save(valise);

        valiseRepository.delete(valise);

        assertEquals(0, valiseRepository.findAll().size());
    }

    @Test
    public void testUpdateValise_Success() throws Exception {
        Client client = new Client();
        client.setName("Client1");
        client.setEmail("client1@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Valise initiale");
        valise.setNumeroValise(11111L);
        valise.setClient(client);
        valiseRepository.save(valise);

        valise.setDescription("Valise mise à jour");
        valiseRepository.save(valise);

        Valise updatedValise = valiseRepository.findById(valise.getId()).orElseThrow();
        assertEquals("Valise mise à jour", updatedValise.getDescription());
    }

    @Test
    void testValiseRelationWithRegle() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regleRepository.save(regle);

        Valise valise = new Valise();
        valise.setClient(client);
        valise.setRegleSortie(regle);
        valiseRepository.save(valise);

        Optional<Valise> found = valiseRepository.findById(valise.getId());
        assertTrue(found.isPresent());
        assertNotNull(found.get().getRegleSortie());
        assertEquals("CODE123", found.get().getRegleSortie().getCoderegle());
    }



    @Test
    void testValiseRelationWithClient() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setClient(client);
        valiseRepository.save(valise);

        Optional<Valise> found = valiseRepository.findById(valise.getId());
        assertTrue(found.isPresent());
        assertEquals("Client Test", found.get().getClient().getName());
        assertEquals("client@test.com", found.get().getClient().getEmail());
    }


    @Test
    void testCreateValise() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        valise.setClient(client);
        valiseRepository.save(valise);

        Optional<Valise> found = valiseRepository.findById(valise.getId());
        assertTrue(found.isPresent());
        assertEquals(123L, found.get().getNumeroValise());
        assertNotNull(found.get().getClient());
        assertEquals("Client Test", found.get().getClient().getName());
    }








    @Test
    void testValiseRelationWithProblemes() {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client@test.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setClient(client);
        valiseRepository.save(valise);

        Probleme probleme = new Probleme();
        probleme.setValise(valise);
        probleme.setDescriptionProbleme("Problème Test");
        problemeRepository.save(probleme);

        List<Probleme> problemes = problemeRepository.findByValiseId(valise.getId());
        assertFalse(problemes.isEmpty());
        assertEquals(valise.getId(), problemes.get(0).getValise().getId());
    }



}
