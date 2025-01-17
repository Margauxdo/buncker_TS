package example.integration.controllers;

import example.entity.JourFerie;
import example.repositories.JourFerieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class JourFerieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @BeforeEach
    void setUp() {
        jourFerieRepository.deleteAll();
    }

    @Test
    public void testCreateJourFerie_Success() throws Exception {
        mockMvc.perform(post("/jourferies/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2024-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        JourFerie savedJourFerie = jourFerieRepository.findAll().get(0);
        assertEquals("2024-01-01", new SimpleDateFormat("yyyy-MM-dd").format(savedJourFerie.getDate()));
    }




    @Test
    public void testListJourFerie() throws Exception {
        // Arrange: Create and save a JourFerie entity
        JourFerie jourFerie = JourFerie.builder()
                .date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
                .build();
        jourFerieRepository.save(jourFerie);

        // Act & Assert: Perform a GET request and verify the response
        mockMvc.perform(get("/jourferies/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("joursFeries/JF_list")) // Corrected expected view name
                .andExpect(model().attributeExists("jourFeries"))
                .andExpect(model().attribute("jourFeries", hasSize(1)))
                .andExpect(model().attribute("jourFeries", hasItem(
                        allOf(
                                hasProperty("id", notNullValue()),
                                hasProperty("date", is(jourFerie.getDate()))
                        )
                )));
    }





    @Test
    public void testDeleteJourFerie() throws Exception {
        JourFerie jourFerie = JourFerie.builder()
                .date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
                .build();
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        mockMvc.perform(post("/jourferies/delete/{id}", savedJourFerie.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jourferies/list"));

        assertTrue(jourFerieRepository.findById(savedJourFerie.getId()).isEmpty());
    }
}
