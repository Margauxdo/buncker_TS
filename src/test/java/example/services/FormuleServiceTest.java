package example.services;

import example.entities.Formule;
import example.repositories.FormuleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class FormuleServiceTest {

    @Mock
    private FormuleRepository formuleRepository;

    @InjectMocks
    private FormuleService formuleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests pour createFormule
    @Test
    public void testCreateFormule_Success() {
        Formule formule = new Formule();
        when(formuleRepository.save(formule)).thenReturn(formule);

        Formule result = formuleService.createFormule(formule);

        Assertions.assertNotNull(result, "Formule should not be null");
        verify(formuleRepository, times(1)).save(formule);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testCreateFormule_Failure_Exception() {
        Formule formule = new Formule();
        when(formuleRepository.save(formule)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            formuleService.createFormule(formule);
        });

        Assertions.assertEquals("Database error", exception.getMessage(),
                "Exception message should match expected error");
        verify(formuleRepository, times(1)).save(formule);
        verifyNoMoreInteractions(formuleRepository);
    }

    // Tests pour updateFormule
    @Test
    public void testUpdateFormule_Success() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(2); // ID différent pour forcer la mise à jour

        when(formuleRepository.existsById(id)).thenReturn(true);
        when(formuleRepository.save(any(Formule.class))).thenAnswer(invocation -> {
            Formule savedFormule = invocation.getArgument(0);
            savedFormule.setId(id); // Met à jour l'ID pour correspondre à `updateFormule`
            return savedFormule;
        });

        Formule result = formuleService.updateFormule(id, formule);

        Assertions.assertNotNull(result, "Formule should not be null");
        Assertions.assertEquals(id, result.getId(), "L'ID de la formule devrait être mis à jour");

        verify(formuleRepository, times(1)).existsById(id);
        verify(formuleRepository, times(1)).save(formule);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testUpdateFormule_Failure_Exception() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(id); // Le même ID pour déclencher l'exception

        when(formuleRepository.existsById(id)).thenReturn(true);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            formuleService.updateFormule(id, formule);
        });

        Assertions.assertEquals("Expression not valid", exception.getMessage(),
                "Exception message should match expected error");

        verify(formuleRepository, times(1)).existsById(id);
        verify(formuleRepository, never()).save(any(Formule.class));
        verifyNoMoreInteractions(formuleRepository);
    }

    // Tests pour getFormuleById
    @Test
    public void testGetFormuleById_Success() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(id);

        when(formuleRepository.findById(id)).thenReturn(Optional.of(formule));

        Formule result = formuleService.getFormuleById(id);

        Assertions.assertNotNull(result, "Formule should not be null");
        Assertions.assertEquals(id, result.getId(), "L'ID de la formule devrait correspondre");

        verify(formuleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testGetFormuleById_Failure_NotFound() {
        int id = 1;

        when(formuleRepository.findById(id)).thenReturn(Optional.empty());

        Formule result = formuleService.getFormuleById(id);

        Assertions.assertNull(result, "Formule should be null if not found");
        verify(formuleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(formuleRepository);
    }

    // Tests pour getAllFormules
    @Test
    public void testGetAllFormules_Success() {
        List<Formule> formules = new ArrayList<>();
        formules.add(new Formule());
        formules.add(new Formule());

        when(formuleRepository.findAll()).thenReturn(formules);

        List<Formule> result = formuleService.getAllFormules();

        Assertions.assertEquals(2, result.size(), "La liste des formules devrait contenir 2 éléments");
        verify(formuleRepository, times(1)).findAll();
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testGetAllFormules_EmptyList() {
        when(formuleRepository.findAll()).thenReturn(new ArrayList<>());

        List<Formule> result = formuleService.getAllFormules();

        Assertions.assertTrue(result.isEmpty(), "La liste des formules devrait être vide");
        verify(formuleRepository, times(1)).findAll();
        verifyNoMoreInteractions(formuleRepository);
    }

    // Tests pour deleteFormule
    @Test
    public void testDeleteFormule_Success() {
        int id = 1;

        formuleService.deleteFormule(id);

        verify(formuleRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testDeleteFormule_Failure_Exception() {
        int id = 1;
        doThrow(new RuntimeException("Database error")).when(formuleRepository).deleteById(id);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            formuleService.deleteFormule(id);
        });

        Assertions.assertEquals("Database error", exception.getMessage(), "Exception message should match expected error");
        verify(formuleRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testNoInteractionWithFormuleRepository_Success() {
        verifyNoInteractions(formuleRepository);
    }

    @Test
    public void testNoInteractionWithFormuleRepository_Failure_Exception() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(id);

        try {
            formuleService.updateFormule(id, formule);
        } catch (RuntimeException e) {
            // Ignore l'exception pour ce test
        }

        // Vérifie que seule l'interaction `existsById` s'est produite et aucune autre
        verify(formuleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(formuleRepository);
    }
}
