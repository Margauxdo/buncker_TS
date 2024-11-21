package example.entity;

import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class SortieSemaineTest {

    @Autowired
    private EntityManager em;

    private SortieSemaine sortieSemaine;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Créer et persister une règle
        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        em.persist(regle);
        em.flush(); // S'assurer que Regle est persistée avant de l'associer à SortieSemaine

        // Créer et persister une SortieSemaine
        sortieSemaine = new SortieSemaine();
        sortieSemaine.setRegle(regle);
        sortieSemaine.setDateSortieSemaine(sdf.parse("2024-12-12"));
        em.persist(sortieSemaine);
        em.flush();
    }

    @Test
    public void testSortieSemainePersistence() {
        Assertions.assertNotNull(sortieSemaine.getId(), "SortieSemaine ID must not be null after persistence");
    }

    @Test
    public void testSortieSemaineDateSortieSemaine() {
        Assertions.assertNotNull(sortieSemaine.getDateSortieSemaine(), "Date de sortie de la semaine ne doit pas être nulle");
    }

    @Test
    public void testSortieSemaineRegleAssociation() {
        Assertions.assertNotNull(sortieSemaine.getRegle(), "SortieSemaine should be associated with a Regle");
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.toString(), sortieSemaine.getRegle().toString(),
                "Regle associated with SortieSemaine should match the persisted Regle");
    }

    @Test
    public void testUpdateDateSortieSemaine() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nouvelleDate = sdf.parse("2025-01-15");
        sortieSemaine.setDateSortieSemaine(nouvelleDate);
        em.merge(sortieSemaine); // Update the entity in the database
        em.flush(); // Ensure the changes are persisted

        SortieSemaine updatedSortieSemaine = em.find(SortieSemaine.class, sortieSemaine.getId());
        Assertions.assertEquals(nouvelleDate, updatedSortieSemaine.getDateSortieSemaine(),
                "The release date of the week should be updated correctly.");
    }
}
