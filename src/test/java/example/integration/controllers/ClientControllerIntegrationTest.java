package example.integration.controllers;

import example.DTO.ClientDTO;
import example.entity.Client;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("valisesList", notNullValue()))
                .andExpect(model().attribute("problemsList", notNullValue()))
                .andExpect(model().attribute("retoursList", notNullValue()))
                .andExpect(model().attribute("rulesList", notNullValue()));
    }

    // Test: Create a new client
    @Test
    public void testCreateClient() throws Exception {
        mockMvc.perform(post("/clients/create")
                        .param("name", "Alice Doe")
                        .param("email", "alice@example.com")
                        .param("adresse", "789 Example St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/list"));

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
                .andExpect(redirectedUrl("/clients/list"));

        Assertions.assertTrue(clientRepository.findById(client.getId()).isEmpty());
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Example St")
                .valises(new ArrayList<>())  // Initialize lists
                .retourSecurites(new ArrayList<>())
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(post("/clients/edit/{id}", client.getId())
                        .param("name", "Jane Doe")
                        .param("email", "jane.doe@example.com")
                        .param("adresse", "456 Example St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/list"));

        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        Assertions.assertNotNull(updatedClient);
        Assertions.assertEquals("Jane Doe", updatedClient.getName());
        Assertions.assertEquals("jane.doe@example.com", updatedClient.getEmail());
    }

    @Test
    public void testListClients() throws Exception {
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Example St")
                .valises(new ArrayList<>())  // Initialize lists
                .retourSecurites(new ArrayList<>())
                .build();
        clientRepository.save(client);

        mockMvc.perform(get("/clients/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_list"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attribute("clients", hasSize(1)))
                .andExpect(model().attribute("clients", hasItem(
                        hasProperty("name", is("John Doe"))
                )));
    }

    @Test
    public void testViewClientDetails() throws Exception {
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Example St")
                .valises(new ArrayList<>())  // Initialize lists
                .retourSecurites(new ArrayList<>())
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/view/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_detail"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client",
                        hasProperty("name", is("John Doe"))));
    }

    @Test
    public void testEditClientForm() throws Exception {
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Example St")
                .valises(new ArrayList<>())  // Initialize lists
                .retourSecurites(new ArrayList<>())
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/edit/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_edit"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client",
                        hasProperty("name", is("John Doe"))));
    }

}
