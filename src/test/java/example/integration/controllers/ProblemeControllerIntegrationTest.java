package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.DTO.ProblemeDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@Transactional
public class ProblemeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProblemeRepository problemeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
        clientRepository.deleteAll();
        valiseRepository.deleteAll();
    }

    @Test
    public void testListProblemes_Success() throws Exception {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .build();
        problemeRepository.save(probleme);

        mockMvc.perform(get("/pb/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("problemes/pb_list"))
                .andExpect(model().attributeExists("problemes"))
                .andExpect(model().attribute("problemes", hasSize(1)))
                .andDo(print());
    }




    @Test
    public void testDeleteProbleme_Success() throws Exception {
        Probleme probleme = Probleme.builder()
                .descriptionProbleme("Test Description")
                .detailsProbleme("Test Details")
                .build();
        Probleme savedProbleme = problemeRepository.save(probleme);

        mockMvc.perform(post("/pb/delete/" + savedProbleme.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pb/list"))
                .andDo(print());

        Optional<Probleme> deletedProbleme = problemeRepository.findById(savedProbleme.getId());
        assertTrue(deletedProbleme.isEmpty());
    }
}

