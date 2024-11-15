package example.entities;

import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class SortieSemaineTest {

    @Autowired

    private EntityManager em;

    private SortieSemaine sortieSemaine;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        em.persist(regle);
        em.flush();

        sortieSemaine = new SortieSemaine();
        sortieSemaine.setRegle(regle);
        sortieSemaine.setDateSortieSemaine(sdf.parse("2024-12-12"));

        em.persist(sortieSemaine);
        em.flush();

    }
    @Test
    public void testSortieSemainePersistence()throws ParseException{
        Assertions.assertNotNull(sortieSemaine.getId());

    }
    @Test
    public void testSortieSemaineDateSortieSemaine(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Assertions.assertNotNull(sortieSemaine.getDateSortieSemaine());
    }
    @Test
    public void testSortieSemaineRegleAssociation(){
        Assertions.assertNotNull(sortieSemaine.getRegle());
        Regle expectedRegle = regleRepository.findAll().get(0);
        Assertions.assertEquals(expectedRegle.toString(), sortieSemaine.getRegle().toString());
    }
    @Test
    public void testUpdateDateSortieSemaine() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nouvelleDate = sdf.parse("2025-01-15");
        sortieSemaine.setDateSortieSemaine(nouvelleDate);
        Assertions.assertEquals(nouvelleDate, sortieSemaine.getDateSortieSemaine(),
                "The release date of the week should be updated correctly.");
    }



}
