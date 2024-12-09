package example.services;

import example.DTO.RegleDTO;
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

        RegleDTO updatedRegle = regleService.updateRegle(id, new RegleDTO());

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
            regleService.updateRegle(id,new RegleDTO());
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
    public void testCreateRegle_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("RULE123");

        RegleDTO regleDTO = RegleDTO.builder()
                .coderegle("RULE123")
                .build();

        when(regleRepository.save(any(Regle.class))).thenReturn(regle);

        // Act
        RegleDTO result = regleService.createRegle(regleDTO);

        // Assert
        Assertions.assertNotNull(result, "La règle ne devrait pas être nulle");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Le code règle devrait correspondre");
        verify(regleRepository, times(1)).save(any(Regle.class));
    }

    @Test
    public void testReadRegle_Success() {
        int id = 1;
        Regle regle = new Regle();
        regle.setId(id);
        regle.setCoderegle("RULE123");

        when(regleRepository.findById(id)).thenReturn(Optional.of(regle));

        RegleDTO result = regleService.readRegle(id);

        Assertions.assertNotNull(result, "La règle ne devrait pas être nulle");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Le code règle devrait correspondre");
        verify(regleRepository, times(1)).findById(id);
    }

    @Test
    public void testReadAllRegles_Success() {
        Regle regle1 = new Regle();
        regle1.setId(1);
        regle1.setCoderegle("RULE1");

        Regle regle2 = new Regle();
        regle2.setId(2);
        regle2.setCoderegle("RULE2");

        when(regleRepository.findAll()).thenReturn(List.of(regle1, regle2));

        List<RegleDTO> result = regleService.readAllRegles();

        Assertions.assertEquals(2, result.size(), "Il devrait y avoir 2 règles");
        Assertions.assertEquals("RULE1", result.get(0).getCoderegle(), "Le code de la première règle devrait correspondre");
        Assertions.assertEquals("RULE2", result.get(1).getCoderegle(), "Le code de la deuxième règle devrait correspondre");
    }




}
