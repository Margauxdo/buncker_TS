package example.services;

import example.DTO.FormuleDTO;
import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormuleServiceTest {

    @Mock
    private FormuleRepository formuleRepository;

    @Mock
    private RegleRepository regleRepository;

    @InjectMocks
    private FormuleService formuleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Méthode utilitaire pour convertir un FormuleDTO en Formule


    // Méthode utilitaire pour convertir une Formule en FormuleDTO
    private FormuleDTO convertToDTO(Formule formule) {
        return FormuleDTO.builder()
                .id(formule.getId())
                .libelle(formule.getLibelle())
                .formule(formule.getFormule())
                .regleId(formule.getRegle() != null ? formule.getRegle().getId() : 0)
                .build();
    }

    @Test
    public void testUpdateFormule_Success() {
        int id = 1;
        Formule existingFormule = new Formule(id, "Ancien Libellé", "Ancienne Formule", null);
        Formule updatedFormule = new Formule(id, "Nouveau Libellé", "Nouvelle Formule", null);
        FormuleDTO updatedFormuleDTO = convertToDTO(updatedFormule);

        when(formuleRepository.findById(id)).thenReturn(Optional.of(existingFormule));
        when(formuleRepository.save(any(Formule.class))).thenReturn(updatedFormule);

        FormuleDTO result = formuleService.updateFormule(id, updatedFormuleDTO);

        assertNotNull(result, "La formule mise à jour ne doit pas être nulle");
        assertEquals("Nouveau Libellé", result.getLibelle(), "Le libellé doit être mis à jour");
        assertEquals("Nouvelle Formule", result.getFormule(), "La formule doit être mise à jour");

        verify(formuleRepository, times(1)).findById(id);
        verify(formuleRepository, times(1)).save(any(Formule.class));
    }

    @Test
    public void testGetFormuleById_Success() {
        int id = 1;
        Formule formule = new Formule(id, "Libellé", "Formule", null);

        when(formuleRepository.findById(id)).thenReturn(Optional.of(formule));

        FormuleDTO result = formuleService.getFormuleById(id);

        assertNotNull(result, "La formule récupérée ne doit pas être nulle");
        assertEquals(id, result.getId(), "L'ID de la formule doit correspondre");

        verify(formuleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllFormules_Success() {
        List<Formule> formules = List.of(
                new Formule(1, "Libellé1", "Formule1", null),
                new Formule(2, "Libellé2", "Formule2", null)
        );

        when(formuleRepository.findAll()).thenReturn(formules);

        List<FormuleDTO> result = formuleService.getAllFormules();

        assertNotNull(result, "La liste des formules ne doit pas être nulle");
        assertEquals(2, result.size(), "La liste des formules doit contenir 2 éléments");

        verify(formuleRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteFormule_Success() {
        int id = 1;

        when(formuleRepository.existsById(id)).thenReturn(true);

        formuleService.deleteFormule(id);

        verify(formuleRepository, times(1)).existsById(id);
        verify(formuleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteFormule_Failure_NotFound() {
        int id = 1;

        when(formuleRepository.existsById(id)).thenReturn(false);

        FormuleNotFoundException exception = assertThrows(FormuleNotFoundException.class, () -> {
            formuleService.deleteFormule(id);
        });

        assertEquals("Formule not found for ID 1", exception.getMessage());

        verify(formuleRepository, times(1)).existsById(id);
        verify(formuleRepository, never()).deleteById(id);
    }
}
