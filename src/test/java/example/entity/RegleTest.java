package example.entity;

import example.entity.SortieSemaine;
import example.repositories.FormuleRepository;
import example.repositories.SortieSemaineRepository;
import example.repositories.TypeRegleRepository;
import example.repositories.ValiseRepository;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class RegleTest {

    @Autowired
    private EntityManager em;

    private Regle regle;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private TypeRegleRepository typeRegleRepository;
    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Créer un Client et persister
        Client client = new Client();
        client.setName("CientA");
        client.setEmail("clientA@exemple.com");
        em.persist(client);

        // Créer une Valise et persister
        Valise valise = new Valise();
        valise.setClient(client);
        em.persist(valise);

        // Créer une Formule et persister
        Formule formule = new Formule();
        formule.setLibelle("LibelleFormule");
        formule.setFormule("ContenuFormule");
        em.persist(formule);

        // Créer un TypeRegle et persister
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeRegleTest");
        em.persist(typeRegle);

        // Créer un Regle et associer les entités créées
        regle = new Regle();
        regle.setFermeJS1(false);
        regle.setFermeJS2(false);
        regle.setFermeJS3(false);
        regle.setFermeJS4(false);
        regle.setFermeJS5(false);
        regle.setFermeJS6(false);
        regle.setFermeJS7(false);
        regle.setDateRegle(sdf.parse("2016-05-20"));
        regle.setReglePourSortie("PourSortie");
        regle.setCoderegle("Coderegle");
        regle.setCalculCalendaire(1234L);
        regle.setFormule(formule);
        regle.setNbjsmEntree(25L);
        regle.setNombreJours(250);
        regle.setTypeEntree("typeEntree");
        regle.setTypeRegle(typeRegle);
        regle.setValise(valise);

        // Créer une SortieSemaine et l'associer
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setRegle(regle);
        regle.getSortieSemaine().add(sortieSemaine);

        // Persist Regle et SortieSemaine
        em.persist(regle);
        em.persist(sortieSemaine);
        em.flush();
    }

    @Test
    public void testRegleValiseAssociation() {
        Assertions.assertNotNull(regle.getValise(), "Valise association should not be null");
        Assertions.assertEquals("Valise de test", regle.getValise().getDescription(), "Valise description mismatch");
    }

    @Test
    public void testRegleSortieSemaineOrphanRemoval() {
        // Ajouter et tester la suppression en cascade de SortieSemaine
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        regle.getSortieSemaine().add(sortieSemaine);
        em.persist(sortieSemaine);
        em.flush();

        // Supprimer la sortieSemaine
        regle.getSortieSemaine().remove(sortieSemaine);
        em.persist(regle);
        em.flush();

        List<SortieSemaine> sorties = sortieSemaineRepository.findAll();
        Assertions.assertTrue(sorties.isEmpty(), "Orphan removal did not work correctly for SortieSemaine");
    }

    @Test
    public void testReglePersitence() {
        Assertions.assertNotNull(regle.getId(), "The id is null");
    }

    @Test
    public void testRegleReglePourSortie() {
        Assertions.assertEquals("PourSortie", regle.getReglePourSortie());
    }

    @Test
    public void testRegleCodeRegle() {
        Assertions.assertEquals("Coderegle", regle.getCoderegle());
    }

    @Test
    public void testRegleDateRegle() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(sdf.parse("2016-05-20"), regle.getDateRegle());
    }

    @Test
    public void testRegleNombreJours() {
        Assertions.assertEquals(250, regle.getNombreJours());
    }

    @Test
    public void testRegleCalculCalendaire() {
        Assertions.assertEquals(1234L, regle.getCalculCalendaire());
    }

    @Test
    public void testRegleFermeJS() {
        Assertions.assertFalse(regle.getFermeJS1());
        Assertions.assertFalse(regle.getFermeJS2());
        Assertions.assertFalse(regle.getFermeJS3());
        Assertions.assertFalse(regle.getFermeJS4());
        Assertions.assertFalse(regle.getFermeJS5());
        Assertions.assertFalse(regle.getFermeJS6());
        Assertions.assertFalse(regle.getFermeJS7());
    }

    @Test
    public void testRegleTypeEntree() {
        Assertions.assertEquals("typeEntree", regle.getTypeEntree());
    }

    @Test
    public void testRegleNBJSMEntree() {
        Assertions.assertEquals(25L, regle.getNbjsmEntree());
    }

    @Test
    public void testRegleSortieSemaineAssociation() {
        Assertions.assertNotNull(regle.getSortieSemaine());
        Assertions.assertFalse(regle.getSortieSemaine().isEmpty());
        SortieSemaine expectedSortieSemaine = sortieSemaineRepository.findAll().get(0);
        Assertions.assertEquals(expectedSortieSemaine.getId(), regle.getSortieSemaine().get(0).getId());
    }

    @Test
    public void testRegleTypeRegleAssociation() {
        Assertions.assertNotNull(regle.getTypeRegle());
        TypeRegle expectedTypeRegle = typeRegleRepository.findAll().get(0);
        Assertions.assertEquals(expectedTypeRegle.getId(), regle.getTypeRegle().getRegle());
    }

    @Test
    public void testRegleFormuleAssociation() {
        Assertions.assertNotNull(regle.getFormule());
        Formule expectedFormule = formuleRepository.findAll().get(0);
        Assertions.assertEquals(expectedFormule.getId(), regle.getFormule().getId());
    }

    @Test
    public void testCascadeDeleteSortieSemaine() {
        em.remove(regle);
        em.flush();

        List<SortieSemaine> sorties = sortieSemaineRepository.findAll();
        Assertions.assertTrue(sorties.isEmpty(), "This week's releases were not deleted in cascade");
    }
}
