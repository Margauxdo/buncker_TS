package templates.typeRegles.controller;

import example.DTO.FormuleDTO;
import example.controller.FormuleController;
import example.interfaces.IFormuleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FormuleControllerTest {

    @InjectMocks
    private FormuleController formuleController;

    @Mock
    private IFormuleService formuleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test : Affichage de la liste des formules - Succès
    @Test
    public void testAfficherListeFormules() {
        List<FormuleDTO> formules = List.of(
                FormuleDTO.builder()
                        .id(1)
                        .libelle("Libelle1")
                        .formule("Description1")
                        .regleIds(Collections.singletonList(101))
                        .build()
        );
        Mockito.when(formuleService.getAllFormules()).thenReturn(formules);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.viewFormuleList(modele);

        Assertions.assertEquals("formules/formule_list", reponse);
        Assertions.assertTrue(modele.containsAttribute("formules"));
        Assertions.assertEquals(formules, modele.getAttribute("formules"));
        Mockito.verify(formuleService, Mockito.times(1)).getAllFormules();
    }

    // Test : Affichage d'une formule par ID - Succès
    @Test
    public void testAfficherFormuleParId_Succes() {
        FormuleDTO formule = FormuleDTO.builder()
                .id(1)
                .libelle("Libelle1")
                .formule("Description1")
                .regleIds(Collections.singletonList(101))
                .build();
        Mockito.when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.viewFormuleById(1, modele);

        Assertions.assertEquals("formules/formule_detail", reponse);
        Assertions.assertTrue(modele.containsAttribute("formule"));
        Assertions.assertEquals(formule, modele.getAttribute("formule"));
        Mockito.verify(formuleService, Mockito.times(1)).getFormuleById(1);
    }




    // Test : Formulaire de modification d'une formule - Non trouvée
    @Test
    public void testAfficherFormulaireModificationFormule_NonTrouvee() {
        Mockito.when(formuleService.getFormuleById(1)).thenThrow(
                new EntityNotFoundException("Formule avec l'ID 1 non trouvée.")
        );

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> formuleController.editFormuleForm(1, new ConcurrentModel())
        );

        Assertions.assertEquals("Formule avec l'ID 1 non trouvée.", exception.getMessage());
        Mockito.verify(formuleService, Mockito.times(1)).getFormuleById(1);
    }

    // Test : Mise à jour d'une formule - Succès
    @Test
    public void testMettreAJourFormule_Succes() {
        FormuleDTO formuleDTO = FormuleDTO.builder()
                .id(1)
                .libelle("Libelle mise à jour")
                .formule("Description mise à jour")
                .regleIds(Collections.singletonList(101))
                .build();
        Mockito.when(formuleService.updateFormule(ArgumentMatchers.eq(1), ArgumentMatchers.any(FormuleDTO.class))).thenReturn(formuleDTO);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.updateFormule(1, formuleDTO, modele);

        Assertions.assertEquals("redirect:/formules/list", reponse);
        Mockito.verify(formuleService, Mockito.times(1)).updateFormule(1, formuleDTO);
    }

    // Test : Suppression d'une formule - Succès
    @Test
    public void testSupprimerFormule_Succes() {
        Mockito.doNothing().when(formuleService).deleteFormule(1);

        String reponse = formuleController.deleteFormule(1);

        Assertions.assertEquals("redirect:/formules/list", reponse);
        Mockito.verify(formuleService, Mockito.times(1)).deleteFormule(1);
    }
}
