package example.controller.entities;

import example.entity.Formule;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class FormuleIntegrationTest {

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    void setUp() {
        regleRepository.findAll().forEach(regle -> {
            regle.setFormule(null);
            regleRepository.save(regle);
        });

        formuleRepository.deleteAll();

        regleRepository.deleteAll();
    }




    @Test
    public void testSaveFormuleSuccess() throws ParseException {
        Regle regle = new Regle();
        regle.setCoderegle("R123");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse("2025-02-02");
        regle.setDateRegle(parsedDate);

        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule1");
        formule.setLibelle("libelle 1");
        formule.setRegle(regle);

        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        Assertions.assertNotNull(savedFormule.getId());
        assertEquals("formule1", savedFormule.getFormule());
        assertEquals("libelle 1", savedFormule.getLibelle());
        assertEquals(regle.getId(), savedFormule.getRegle().getId());
    }



    @Test
    public void testUpdateFormuleSuccess() throws ParseException {
        Regle regle = new Regle();
        regle.setCoderegle("R125");
        regle.setDateRegle(new Date());
        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule4");
        formule.setLibelle("libelle 4");
        formule.setRegle(regle);
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        savedFormule.setLibelle("libelle 4 modifié");
        savedFormule.setFormule("formule4 modifiée");
        Formule updatedFormule = formuleRepository.saveAndFlush(savedFormule);

        assertEquals("libelle 4 modifié", updatedFormule.getLibelle());
        assertEquals("formule4 modifiée", updatedFormule.getFormule());
    }

    @Test
    public void testFindFormuleNotFound() {
        Optional<Formule> formule = formuleRepository.findById(999);

        assertTrue(formule.isEmpty());
    }


    @Test
    public void testDeleteFormuleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R126");
        regle.setDateRegle(new Date());
        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule5");
        formule.setLibelle("libelle 5");
        formule.setRegle(regle);
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        formuleRepository.delete(savedFormule);

        Optional<Formule> deletedFormule = formuleRepository.findById(savedFormule.getId());
        assertTrue(deletedFormule.isEmpty());
    }
    @Test
    void testCreateFormule() {
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");

        formuleRepository.save(formule);

        Optional<Formule> found = formuleRepository.findById(formule.getId());

        assertTrue(found.isPresent());
        assertEquals("Formule Test", found.get().getLibelle());
    }


    @Test
    void testFormuleRelationWithRegle() {
        Formule formule = new Formule();
        formule.setLibelle("Formule Test");
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle.setFormule(savedFormule);
        regleRepository.saveAndFlush(regle);

        Optional<Regle> foundRegle = regleRepository.findById(regle.getId());
        assertTrue(foundRegle.isPresent());
        assertEquals(savedFormule.getId(), foundRegle.get().getFormule().getId());
    }



    @Test
    public void testDeleteFormuleWithRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("R127");
        regle.setDateRegle(new Date());
        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule6");
        formule.setLibelle("libelle 6");
        formule.setRegle(regle);
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        regle.setFormule(null);
        regleRepository.saveAndFlush(regle);

        formuleRepository.delete(savedFormule);

        Optional<Formule> deletedFormule = formuleRepository.findById(savedFormule.getId());
        assertTrue(deletedFormule.isEmpty());
    }



}
