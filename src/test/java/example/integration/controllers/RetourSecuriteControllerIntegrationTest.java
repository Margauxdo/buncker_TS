package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.controller.RetourSecuriteController;
import example.entity.Client;
import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    @Autowired
    private RetourSecuriteController RScontroller;


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
        retourSecurite.setClients(new ArrayList<>());

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
        retourSecurite.setClients(new ArrayList<>());

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
        retourSecurite.setClients(new ArrayList<>());
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
    @Test
    public void testCreateRetourSecurite_MissingFields_ShouldReturnBadRequest() throws Exception {
        RetourSecurite retourSecurite = new RetourSecurite();
        mvc.perform(post("/api/retourSecurite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retourSecurite)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateRetourSecurite_InvalidInput() {
        RetourSecurite retourSecurite = new RetourSecurite();

        when(retourSecuriteService.createRetourSecurite(any(RetourSecurite.class)))
                .thenThrow(new DataIntegrityViolationException("Invalid input"));

        ResponseEntity<RetourSecurite> result = RScontroller.createRetourSecurite(retourSecurite);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(retourSecuriteService, times(1)).createRetourSecurite(any(RetourSecurite.class));
    }

    @Test
    public void testUpdateRetourSecurite_EntityNotFound() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteService.updateRetourSecurite(eq(9999), any(RetourSecurite.class)))
                .thenThrow(new EntityNotFoundException("Entity not found"));

        ResponseEntity<RetourSecurite> response = RScontroller.updateRetourSecurite(9999, retourSecurite);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(retourSecuriteService, times(1)).updateRetourSecurite(eq(9999), any(RetourSecurite.class));
    }





}
