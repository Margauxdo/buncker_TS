package example.integration.controllers;

import example.entity.*;
import example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class RegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    void setUp() {
        regleRepository.deleteAll();
        typeRegleRepository.deleteAll();
        formuleRepository.deleteAll();
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
        // Création des entités liées
        TypeRegle typeRegle = typeRegleRepository.save(
                TypeRegle.builder().nomTypeRegle("Type Test").build());

        Formule formule = formuleRepository.save(
                Formule.builder().libelle("Formule Test").build());

        Regle regle = regleRepository.save(Regle.builder()
                .coderegle("AW56")
                .reglePourSortie("Regle Test")
                .typeRegle(typeRegle)
                .formule(formule)
                .dateRegle(new Date())
                .build());

        // Test de récupération par ID
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
                .andExpect(status().isOk())
                .andExpect(view().name("regles/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("non trouvé")))
                .andDo(print());
    }

    @Test
    public void testDeleteRegle_Success() throws Exception {
        TypeRegle typeRegle = typeRegleRepository.save(
                TypeRegle.builder().nomTypeRegle("Type Test").build());

        Formule formule = formuleRepository.save(
                Formule.builder().libelle("Formule Test").build());

        Regle regle = regleRepository.save(Regle.builder()
                .coderegle("R3")
                .typeRegle(typeRegle)
                .formule(formule)
                .dateRegle(new Date())
                .build());

        mockMvc.perform(post("/regles/delete/" + regle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/list"))
                .andDo(print());

        assertFalse(regleRepository.existsById(regle.getId()));
    }

    @Test
    public void testEditRegle_Success() throws Exception {
        Regle regle = regleRepository.save(Regle.builder()
                .coderegle("EDIT1")
                .reglePourSortie("Initial Rule")
                .build());

        mockMvc.perform(post("/regles/edit/" + regle.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("coderegle", "EDIT1_UPDATED")
                        .param("reglePourSortie", "Updated Rule"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/regles/list"))
                .andDo(print());

        Regle updatedRegle = regleRepository.findById(regle.getId()).orElse(null);
        assertNotNull(updatedRegle);
        assertEquals("EDIT1_UPDATED", updatedRegle.getCoderegle());
        assertEquals("Updated Rule", updatedRegle.getReglePourSortie());
    }
}
