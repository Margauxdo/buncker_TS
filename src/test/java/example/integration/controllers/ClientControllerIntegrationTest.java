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

    // Test: View all clients
    @Test
    public void testViewClients() throws Exception {
        Client client = Client.builder()
                .name("John Doe")
                .email("john@example.com")
                .adresse("123 Test St")
                .build();
        clientRepository.save(client);

        mockMvc.perform(get("/clients/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_list"))
                .andExpect(model().attribute("clients", hasSize(1)))
                .andExpect(model().attribute("clients", hasItem(
                        hasProperty("name", is("John Doe"))
                )));
    }

    // Test: View a single client by ID
    @Test
    public void testViewClientById() throws Exception {
        Client client = Client.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .adresse("456 Sample St")
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/view/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_detail"))
                .andExpect(model().attribute("client", hasProperty("name", is("Jane Doe"))));
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
                .andExpect(redirectedUrl("/clients/clients_list"));

        Client savedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals("Alice Doe", savedClient.getName());
        Assertions.assertEquals("alice@example.com", savedClient.getEmail());
    }

    // Test: Show edit client form
    @Test
    public void testEditClientForm() throws Exception {
        Client client = Client.builder()
                .name("Edit Test")
                .email("edit@example.com")
                .adresse("123 Edit St")
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/edit/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_edit"))
                .andExpect(model().attribute("client", hasProperty("name", is("Edit Test"))));
    }

    // Test: Edit an existing client
    @Test
    public void testEditClient() throws Exception {
        Client client = Client.builder()
                .name("Original Name")
                .email("original@example.com")
                .adresse("123 Original St")
                .build();
        client = clientRepository.save(client);

        mockMvc.perform(post("/clients/edit/{id}", client.getId())
                        .param("name", "Updated Name")
                        .param("email", "updated@example.com")
                        .param("adresse", "123 Updated St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/clients_list"));

        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        Assertions.assertEquals("Updated Name", updatedClient.getName());
        Assertions.assertEquals("updated@example.com", updatedClient.getEmail());
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
                .andExpect(redirectedUrl("/clients/clients_list"));

        Assertions.assertTrue(clientRepository.findById(client.getId()).isEmpty());
    }
}
