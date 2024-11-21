package example.entity;

import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TypeRegleTest {

    @Autowired
    private EntityManager em;

    private TypeRegle typeRegle;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() throws Exception {
        // Création de Regles
        Regle regles1 = new Regle();
        regles1.setCoderegle("CODE123");
        em.persist(regles1);

        Regle regles2 = new Regle();
        regles2.setCoderegle("CODE456");
        em.persist(regles2);

        // Création de TypeRegle
        typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeRegleTest");
        typeRegle.setRegle(regles1); // Associe une seule règle à ce type
        em.persist(typeRegle);
        em.flush();
    }

    @Test
    public void testTypeReglePersistence() {
        Assertions.assertNotNull(typeRegle.getId(), "TypeRegle ID must not be null after persistence");
    }

    @Test
    public void testTypeRegleNomTypeRegle() {
        Assertions.assertEquals("TypeRegleTest", typeRegle.getNomTypeRegle(), "TypeRegle name should match");
    }

    @Test
    public void testTypeRegleAssociationRegle() {
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertNotNull(typeRegle.getRegle(), "The associated Regle should not be null");
        Assertions.assertEquals(expectedRegle.getId(), typeRegle.getRegle().getId(), "The associated Regle ID should match");
    }

    @Test
    public void testCascadeDeletion() {
        em.remove(typeRegle);
        em.flush();

        // Verifies that TypeRegle is deleted
        TypeRegle deletedTypeRegle = em.find(TypeRegle.class, typeRegle.getId());
        Assertions.assertNull(deletedTypeRegle, "TypeRegle should be deleted");

        // Verify associated Regle is not deleted because cascade is not applied on Regle
        Regle regle = em.find(Regle.class, typeRegle.getRegle().getId());
        Assertions.assertNotNull(regle, "Associated Regle should not be deleted");
    }
}
