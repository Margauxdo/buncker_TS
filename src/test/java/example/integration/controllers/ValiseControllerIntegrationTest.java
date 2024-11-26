
package example.integration.controllers;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    private Client client;
    private TypeValise typeValise;

    @BeforeEach
    public void setUp() {
        // Initialisation des dépendances nécessaires
        client = clientRepository.save(Client.builder().name("Client Test").build());
        typeValise = typeValiseRepository.save(TypeValise.builder().description("Type Test").build());
    }

    @Test
    public void testViewValises() throws Exception {
        Valise valise = valiseRepository.save(Valise.builder()
                .description("Test Valise")
                .client(client)
                .typeValise(typeValise)
                .build());

        mockMvc.perform(get("/valise/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/valises_list"))
                .andExpect(model().attributeExists("valises"))
                .andExpect(model().attribute("valises", hasSize(1)))
                .andExpect(model().attribute("valises", hasItem(
                        hasProperty("description", is("Test Valise"))
                )));
    }

    @Test
    public void testCreateValiseForm() throws Exception {
        mockMvc.perform(get("/valise/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/valise_create"))
                .andExpect(model().attributeExists("valise"));
    }

    @Test
    public void testCreateValise() throws Exception {
        mockMvc.perform(post("/valise/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "New Valise")
                        .param("client.id", String.valueOf(client.getId()))
                        .param("typeValise.id", String.valueOf(typeValise.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valises/valises_list"));

        Valise savedValise = valiseRepository.findAll().get(0);
        assert savedValise.getDescription().equals("New Valise");
    }

    @Test
    public void testEditValiseForm() throws Exception {
        Valise valise = valiseRepository.save(Valise.builder()
                .description("Edit Test")
                .client(client)
                .typeValise(typeValise)
                .build());

        mockMvc.perform(get("/valise/edit/{id}", valise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("valises/valise_edit"))
                .andExpect(model().attributeExists("valise"))
                .andExpect(model().attribute("valise", hasProperty("description", is("Edit Test"))));
    }

    @Test
    public void testUpdateValise() throws Exception {
        Valise valise = valiseRepository.save(Valise.builder()
                .description("Update Test")
                .client(client)
                .typeValise(typeValise)
                .build());

        mockMvc.perform(post("/valise/edit/{id}", valise.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "Updated Description")
                        .param("client.id", String.valueOf(client.getId()))
                        .param("typeValise.id", String.valueOf(typeValise.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valises/valises_list"));

        Valise updatedValise = valiseRepository.findById(valise.getId()).orElse(null);
        assert updatedValise != null;
        assert updatedValise.getDescription().equals("Updated Description");
    }

    @Test
    public void testDeleteValise() throws Exception {
        Valise valise = valiseRepository.save(Valise.builder()
                .description("Delete Test")
                .client(client)
                .typeValise(typeValise)
                .build());

        mockMvc.perform(post("/valise/delete/{id}", valise.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/valises/valises_list"));

        assert valiseRepository.findById(valise.getId()).isEmpty();
    }
}
