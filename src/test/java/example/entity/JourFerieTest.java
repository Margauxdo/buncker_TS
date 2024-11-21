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
import java.util.Collections;
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

        @Autowired
        private RegleRepository regleRepository;

        private JourFerie jourFerie;

        @BeforeEach
        public void setUp() {
            // Initialisation de la règle
            Regle regle = new Regle();
            regle.setCoderegle("Code123");
            regle.setDateRegle(new Date());
            regleRepository.save(regle);

            // Persist la règle
            entityManager.persist(regle);

            // Initialisation de JourFerie et association avec la règle
            jourFerie = new JourFerie();
            jourFerie.setRegles(Collections.singletonList(regle));  // Liste de règles associée
            jourFerie.setJoursFerieList(Arrays.asList(new Date(), new Date()));  // Liste de jours fériés

            // Persist JourFerie
            entityManager.persist(jourFerie);
            entityManager.flush();
        }

        @Test
        public void testPersistJourFerie() {
            JourFerie jourFerie = new JourFerie();
            jourFerie.setJoursFerieList(Arrays.asList(new Date(), new Date()));  // Liste de dates

            entityManager.persist(jourFerie);
            entityManager.flush();

            assertNotNull(jourFerie.getId(), "ID should not be null after persisting");
        }

        @Test
        public void testJourFerieRelationWithRegle() {
            Regle regle = new Regle();
            regle.setCoderegle("Code123");
            regle.setDateRegle(new Date());

            jourFerie.setRegles(Collections.singletonList(regle)); // Ajouter la règle au jour férié
            entityManager.persist(regle);
            entityManager.persist(jourFerie);  // Persist JourFerie

            entityManager.flush();

            JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());
            assertNotNull(retrievedJourFerie);
            assertEquals(1, retrievedJourFerie.getRegles().size(), "Should have one associated rule");
        }

        @Test
        public void testJoursFerieListPersistence() {
            jourFerie.setJoursFerieList(Arrays.asList(new Date(), new Date()));  // Liste de dates

            entityManager.persist(jourFerie);
            entityManager.flush();

            JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());
            assertNotNull(retrievedJourFerie.getJoursFerieList());
            assertEquals(2, retrievedJourFerie.getJoursFerieList().size(), "Should have two dates in joursFerieList");
        }

        @Test
        public void testRelationWithFormule() {
            Formule formule = new Formule();
            formule.setFormule("Formule Test");

            entityManager.persist(formule);  // Persist Formule

            jourFerie.setJoursFerieList(Arrays.asList(new Date()));  // Liste de dates
            entityManager.persist(jourFerie);  // Persist JourFerie

            entityManager.flush();

            JourFerie retrievedJourFerie = entityManager.find(JourFerie.class, jourFerie.getId());
            assertNotNull(retrievedJourFerie.getJoursFerieList());
            assertEquals(1, retrievedJourFerie.getJoursFerieList().size(), "Should have one date in joursFerieList");
        }
    }
