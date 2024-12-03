package example.integration.controllers;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.interfaces.ITypeRegleService;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import example.services.RegleService;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;





import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class TypeRegleControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ITypeRegleService typeRegleService;
    @Autowired
    private RegleService regleService;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeRegleRepository typeRegleRepository;


    @Test
    void testViewAllTypeRegles() throws Exception {
        // Créer et sauvegarder une entité Regle
        Regle regle = Regle.builder()
                .coderegle("R001")
                .reglePourSortie("Sortie Simple")
                .dateRegle(new Date())
                .build();
        regleRepository.save(regle);

        // Créer et sauvegarder une entité TypeRegle liée à l'entité Regle
        TypeRegle tr = TypeRegle.builder()
                .nomTypeRegle("type regle v1")
                .regle(regle)
                .build();
        typeRegleRepository.save(tr);

        // Effectuer une requête GET et valider les résultats
        mockMvc.perform(get("/typeRegle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TR_list"))
                .andExpect(model().attributeExists("typeRegles"))
                .andExpect(model().attribute("typeRegles", hasItem(
                        allOf(
                                hasProperty("nomTypeRegle"),
                                hasProperty("regle", allOf(
                                        hasProperty("coderegle")
                                ))
                        )
                )));
    }


    @Test
    void testCreateTypeRegle() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse("2024-01-01");

        Regle regle = new Regle();
        regle.setCoderegle("REG001");
        regle.setDateRegle(parsedDate);
        regle.setReglePourSortie("Description de la règle");
        regle = regleRepository.save(regle);

        mockMvc.perform(post("/typeRegle/create")
                        .contentType("application/x-www-form-urlencoded")
                        .param("nomTypeRegle", "NewType")
                        .param("regle", String.valueOf(regle.getId())))
                .andExpect(status().isFound()) // 302 Found pour une redirection
                .andExpect(header().string("Location", "/typeRegles/TR_list"));

        List<TypeRegle> typeRegles = typeRegleRepository.findAll();
        assertEquals(1, typeRegles.size());
        assertEquals("NewType", typeRegles.get(0).getNomTypeRegle());
        assertEquals(regle.getId(), typeRegles.get(0).getRegle().getId());
    }



    @Test
    void testEditTypeRegle() throws Exception {
        // Arrange: Create and persist a Regle
        Regle regle = new Regle();
        regle.setCoderegle("REG001");
        regle.setDateRegle(new Date());
        regle.setReglePourSortie("Description de test");
        regle = regleRepository.save(regle);

        // Arrange: Create and persist a TypeRegle associated with the Regle
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeToEdit");
        typeRegle.setRegle(regle);
        typeRegle = typeRegleRepository.save(typeRegle);

        // Act & Assert: Edit the TypeRegle
        mockMvc.perform(post("/typeRegle/edit/" + typeRegle.getId())
                        .param("nomTypeRegle", "UpdatedType")
                        .param("regle", String.valueOf(regle.getId())))
                .andExpect(status().is3xxRedirection()) // Assert redirect status
                .andExpect(redirectedUrl("/typeRegles/TR_list")); // Assert redirection URL

        // Assert: Verify the changes in the database
        TypeRegle updatedTypeRegle = typeRegleRepository.findById(typeRegle.getId()).orElseThrow();
        assertEquals("UpdatedType", updatedTypeRegle.getNomTypeRegle());
        assertEquals(regle.getId(), updatedTypeRegle.getRegle().getId());
    }




    @Test
    void testDeleteTypeRegle() throws Exception {
        // Arrange: Créez une Regle et un TypeRegle associés
        Regle regle = new Regle();
        regle.setCoderegle("REG001");
        regle.setDateRegle(new Date());
        regle.setReglePourSortie("Description pour test");
        regle = regleRepository.save(regle); // Persist la Regle

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeToDelete");
        typeRegle.setRegle(regle);
        typeRegle = typeRegleRepository.save(typeRegle); // Persist le TypeRegle

        // Act & Assert: Supprimez le TypeRegle
        mockMvc.perform(post("/typeRegle/delete/" + typeRegle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeRegles/TR_list"));

        // Vérifiez que le TypeRegle a été supprimé
        boolean exists = typeRegleRepository.existsById(typeRegle.getId());
        assertFalse(exists, "Le TypeRegle n'a pas été supprimé correctement.");
    }



    @Test
        void testDeleteTypeRegle_NotFound() throws Exception {
            // Tester la suppression d'un TypeRegle inexistant
            mockMvc.perform(post("/typeRegle/delete/999"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("typeRegles/error"))
                    .andExpect(model().attributeExists("errorMessage"));
        }
    }


