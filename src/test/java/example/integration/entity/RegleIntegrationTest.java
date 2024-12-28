package example.integration.entity;

import example.entity.*;
import example.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RegleIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private TypeRegleRepository typeRegleRepository;
    ;
    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private JourFerieRepository jourFerieRepository;


    @BeforeEach
    public void setUp() {

        regleRepository.deleteAll();
    }

    @Test
    public void testSaveRegleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setNombreJours(5);

        Regle savedRegle = regleRepository.saveAndFlush(regle);

        assertNotNull(savedRegle.getId());
        assertEquals("R001", savedRegle.getCoderegle());
    }

    @Test
    public void testUpdateEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        savedRegle.setCoderegle("R002");
        Regle updatedRegle = regleRepository.saveAndFlush(savedRegle);

        assertEquals("R002", updatedRegle.getCoderegle());
    }

    @Test
    public void testDeleteEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R003");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        regleRepository.deleteById(savedRegle.getId());
        regleRepository.flush();

        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());
        assertFalse(foundRegle.isPresent());
    }


    @Test
    public void testFindEntityByIdSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R005");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());
        assertTrue(foundRegle.isPresent());
    }

    @Test
    public void testFindEntityByIdNotFound() {
        Optional<Regle> foundRegle = regleRepository.findById(-1);
        assertFalse(foundRegle.isPresent());
    }


    @Test
    @Transactional
    public void testSaveEntitySuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Règle test");
        regle.setDateRegle(new Date());
        regle.setNombreJours(7);

        Regle savedRegle = regleRepository.save(regle);

        Assertions.assertNotNull(savedRegle.getId(), "L'ID de la règle doit être généré.");
        Assertions.assertEquals("R001", savedRegle.getCoderegle(), "Le code de la règle doit correspondre.");
    }

}
