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
    public void testViewFormuleList() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Formule 1")
                .formule("Description de la formule 1") // Utilisation correcte
                .build();
        formuleRepository.save(formule);

        mockMvc.perform(get("/formules/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_list"))
                .andExpect(model().attributeExists("formules"))
                .andExpect(model().attribute("formules", hasSize(1)))
                .andExpect(model().attribute("formules", hasItem(
                        hasProperty("libelle", is("Formule 1"))
                )))
                .andExpect(model().attribute("formules", hasItem(
                        hasProperty("formule", is("Description de la formule 1")) // Vérifie le bon attribut
                )));
    }


    @Test
    public void testViewFormuleDetails() throws Exception {
        Regle regle = new Regle();
        regle.setCoderegle("R001");

        Formule formule = Formule.builder()
                .libelle("Formule 2")
                .formule("Description 2")
                .regle(regle) // Associer une règle à la formule
                .build();
        Formule savedFormule = formuleRepository.save(formule);

        mockMvc.perform(get("/formules/view/{id}", savedFormule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_detail"))
                .andExpect(model().attribute("formule", hasProperty("libelle", is("Formule 2"))))
                .andExpect(model().attribute("formule", hasProperty("formule", is("Description 2"))))
                .andExpect(model().attribute("formule", hasProperty("regle", hasProperty("coderegle", is("R001"))))); // Vérification de la règle
    }



    @Test
    public void testCreateFormuleForm() throws Exception {
        mockMvc.perform(get("/formules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_create"))
                .andExpect(model().attributeExists("formule"));
    }




    @Test
    public void testEditFormuleForm() throws Exception {
        Formule formule = Formule.builder()
                .libelle("Formule 4")
                .formule("Description 4")
                .build();
        Formule savedFormule = formuleRepository.save(formule);

        mockMvc.perform(get("/formules/edit/" + savedFormule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formules/formule_edit"))
                .andExpect(model().attribute("formule", hasProperty("libelle", is("Formule 4"))));
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
