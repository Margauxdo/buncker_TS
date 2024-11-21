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
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class RegleManuelleTest {

    @Autowired
    private EntityManager em;

    private RegleManuelle regleManuelle;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Création de l'objet RegleManuelle
        regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("admin1");
        regleManuelle.setDescriptionRegle("description de la regle");
        regleManuelle.setDateRegle(sdf.parse("2025-12-12"));
        regleManuelle.setReglePourSortie("regle pour la fermeture de la valise");
        regleManuelle.setCoderegle("code 1234");
        regleManuelle.setCalculCalendaire(365L);
        regleManuelle.setFermeJS1(Boolean.FALSE);
        regleManuelle.setFermeJS2(Boolean.FALSE);
        regleManuelle.setFermeJS3(Boolean.FALSE);
        regleManuelle.setFermeJS4(Boolean.FALSE);
        regleManuelle.setFermeJS5(Boolean.FALSE);
        regleManuelle.setFermeJS6(Boolean.FALSE);
        regleManuelle.setFermeJS7(Boolean.FALSE);

        em.persist(regleManuelle);
        em.flush();
        em.refresh(regleManuelle);
    }

    @Test
    public void testRegleManuellePersistence() {
        Assertions.assertNotNull(regleManuelle.getId(),
                "Rule ID must not be null after persistence");
    }

    @Test
    public void testRegleManuelleDescriptionRegle() {
        Assertions.assertEquals("description de la regle", regleManuelle.getDescriptionRegle(),
                "Description should match");
    }

    @Test
    public void testRegleManuelleCreateurRegle() {
        Assertions.assertEquals("admin1", regleManuelle.getCreateurRegle(),
                "Creator should match");
    }

    @Test
    public void testRegleManuelleDateRegle() {
        Assertions.assertNotNull(regleManuelle.getDateRegle());
        RegleManuelle expectedRegle = (RegleManuelle) regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getDateRegle(), regleManuelle.getDateRegle(),
                "Regle Date should match");
    }

    @Test
    public void testRegleManuelleCodeRegle() {
        Assertions.assertNotNull(regleManuelle.getCoderegle());
        RegleManuelle expectedRegle = (RegleManuelle) regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getCoderegle(), regleManuelle.getCoderegle(),
                "Code Rule should match");
    }

    @Test
    public void testRegleManuelleReglePourSortie() {
        Assertions.assertNotNull(regleManuelle.getReglePourSortie(), "regle pour la fermeture de la valise");
        RegleManuelle expectedRegle = (RegleManuelle) regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getId(), regleManuelle.getId(),
                "The ID should match the ID in the repository");
    }

    @Test
    public void testRegleManuelleFermeJSFlags() {
        Assertions.assertNotNull(regleManuelle.getFermeJS1(), "FermeJS1 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS2(), "FermeJS2 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS3(), "FermeJS3 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS4(), "FermeJS4 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS5(), "FermeJS5 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS6(), "FermeJS6 must not be null");
        Assertions.assertNotNull(regleManuelle.getFermeJS7(), "FermeJS7 must not be null");

        RegleManuelle expectedRegle = (RegleManuelle) regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getId(), regleManuelle.getId(),
                "The Manual Rule ID should match the ID in the repository");
    }

    @Test
    public void testInheritedAttributesPersistence() {
        RegleManuelle persistedRegle = em.find(RegleManuelle.class, regleManuelle.getId());
        Assertions.assertNotNull(persistedRegle, "Manual rule should be persisted.");
        Assertions.assertEquals("code 1234", persistedRegle.getCoderegle(),
                "Code ruler should be 'code 1234'.");
    }

    @Test
    public void testUpdateDescriptionRegle() {
        regleManuelle.setDescriptionRegle("Nouvelle description");
        em.merge(regleManuelle);
        em.flush();

        RegleManuelle updatedRegle = em.find(RegleManuelle.class, regleManuelle.getId());
        Assertions.assertEquals("Nouvelle description", updatedRegle.getDescriptionRegle(),
                "Description Rule should be updated to 'Nouvelle description'.");
    }

    @Test
    public void testUpdateCreateurRegle() {
        regleManuelle.setCreateurRegle("new admin");
        em.merge(regleManuelle);
        em.flush();

        RegleManuelle updatedRegle = em.find(RegleManuelle.class, regleManuelle.getId());
        Assertions.assertEquals("new admin", updatedRegle.getCreateurRegle(),
                "Creator Rule should be updated to 'new admin'.");
    }

    @Test
    public void testCascadeDeleteRegles() {
        RegleManuelle persistedRegleManuelle = em.find(RegleManuelle.class, regleManuelle.getId());
        Assertions.assertNotNull(persistedRegleManuelle);

        em.remove(persistedRegleManuelle);
        em.flush();

        List<Regle> regles = em.createQuery("SELECT r FROM Regle r", Regle.class).getResultList();
        Assertions.assertTrue(((List<?>) regles).isEmpty(), "All associated Regle entities should be removed due to cascade delete");
    }
}
