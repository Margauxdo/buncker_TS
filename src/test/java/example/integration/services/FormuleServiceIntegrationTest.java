package example.integration.services;

import example.entities.Formule;
import example.repositories.FormuleRepository;
import example.services.FormuleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class FormuleServiceIntegrationTest {

    @Autowired
    FormuleService formuleService;

    @Autowired
    private FormuleRepository formuleRepository;

    private Formule formule;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();

        formule = Formule.builder()
                .libelle("libellé 1")
                .formule("formule standard")
                .build();

    }
    @Test
    public void testCreateFormule() {
        // Act
        Formule savedFormule = formuleService.createFormule(formule);

        // Assert
        assertNotNull(savedFormule);
        assertNotNull(savedFormule.getId());
        assertEquals("libellé 1", savedFormule.getLibelle());
        assertEquals("formule standard", savedFormule.getFormule());
    }
    @Test
    public void testUpdateFormule() {
        // Arrange
        Formule savedFormule = formuleService.createFormule(formule);

        // Act
        savedFormule.setLibelle("libelle 25");
        savedFormule.setFormule("formule avancée");
        Formule updatedFormule = formuleService.updateFormule(savedFormule.getId(), savedFormule);

        // Assert
        assertNotNull(updatedFormule);
        assertEquals(savedFormule.getId(), updatedFormule.getId());
        assertEquals("libelle 25", updatedFormule.getLibelle());
        assertEquals("formule avancée", updatedFormule.getFormule());
    }

    @Test
    public void testGetFormuleById() {
        // Arrange
        Formule savedFormule = formuleService.createFormule(formule);

        //Act
        Formule formuleById = formuleService.getFormuleById(savedFormule.getId());

        //Assert
        assertNotNull(formuleById);
        assertEquals(savedFormule.getId(), formuleById.getId());
        assertEquals("libellé 1", formuleById.getLibelle());
    }
    @Test
    public void testGetAllFormules() {
        // Arrange : Crée deux instances de Formule et les enregistre dans le repository
        Formule form1 = Formule.builder()
                .libelle("libelle 1")
                .formule("formule 1")
                .build();

        Formule form2 = Formule.builder()
                .libelle("libelle 2")
                .formule("formule 2")
                .build();

        // Sauvegarde les deux formules dans le repository et force le flush
        formuleRepository.save(form1);
        formuleRepository.save(form2);
        formuleRepository.flush();

        // Act : Récupère toutes les formules via le service
        List<Formule> formules = formuleService.getAllFormules();

        // Assert : Vérifie que la liste contient bien 2 formules
        assertNotNull(formules, "La liste des formules ne doit pas être nulle");
        assertEquals(2, formules.size(), "La liste des formules devrait contenir 2 éléments");
    }



    @Test
    public void testDeleteFormule() {
        //Arrange
        Formule savedFormule = formuleService.createFormule(formule);

        //Act
        formuleService.deleteFormule(savedFormule.getId());

        //Assert
        Formule deletedFormule = formuleService.getFormuleById(savedFormule.getId());
        assertNull(deletedFormule);
    }
}
