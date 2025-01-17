package example.integration.controllers;

import example.entity.Formule;
import example.entity.Regle;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integrationtest")
public class FormuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IFormuleService formuleService;

    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    void setUp() {
        formuleRepository.deleteAll();
    }

    // Test: Show create formule form
    @Test
    public void testCreateFormuleForm() throws Exception {
        mockMvc.perform(get("/formules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_create"))
                .andExpect(model().attributeExists("formule"))
                .andExpect(model().attributeExists("regles"));
    }

    // Test: Delete a formule
    @Test
    public void testDeleteFormule() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Formule 6")
                .formule("Description 6")
                .build();
        Formule savedFormule = formuleRepository.save(formule);

        // Act & Assert
        mockMvc.perform(post("/formules/delete/{id}", savedFormule.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/formules/list"));

        // Verify
        assertFalse(formuleRepository.findById(savedFormule.getId()).isPresent());
    }

    // Additional test: View formule list
    @Test
    public void testViewFormuleList() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Formule 1")
                .formule("Description 1")
                .build();

        Regle regle = Regle.builder()
                .coderegle("Regle 1")
                .formule(formule)
                .build();
        formule.setRegles(List.of(regle)); // Set the relationship
        formuleRepository.save(formule);

        // Act & Assert
        mockMvc.perform(get("/formules/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_list"))
                .andExpect(model().attributeExists("formules"));
    }


    // Additional test: View formule by ID
    @Test
    public void testViewFormuleById() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Formule 1")
                .formule("Description 1")
                .build();

        // Add associated Regle
        Regle regle = Regle.builder()
                .coderegle("Regle 1")
                .formule(formule) // Ensure bi-directional relationship
                .build();
        formule.setRegles(List.of(regle));

        Formule savedFormule = formuleRepository.save(formule);

        // Act & Assert
        mockMvc.perform(get("/formules/view/{id}", savedFormule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_detail"))
                .andExpect(model().attributeExists("formule"));
    }

}
