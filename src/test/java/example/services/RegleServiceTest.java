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
import org.springframework.data.domain.Sort;

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
        // Arrange: Create a valid Regle object
        Regle regle = new Regle();
        regle.setCoderegle("RULE123"); // Set a valid coderegle to pass validation

        // Mock the repository save behavior
        when(regleRepository.save(regle)).thenReturn(regle);

        // Act: Call the service method
        Regle result = regleService.createRegle(regle);

        // Assert: Verify the result and interactions
        Assertions.assertNotNull(result, "Rule should not be null");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Coderegle should match the expected value");
        verify(regleRepository, times(1)).save(regle);
        verifyNoMoreInteractions(regleRepository);
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

        Assertions.assertEquals("Règle non trouvée avec l'ID 1", exception.getMessage(),
                "Exception message should match the expected error message.");
        verify(regleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(regleRepository);
    }

    @Test
    public void testUpdateRegle_NullInput_ShouldThrowIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            regleService.updateRegle(1, null);
        });

        Assertions.assertEquals("La règle fournie est nulle.", exception.getMessage(),
                "Exception message should match the expected error message.");

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
        // Arrange: Mock the repository to return a list of two `Regle` objects
        List<Regle> regles = new ArrayList<>();

        Regle regle1 = new Regle();
        regle1.setId(1);
        regle1.setCoderegle("Rule1");
        regle1.setReglePourSortie("Description1");

        Regle regle2 = new Regle();
        regle2.setId(2);
        regle2.setCoderegle("Rule2");
        regle2.setReglePourSortie("Description2");

        regles.add(regle1);
        regles.add(regle2);

        when(regleRepository.findAll()).thenReturn(regles);

        // Act: Call the service method
        List<Regle> result = regleService.readAllRegles();

        // Assert: Verify the results
        Assertions.assertEquals(2, result.size(), "There should be 2 rules");
        Assertions.assertEquals("Rule1", result.get(0).getCoderegle(), "First rule's code should match");
        Assertions.assertEquals("Rule2", result.get(1).getCoderegle(), "Second rule's code should match");
        verify(regleRepository, times(1)).findAll();
        verifyNoMoreInteractions(regleRepository);
    }



}
