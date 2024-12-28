package example.integration.entity;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Regle;
import example.entity.RetourSecurite;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.RegleRepository;
import example.repositories.RetourSecuriteRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MouvementIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        livreurRepository.deleteAll();
        retourSecuriteRepository.deleteAll();
        valiseRepository.deleteAll();
    }



}









