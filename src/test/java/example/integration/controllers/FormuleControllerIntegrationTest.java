package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Formule;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

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

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();
    }


    @Test
    public void testRegleRepositoryFindByCoderegle() {
        Regle regle = regleRepository.findByCoderegle("R001");
        System.out.println(regle);
    }



    @Test
    public void testGetAllFormules_shouldReturnEmptyList() throws Exception {
        // Act
        mockMvc.perform(get("/formules/api")
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetFormuleById_shouldReturnNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/formules/api/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFormule_shouldReturnCreatedFormule() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Sortie spéciale");
        regle.setDateRegle(new Date());
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("Libelle Test")
                .formule("Formule Test")
                .regle(regle)
                .build();

        // Act
        mockMvc.perform(post("/formules/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formule)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libelle", is("Libelle Test")))
                .andExpect(jsonPath("$.formule", is("Formule Test")))
                .andExpect(jsonPath("$.regle.coderegle", is("R001")));
    }



    @Test
    public void testCreateFormule_shouldReturnBadRequestForInvalidData() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .formule("Formule Test sans libelle")
                .build();

        // Act et Assert
        mockMvc.perform(post("/formules/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formule)))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testUpdateFormule_shouldUpdateFormuleWhenFormuleExists() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Libelle Initial")
                .formule("Formule Initiale")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        Formule updatedFormule = Formule.builder()
                .libelle("Libelle Mis à Jour")
                .formule("Formule Mise à Jour")
                .build();

        // Act
        mockMvc.perform(put("/formules/api/{id}", savedFormule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFormule)))
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle", is("Libelle Mis à Jour")))
                .andExpect(jsonPath("$.formule", is("Formule Mise à Jour")));
    }

    @Test
    public void testUpdateFormule_shouldReturnNotFoundWhenFormuleDoesNotExist() throws Exception {
        // Arrange
        Formule updatedFormule = Formule.builder()
                .libelle("Libelle Inexistant")
                .formule("Formule Inexistante")
                .build();

        // Act
        mockMvc.perform(put("/formules/api/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFormule)))
        // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFormule_shouldReturnNoContentWhenFormuleExists() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Libelle à Supprimer")
                .formule("Formule à Supprimer")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        // Act
        mockMvc.perform(delete("/formules/api/{id}", savedFormule.getId())
             .contentType(MediaType.APPLICATION_JSON))
        // Assert
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFormule_shouldReturnNotFoundWhenFormuleDoesNotExist() throws Exception {
        // Act
        mockMvc.perform(delete("/formules/api/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
        // Assert
           .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFormuleForm_shouldReturnFormuleCreateView() throws Exception {
        mockMvc.perform(get("/formules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("formule_create"))
                .andExpect(model().attributeExists("formule"));
    }

    @Test
    public void testCreateFormule_shouldCreateFormuleAndRedirect() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Sortie spéciale");
        regle.setDateRegle(new Date());
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("Libelle Test")
                .formule("Formule Test")
                .regle(regle)
                .build();

        // Act
        mockMvc.perform(post("/formules/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("libelle", formule.getLibelle())
                        .param("formule", formule.getFormule())
                        .param("regle", formule.getRegle().toString())) // Adaptation pour passer l'objet 'regle' selon le format attendu
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/formules/list"));
    }

    @Test
    public void testEditFormuleForm_shouldReturnFormuleEditView() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Libelle à Modifier")
                .formule("Formule à Modifier")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        // Act
        mockMvc.perform(get("/formules/edit/{id}", savedFormule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formule_edit"))
                .andExpect(model().attributeExists("formule"))
                .andExpect(model().attribute("formule", savedFormule));
    }

    @Test
    public void testUpdateFormule_shouldUpdateFormuleAndRedirect() throws Exception {
        // Arrange: Créez une règle si elle n'existe pas
        Regle regle = regleRepository.findByCoderegle("R001");
        if (regle == null) {
            regle = new Regle();
            regle.setCoderegle("R001");
            regle.setReglePourSortie("Sortie spéciale");
            regle.setDateRegle(new Date());
            regle = regleRepository.save(regle);
        }

        // Créez la formule initiale
        Formule formule = Formule.builder()
                .libelle("Libelle Initial")
                .formule("Formule Initiale")
                .regle(regle)
                .build();

        Formule savedFormule = formuleRepository.save(formule); // Sauvegarder la formule initiale

        // Préparez la formule mise à jour
        Formule updatedFormule = Formule.builder()
                .libelle("Libelle Mis à Jour")
                .formule("Formule Mise à Jour")
                .regle(regle)  // Utilisez la même règle
                .build();

        // Act: Faites une requête pour mettre à jour la formule
        mockMvc.perform(post("/formules/edit/{id}", savedFormule.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("libelle", updatedFormule.getLibelle())
                        .param("formule", updatedFormule.getFormule())
                        .param("regle", String.valueOf(updatedFormule.getRegle().getId())))
                .andExpect(status().is3xxRedirection()) // Redirection après l'update
                .andExpect(redirectedUrl("/formules/list")); // Vérification de la redirection vers la liste
    }


    @Test
    public void testDeleteFormule_shouldDeleteFormuleAndRedirect() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Formule à Supprimer")
                .formule("Formule à Supprimer")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        // Act
        mockMvc.perform(post("/formules/delete/{id}", savedFormule.getId()))
                // Assert
                .andExpect(status().is3xxRedirection()) // Vérifie la redirection
                .andExpect(redirectedUrl("/formules/list")); // Vérifie que la redirection va vers /formules/list

        // Vérification que la formule est bien supprimée
        mockMvc.perform(get("/formules/api/{id}", savedFormule.getId()))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testViewFormuleById_shouldReturnFormuleDetailView() throws Exception {
        // Arrange
        Formule formule = Formule.builder()
                .libelle("Formule Détail")
                .formule("Détails de la Formule")
                .build();

        Formule savedFormule = formuleRepository.save(formule);

        // Act
        mockMvc.perform(get("/formules/view/{id}", savedFormule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("formule_detail"))
                .andExpect(model().attribute("formule", savedFormule));
    }

    @Test
    public void testViewFormuleList_shouldReturnFormuleListView() throws Exception {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Sortie spéciale");
        regle.setDateRegle(new Date());
        regle = regleRepository.save(regle);

        Formule formule1 = Formule.builder()
                .libelle("Libelle 1")
                .formule("Formule 1")
                .regle(regle)
                .build();
        Formule formule2 = Formule.builder()
                .libelle("Libelle 2")
                .formule("Formule 2")
                .regle(regle)
                .build();

        formuleRepository.save(formule1);
        formuleRepository.save(formule2);

        // Act
        mockMvc.perform(get("/formules/list"))
                .andExpect(status().isOk())  // Vérification du status HTTP 200 OK
                .andExpect(view().name("formule_list"))  // Vérification de la vue renvoyée
                .andExpect(model().attributeExists("formules"))  // Vérification de l'existence de l'attribut "formules"
                .andExpect(model().attribute("formules", hasSize(2)))  // Vérification de la taille de la liste des formules
                .andExpect(model().attribute("formules", hasItem(
                        allOf(
                                hasProperty("libelle", is("Libelle 1")),
                                hasProperty("formule", is("Formule 1"))
                        )
                )))
                .andExpect(model().attribute("formules", hasItem(
                        allOf(
                                hasProperty("libelle", is("Libelle 2")),
                                hasProperty("formule", is("Formule 2"))
                        )
                )));
    }




}
