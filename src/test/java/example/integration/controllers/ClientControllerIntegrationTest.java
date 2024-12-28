package example.integration.controllers;

import example.entity.Client;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

  // Test: Show create client form
    @Test
    public void testCreateClientForm() throws Exception {
        mockMvc.perform(get("/clients/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_create"))
                .andExpect(model().attributeExists("client"));
    }

    // Test: Create a new client
    @Test
    public void testCreateClient() throws Exception {
        mockMvc.perform(post("/clients/create")
                        .param("name", "Alice Doe")
                        .param("email", "alice@example.com")
                        .param("adresse", "789 Example St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/list")); // Correspond au chemin du contrôleur

        Client savedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals("Alice Doe", savedClient.getName());
        Assertions.assertEquals("alice@example.com", savedClient.getEmail());
    }



    // Test: Delete a client
    @Test
    public void testDeleteClient() throws Exception {
        Client client = Client.builder()
                .name("Delete Test")
                .email("delete@example.com")
                .adresse("123 Delete St")
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(post("/clients/delete/{id}", client.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/list")); // Correspond à la redirection définie dans le contrôleur

        Assertions.assertTrue(clientRepository.findById(client.getId()).isEmpty());
    }



}
