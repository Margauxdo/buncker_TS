package example.integration.repositories;

import example.entity.JourFerie;
import example.repositories.JourFerieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class JourFerieRepositoryIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
    }

    @Test
    public void testSaveJourFerieSuccess() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();

        // Act
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        // Assert
        assertThat(savedJourFerie.getId()).isNotNull();
        assertThat(savedJourFerie.getDate()).isNotNull();
    }

    @Test
    public void testFindJourFerieByIdSuccess() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        // Act
        Optional<JourFerie> foundJourFerie = jourFerieRepository.findById(savedJourFerie.getId());

        // Assert
        assertThat(foundJourFerie).isPresent();
        assertThat(foundJourFerie.get().getDate()).isEqualTo(savedJourFerie.getDate());
    }

    @Test
    public void testFindAllJourFeries() {
        // Arrange
        JourFerie jourFerie1 = JourFerie.builder().date(new Date()).build();
        JourFerie jourFerie2 = JourFerie.builder().date(new Date()).build();

        jourFerieRepository.save(jourFerie1);
        jourFerieRepository.save(jourFerie2);

        // Act
        List<JourFerie> jourFeries = jourFerieRepository.findAll();

        // Assert
        assertThat(jourFeries).hasSize(2);
    }

    @Test
    public void testDeleteJourFerieSuccess() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        // Act
        jourFerieRepository.delete(savedJourFerie);
        Optional<JourFerie> deletedJourFerie = jourFerieRepository.findById(savedJourFerie.getId());

        // Assert
        assertThat(deletedJourFerie).isNotPresent();
    }

    @Test
    public void testSaveJourFerieWithoutDateFails() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder().build();

        // Act & Assert
        Exception exception = assertThrows(
                Exception.class,
                () -> jourFerieRepository.save(jourFerie)
        );

        assertThat(exception.getMessage()).contains("not-null property references a null or transient value");
    }

    @Test
    public void testFindByIdNotFound() {
        // Act
        Optional<JourFerie> jourFerie = jourFerieRepository.findById(-1);

        // Assert
        assertThat(jourFerie).isNotPresent();
    }

    @Test
    public void testUpdateJourFerieSuccess() {
        // Arrange
        JourFerie jourFerie = JourFerie.builder()
                .date(new Date())
                .build();
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        // Act
        savedJourFerie.setDate(new Date());
        JourFerie updatedJourFerie = jourFerieRepository.save(savedJourFerie);

        // Assert
        assertThat(updatedJourFerie.getDate()).isNotNull();
        assertThat(updatedJourFerie.getId()).isEqualTo(savedJourFerie.getId());
    }
}
