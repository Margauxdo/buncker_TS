
package example.integration.controllers;

import example.entity.*;
import example.interfaces.IRegleService;
import example.repositories.*;
import example.services.RegleService;
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

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class RegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IRegleService regleService;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        regleRepository.deleteAll();
    }

    @Test
    public void testViewAllRegles_Success() throws Exception {
        mockMvc.perform(get("/regles/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_list"))
                .andExpect(model().attributeExists("regles"))
                .andDo(print());
    }

    @Test
    public void testViewRegleById_Success() throws Exception {
        // Create and save a Regle
        Regle regle = regleRepository.save(Regle.builder()
                .coderegle("AW56")
                .reglePourSortie("regle WW1")
                .build());

        // Create and save a TypeRegle linked to the Regle
        TypeRegle typeRegle = typeRegleRepository.save(TypeRegle.builder()
                .nomTypeRegle("type regle XX")
                .regle(regle) // Associate with the Regle
                .build());

        // Perform the GET request
        mockMvc.perform(get("/regles/view/" + regle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("regles/regle_details"))
                .andExpect(model().attributeExists("regle"))
                .andExpect(model().attribute("regle", hasProperty("coderegle", is("AW56"))))
                .andDo(print());
    }


    @Test
    public void testViewRegleById_Failure() throws Exception {
        mockMvc.perform(get("/regles/view/999"))
                .andExpect(status().isOk()) // Ensure the controller returns HTTP 200 for the error page
                .andExpect(view().name("regles/error")) // Verify the error view is rendered
                .andExpect(model().attributeExists("errorMessage")) // Ensure the error message is present
                .andDo(print());
    }


    @Test
    public void testCreateRegle_Success() throws Exception {
        // Create and save a TypeRegle and Formule
        TypeRegle typeRegle = typeRegleRepository.save(TypeRegle.builder().nomTypeRegle("Test Type").build());
        Formule formule = formuleRepository.save(Formule.builder().libelle("Test Formule").build());

        // Perform the create operation
        mockMvc.perform(post("/regles/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("coderegle", "R2")
                        .param("dateRegle", "12/01/2024") // Format matches the @InitBinder logic
                        .param("typeRegle.id", String.valueOf(typeRegle.getId())) // Correct ID for relationships
                        .param("formule.id", String.valueOf(formule.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/list")) // Ensure redirection matches controller
                .andDo(print());

        // Validate the created Regle
        Regle savedRegle = regleRepository.findByCoderegle("R2");
        assertNotNull(savedRegle);
        assertEquals("R2", savedRegle.getCoderegle());
        assertEquals(typeRegle.getId(), savedRegle.getTypeRegle().getId());
        assertEquals(formule.getId(), savedRegle.getFormule().getId());
    }



    @Test
    public void testDeleteRegle_Success() throws Exception {
        // Create and save a TypeRegle
        TypeRegle typeRegle = typeRegleRepository.save(TypeRegle.builder().nomTypeRegle("Test Type").build());

        // Create and save a Formule
        Formule formule = formuleRepository.save(Formule.builder().libelle("Test Formule").build());

        // Create and save a Regle
        Regle regle = regleRepository.save(Regle.builder()
                .coderegle("R3")
                .typeRegle(typeRegle)
                .formule(formule)
                .dateRegle(new java.util.Date())
                .build());

        // Perform delete operation
        mockMvc.perform(post("/regles/delete/" + regle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/list"))

                .andDo(print());

        // Assert that the Regle no longer exists in the database
        assertFalse(regleRepository.existsById(regle.getId()));
    }




}
