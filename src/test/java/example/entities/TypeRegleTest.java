package example.entities;

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
        em.persist(regles1);

        Regle regles2 = new Regle();
        em.persist(regles2);

        List<Regle> listRegles = new ArrayList<>();
        listRegles.add(regles1);
        listRegles.add(regles2);

        typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("regle1");
        typeRegle.setNomTypeRegle("regle2");
        typeRegle.setListTypesRegles(listRegles);
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
        Assertions.assertNotNull(typeRegle.getListTypesRegles(), "la liste des regles ne devrait pas être null");
        Assertions.assertEquals(2, typeRegle.getListTypesRegles().size());
    }
    @Test
    public void testTypeRegleAssociationRegle() {
        List<Regle> associatedRegles = typeRegle.getListTypesRegles();
        Assertions.assertNotNull(associatedRegles, "La liste des règles associées ne devrait pas être nulle.");
        Assertions.assertEquals(2, associatedRegles.size(), "Le type de règle devrait être associé à deux règles.");
    }
    @Test
    public void testCascadeDeletion() {
        em.remove(typeRegle);
        em.flush();

        TypeRegle deletedTypeRegle = em.find(TypeRegle.class, typeRegle.getId());
        Assertions.assertNull(deletedTypeRegle, "TypeRegle devrait être supprimé.");

        List<Regle> remainingRegles = em.createQuery("SELECT r FROM Regle r", Regle.class).getResultList();
        Assertions.assertTrue(remainingRegles.isEmpty(), "Les règles associées devraient être supprimées en cascade.");
    }
}
