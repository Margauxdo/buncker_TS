package example.entity;

import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class JourFerieTest {

    @Autowired
    private EntityManager entityManager;

    private JourFerie jourFerie;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        Regle regle = new Regle();
        regle.setCoderegle("Code123");
        regle.setDateRegle(new Date());
        regleRepository.save(regle);

        entityManager.persist(regle);

        jourFerie = new JourFerie();
        jourFerie.setRegles((List<Regle>) regle);
        jourFerie.setJoursFerieList(Arrays.asList(new Date(), new Date()));
    }

    @Test
    public void testJourFeriePersistence() {
        // Persist de `JourFerie`
        entityManager.persist(jourFerie);
        entityManager.flush();

        // Vérifie que `JourFerie` est bien persisté
        assertNotNull(jourFerie.getId());
    }

    @Test
    public void testJourFerieRelationWithRegle() {
        // Persist de `JourFerie` pour que la relation soit sauvegardée
        entityManager.persist(jourFerie);
        entityManager.flush();

        // Charge `JourFerie` depuis la base de données
        JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());

        // Vérifie que la relation avec `Regle` est bien établie
        assertNotNull(retrievedJourFerie.getRegles());
        assertEquals(jourFerie.getRegles().get(jourFerie.getId()), retrievedJourFerie.getJoursFerieList().get(1));
    }


    @Test
    public void testJoursFerieListPersistence() {
        entityManager.persist(jourFerie);
        entityManager.flush();

        JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());

        List<Date> joursFerieList = retrievedJourFerie.getJoursFerieList();
        assertNotNull(joursFerieList);
        assertEquals(2, joursFerieList.size());
    }
    @Test
    public void testRelationWithFormule() {
        Formule formule = new Formule();
        formule.setFormule("Formule Test");
        entityManager.persist(formule);

        JourFerie jourFerie = new JourFerie();
        jourFerie.setJoursFerieList((List<Date>) formule); // Ajout de la relation
        jourFerie.setJoursFerieList(Arrays.asList(new Date()));

        entityManager.persist(jourFerie);
        entityManager.flush();

        JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());
        assertNotNull(retrievedJourFerie.getJoursFerieList());
        assertEquals(((List<?>) formule).get(1), retrievedJourFerie.getJoursFerieList().get(1));
    }


}

