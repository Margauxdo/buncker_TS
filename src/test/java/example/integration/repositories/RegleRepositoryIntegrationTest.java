package example.integration.repositories;

import example.entity.*;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import example.repositories.TypeRegleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class RegleRepositoryIntegrationTest {
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;
    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @BeforeEach
    public void setUp() {
        regleRepository.deleteAll();
    }
    @Test
    public void testSaveRegleSuccess(){
        Formule form = new Formule();
        form.setLibelle("libelle test regle");
        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("nom type regle A");
        Valise val = new Valise();
        val.setNumeroValise(2556L);
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setFormule(form);
        regle.getTypeRegle();
        regle.setValise(val);
        Regle savedregle = regleRepository.save(regle);
        assertNotNull(savedregle.getId());
        assertEquals("AW5698", savedregle.getCoderegle());


    }
    @Test
    public void testFindRegleById(){

        Regle regle = new Regle();
        regle.setCoderegle("AW5698");

        Regle savedregle = regleRepository.save(regle);
        Optional<Regle> foundregle = regleRepository.findById(savedregle.getId());
        assertTrue(foundregle.isPresent());
        assertEquals("AW5698", foundregle.get().getCoderegle());


    }
    @Test
    public void testDeleteRegle(){
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");

        regleRepository.deleteById(regle.getId());
        Optional<Regle> deleteRegle = regleRepository.findById(regle.getId());
        assertFalse(deleteRegle.isPresent());
    }
    @Test
    public void testFindRegleByIdFail(){
        List<Regle> regles = regleRepository.findAll();
        assertTrue(regles.isEmpty());
    }
    @Test
    public void testUpdateRegle(){
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        Regle savedregle = regleRepository.save(regle);
        savedregle.setCoderegle("AW56987");
        Regle updatedregle = regleRepository.save(savedregle);
        Optional<Regle> updatedRegle = regleRepository.findById(updatedregle.getId());
        assertTrue(updatedRegle.isPresent());
        assertEquals("AW56987", updatedRegle.get().getCoderegle());

    }
    @Test
    public void testFindAllRegles(){
        Regle regleA = new Regle();
        regleA.setCoderegle("AW5698");
        regleRepository.save(regleA);
        Regle regleB = new Regle();
        regleB.setCoderegle("AW56987");
        regleRepository.save(regleB);
        List<Regle> findRegles = regleRepository.findAll();
        assertNotNull(findRegles);
        assertTrue(findRegles.size() >= 2);
        findRegles.sort(Comparator.comparing(Regle::getCoderegle));
        assertEquals("AW5698", findRegles.get(0).getCoderegle());
        assertEquals("AW56987", findRegles.get(1).getCoderegle());

        findRegles.forEach(System.out::println);

    }
    @Test
    public void testFindRegleByFormule(){
        Formule formule = new Formule();
        formule.setLibelle("libelle test regle");
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setFormule(formule);
        Regle savedregle = regleRepository.save(regle);
        Optional<Regle> foundregle = regleRepository.findById(savedregle.getId());
        assertTrue(foundregle.isPresent());
        assertEquals("libelle test regle", foundregle.get().getFormule().getLibelle());
    }
    @Test
    public void testFindRegleByTypeRegle() {
        // Arrange
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type ABC");

        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setTypeRegle(typeRegle);
        regle = regleRepository.saveAndFlush(regle);

        typeRegle.setRegle(regle);

        typeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Act
        Optional<Regle> foundRegle = regleRepository.findById(regle.getId());

        // Assert
        assertTrue(foundRegle.isPresent(), "La règle devrait être présente.");
        assertNotNull(foundRegle.get().getTypeRegle(), "Le TypeRegle ne devrait pas être null.");
        assertEquals("Type ABC", foundRegle.get().getTypeRegle().getNomTypeRegle(),
                "Le nom du TypeRegle ne correspond pas.");
    }



    @Test
    public void testFindRegleByValise(){
        Valise val = new Valise();
        val.setNumeroValise(2556L);
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setValise(val);
        Regle savedregle = regleRepository.save(regle);
        Optional<Regle> foundregle = regleRepository.findById(savedregle.getId());
        assertTrue(foundregle.isPresent());
        assertEquals(2556L, foundregle.get().getValise().getNumeroValise());
    }



    @Test
    public void testCascadeDeleteRegleWhenDeletingFormule() {
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");

        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setFormule(formule);
        regleRepository.save(regle);

        regleRepository.delete(regle);

        List<Regle> remainingRegles = regleRepository.findAll();
        assertTrue(remainingRegles.isEmpty());
    }
    @Test
    public void testOrphanRemovalForFormule() {
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");
        Regle regle = new Regle();
        regle.setCoderegle("AW5698");
        regle.setFormule(formule);
        regleRepository.save(regle);

        regle.setFormule(null);
        regleRepository.save(regle);

        List<Regle> reglesWithoutFormule = regleRepository.findAll();
        assertNull(reglesWithoutFormule.get(0).getFormule());
    }
    @Test
    public void testOrphanRemovalForSortieSemaine() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("OrphanTest");
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        regle.getSortieSemaine().add(sortieSemaine);
        regleRepository.saveAndFlush(regle);

        // Act
        regle.getSortieSemaine().clear();
        regleRepository.saveAndFlush(regle);

        // Assert
        Assertions.assertTrue(sortieSemaineRepository.findAll().isEmpty(), "Orphan removal did not occur for SortieSemaine");
    }




}
