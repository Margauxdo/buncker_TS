package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.controller.RetourSecuriteController;
import example.entities.Client;
import example.entities.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class RetourSecuriteControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @SpyBean
    private IRetourSecuriteService retourSecuriteService;
    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    public void setUp() throws Exception {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();
    }



    @Test
    public void testCreateRS_ShouldCreateRetourSecuriteWhenDataIsValid() throws Exception {
        Client client = new Client();
        client.setName("Bonningue");
        client.setEmail("bonningue@gmail.com");
        client = clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(25L);
        retourSecurite.setDatesecurite(new Date());
        retourSecurite.setCloture(false);
        retourSecurite.setClient(client);

        mvc.perform(post("/api/retourSecurite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retourSecurite)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero", is(25)))
                .andExpect(jsonPath("$.cloture", is(false)));
    }




    @Test
    public void testCreateRS_ShouldReturnBadRequestWhenRetourSecuriteIsNullOrInvalid() throws Exception {
        mvc.perform(post("/api/retourSecurite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateRS_ShouldReturnBadRequestWhenUpdateDataIsInvalid() throws Exception {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(null);

        Client client = new Client();
        client.setId(1);
        retourSecurite.setClient(client);

        mvc.perform(put("/api/retourSecurite/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retourSecurite)))
                .andExpect(status().isBadRequest());
    }




    @Test
    public void testUpdateRS_ShouldReturnNotFoundWhenUpdatingNonExistentRetourSecurite() throws Exception {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(25L);

        mvc.perform(put("/api/retourSecurite/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retourSecurite)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRS_ShouldReturnInternalServerErrorWhenDatabaseErrorOccursOnUpdate() throws Exception {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(25L);

        Mockito.doThrow(new RuntimeException("Simulated database error"))
                .when(retourSecuriteService).updateRetourSecurite(eq(-1), any(RetourSecurite.class));

        mvc.perform(put("/api/retourSecurite/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retourSecurite)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteRS_ShouldDeleteRetourSecuriteWhenIdExists() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("testclient@example.com");
        clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(25L);
        retourSecurite.setClient(client);
        retourSecuriteRepository.save(retourSecurite);

        mvc.perform(delete("/api/retourSecurite/{id}", retourSecurite.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRS_ShouldReturnNotFoundWhenDeletingNonExistentRetourSecurite() throws Exception {
        mvc.perform(delete("/api/retourSecurite/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
