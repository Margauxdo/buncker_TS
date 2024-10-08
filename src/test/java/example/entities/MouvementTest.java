package example.entities;

import example.repositories.ValiseRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MouvementTest {

    @Autowired
    private EntityManager em;

    private Mouvement mouvement;

    @Autowired
    private ValiseRepository valiseRepository;

    private SimpleDateFormat sdf;

    @BeforeEach
    public void setUp() throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Create a Valise instance to associate with Mouvement
        Valise valise = new Valise();
        valiseRepository.save(valise);

        // Initialize Mouvement entity with proper Date values
        mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(sdf.parse("2024-10-25"));
        mouvement.setDateRetourPrevue(sdf.parse("2025-01-05"));
        mouvement.setDateSortiePrevue(sdf.parse("2025-01-01"));
        mouvement.setStatutSortie("close");
        mouvement.setValise(valise);

        em.persist(mouvement);
        em.flush();
    }

    @Test
    public void testMouvementPersistence() {
        Assertions.assertNotNull(mouvement.getId(), "Mouvement should have a non-null ID after persistence");
    }

    @Test
    public void testMouvementValiseAssociation() {
        // Verify that the associated Valise is correctly linked to the Mouvement
        Assertions.assertNotNull(mouvement.getValise(), "Mouvement should be associated with a Valise");
        Assertions.assertEquals(mouvement.getValise().getId(), valiseRepository.findAll().get(0).getId(), "Valise ID should match");
    }

    @Test
    public void testMouvementDateHeureMouvement() throws ParseException {
        // Verify that dateHeureMouvement is stored and retrieved correctly
        Date expectedDate = sdf.parse("2024-10-25");
        Assertions.assertEquals(expectedDate, mouvement.getDateHeureMouvement(), "dateHeureMouvement should match the expected date");
    }

    @Test
    public void testMouvementDateRetourPrevue() throws ParseException {
        // Verify that dateRetourPrevue is stored and retrieved correctly
        Date expectedDate = sdf.parse("2025-01-05");
        Assertions.assertEquals(expectedDate, mouvement.getDateRetourPrevue(), "dateRetourPrevue should match the expected date");
    }

    @Test
    public void testMouvementDateSortiePrevue() throws ParseException {
        // Verify that dateSortiePrevue is stored and retrieved correctly
        Date expectedDate = sdf.parse("2025-01-01");
        Assertions.assertEquals(expectedDate, mouvement.getDateSortiePrevue(), "dateSortiePrevue should match the expected date");
    }

    @Test
    public void testMouvementStatutSortie() {
        // Verify that statutSortie is stored and retrieved correctly
        Assertions.assertEquals("close", mouvement.getStatutSortie(), "statutSortie should match the expected value 'close'");
    }
}

