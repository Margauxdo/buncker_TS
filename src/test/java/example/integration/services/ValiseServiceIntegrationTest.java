package example.integration.services;

import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeValise;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import example.services.ValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("testintegration")
@Transactional
class ValiseServiceIntegrationTest {

    @Autowired
    private ValiseService valiseService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private RegleRepository regleRepository;

    private Client client;
    private TypeValise typeValise;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .telephoneExploitation("123456789")
                .build();
        client = clientRepository.save(client);

        typeValise = TypeValise.builder()
                .proprietaire("Propriétaire 1")
                .description("Description TypeValise")
                .build();
        typeValise = typeValiseRepository.save(typeValise);
    }







}
