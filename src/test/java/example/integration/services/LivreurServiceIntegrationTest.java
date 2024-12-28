package example.integration.services;

import example.DTO.LivreurDTO;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.exceptions.RegleNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.LivreurService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
public class LivreurServiceIntegrationTest {

    @Autowired
    private LivreurService livreurService;

    private Mouvement mouvement;
    private Livreur livreur;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date(System.currentTimeMillis() + 86400000L)) // +1 day
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 172800000L)) // +2 days
                .build();

        livreur = Livreur.builder()
                .nomLivreur("John Doe")
                .codeLivreur("LIV123")
                .motDePasse("password")
                .mouvements((List<Mouvement>) mouvement)
                .build();
    }


}
