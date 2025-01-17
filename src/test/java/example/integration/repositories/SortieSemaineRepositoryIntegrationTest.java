package example.integration.repositories;

import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class SortieSemaineRepositoryIntegrationTest {

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
    public void testSaveSortieSemaine() {
        SortieSemaine sortie = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .build();

        SortieSemaine savedSortie = sortieSemaineRepository.save(sortie);

        assertThat(savedSortie.getId()).isNotNull();
        assertThat(savedSortie.getDateSortieSemaine()).isNotNull();
    }

    @Test
    public void testFindSortieSemaineById() {
        SortieSemaine sortie = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .build();

        SortieSemaine savedSortie = sortieSemaineRepository.save(sortie);
        Optional<SortieSemaine> foundSortie = sortieSemaineRepository.findById(savedSortie.getId());

        assertTrue(foundSortie.isPresent());
        assertEquals(savedSortie.getId(), foundSortie.get().getId());
    }

    @Test
    public void testFindAllSortieSemaines() {
        SortieSemaine sortie1 = SortieSemaine.builder().dateSortieSemaine(new Date()).build();
        SortieSemaine sortie2 = SortieSemaine.builder().dateSortieSemaine(new Date()).build();

        sortieSemaineRepository.save(sortie1);
        sortieSemaineRepository.save(sortie2);

        List<SortieSemaine> sorties = sortieSemaineRepository.findAll();
        assertThat(sorties).hasSize(2);
    }

    @Test
    public void testDeleteSortieSemaine() {
        SortieSemaine sortie = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .build();

        SortieSemaine savedSortie = sortieSemaineRepository.save(sortie);
        sortieSemaineRepository.delete(savedSortie);

        Optional<SortieSemaine> deletedSortie = sortieSemaineRepository.findById(savedSortie.getId());
        assertThat(deletedSortie).isNotPresent();
    }

    @Test
    public void testSaveSortieSemaineWithRegles() {
        Regle regle = Regle.builder().coderegle("R001").build();
        regle = regleRepository.save(regle);

        SortieSemaine sortie = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regles(List.of(regle))
                .build();

        SortieSemaine savedSortie = sortieSemaineRepository.save(sortie);

        assertThat(savedSortie.getId()).isNotNull();
        assertThat(savedSortie.getRegles()).hasSize(1);
        assertThat(savedSortie.getRegles().get(0).getCoderegle()).isEqualTo("R001");
    }

    @Test
    public void testDeleteSortieSemaineWithRegles() {
        Regle regle = Regle.builder().coderegle("R001").build();
        regle = regleRepository.save(regle);

        SortieSemaine sortie = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regles(List.of(regle))
                .build();

        SortieSemaine savedSortie = sortieSemaineRepository.save(sortie);
        sortieSemaineRepository.delete(savedSortie);

        Optional<SortieSemaine> deletedSortie = sortieSemaineRepository.findById(savedSortie.getId());
        assertThat(deletedSortie).isNotPresent();

        List<Regle> remainingRegles = regleRepository.findAll();
        assertTrue(remainingRegles.isEmpty()); // Vérifie le cascade
    }

    @Test
    public void testFindSortieSemaineByNonExistentId() {
        Optional<SortieSemaine> sortie = sortieSemaineRepository.findById(-1);
        assertThat(sortie).isNotPresent();
    }
}
