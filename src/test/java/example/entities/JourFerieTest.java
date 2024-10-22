package example.entities;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    @BeforeEach
    public void setUp() {
        Regle regle = new Regle();
        regle.setCoderegle("Code123"); // Ajoutez une valeur pour le champ obligatoire `coderegle`
        regle.setDateRegle(new Date());

        // Persist une instance de `Regle` avant de créer `JourFerie`
        entityManager.persist(regle);

        // Initialisation de l'entité `JourFerie`
        jourFerie = new JourFerie();
        jourFerie.setRegle(regle);  // Association avec une `Regle`
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
        assertNotNull(retrievedJourFerie.getRegle());
        assertEquals(jourFerie.getRegle().getId(), retrievedJourFerie.getRegle().getId());
    }


    @Test
    public void testJoursFerieListPersistence() {
        // Persist de `JourFerie` avec la liste des jours fériés
        entityManager.persist(jourFerie);
        entityManager.flush();

        // Charge `JourFerie` depuis la base de données
        JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());

        // Vérifie que la liste des jours fériés est bien sauvegardée
        List<Date> joursFerieList = retrievedJourFerie.getJoursFerieList();
        assertNotNull(joursFerieList);
        assertEquals(2, joursFerieList.size());
    }

}

