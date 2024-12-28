
package example.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.LivreurService;
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

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
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
    private MouvementRepository mouvementRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private LivreurService livreurService;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        livreurRepository.deleteAll();

    }



    @Test
    public void testListLivreurs_Success() throws Exception {
        mockMvc.perform(get("/livreurs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("livreurs/livreur_list"))
                .andExpect(model().attributeExists("livreurs"))
                .andDo(print());
    }



    @Test
    public void testDeleteLivreur_Failure_NotFound() throws Exception {
        mockMvc.perform(post("/livreurs/delete/999")) // ID inexistant
                .andExpect(status().isOk()) // Vue d'erreur retournée
                .andExpect(view().name("livreurs/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andDo(print());
    }












}
