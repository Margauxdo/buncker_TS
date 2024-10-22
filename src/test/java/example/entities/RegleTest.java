package example.entities;

import example.repositories.FormuleRepository;
import example.repositories.SortieSemaineRepository;
import example.repositories.TypeRegleRepository;
import example.repositories.ValiseRepository;
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
import java.util.ArrayList;
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

        // Création et persistance du Client
        Client client = new Client();
        client.setName("NomDuClient");
        client.setEmail("email@exemple.com");
        em.persist(client);

        // Création et persistance de la Formule
        Formule formule = new Formule();
        formule.setLibelle("LibelleFormule");
        formule.setFormule("ContenuFormule");
        em.persist(formule);

        // Création et persistance de TypeRegle
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeRegleTest");
        em.persist(typeRegle);

        // Création et persistance de Valise
        Valise valise = new Valise();
        valise.setClient(client);
        em.persist(valise);

        // Création et persistance de Regle
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
        regle.setNBJSMEntree(25L);
        regle.setNombreJours(250);
        regle.setTypeEntree("typeEntree");
        regle.setTypeRegle(typeRegle);
        regle.setValise(valise);

        // Initialisation de la collection sortieSemaine
        List<SortieSemaine> sorties = new ArrayList<>();
        regle.setSortieSemaine(sorties);
        em.persist(regle);
        em.flush();

        // Création et persistance de SortieSemaine
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setRegle(regle);
        sorties.add(sortieSemaine);
        em.persist(sortieSemaine);
        em.flush();
    }

    @Test
    public void testReglePersitence() {
        Assertions.assertNotNull(regle.getId(), "Le id est null");
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
        Assertions.assertEquals(25L, regle.getNBJSMEntree());
    }

    @Test
    public void testRegleValiseAssociation() {
        Assertions.assertNotNull(regle.getValise());
        Valise expectedValise = valiseRepository.findAll().get(0);
        Assertions.assertEquals(expectedValise.getId(), regle.getValise().getId());
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
        Assertions.assertEquals(expectedTypeRegle.getId(), regle.getTypeRegle().getId());
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
        Assertions.assertTrue(sorties.isEmpty(), "Les sorties de la semaine n'ont pas été supprimées en cascade");
    }
}
