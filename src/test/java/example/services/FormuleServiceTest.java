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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private FormuleDTO createFormuleDTO() {
        return FormuleDTO.builder()
                .id(1)
                .libelle("Formule Test")
                .formule("Test Formula")
                .regleIds(List.of(1, 2))
                .build();
    }

    private Formule createFormule() {
        return Formule.builder()
                .id(1)
                .libelle("Formule Test")
                .formule("Test Formula")
                .regles(new ArrayList<>())
                .build();
    }

    @Test
    public void testCreateFormule_Success() {
        // Arrange
        FormuleDTO formuleDTO = createFormuleDTO();
        Formule formule = createFormule();
        Regle regle1 = new Regle();
        regle1.setId(1);
        Regle regle2 = new Regle();
        regle2.setId(2);

        // Configuration des mocks
        when(regleRepository.findById(1)).thenReturn(Optional.of(regle1));
        when(regleRepository.findById(2)).thenReturn(Optional.of(regle2));
        when(formuleRepository.save(any(Formule.class))).thenReturn(formule);

        // Act
        FormuleDTO result = formuleService.createFormule(formuleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(formuleDTO.getLibelle(), result.getLibelle());
        verify(formuleRepository, times(1)).save(any(Formule.class));
        verify(regleRepository, times(1)).findById(1);
        verify(regleRepository, times(1)).findById(2);
    }


    @Test
    public void testUpdateFormule_Success() {
        // Arrange
        int id = 1;
        Formule existingFormule = createFormule();
        FormuleDTO updateDTO = createFormuleDTO();
        updateDTO.setLibelle("Updated Libelle");

        when(formuleRepository.findById(id)).thenReturn(Optional.of(existingFormule));
        when(formuleRepository.save(any(Formule.class))).thenReturn(existingFormule);

        // Act
        FormuleDTO result = formuleService.updateFormule(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Libelle", result.getLibelle());
        verify(formuleRepository, times(1)).findById(id);
        verify(formuleRepository, times(1)).save(existingFormule);
    }

    @Test
    public void testUpdateFormule_NotFound() {
        int id = 1;
        FormuleDTO updateDTO = createFormuleDTO();

        when(formuleRepository.findById(id)).thenReturn(Optional.empty());

        FormuleNotFoundException exception = assertThrows(
                FormuleNotFoundException.class,
                () -> formuleService.updateFormule(id, updateDTO)
        );

        assertEquals("Formule introuvable avec l'ID : 1", exception.getMessage());
        verify(formuleRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteFormule_Success() {
        int id = 1;
        Formule formule = createFormule();

        when(formuleRepository.findById(id)).thenReturn(Optional.of(formule));
        doNothing().when(formuleRepository).delete(formule);

        formuleService.deleteFormule(id);

        verify(formuleRepository, times(1)).findById(id);
        verify(formuleRepository, times(1)).delete(formule);
    }

    @Test
    public void testDeleteFormule_NotFound() {
        int id = 1;

        when(formuleRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> formuleService.deleteFormule(id)
        );

        assertEquals("Formule introuvable avec l'ID : 1", exception.getMessage());
        verify(formuleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetFormuleById_Success() {
        int id = 1;
        Formule formule = createFormule();

        when(formuleRepository.findById(id)).thenReturn(Optional.of(formule));

        FormuleDTO result = formuleService.getFormuleById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(formuleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetFormuleById_NotFound() {
        int id = 1;

        when(formuleRepository.findById(id)).thenReturn(Optional.empty());

        FormuleNotFoundException exception = assertThrows(
                FormuleNotFoundException.class,
                () -> formuleService.getFormuleById(id)
        );

        assertEquals("Formule introuvable avec l'ID : 1", exception.getMessage());
        verify(formuleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllFormules_Success() {
        List<Formule> formules = new ArrayList<>();
        formules.add(createFormule());
        formules.add(createFormule());

        when(formuleRepository.findAll()).thenReturn(formules);

        List<FormuleDTO> result = formuleService.getAllFormules();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(formuleRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllFormules_EmptyList() {
        when(formuleRepository.findAll()).thenReturn(new ArrayList<>());

        List<FormuleDTO> result = formuleService.getAllFormules();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(formuleRepository, times(1)).findAll();
    }
}
