package example.integration.services;

import example.DTO.FormuleDTO;
import example.entity.Formule;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import example.services.FormuleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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

    @Autowired
    private RegleRepository regleRepository;

    private Formule formule;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();

        formule = Formule.builder()
                .libelle("libellé 1")
                .formule("formule standard")
                .build();
    }

    private FormuleDTO convertToDTO(Formule formule) {
        return FormuleDTO.builder()
                .cleFormule(1)
                .libelle(formule.getLibelle())
                .formule(formule.getFormule())
                .regleIds(List.of(1))
                .build();
    }

    @Test
    public void testCreateFormule() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("libellé 1")
                .formule("formule standard")
                .regle(List.of(regle))
                .build();

        FormuleDTO formuleDTO = convertToDTO(formule);

        // Act
        FormuleDTO savedFormule = formuleService.createFormule(formuleDTO);

        // Assert
        assertNotNull(savedFormule);
        assertNotNull(savedFormule.getFormule());
        assertEquals("libellé 1", savedFormule.getLibelle());
        assertEquals("formule standard", savedFormule.getFormule());
        assertEquals(regle.getId(), savedFormule.getRegleIds());
    }

    @Test
    public void testUpdateFormule() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("libellé 1")
                .formule("formule standard")
                .regle(List.of(regle))
                .build();

        FormuleDTO formuleDTO = convertToDTO(formule);
        FormuleDTO savedFormule = formuleService.createFormule(formuleDTO);

        // Act
        savedFormule.setLibelle("libelle 25");
        savedFormule.setFormule("formule avancée");
        FormuleDTO updatedFormule = formuleService.updateFormule(savedFormule.getCleFormule(), savedFormule);

        // Assert
        assertNotNull(updatedFormule);
        assertEquals(savedFormule.getCleFormule(), updatedFormule.getCleFormule());
        assertEquals("libelle 25", updatedFormule.getLibelle());
        assertEquals("formule avancée", updatedFormule.getFormule());
        assertEquals(regle.getId(), updatedFormule.getRegleIds());
    }

    @Test
    public void testGetFormuleById() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("libellé 1")
                .formule("formule standard")
                .regle(List.of(Regle.builder().coderegle("CODE123").build()))
                .build();

        FormuleDTO formuleDTO = convertToDTO(formule);
        FormuleDTO savedFormule = formuleService.createFormule(formuleDTO);

        // Act
        FormuleDTO formuleById = formuleService.getFormuleById(savedFormule.getCleFormule());

        // Assert
        assertNotNull(formuleById);
        assertEquals(savedFormule.getCleFormule(), formuleById.getCleFormule());
        assertEquals("libellé 1", formuleById.getLibelle());
        assertEquals("formule standard", formuleById.getFormule());
        assertEquals(regle.getId(), formuleById.getRegleIds());
    }

    @Test
    public void testGetAllFormules() {
        // Arrange
        Formule form1 = Formule.builder()
                .libelle("libelle 1")
                .formule("formule 1")
                .build();

        Formule form2 = Formule.builder()
                .libelle("libelle 2")
                .formule("formule 2")
                .build();

        formuleRepository.save(form1);
        formuleRepository.save(form2);
        formuleRepository.flush();

        // Act
        List<FormuleDTO> formules = formuleService.getAllFormules();

        // Assert
        assertNotNull(formules, "La liste des formules ne doit pas être nulle");
        assertEquals(2, formules.size(), "La liste des formules devrait contenir 2 éléments");
    }

    @Test
    @Transactional
    public void testDeleteFormule() {
        // Arrange
        Regle regle = Regle.builder()
                .coderegle("CODE123")
                .build();
        regle = regleRepository.save(regle);

        Formule formule = Formule.builder()
                .libelle("Libellé Test")
                .formule("Description Test")
                .regle(List.of(regle))
                .build();

        FormuleDTO formuleDTO = convertToDTO(formule);
        FormuleDTO savedFormule = formuleService.createFormule(formuleDTO);

        // Ensure the formule exists before deletion
        assertNotNull(formuleService.getFormuleById(savedFormule.getCleFormule()));

        // Act
        formuleService.deleteFormule(savedFormule.getCleFormule());

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            formuleService.getFormuleById(savedFormule.getCleFormule());
        }, "Formule should be deleted and not retrievable");
    }
}
