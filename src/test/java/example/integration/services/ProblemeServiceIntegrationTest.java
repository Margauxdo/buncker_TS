package example.integration.services;

import example.DTO.ProblemeDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProblemeServiceIntegrationTest {

    @Autowired
    private ProblemeService problemeService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;
    private Valise valise;
    private Probleme probleme;

    @BeforeEach
    public void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        client = clientRepository.save(client);

        valise = Valise.builder()
                .description("Valise de test")
                .numeroValise(String.valueOf(123456L))
                .refClient("RefClientTest")
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        probleme = Probleme.builder()
                .descriptionProbleme("Test description")
                .detailsProbleme("Test details")
                .clients((List<Client>) client)
                .valise(valise)
                .build();
    }


}
