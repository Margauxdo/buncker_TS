package example.integration.controllers;

import example.entity.*;
import example.interfaces.IMouvementService;
import example.repositories.*;

import example.services.MouvementService;
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
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class MouvementControllerIntegrationTest {


        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private MouvementRepository mouvementRepository;
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private ValiseRepository valiseRepository;

        private Mouvement mouvement;
    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private MouvementService mouvementService;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {

        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }


    @Test
    public void testListMouvement() throws Exception {

        mockMvc.perform(get("/mouvements/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mouvements/mouv_list"))
                .andExpect(model().attributeExists("mouvements"))
                .andDo(print());
    }




}
