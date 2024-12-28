package example.integration.entity;

import example.DTO.JourFerieDTO;
import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import example.services.JourFerieService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class JourFerieIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private JourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
        regleRepository.deleteAll();
        formuleRepository.deleteAll();

    }



}
