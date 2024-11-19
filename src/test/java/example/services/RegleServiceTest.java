package example.services;

import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class RegleServiceTest {

    @Mock
    private RegleRepository regleRepository;

    @InjectMocks
    private RegleService regleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRegle_Success() {
        Regle regle = new Regle();
        when(regleRepository.save(regle)).thenReturn(regle);

        Regle result = regleService.createRegle(regle);

        Assertions.assertNotNull(result, "Rule should not be null");
        verify(regleRepository, times(1)).save(regle);
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testCreateRegle_NullInput_ShouldThrowException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            regleService.createRegle(null);
        });

        Assertions.assertEquals("Ruler cannot be null", exception.getMessage());
        verifyNoInteractions(regleRepository);
    }


    @Test
    public void testReadRegle_Success() {
        int id = 1;
        Regle regle = new Regle();
        regle.setId(id);
        when(regleRepository.findById(id)).thenReturn(Optional.of(regle));

        Regle result = regleService.readRegle(id);

        Assertions.assertNotNull(result, "Rule should not be null");
        Assertions.assertEquals(id, result.getId(), "Ruler ID should be correct");
        verify(regleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testReadRegle_NotFound_ShouldThrowException() {
        int id = 1;
        when(regleRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RegleNotFoundException.class, () -> {
            regleService.readRegle(id);
        });

        Assertions.assertEquals("Ruler with id 1 not found", exception.getMessage());
        verify(regleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testUpdateRegle_Success() {
        int id = 1;
        Regle regle = new Regle();
        regle.setId(id);

        when(regleRepository.findById(id)).thenReturn(Optional.of(regle));
        when(regleRepository.save(regle)).thenReturn(regle);

        Regle updatedRegle = regleService.updateRegle(id, regle);

        Assertions.assertNotNull(updatedRegle, "Updated rule should not be null");
        Assertions.assertEquals(id, updatedRegle.getId(), "Rule ID should be updated");
        verify(regleRepository, times(1)).findById(id);
        verify(regleRepository, times(1)).save(regle);
        verifyNoMoreInteractions(regleRepository);
    }


    @Test
    public void testUpdateRegle_NotFound_ShouldThrowException() {
        int id = 1;
        Regle regle = new Regle();
        when(regleRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RegleNotFoundException.class, () -> {
            regleService.updateRegle(id, regle);
        });

        Assertions.assertEquals("Ruler withID 1 not found", exception.getMessage());
        verify(regleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleRepository);
    }



    @Test
    public void testUpdateRegle_NullInput_ShouldThrowNotFoundException() {
        Exception exception = Assertions.assertThrows(RegleNotFoundException.class, () -> {
            regleService.updateRegle(1, null);
        });

        Assertions.assertEquals("Ruler with id 1 not found for update", exception.getMessage());
        verifyNoInteractions(regleRepository);
    }



    @Test
    public void testDeleteRegle_Success() {
        int id = 1;
        when(regleRepository.existsById(id)).thenReturn(true);

        regleService.deleteRegle(id);

        verify(regleRepository, times(1)).existsById(id);
        verify(regleRepository, times(1)).deleteById(id);
    }


    @Test
    public void testDeleteRegle_NotFound_ShouldThrowException() {
        int id = 1;
        when(regleRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RegleNotFoundException.class, () -> {
            regleService.deleteRegle(id);
        });

        Assertions.assertEquals("Ruler with id 1 not found, cannot delete", exception.getMessage());
        verify(regleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testReadAllRegles_Success() {
        List<Regle> regles = new ArrayList<>();
        regles.add(new Regle());
        regles.add(new Regle());

        when(regleRepository.findAll()).thenReturn(regles);

        List<Regle> result = regleService.readAllRegles();

        Assertions.assertEquals(2, result.size(), "There should be 2 rules");
        verify(regleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testReadAllRegles_EmptyList() {
        when(regleRepository.findAll()).thenReturn(new ArrayList<>());

        List<Regle> result = regleService.readAllRegles();

        Assertions.assertTrue(result.isEmpty(), "Rule list should be empty");
        verify(regleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleRepository);
    }
}
