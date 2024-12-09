package example.controller;

import example.DTO.FormuleDTO;
import example.interfaces.IFormuleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

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
                        .regleId(101)
                        .build()
        );
        when(formuleService.getAllFormules()).thenReturn(formules);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.viewFormuleList(modele);

        assertEquals("formules/formule_list", reponse);
        assertTrue(modele.containsAttribute("formules"));
        assertEquals(formules, modele.getAttribute("formules"));
        verify(formuleService, times(1)).getAllFormules();
    }

    // Test : Affichage d'une formule par ID - Succès
    @Test
    public void testAfficherFormuleParId_Succes() {
        FormuleDTO formule = FormuleDTO.builder()
                .id(1)
                .libelle("Libelle1")
                .formule("Description1")
                .regleId(101)
                .build();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.viewFormuleById(1, modele);

        assertEquals("formules/formule_detail", reponse);
        assertTrue(modele.containsAttribute("formule"));
        assertEquals(formule, modele.getAttribute("formule"));
        verify(formuleService, times(1)).getFormuleById(1);
    }

    // Test : Formulaire de création d'une formule
    @Test
    public void testAfficherFormulaireCreationFormule() {
        Model modele = new ConcurrentModel();
        String reponse = formuleController.createFormuleForm(modele);

        assertEquals("formules/formule_create", reponse);
        assertTrue(modele.containsAttribute("formule"));
        assertNotNull(modele.getAttribute("formule"));
    }

    // Test : Formulaire de modification d'une formule - Succès
    @Test
    public void testAfficherFormulaireModificationFormule_Succes() {
        FormuleDTO formule = FormuleDTO.builder()
                .id(1)
                .libelle("Libelle1")
                .formule("Description1")
                .regleId(101)
                .build();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.editFormuleForm(1, modele);

        assertEquals("formules/formule_edit", reponse);
        assertTrue(modele.containsAttribute("formule"));
        assertEquals(formule, modele.getAttribute("formule"));
        verify(formuleService, times(1)).getFormuleById(1);
    }

    // Test : Formulaire de modification d'une formule - Non trouvée
    @Test
    public void testAfficherFormulaireModificationFormule_NonTrouvee() {
        when(formuleService.getFormuleById(1)).thenThrow(
                new EntityNotFoundException("Formule avec l'ID 1 non trouvée.")
        );

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> formuleController.editFormuleForm(1, new ConcurrentModel())
        );

        assertEquals("Formule avec l'ID 1 non trouvée.", exception.getMessage());
        verify(formuleService, times(1)).getFormuleById(1);
    }

    // Test : Mise à jour d'une formule - Succès
    @Test
    public void testMettreAJourFormule_Succes() {
        FormuleDTO formuleDTO = FormuleDTO.builder()
                .id(1)
                .libelle("Libelle mise à jour")
                .formule("Description mise à jour")
                .regleId(101)
                .build();
        when(formuleService.updateFormule(eq(1), any(FormuleDTO.class))).thenReturn(formuleDTO);

        Model modele = new ConcurrentModel();
        String reponse = formuleController.updateFormule(1, formuleDTO, modele);

        assertEquals("redirect:/formules/list", reponse);
        verify(formuleService, times(1)).updateFormule(1, formuleDTO);
    }

    // Test : Suppression d'une formule - Succès
    @Test
    public void testSupprimerFormule_Succes() {
        doNothing().when(formuleService).deleteFormule(1);

        String reponse = formuleController.deleteFormule(1);

        assertEquals("redirect:/formules/list", reponse);
        verify(formuleService, times(1)).deleteFormule(1);
    }
}
