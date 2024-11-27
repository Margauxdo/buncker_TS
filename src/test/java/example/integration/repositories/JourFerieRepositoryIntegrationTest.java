package example.integration.repositories;

import example.entity.JourFerie;
import example.entity.Regle;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class JourFerieRepositoryIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
    }

    @Test
    public void testSaveJourFerie() {
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        assertThat(savedJourFerie.getId()).isNotNull();
        assertThat(savedJourFerie.getDate()).isNotNull();
    }

    @Test
    public void testFindJourFerieById() {
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        Optional<JourFerie> foundJourFerie = jourFerieRepository.findById(savedJourFerie.getId());
        assertThat(foundJourFerie).isPresent();
        assertThat(foundJourFerie.get().getDate()).isEqualTo(jourFerie.getDate());
    }

    @Test
    public void testFindAllJourFeries() {
        JourFerie jourFerie1 = JourFerie.builder().date(new Date()).build();
        JourFerie jourFerie2 = JourFerie.builder().date(new Date()).build();

        jourFerieRepository.save(jourFerie1);
        jourFerieRepository.save(jourFerie2);

        List<JourFerie> jourFeries = jourFerieRepository.findAll();
        assertThat(jourFeries).hasSize(2);
    }

    @Test
    public void testDeleteJourFerie() {
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);
        jourFerieRepository.delete(savedJourFerie);

        Optional<JourFerie> deletedJourFerie = jourFerieRepository.findById(savedJourFerie.getId());
        assertThat(deletedJourFerie).isNotPresent();
    }


    @Test
    public void testSaveJourFerieWithoutDateFails() {
        JourFerie jourFerie = JourFerie.builder().build();

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> jourFerieRepository.save(jourFerie)
        );

        assertThat(exception.getMessage()).contains("not-null property references a null or transient value");
    }
    @Test
    public void testFindByIdNotFound() {
        Optional<JourFerie> jourFerie = jourFerieRepository.findById(-1);
        assertThat(jourFerie).isNotPresent();
    }
    @Test
    public void testUpdateJourFerie() {
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        savedJourFerie.setDate(new Date());
        JourFerie updatedJourFerie = jourFerieRepository.save(savedJourFerie);

        assertThat(updatedJourFerie.getDate()).isNotNull();
        assertThat(updatedJourFerie.getId()).isEqualTo(savedJourFerie.getId());
    }

    @Test
    public void testDeleteJourFerieWithoutRegles() {
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        jourFerieRepository.delete(savedJourFerie);

        Optional<JourFerie> deletedJourFerie = jourFerieRepository.findById(savedJourFerie.getId());
        assertThat(deletedJourFerie).isNotPresent();
    }








}
