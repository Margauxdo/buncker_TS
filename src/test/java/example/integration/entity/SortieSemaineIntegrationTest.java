package example.integration.entity;

import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class SortieSemaineIntegrationTest {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testCreateSortieSemaine() {
        // Arrange
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());

        // Act
        SortieSemaine savedSortie = sortieSemaineRepository.saveAndFlush(sortieSemaine);

        // Assert
        assertNotNull(savedSortie.getId());
        assertNotNull(savedSortie.getDateSortieSemaine());
    }

    @Test
    public void testUpdateSortieSemaine() {
        // Arrange
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        SortieSemaine savedSortie = sortieSemaineRepository.saveAndFlush(sortieSemaine);

        // Act
        Date newDate = new Date();
        savedSortie.setDateSortieSemaine(newDate);
        SortieSemaine updatedSortie = sortieSemaineRepository.saveAndFlush(savedSortie);

        // Assert
        assertEquals(newDate, updatedSortie.getDateSortieSemaine());
    }

    @Test
    public void testDeleteSortieSemaine() {
        // Arrange
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        SortieSemaine savedSortie = sortieSemaineRepository.saveAndFlush(sortieSemaine);

        // Act
        sortieSemaineRepository.deleteById(savedSortie.getId());
        Optional<SortieSemaine> deletedSortie = sortieSemaineRepository.findById(savedSortie.getId());

        // Assert
        assertFalse(deletedSortie.isPresent());
    }

    @Test
    public void testSortieSemaineWithRegles() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setNombreJours(5);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.getRegles().add(regle);
        regle.setSortieSemaine(sortieSemaine);

        SortieSemaine savedSortie = sortieSemaineRepository.saveAndFlush(sortieSemaine);

        // Act
        Optional<SortieSemaine> foundSortie = sortieSemaineRepository.findById(savedSortie.getId());

        // Assert
        assertTrue(foundSortie.isPresent());
        assertEquals(1, foundSortie.get().getRegles().size());
        assertEquals("R001", foundSortie.get().getRegles().get(0).getCoderegle());
    }

    @Test
    public void testFindSortieSemaineByIdNotFound() {
        // Act
        Optional<SortieSemaine> foundSortie = sortieSemaineRepository.findById(9999);

        // Assert
        assertFalse(foundSortie.isPresent());
    }


}
