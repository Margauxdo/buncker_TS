package example.entity;

import example.services.FormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
    @DataJpaTest
    @ActiveProfiles("test")
    public class FormuleTest {

        private static final Logger logger = LoggerFactory.getLogger(FormuleTest.class);

        @Autowired
        private FormuleRepository formuleRepository;

        @MockBean
        private RegleRepository regleRepository;
        @MockBean
        private EntityManager em;

        private FormuleService formuleService;
        private Formule formule;
        private Regle regle;

        @BeforeEach
        public void setUp() {
            logger.info("Initialisation des tests...");
            formuleService = new FormuleService(formuleRepository, regleRepository);

            // Initialisation de la règle
            regle = new Regle();
            regle.setId(1);
            regle.setCoderegle("code rule");
            regle.setReglePourSortie("Test Rule");

            when(regleRepository.existsById(1)).thenReturn(true);
            when(regleRepository.findById(1)).thenReturn(Optional.of(regle));

            logger.info("Création de la règle : {}", regle);

            // Création de la formule et association à la règle
            formule = new Formule();
            formule.setLibelle("Sample Libelle");
            formule.setFormule("Sample Formula");
            formule.setRegle(regle);

            // Persist la règle et la formule
            em.persist(regle); // Persist la règle pour garantir la relation
            formule = formuleRepository.save(formule); // Persist la formule

            logger.info("Formule initialisée : {}", formule);
        }

        @Test
        public void testCreateFormule() {
            logger.info("Démarrage du test : testCreateFormule");
            Formule newFormule = new Formule();
            newFormule.setLibelle("New Libelle");
            newFormule.setFormule("New Formula");
            newFormule.setRegle(regle);

            Formule savedFormule = formuleService.createFormule(newFormule);

            logger.info("Formule créée avec succès : {}", savedFormule);

            assertNotNull(savedFormule.getId(), "ID should not be null after saving");
            assertEquals("New Libelle", savedFormule.getLibelle(), "Libelle should match the set value");
            verify(formuleRepository, times(1)).save(newFormule);
        }

        @Test
        public void testGetFormuleById() {
            logger.info("Démarrage du test : testGetFormuleById");
            Optional<Formule> foundFormule = formuleRepository.findById(formule.getId());

            assertTrue(foundFormule.isPresent(), "Formule should be found by ID");
            assertEquals("Sample Libelle", foundFormule.get().getLibelle(), "Found Formule should match the saved one");
        }

        @Test
        public void testUpdateFormule() {
            logger.info("Démarrage du test : testUpdateFormule");
            formule.setLibelle("Updated Libelle");
            Formule updatedFormule = formuleService.updateFormule(formule.getId(), formule);

            assertEquals("Updated Libelle", updatedFormule.getLibelle(), "Libelle should be updated");
        }

        @Test
        public void testDeleteFormule() {
            logger.info("Démarrage du test : testDeleteFormule");
            formuleService.deleteFormule(formule.getId());

            Optional<Formule> deletedFormule = formuleRepository.findById(formule.getId());

            assertFalse(deletedFormule.isPresent(), "Formule should be deleted");
        }

        @Test
        public void testNonNullLibelleConstraint() {
            logger.info("Démarrage du test : testNonNullLibelleConstraint");
            Formule invalidFormule = new Formule();
            invalidFormule.setFormule("Invalid Formula");

            Exception exception = assertThrows(Exception.class, () -> formuleRepository.saveAndFlush(invalidFormule));
            assertTrue(exception.getMessage().contains("not-null"), "Exception should mention 'not-null'");
        }
    }
