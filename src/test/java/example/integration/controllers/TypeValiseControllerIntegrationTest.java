package example.integration.controllers;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TypeValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TypeValiseRepository typeValiseRepository;


    private TypeValise testTypeValise;
    private Valise testValise;
    private Client client1;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        valiseRepository.deleteAll();
        typeValiseRepository.deleteAll();

        assertEquals(0, typeValiseRepository.count(), "La base de données doit être vide après suppression !");
        Client client1 = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client1);

        Valise val1 = Valise.builder()
                    .client(client1)
                    .description("descript val1")
                    .numeroValise(25l)
                    .refClient("AW56")
                    .build();
        testValise = valiseRepository.save(val1);

        TypeValise baseTypeValise = TypeValise.builder()
                .description("Description par défaut")
                .proprietaire("Propriétaire par défaut")
                .valise(val1)
                .build();

        testTypeValise = typeValiseRepository.save(baseTypeValise);


    }

    @Test
    void testViewTypeValises() throws Exception {
        TypeValise typeValise = TypeValise.builder()
                .description("description du type de ma valise")
                .proprietaire("John Doe")
                .build();
        typeValiseRepository.save(typeValise);

        mockMvc.perform(get("/typeValise/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_list"))
                .andExpect(model().attributeExists("typeValises"))
                .andExpect(model().attribute("typeValises", hasItem(
                        allOf(
                                hasProperty("proprietaire", is("John Doe")),
                                hasProperty("description", is("description du type de ma valise"))
                        )
                )));
    }


    @Test
    void testCreateTypeValiseForm() throws Exception {
        mockMvc.perform(get("/typeValise/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_create"))
                .andExpect(model().attributeExists("typeValise"));
    }



    @Test
    void testEditTypeValiseForm_Success() throws Exception {
        // Step 1: Create and save a TypeValise entity
        TypeValise typeValise = typeValiseRepository.save(TypeValise.builder()
                .description("Default Description")
                .proprietaire("Default Proprietaire")
                .build());

        // Step 2: Perform the GET request to retrieve the edit form
        mockMvc.perform(get("/typeValise/edit/{id}", typeValise.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/TV_edit")) // Ensure this matches the Thymeleaf view name
                .andExpect(model().attributeExists("typeValise")) // Check the model attribute existence
                .andExpect(model().attribute("typeValise", allOf(
                        hasProperty("proprietaire", is("Default Proprietaire")),
                        hasProperty("description", is("Default Description"))
                )));
    }



    @Test
    void testEditTypeValise() throws Exception {
        // Step 1: Create and save a TypeValise entity
        TypeValise typeValise = typeValiseRepository.save(TypeValise.builder()
                .description("Original Description")
                .proprietaire("Original Proprietaire")
                .build());

        // Step 2: Perform the edit operation
        mockMvc.perform(post("/typeValise/edit/{id}", typeValise.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("proprietaire", "Updated Proprietaire")
                        .param("description", "Updated Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeValises/TV_list")); // Ensure consistency with the URL

        // Step 3: Verify the updated values in the database
        TypeValise updatedTypeValise = typeValiseRepository.findById(typeValise.getId()).orElse(null);

        assert updatedTypeValise != null;
        assert "Updated Proprietaire".equals(updatedTypeValise.getProprietaire());
        assert "Updated Description".equals(updatedTypeValise.getDescription());
    }


    @Test
    void testDeleteTypeValise_Success() throws Exception {
        // Arrange
        TypeValise typeValise = TypeValise.builder()
                .description("Description du type de valise")
                .proprietaire("John Doe")
                .build();
        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        // Act & Assert
        mockMvc.perform(post("/typeValise/delete/{id}", savedTypeValise.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeValises/TV_list"));

        // Verify
        assertTrue(typeValiseRepository.findById(savedTypeValise.getId()).isEmpty());
    }



    @Test
    void testDeleteTypeValise_NotFound() throws Exception {
        mockMvc.perform(post("/typeValise/delete/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("typeValises/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("TypeValise avec l'ID 999 non trouvé !")));
    }
}

