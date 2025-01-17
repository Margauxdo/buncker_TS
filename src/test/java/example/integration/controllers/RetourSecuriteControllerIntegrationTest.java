package example.integration.controllers;

import example.entity.RetourSecurite;
import example.entity.Client;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class RetourSecuriteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    private RetourSecurite savedRetourSecurite;

    @BeforeEach
    public void setUp() {
        // Création d'un client pour les tests
        Client client = clientRepository.save(Client.builder()
                .name("Client Test")
                .email("test@example.com")
                .build());

        // Création et sauvegarde d'un retour sécurité
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(123456L)
                .datesecurite(new Date())
                .cloture(false)
                .client(client)
                .build();

        savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
    }



    @Test
    public void testViewRetourSecuriteById_Success() throws Exception {
        mockMvc.perform(get("/retourSecurite/view/" + savedRetourSecurite.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/RS_details"))
                .andExpect(model().attributeExists("retourSecurite"))
                .andExpect(model().attribute("retourSecurite", hasProperty("numero", is(123456L))))
                .andDo(print());
    }



    @Test
    public void testDeleteRetourSecurite_Success() throws Exception {
        mockMvc.perform(post("/retourSecurite/delete/" + savedRetourSecurite.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retourSecurite/list"))
                .andDo(print());

        assertFalse(retourSecuriteRepository.existsById(savedRetourSecurite.getId()));
    }
}
