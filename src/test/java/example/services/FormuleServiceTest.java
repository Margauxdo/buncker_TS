package example.services;

import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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



    @Test
    public void testUpdateFormule_Success() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(2);

        when(formuleRepository.findById(id)).thenReturn(Optional.of(new Formule(id, "libellé existant", "formule existante", null)));

        when(formuleRepository.save(any(Formule.class))).thenAnswer(invocation -> {
            Formule savedFormule = invocation.getArgument(0);
            savedFormule.setId(id);
            return savedFormule;
        });

        Formule result = formuleService.updateFormule(id, formule);

        assertNotNull(result, "Formule should not be null");
        assertEquals(id, result.getId(), "L'ID de la formule devrait être mis à jour");

        verify(formuleRepository, times(1)).findById(id);
        verify(formuleRepository, times(1)).save(any(Formule.class));
        verifyNoMoreInteractions(formuleRepository);
    }






    @Test
    public void testGetFormuleById_Success() {
        int id = 1;
        Formule formule = new Formule();
        formule.setId(id);

        when(formuleRepository.findById(id)).thenReturn(Optional.of(formule));

        Formule result = formuleService.getFormuleById(id);

        assertNotNull(result, "Formule should not be null");
        assertEquals(id, result.getId(), "L'ID de la formule devrait correspondre");

        verify(formuleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testGetFormuleById_Failure_NotFound() {
        int id = 1;

        when(formuleRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            formuleService.getFormuleById(id);
        });

        assertEquals("Formule avec l'Id 1 n'existe pas !", exception.getMessage());
        verify(formuleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(formuleRepository);
    }



    @Test
    public void testGetAllFormules_Success() {
        List<Formule> formules = new ArrayList<>();
        formules.add(new Formule());
        formules.add(new Formule());

        when(formuleRepository.findAll()).thenReturn(formules);

        List<Formule> result = formuleService.getAllFormules();

        assertEquals(2, result.size(), "La liste des formules devrait contenir 2 éléments");
        verify(formuleRepository, times(1)).findAll();
        verifyNoMoreInteractions(formuleRepository);
    }

    @Test
    public void testGetAllFormules_EmptyList() {
        when(formuleRepository.findAll()).thenReturn(new ArrayList<>());

        List<Formule> result = formuleService.getAllFormules();

        assertNotNull(result, "Le résultat ne doit pas être null");
        Assertions.assertTrue(result.isEmpty(), "La liste des formules devrait être vide");

        verify(formuleRepository, times(1)).findAll();
        verifyNoMoreInteractions(formuleRepository); // Assure qu'il n'y a pas d'autres interactions
    }


    @Test
    public void testDeleteFormule_Success() {
        int id = 1;

        // Arrange
        when(formuleRepository.existsById(id)).thenReturn(true);

        // Act
        formuleService.deleteFormule(id);

        // Assert
        verify(formuleRepository, times(1)).existsById(id);
        verify(formuleRepository, times(1)).deleteById(id);
    }



    @Test
    public void testDeleteFormule_Failure_NotFound() {
        int id = 1;

        when(formuleRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(FormuleNotFoundException.class, () -> {
            formuleService.deleteFormule(id);
        });

        assertEquals("Formule not found for ID " + id, exception.getMessage(), "Exception message should match expected error");

        verify(formuleRepository, times(1)).existsById(id);

        verify(formuleRepository, never()).deleteById(id);
        verifyNoMoreInteractions(formuleRepository);
    }


    @Test
    public void testDeleteFormule_Failure_DatabaseException() {
        int id = 1;

        when(formuleRepository.existsById(id)).thenReturn(true);

        doThrow(new DataIntegrityViolationException("Database error")).when(formuleRepository).deleteById(id);

        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            formuleService.deleteFormule(id);
        });

        assertEquals("Database error", exception.getMessage(), "Exception message should match expected error");

        verify(formuleRepository, times(1)).existsById(id);
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
        }

        verify(formuleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(formuleRepository);
    }





}

