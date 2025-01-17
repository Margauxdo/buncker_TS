package example.integration.controllers;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class MouvementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testListMouvement_Success() throws Exception {
        // Arrange
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse("2025-01-17T10:00"))
                .statutSortie("SORTIE")
                .dateSortiePrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-20"))
                .dateRetourPrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-25"))
                .build();
        mouvementRepository.save(mouvement);

        // Act & Assert
        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andExpect(model().attribute("mouvements", hasSize(1)))
                .andExpect(model().attribute("mouvements", hasItem(
                        allOf(
                                hasProperty("dateHeureMouvement", is(mouvement.getDateHeureMouvement())),
                                hasProperty("statutSortie", is("SORTIE"))
                        )
                )))
                .andDo(print());
    }

    @Test
    public void testListMouvement_Empty() throws Exception {
        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andExpect(model().attribute("mouvements", hasSize(0)))
                .andDo(print());
    }


    @Test
    public void testDeleteMouvement_Success() throws Exception {
        // Arrange
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse("2025-01-17T10:00"))
                .statutSortie("SORTIE")
                .dateSortiePrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-20"))
                .dateRetourPrevue(new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-25"))
                .build();
        mouvement = mouvementRepository.save(mouvement);

        // Act & Assert
        mockMvc.perform(post("/mouvements/delete/" + mouvement.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mouvements/list"))
                .andDo(print());

        // Verify deletion
        assertEquals(0, mouvementRepository.findAll().size());
    }


}
