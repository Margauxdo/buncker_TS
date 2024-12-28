package example.integration.controllers;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TypeValiseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TypeValiseRepository typeValiseRepository;


    private TypeValise testTypeValise;
    private Valise testValise;
    private Client client1;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        valiseRepository.deleteAll();
        typeValiseRepository.deleteAll();

        assertEquals(0, typeValiseRepository.count(), "La base de données doit être vide après suppression !");
        Client client1 = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        clientRepository.save(client1);

        Valise val1 = Valise.builder()
                    .client(client1)
                    .description("descript val1")
                    .numeroValise(String.valueOf(25l))
                    .refClient("AW56")
                    .build();
        testValise = valiseRepository.save(val1);

        TypeValise baseTypeValise = TypeValise.builder()
                .description("Description par défaut")
                .proprietaire("Propriétaire par défaut")

                .build();

        testTypeValise = typeValiseRepository.save(baseTypeValise);


    }

}

