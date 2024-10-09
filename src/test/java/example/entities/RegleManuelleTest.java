package example.entities;

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
public class RegleManuelleTest {

    @Autowired
    private EntityManager em;

    private RegleManuelle regleManuelle;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Initialiser et sauvegarder l'entité RegleManuelle
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

        em.persist(regleManuelle); // Persist l'entité dans la base de données
        em.flush(); // Force la persistance en base
        em.refresh(regleManuelle); // Rafraîchit l'entité pour s'assurer que l'ID est chargé
    }

    @Test
    public void testRegleManuellePersistence() {
        // Vérifier que l'ID de regleManuelle est bien attribué après persistance
        Assertions.assertNotNull(regleManuelle.getId(), "L'ID de la règle ne doit pas être nul après persistance");
    }


@Test
    public void testRegleManuelleDescriptionRegle() {
        Assertions.assertEquals(regleManuelle.getDescriptionRegle(), "description de la regle");

    }
    @Test
    public void testRegleManuelleCreateurRegle(){
        Assertions.assertEquals(regleManuelle.getCreateurRegle(), "admin1");

    }
    @Test
    public void testRegleManuelleDateRegle(){
        Assertions.assertNotNull(regleManuelle.getDateRegle());
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getDateRegle(), regleManuelle.getDateRegle());


    }
    @Test
    public void testRegleManuelleCodeRegle(){
        Assertions.assertNotNull(regleManuelle.getCoderegle());
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getCoderegle(), regleManuelle.getCoderegle());

    }
    @Test
    public void testRegleManuelleReglePourSortie(){
        Assertions.assertNotNull(regleManuelle.getReglePourSortie(), "regle pour la fermeture de la valise");
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getId(), regleManuelle.getId());
    }
    @Test
    public void testRegleManuelleFermeJSFlags() {
        // Verify each FermeJS flag is not null and check expected values if required
        Assertions.assertNotNull(regleManuelle.getFermeJS1(), "FermeJS1 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS2(), "FermeJS2 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS3(), "FermeJS3 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS4(), "FermeJS4 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS5(), "FermeJS5 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS6(), "FermeJS6 ne doit pas être nul");
        Assertions.assertNotNull(regleManuelle.getFermeJS7(), "FermeJS7 ne doit pas être nul");

        // Retrieve the persisted entity from the repository
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.getId(), regleManuelle.getId(), "L'ID de RegleManuelle devrait correspondre à l'ID dans le repository");
    }


}

