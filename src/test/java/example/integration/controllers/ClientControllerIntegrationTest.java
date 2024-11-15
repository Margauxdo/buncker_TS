package example.integration.controllers;

import example.entities.Client;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void getAllClients_shouldReturnClients_whenClientsExist() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client.setTelephoneExploitation("123456789");
        client.setVille("Ville");
        client.setEnvoiparDefaut("EnvoiParDefaut");
        client.setMemoRetourSecurite1("Memo1");
        client.setMemoRetourSecurite2("Memo2");
        client.setTypeSuivie("TypeSuivi");
        client.setCodeClient("CodeClient");

        clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Nom Test")));
    }

    @Test
    public void getClientById_shouldReturnClient_whenClientExists() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client.setTelephoneExploitation("123456789");
        client.setVille("Ville");
        client.setEnvoiparDefaut("EnvoiParDefaut");
        client.setMemoRetourSecurite1("Memo1");
        client.setMemoRetourSecurite1("Memo2");
        client.setTypeSuivie("TypeSuivi");
        client.setCodeClient("CodeClient");

        client = clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(get("/api/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nom Test")));
    }

    @Test
    public void getClientById_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/clients/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createClient_shouldCreateClient_whenValidRequest() throws Exception {
        // Arrange
        String clientJson = "{\"name\":\"Nom Test\",\"adresse\":\"Adresse Test\",\"email\":\"email@example.com\"}";

        // Act & Assert
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Nom Test")));
    }

    @Test
    public void createClient_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        // Arrange
        String clientJson = "{\"name\":\"\",\"email\":\"invalid_email\"}";

        // Act & Assert
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateClient_shouldUpdateClient_whenClientExists() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client.setTelephoneExploitation("123456789");
        client.setVille("Ville");

        client = clientRepository.save(client);

        String updatedClientJson = String.format(
                "{\"id\": %d, \"name\":\"Nom Mis à Jour\", \"adresse\":\"Adresse Mis à Jour\", \"email\":\"new_email@example.com\"}",
                client.getId()
        );

        // Act & Assert
        mockMvc.perform(put("/api/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nom Mis à Jour")));
    }

    @Test
    public void updateClient_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        // Arrange
        String updatedClientJson = "{\"name\":\"Nom Test\",\"adresse\":\"Adresse Test\",\"email\":\"email@example.com\"}";

        // Act & Assert
        mockMvc.perform(put("/api/clients/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteClient_shouldReturnNoContent_whenClientExists() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Nom Test");
        client.setAdresse("Adresse Test");
        client.setEmail("email@example.com");
        client.setTelephoneExploitation("123456789");
        client.setVille("Ville");

        client = clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(delete("/api/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteClient_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/clients/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
