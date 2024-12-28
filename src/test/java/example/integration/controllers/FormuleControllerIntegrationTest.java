package example.integration.controllers;

import example.entity.Formule;
import example.entity.Regle;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("integrationtest")
public class FormuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IFormuleService formuleService;

    @Autowired
    private FormuleRepository formuleRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        formuleRepository.deleteAll();
    }





    @Test
    public void testCreateFormuleForm() throws Exception {
        mockMvc.perform(get("/formules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_create"))
                .andExpect(model().attributeExists("formule"));
    }






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
        Assertions.assertTrue(formuleRepository.findById(savedFormule.getId()).isEmpty());
    }






}
