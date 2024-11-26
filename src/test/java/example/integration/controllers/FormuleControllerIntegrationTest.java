package example.integration.controllers;

import example.entity.Formule;
import example.repositories.FormuleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class FormuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();
    }

    // Test: View all formules
    @Test
    public void testViewFormuleList() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Formule Test")
                .formule("Description Test")
                .build();
        formuleRepository.save(formule);

        mockMvc.perform(get("/formules/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_list"))
                .andExpect(model().attribute("formules", hasSize(1)))
                .andExpect(model().attribute("formules", hasItem(
                        hasProperty("libelle", is("Formule Test"))
                )));
    }

    // Test: View a single formule by ID
    @Test
    public void testViewFormuleById() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Formule Test")
                .formule("Description Test")
                .build();
        formule = formuleRepository.save(formule);

        mockMvc.perform(get("/formules/view/{id}", formule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_detail"))
                .andExpect(model().attribute("formule", hasProperty("libelle", is("Formule Test"))));
    }

    // Test: Show create formule form
    @Test
    public void testCreateFormuleForm() throws Exception {
        mockMvc.perform(get("/formules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_create"))
                .andExpect(model().attributeExists("formule"));
    }

    // Test: Create a new formule
    @Test
    public void testCreateFormule() throws Exception {
        mockMvc.perform(post("/formules/create")
                        .param("libelle", "New Formule")
                        .param("formule", "This is a test formule"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/formules/list"));

        Formule savedFormule = formuleRepository.findAll().get(0);
        Assertions.assertEquals("New Formule", savedFormule.getLibelle());
        Assertions.assertEquals("This is a test formule", savedFormule.getFormule());
    }

    // Test: Show edit formule form
    @Test
    public void testEditFormuleForm() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Edit Test")
                .formule("Edit Description")
                .build();
        formule = formuleRepository.save(formule);

        mockMvc.perform(get("/formules/edit/{id}", formule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_edit"))
                .andExpect(model().attribute("formule", hasProperty("libelle", is("Edit Test"))));
    }

    // Test: Edit an existing formule
    @Test
    public void testUpdateFormule() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Old Formule")
                .formule("Old Description")
                .build();
        formule = formuleRepository.save(formule);

        mockMvc.perform(post("/formules/edit/{id}", formule.getId())
                        .param("libelle", "Updated Formule")
                        .param("formule", "Updated Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/formules/list"));

        Formule updatedFormule = formuleRepository.findById(formule.getId()).orElseThrow();
        Assertions.assertEquals("Updated Formule", updatedFormule.getLibelle());
        Assertions.assertEquals("Updated Description", updatedFormule.getFormule());
    }

    // Test: Delete a formule
    @Test
    public void testDeleteFormule() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Delete Test")
                .formule("Delete Description")
                .build();
        formule = formuleRepository.save(formule);

        // Act & Assert
        mockMvc.perform(post("/formules/delete/{id}", formule.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Expecting a redirection
                .andExpect(redirectedUrl("/formules/formules_list")); // Updated URL

        // Validate that the formule is removed from the database
        Assertions.assertTrue(formuleRepository.findById(formule.getId()).isEmpty());
    }


}
