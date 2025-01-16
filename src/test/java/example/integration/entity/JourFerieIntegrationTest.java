package example.integration.entity;

import example.entity.JourFerie;
import example.repositories.JourFerieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class JourFerieIntegrationTest {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @BeforeEach
    public void setUp() {
        jourFerieRepository.deleteAll();
    }

    @Test
    void testCreateJourFerie() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());

        // Act
        JourFerie savedJourFerie = jourFerieRepository.saveAndFlush(jourFerie);

        // Assert
        assertNotNull(savedJourFerie.getId());
        assertEquals(jourFerie.getDate(), savedJourFerie.getDate());
    }

    @Test
    void testReadJourFerie() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        Date currentDate = new Date();

        // Truncate time from the current date
        Date truncatedDate = truncateTime(currentDate);

        jourFerie.setDate(truncatedDate);
        JourFerie savedJourFerie = jourFerieRepository.saveAndFlush(jourFerie);

        // Act
        Optional<JourFerie> foundJourFerie = jourFerieRepository.findById(savedJourFerie.getId());

        // Assert
        assertTrue(foundJourFerie.isPresent());
        assertEquals(savedJourFerie.getId(), foundJourFerie.get().getId());
        assertEquals(truncatedDate, foundJourFerie.get().getDate()); // Compare truncated dates
    }

    // Helper method to truncate time from a Date object
    private Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    @Test
    void testUpdateJourFerie() throws InterruptedException {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date()); // Initial date
        JourFerie savedJourFerie = jourFerieRepository.saveAndFlush(jourFerie);

        // Simulate a delay to ensure the updated date is different
        Thread.sleep(1000); // 1 second delay

        // Act
        Date newDate = new Date(); // New date for update
        savedJourFerie.setDate(newDate);
        JourFerie updatedJourFerie = jourFerieRepository.saveAndFlush(savedJourFerie);

        // Assert
        assertEquals(savedJourFerie.getId(), updatedJourFerie.getId());
        assertEquals(newDate, updatedJourFerie.getDate()); // Verify the date was updated
    }

    @Test
    void testDeleteJourFerie() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(new Date());
        JourFerie savedJourFerie = jourFerieRepository.saveAndFlush(jourFerie);

        // Act
        jourFerieRepository.delete(savedJourFerie);
        Optional<JourFerie> deletedJourFerie = jourFerieRepository.findById(savedJourFerie.getId());

        // Assert
        assertTrue(deletedJourFerie.isEmpty());
    }

    @Test
    void testCreateJourFerieWithNullDateThrowsException() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(null); // Null date to trigger validation

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> jourFerieRepository.saveAndFlush(jourFerie));
    }
}
