package example.integration.controllers;

import example.DTO.SortieSemaineDTO;
import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class SortieSemaineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    private Regle regle;

    @BeforeEach
    public void setUp() {
        // Create and save a Regle entity for relationships
        regle = Regle.builder()
                .coderegle("R001")
                .dateRegle(new Date())
                .build();
        regle = regleRepository.save(regle);
    }

    @Test
    public void testCreateSortieSemaineForm() throws Exception {
        mockMvc.perform(get("/sortieSemaine/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_create"))
                .andExpect(model().attributeExists("sortieSemaine"));
    }



    @Test
    public void testGetSortieSemaineList_Success() throws Exception {
        // Save a SortieSemaine
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regles(List.of(regle))
                .build();
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        mockMvc.perform(get("/sortieSemaine/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_list"))
                .andExpect(model().attributeExists("sortiesSemaine"))
                .andExpect(model().attribute("sortiesSemaine", hasSize(1)))
                .andExpect(model().attribute("sortiesSemaine", hasItem(
                        hasProperty("id", is(sortieSemaine.getId()))
                )))
                .andDo(print());
    }


}
