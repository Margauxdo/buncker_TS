package example.entities;

import example.repositories.FormuleRepository;
import example.repositories.SortieSemaineRepository;
import example.repositories.TypeRegleRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
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

        // Créer et persister l'entité Client avec le nom et l'email initialisés
        Client client = new Client();
        client.setName("NomDuClient");  // Initialisation du champ obligatoire `name`
        client.setEmail("email@exemple.com");  // Initialisation du champ obligatoire `email`
        em.persist(client);

        // Créer et persister les autres entités
        Formule formule = new Formule();
        em.persist(formule);

        SortieSemaine sortieSemaine = new SortieSemaine();
        em.persist(sortieSemaine);

        // Créer et persister l'entité TypeRegle avec la propriété nomTypeRegle
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("TypeRegleTest");  // Initialisation de nomTypeRegle
        em.persist(typeRegle);

        // Associez `client` avec `valise` et persistez `valise`
        Valise valise = new Valise();
        valise.setClient(client);  // Associe Client à Valise
        em.persist(valise);

        em.flush();  // Synchronise toutes les entités persistées

        // Créez et configurez l'entité `Regle`
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

        List<SortieSemaine> sorties = new ArrayList<>();
        sorties.add(sortieSemaine);
        regle.setSortieSemaine(sorties);

        regle.setTypeEntree("typeEntree");
        regle.setTypeRegle(typeRegle);  // Associe TypeRegle à Regle
        regle.setValise(valise);  // Associe Valise à Regle

        em.persist(regle);
        em.flush();  // Forcer la persistance de regle
    }


    @Test
    public void testReglePersitence(){
        Assertions.assertNotNull(regle.getId(),"Le id est null");
    }

    @Test
    public void testRegleReglePourSortie(){
        Assertions.assertEquals(regle.getReglePourSortie(),"PourSortie");

    }
    @Test
    public void testRegleCodeRegle(){
        Assertions.assertEquals(regle.getCoderegle(),"Coderegle");
    }
    @Test
    public void testRegleDateRegle() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(regle.getDateRegle(),(sdf.parse("2016-05-20")));

    }
    @Test
    public void testRegleNombreJours(){
        Assertions.assertEquals(regle.getNombreJours(),250);
    }
    @Test
    public void testRegleCalculCalendaire(){
        Assertions.assertEquals(regle.getCalculCalendaire(),1234L);

    }@Test
    public void testRegleFermeJS(){
        Assertions.assertEquals(regle.getFermeJS1(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS2(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS3(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS4(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS5(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS6(),Boolean.FALSE);
        Assertions.assertEquals(regle.getFermeJS7(),Boolean.FALSE);


    }
    @Test
    public void testRegleTypeEntree(){
        Assertions.assertEquals(regle.getTypeEntree(),"typeEntree");
    }
    @Test
    public void testRegleNBJSMEntree(){
        Assertions.assertEquals(regle.getNBJSMEntree(),25L);
    }
    @Test
    public void testRegleValiseAssociation(){
        Assertions.assertNotNull(regle.getValise());
        Valise expectedValise = valiseRepository.findAll().get(0);
        Assertions.assertEquals(expectedValise.getId(),regle.getValise().getId());
    }
    @Test
    public void testRegleSortieSemaineAssociation(){
        Assertions.assertNotNull(regle.getSortieSemaine());
        Assertions.assertFalse(regle.getSortieSemaine().isEmpty());
        SortieSemaine expectedSortieSemaine = sortieSemaineRepository.findAll().get(0);
        Assertions.assertEquals(expectedSortieSemaine.getId(), regle.getSortieSemaine().get(0).getId());
    }
    @Test
    public void testRegleTypeRegleAssociation(){
        Assertions.assertNotNull(regle.getTypeRegle());
        TypeRegle expectedTypeRegle = typeRegleRepository.findAll().get(0);
        Assertions.assertEquals(expectedTypeRegle.getId(),regle.getTypeRegle().getId());
    }
    @Test
    public void testRegleFormuleAssociation(){
        Assertions.assertNotNull(regle.getFormule());
        Formule expectedFormule = formuleRepository.findAll().get(0);
        Assertions.assertEquals(expectedFormule.getId(), regle.getFormule().getId());
    }
    @Test
    public void testCascadeDeleteSortieSemaine(){

    }
    @Test
    public void testNonNullCoderegle(){

    }



}
