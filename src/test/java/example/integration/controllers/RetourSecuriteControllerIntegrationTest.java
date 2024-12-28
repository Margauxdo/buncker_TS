
package example.integration.controllers;

import example.entity.RetourSecurite;
import example.repositories.RetourSecuriteRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("integrationtest")
@Transactional
public class RetourSecuriteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    private RetourSecurite savedRetourSecurite;

    @BeforeEach
    public void setUp() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(123456L)
                .datesecurite(new Date())
                .cloture(false)
                .build();
        savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
    }



}
