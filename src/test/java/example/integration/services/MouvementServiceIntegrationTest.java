package example.integration.services;

import example.DTO.ClientDTO;
import example.DTO.MouvementDTO;
import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.ClientService;
import example.services.MouvementService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MouvementServiceIntegrationTest {

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Valise valise;
    private Mouvement mouvement;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        Client client = Client.builder()
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

        mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 86400000L)) // +1 jour
                .valise(valise)
                .build();
        mouvementRepository.deleteAll();;
    }




}
