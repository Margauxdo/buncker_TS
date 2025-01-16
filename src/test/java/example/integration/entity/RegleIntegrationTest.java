package example.integration.entity;

import example.entity.*;
import example.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RegleIntegrationTest {

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        regleRepository.deleteAll();
        typeRegleRepository.deleteAll();
        sortieSemaineRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testCreateRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setReglePourSortie("Règle de test");
        regle.setDateRegle(new Date());
        regle.setNombreJours(5);

        // Act
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Assert
        assertNotNull(savedRegle.getId());
        assertEquals("R001", savedRegle.getCoderegle());
        assertEquals("Règle de test", savedRegle.getReglePourSortie());
    }

    @Test
    public void testUpdateRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R002");
        regle.setNombreJours(7);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Act
        savedRegle.setCoderegle("R002-Updated");
        savedRegle.setNombreJours(10);
        Regle updatedRegle = regleRepository.saveAndFlush(savedRegle);

        // Assert
        assertEquals("R002-Updated", updatedRegle.getCoderegle());
        assertEquals(10, updatedRegle.getNombreJours());
    }

    @Test
    public void testDeleteRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R003");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Act
        regleRepository.deleteById(savedRegle.getId());
        Optional<Regle> deletedRegle = regleRepository.findById(savedRegle.getId());

        // Assert
        assertFalse(deletedRegle.isPresent());
    }

    @Test
    public void testFindRegleById() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R004");
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Act
        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());

        // Assert
        assertTrue(foundRegle.isPresent());
        assertEquals("R004", foundRegle.get().getCoderegle());
    }


    @Test
    public void testRegleWithTypeRegle() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type 1");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        Regle regle = new Regle();
        regle.setCoderegle("R006");
        regle.setTypeRegle(savedTypeRegle);
        Regle savedRegle = regleRepository.saveAndFlush(regle);

        // Act
        Optional<Regle> foundRegle = regleRepository.findById(savedRegle.getId());

        // Assert
        assertTrue(foundRegle.isPresent());
        assertNotNull(foundRegle.get().getTypeRegle());
        assertEquals("Type 1", foundRegle.get().getTypeRegle().getNomTypeRegle());
    }
}
