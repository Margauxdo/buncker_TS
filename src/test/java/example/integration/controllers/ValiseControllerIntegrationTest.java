package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import example.entities.Client;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class ValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;
    private Valise valise;

    @BeforeEach
    void setUp() {
        valiseRepository.deleteAll();
        clientRepository.deleteAll();

        client = new Client();
        client.setName("Client Test");
        client.setEmail("getvalisetest@example.com");
        clientRepository.save(client);

        valise = new Valise();
        valise.setDescription("Valise de test");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.save(valise);

        System.out.println("Client ID : " + client.getId());
        System.out.println("Valise ID : " + valise.getId());

            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


    }

    @Test
    public void testGetValiseById_Success() throws Exception {
        assertTrue(valiseRepository.findById(valise.getId()).isPresent(), "La valise n'existe pas dans la base de données.");

        mockMvc.perform(get("/api/valise/" + valise.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Valise de test"))
                .andExpect(jsonPath("$.numeroValise").value(123456));
    }




    @Test
    public void testGetAllValises() throws Exception {
        mockMvc.perform(get("/api/valise")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Valise de test"))
                .andExpect(jsonPath("$[0].numeroValise").value(123456));
    }



    @Test
    public void testGetValiseById_NotFound() throws Exception {
        mockMvc.perform(get("/api/valise/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateValise_Success() throws Exception {
        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("uniqueemail_" + UUID.randomUUID() + "@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Nouvelle valise");
        valise.setNumeroValise(789101L);
        valise.setClient(client);

        String valiseJson = objectMapper.writeValueAsString(valise);

        mockMvc.perform(post("/api/valise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valiseJson))
                .andExpect(status().isCreated());
    }


    @Test
    public void testDeleteValise_Success() throws Exception {
        mockMvc.perform(delete("/api/valise/" + valise.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteValise_NotFound() throws Exception {
        mockMvc.perform(delete("/api/valise/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
