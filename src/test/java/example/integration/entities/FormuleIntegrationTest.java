package example.integration.entities;

import example.entities.Formule;
import example.entities.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class FormuleIntegrationTest {

    @Autowired
    private FormuleRepository formuleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
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

        regle = regleRepository.saveAndFlush(regle); // Sauvegarder la règle

        Formule formule = new Formule();
        formule.setFormule("formule1");
        formule.setLibelle("libelle 1");
        formule.setRegle(regle);

        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        // Vérification
        Assertions.assertNotNull(savedFormule.getId());
        Assertions.assertEquals("formule1", savedFormule.getFormule());
        Assertions.assertEquals("libelle 1", savedFormule.getLibelle());
        Assertions.assertEquals(regle.getId(), savedFormule.getRegle().getId());
    }




    @Test
    public void testSaveFormuleFailure() {
        // Créer une Regle valide
        Regle regle = new Regle();
        regle.setCoderegle("R124");
        regle.setDateRegle(new Date());

        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule2");
        formule.setLibelle(null);
        formule.setRegle(regle);

        // Vérifier que l'exception DataIntegrityViolationException est levée lors de la tentative de sauvegarde
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            formuleRepository.saveAndFlush(formule);
        });
    }





    @Test
    public void testSaveFormuleWithoutRegleThrowsException() {
        // Créer une Formule sans Regle
        Formule formule = new Formule();
        formule.setFormule("formule3");
        formule.setLibelle("libelle 3");
        formule.setRegle(null); // Pas de Regle associée

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            formuleRepository.saveAndFlush(formule);
        });
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

        // Modifier la Formule
        savedFormule.setLibelle("libelle 4 modifié");
        savedFormule.setFormule("formule4 modifiée");
        Formule updatedFormule = formuleRepository.saveAndFlush(savedFormule);

        // Vérification des modifications
        Assertions.assertEquals("libelle 4 modifié", updatedFormule.getLibelle());
        Assertions.assertEquals("formule4 modifiée", updatedFormule.getFormule());
    }

    @Test
    public void testFindFormuleNotFound() {
        // Essayer de trouver une Formule avec un ID inexistant
        Optional<Formule> formule = formuleRepository.findById(999);

        // Vérifier que l'optionnel est vide
        Assertions.assertTrue(formule.isEmpty());
    }


    @Test
    public void testDeleteFormuleSuccess() {
        // Créer et sauvegarder une Regle et une Formule
        Regle regle = new Regle();
        regle.setCoderegle("R126");
        regle.setDateRegle(new Date());
        regle = regleRepository.saveAndFlush(regle);

        Formule formule = new Formule();
        formule.setFormule("formule5");
        formule.setLibelle("libelle 5");
        formule.setRegle(regle);
        Formule savedFormule = formuleRepository.saveAndFlush(formule);

        // Supprimer la Formule
        formuleRepository.delete(savedFormule);

        // Vérifier que la Formule a été supprimée
        Optional<Formule> deletedFormule = formuleRepository.findById(savedFormule.getId());
        Assertions.assertTrue(deletedFormule.isEmpty());
    }
}
