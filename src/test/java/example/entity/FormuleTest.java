package example.entity;

import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // Utilise le profil pour H2
public class FormuleTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private RegleRepository regleRepository;

    private Formule formule;
    private Regle regleA;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'entité `Regle`
        regleA = new Regle();
        regleA.setCoderegle("code regle test");
        regleA.setReglePourSortie("Sortie Test");
        regleRepository.save(regleA);

        // Initialisation de l'entité `Formule`
        formule = Formule.builder()
                .libelle("libelle test de la formule")
                .formule("formule test")
                .regle(regleA)
                .build();
    }

    @Test
    public void testSaveFormule() {
        // Sauvegarder l'entité Formule
        em.persist(formule);
        em.flush();

        // Charger et vérifier l'entité sauvegardée
        Formule savedFormule = em.find(Formule.class, formule.getId());
        Assertions.assertNotNull(savedFormule);
        Assertions.assertEquals("libelle test de la formule", savedFormule.getLibelle());
        Assertions.assertEquals("formule test", savedFormule.getFormule());
    }

    @Test
    public void testValidationConstraintOnLibelle() {
        // Création d'une entité Formule invalide (libelle absent)
        Formule invalidFormule = Formule.builder()
                .formule("Description sans libelle")
                .build();

        // Vérifie que ConstraintViolationException est levée
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            em.persist(invalidFormule);
            em.flush();
        });
    }

    @Test
    public void testRelationWithRegle() {
        // Persister l'entité et vérifier la relation avec Regle
        em.persist(formule);
        em.flush();

        Formule savedFormule = em.find(Formule.class, formule.getId());
        Assertions.assertNotNull(savedFormule);
        Assertions.assertNotNull(savedFormule.getRegle());
        Assertions.assertEquals("code regle test", savedFormule.getRegle().getCoderegle());
    }

    @Test
    public void testNullableRelationWithRegle() {
        // Assigner null à la relation avec Regle
        formule.setRegle(null);
        em.persist(formule);
        em.flush();

        Formule savedFormule = em.find(Formule.class, formule.getId());
        Assertions.assertNotNull(savedFormule);
        Assertions.assertNull(savedFormule.getRegle());
    }
}
