package example.integration.repositories;

import example.entities.RegleManuelle;
import example.entities.TypeRegle;
import example.entities.Valise;
import example.repositories.RegleManuelleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class RegleManuelleRepositoryIntegrationTest {

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    @BeforeEach
    public void setUp() {
        regleManuelleRepository.deleteAll();

    }
    @Test
    public void testSaveRegleManuelleSuccess(){
        Valise val = new Valise();
        val.setNumeroValise(1234L);
        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("type regle test");

        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("jean Paul Martin");
        regleManuelle.setCoderegle("12345");
        regleManuelle.setValise(val);
        regleManuelle.setTypeRegle(tp);

        RegleManuelle savedRM = regleManuelleRepository.save(regleManuelle);
        assertNotNull(savedRM.getId());
        assertEquals("jean Paul Martin", savedRM.getCreateurRegle());
        assertEquals("12345", savedRM.getCoderegle());


    }
    @Test
    public void testFindRegleManuelleByIdSuccess(){
        Valise val = new Valise();
        val.setNumeroValise(1234L);
        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("type regle test");
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("jean Paul Martin");
        regleManuelle.setCoderegle("12345");
        regleManuelle.setValise(val);
        regleManuelle.setTypeRegle(tp);
        RegleManuelle savedRM = regleManuelleRepository.save(regleManuelle);
        Optional<RegleManuelle> foundRM = regleManuelleRepository.findById(savedRM.getId());
        assertTrue(foundRM.isPresent());
        assertEquals("jean Paul Martin", foundRM.get().getCreateurRegle());
        assertEquals("12345", foundRM.get().getCoderegle());


    }
    @Test
    public void testDeleteRegleManuelle(){
        Valise val = new Valise();
        val.setNumeroValise(1234L);
        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("type regle test");
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("jean Paul Martin");
        regleManuelle.setCoderegle("12345");
        regleManuelle.setValise(val);
        regleManuelle.setTypeRegle(tp);
        regleManuelleRepository.deleteById(regleManuelle.getId());
        Optional<RegleManuelle> deletedRM = regleManuelleRepository.findById(regleManuelle.getId());
        assertFalse(deletedRM.isPresent());
    }
    @Test
    public void testFindRegleManuelleByIdFail(){
        List<RegleManuelle> foundRM = regleManuelleRepository.findAll();
        assertTrue(foundRM.isEmpty());

    }
    @Test
    public void testUpdateRegleManuelle(){

        RegleManuelle rm = new RegleManuelle();
        rm.setCreateurRegle("jean Paul Martin");
        rm.setCoderegle("12345");
        RegleManuelle savedRM = regleManuelleRepository.save(rm);
        savedRM.setCreateurRegle("Francois Xavier");
        savedRM.setCoderegle("Am2569");
        RegleManuelle updatedRM = regleManuelleRepository.save(savedRM);
        Optional<RegleManuelle> foundRM = regleManuelleRepository.findById(updatedRM.getId());
        assertTrue(foundRM.isPresent());
        assertEquals("Francois Xavier", foundRM.get().getCreateurRegle());
        assertEquals("Am2569", foundRM.get().getCoderegle());


    }
    @Test
    public void testFindAllRegleManuelle(){
        RegleManuelle rm1 = new RegleManuelle();
        rm1.setCreateurRegle("Jean Paul Martin");
        rm1.setCoderegle("12345");
        regleManuelleRepository.save(rm1);

        RegleManuelle rm2 = new RegleManuelle();
        rm2.setCreateurRegle("Francois Xavier");
        rm2.setCoderegle("Am2569");
        regleManuelleRepository.save(rm2);

        List<RegleManuelle> foundRM = regleManuelleRepository.findAll();
        assertNotNull(foundRM);
        assertTrue(foundRM.size() >= 2);

        foundRM.sort(Comparator.comparing(RegleManuelle::getCreateurRegle));

        assertEquals("Francois Xavier", foundRM.get(0).getCreateurRegle());
        assertEquals("Am2569", foundRM.get(0).getCoderegle());

        assertEquals("Jean Paul Martin", foundRM.get(1).getCreateurRegle());
        assertEquals("12345", foundRM.get(1).getCoderegle());

        foundRM.forEach(System.out::println);
    }

    @Test
    public void testDeleteRegleManuelleWithValise(){
        Valise val = new Valise();
        val.setNumeroValise(1234L);
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("francois Xavier");
        regleManuelle.setCoderegle("Am2569");
        regleManuelle.setValise(val);
        RegleManuelle savedRM = regleManuelleRepository.save(regleManuelle);
        regleManuelleRepository.deleteById(regleManuelle.getId());
        Optional<RegleManuelle> foundRM = regleManuelleRepository.findById(regleManuelle.getId());
        assertFalse(foundRM.isPresent());
    }
    @Test
    public void testCascadeDeleteRegleManuelleWithValise() {
        Valise val = new Valise();
        val.setNumeroValise(1234L);

        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("type regle test");

        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("Jean Paul Martin");
        regleManuelle.setCoderegle("12345");
        regleManuelle.setValise(val);
        regleManuelle.setTypeRegle(tp);

        RegleManuelle savedRM = regleManuelleRepository.save(regleManuelle);

        regleManuelleRepository.delete(savedRM);

        Optional<RegleManuelle> foundRM = regleManuelleRepository.findById(savedRM.getId());
        assertFalse(foundRM.isPresent(), "La suppression en cascade n'a pas fonctionné pour RegleManuelle");

    }
    @Test
    public void testSaveRegleManuelleWithMissingRequiredFields() {
        RegleManuelle regleManuelle = new RegleManuelle();

        assertThrows(Exception.class, () -> {
            regleManuelleRepository.save(regleManuelle);
        }, "La sauvegarde de RegleManuelle aurait dû échouer à cause de champs obligatoires manquants");
    }
    @Test
    public void testFindAllPerformance() {
        for (int i = 0; i < 1000; i++) {
            RegleManuelle rm = new RegleManuelle();
            rm.setCreateurRegle("Createur " + i);
            rm.setCoderegle("Code" + i);
            regleManuelleRepository.save(rm);
        }

        long startTime = System.currentTimeMillis();
        List<RegleManuelle> foundRM = regleManuelleRepository.findAll();
        long endTime = System.currentTimeMillis();

        assertNotNull(foundRM);
        assertTrue(foundRM.size() >= 1000, "Le nombre d'entités récupérées est incorrect");
        System.out.println("Temps de récupération pour 1000 entités : " + (endTime - startTime) + "ms");
    }
    @Test
    public void testRegleManuelleWithTypeRegle() {
        TypeRegle tp = new TypeRegle();
        tp.setNomTypeRegle("Type Regle Test");

        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setCreateurRegle("Jean Paul Martin");
        regleManuelle.setCoderegle("12345");
        regleManuelle.setDescriptionRegle("Description de test"); // Fournir une description valide
        regleManuelle.setTypeRegle(tp);

        RegleManuelle savedRM = regleManuelleRepository.save(regleManuelle);

        Optional<RegleManuelle> foundRM = regleManuelleRepository.findById(savedRM.getId());
        assertTrue(foundRM.isPresent());
        assertEquals("Type Regle Test", foundRM.get().getTypeRegle().getNomTypeRegle());
    }








}
