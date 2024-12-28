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
        void testDeleteTypeRegle_NotFound() throws Exception {
            // Tester la suppression d'un TypeRegle inexistant
            mockMvc.perform(post("/typeRegle/delete/999"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("typeRegles/error"))
                    .andExpect(model().attributeExists("errorMessage"));
        }
    }


