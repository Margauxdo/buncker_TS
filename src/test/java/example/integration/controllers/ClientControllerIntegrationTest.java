package example.integration.controllers;

import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    // API Tests
    @Test
    @Transactional
    public void getAllClients_shouldReturnClients_whenClientsExist() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        clientRepository.save(client);

        mockMvc.perform(get("/clients/api")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nom Test"));
    }

    @Test
    @Transactional
    public void getClientById_shouldReturnClient_whenClientExists() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/api/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nom Test"))
                .andExpect(jsonPath("$.email").value("email@example.com"));
    }

    @Test
    public void getClientById_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        mockMvc.perform(get("/clients/api/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createClient_shouldCreateClient_whenValidRequest() throws Exception {
        String clientJson = "{\"name\":\"Nom Test\",\"adresse\":\"Adresse Test\",\"email\":\"email@example.com\"}";

        mockMvc.perform(post("/clients/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Nom Test")));
    }

    @Test
    public void createClient_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        String clientJson = "{\"name\":\"\",\"email\":\"invalid_email\"}";

        mockMvc.perform(post("/clients/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateClient_shouldUpdateClient_whenClientExists() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        String updatedClientJson = String.format(
                "{\"id\": %d, \"name\":\"Nom Mis à Jour\", \"adresse\":\"Adresse Mis à Jour\", \"email\":\"new_email@example.com\"}",
                client.getId()
        );

        mockMvc.perform(put("/clients/api/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nom Mis à Jour")));
    }

    @Test
    public void updateClient_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        String updatedClientJson = "{\"name\":\"Nom Test\",\"adresse\":\"Adresse Test\",\"email\":\"email@example.com\"}";

        mockMvc.perform(put("/clients/api/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteClient_shouldReturnNoContent_whenClientExists() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        mockMvc.perform(delete("/clients/api/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteClient_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        mockMvc.perform(delete("/clients/api/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Thymeleaf Tests
    @Test
    public void testViewClients() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        clientRepository.save(client);

        mockMvc.perform(get("/clients/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_list"))  // Correct view name
                .andExpect(model().attribute("clients", hasSize(1)))
                .andExpect(model().attribute("clients", hasItem(
                        hasProperty("name", is("Nom Test"))  // Ensure client.name is correct
                )));
    }


    @Test
    public void testViewClientById() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/view/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_detail"))  // Updated view name
                .andExpect(model().attribute("client", hasProperty("name", is("Nom Test"))));
    }


    @Test
    public void testCreateClientForm() throws Exception {
        mockMvc.perform(get("/clients/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_create"))  // Modifiez ici
                .andExpect(model().attributeExists("client"));
    }
    @Test
    public void testDeleteClientCascadeRelations() throws Exception {
        Client client = new Client();
        client.setName("Client with Relations");
        client.setEmail("relations@example.com");

        Valise valise = new Valise();
        valise.setClient(client);
        client.getValises().add(valise);

        clientRepository.save(client);

        mockMvc.perform(delete("/clients/api/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(clientRepository.findById(client.getId()).isEmpty());
        Assertions.assertTrue(valiseRepository.findAll().isEmpty());
    }


    @Test
    public void testEditClientForm() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/edit/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/client_edit"))

                .andExpect(model().attribute("client", hasProperty("name", is("Nom Test"))));
    }

    @Test
    public void testDeleteClient() throws Exception {
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client = clientRepository.save(client);

        mockMvc.perform(post("/clients/delete/{id}", client.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/list"));
    }
}
