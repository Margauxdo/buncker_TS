package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Livreur;
import example.repositories.LivreurRepository;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class LivreurControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        livreurRepository.deleteAll();
    }

    @Test
    public void testListLivreurs_Success() throws Exception {
        Livreur livreur = Livreur.builder()
                .codeLivreur("CODE123")
                .nomLivreur("John")
                .prenomLivreur("Doe")
                .numeroCartePro("12345")
                .telephonePortable("0123456789")
                .build();
        livreurRepository.save(livreur);

        mockMvc.perform(get("/livreurs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_list"))
                .andExpect(model().attributeExists("livreurs"))
                .andExpect(model().attribute("livreurs", hasSize(1)))
                .andExpect(model().attribute("livreurs", hasItem(
                        allOf(
                                hasProperty("codeLivreur", is("CODE123")),
                                hasProperty("nomLivreur", is("John")),
                                hasProperty("prenomLivreur", is("Doe"))
                        )
                )))
                .andDo(print());
    }

    @Test
    public void testDeleteLivreur_Failure_NotFound() throws Exception {
        mockMvc.perform(post("/livreurs/delete/999")) // Non-existent ID
                .andExpect(status().isOk()) // Error view returned
                .andExpect(view().name("livreurs/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Erreur lors de la suppression du livreur")))
                .andDo(print());
    }

    @Test
    public void testDeleteLivreur_Success() throws Exception {
        Livreur livreur = Livreur.builder()
                .codeLivreur("CODE123")
                .nomLivreur("John")
                .prenomLivreur("Doe")
                .numeroCartePro("12345")
                .telephonePortable("0123456789")
                .build();
        Livreur savedLivreur = livreurRepository.save(livreur);

        mockMvc.perform(post("/livreurs/delete/" + savedLivreur.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/livreurs/list"))
                .andDo(print());
    }


    @Test
    public void testViewLivreurDetails_Success() throws Exception {
        Livreur livreur = Livreur.builder()
                .codeLivreur("CODE789")
                .nomLivreur("Alice")
                .prenomLivreur("Smith")
                .numeroCartePro("67890")
                .telephonePortable("0123456789")
                .build();
        Livreur savedLivreur = livreurRepository.save(livreur);

        mockMvc.perform(get("/livreurs/view/" + savedLivreur.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_details"))
                .andExpect(model().attributeExists("livreur"))
                .andExpect(model().attribute("livreur", hasProperty("codeLivreur", is("CODE789"))))
                .andDo(print());
    }




}
