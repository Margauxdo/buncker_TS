package example.entity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TypeRegleTest {
    @Autowired
    private EntityManager em;
    private TypeRegle typeRegle;

    @BeforeEach
    public void setUp() throws Exception {
        Regle regles1 = new Regle();
        regles1.setCoderegle("CODE123");
        em.persist(regles1);

        Regle regles2 = new Regle();
        regles2.setCoderegle("CODE456");
        em.persist(regles2);

        List<Regle> listRegles = new ArrayList<>();
        listRegles.add(regles1);
        listRegles.add(regles2);

        typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("regle1");
        typeRegle.setNomTypeRegle("regle2");
        typeRegle.setRegle(regles1);
        em.persist(typeRegle);
        em.flush();

    }
    @Test
    public void testTypeReglePersistence() {
        Assertions.assertNotNull(typeRegle.getId());
    }
    @Test
    public void testTypeRegleNomTypeREgle() {
        Assertions.assertNotNull(typeRegle.getNomTypeRegle(), "regle1");
        Assertions.assertNotNull(typeRegle.getNomTypeRegle(), "regle2");

    }
    @Test
    public void testTypeRegleListTypesREgle() {
        Assertions.assertNotNull(typeRegle.getNomTypeRegle(), "the list of rules should not be null");
        Assertions.assertEquals(2, typeRegle.getRegle().getTypeRegles().get(0));
    }
    @Test
    public void testTypeRegleAssociationRegle() {
        List<Regle> associatedRegles = (List<Regle>) typeRegle.getRegle();
        Assertions.assertNotNull(associatedRegles, "The list of associated rules should not be null.");
        Assertions.assertEquals(2, associatedRegles.size(), "The rule type should be associated with two rules.");
    }
    @Test
    public void testCascadeDeletion() {
        em.remove(typeRegle);
        em.flush();

        TypeRegle deletedTypeRegle = em.find(TypeRegle.class, typeRegle.getId());
        Assertions.assertNull(deletedTypeRegle, "Type Rule should be deleted.");

        List<Regle> remainingRegles = em.createQuery("SELECT r FROM Regle r", Regle.class).getResultList();
        Assertions.assertTrue(remainingRegles.isEmpty(), "Associated rules should be deleted in cascade.");
    }
}
