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
        // Arrange
        RegleDTO regleDTO = RegleDTO.builder()
                .coderegle("RULE123")
                .reglePourSortie("Sortie spéciale")
                .build();

        Regle regle = Regle.builder()
                .id(1)
                .coderegle("RULE123")
                .reglePourSortie("Sortie spéciale")
                .build();

        when(regleRepository.save(any(Regle.class))).thenReturn(regle);

        // Act
        Regle result = regleService.createRegle(regleDTO);

        // Assert
        Assertions.assertNotNull(result, "La règle créée ne doit pas être null");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Le code règle doit correspondre");
        verify(regleRepository, times(1)).save(any(Regle.class));
    }

    @Test
    public void testUpdateRegle_Success() {
        // Arrange
        int id = 1;
        Regle existingRegle = Regle.builder()
                .id(id)
                .coderegle("RULE123")
                .reglePourSortie("Ancienne sortie")
                .build();

        RegleDTO regleDTO = RegleDTO.builder()
                .coderegle("NEW_RULE123")
                .reglePourSortie("Nouvelle sortie")
                .build();

        when(regleRepository.findById(id)).thenReturn(Optional.of(existingRegle));
        when(regleRepository.save(any(Regle.class))).thenReturn(existingRegle);

        // Act
        RegleDTO updatedRegle = regleService.updateRegle(id, regleDTO);

        // Assert
        Assertions.assertNotNull(updatedRegle, "La règle mise à jour ne doit pas être null");
        Assertions.assertEquals("NEW_RULE123", updatedRegle.getCoderegle(), "Le code règle doit correspondre");
        Assertions.assertEquals("Nouvelle sortie", updatedRegle.getReglePourSortie(), "La sortie doit correspondre");
        verify(regleRepository, times(1)).findById(id);
        verify(regleRepository, times(1)).save(existingRegle);
    }

    @Test
    public void testGetRegleById_Success() {
        // Arrange
        int id = 1;
        Regle regle = Regle.builder()
                .id(id)
                .coderegle("RULE123")
                .reglePourSortie("Sortie test")
                .build();

        when(regleRepository.findById(id)).thenReturn(Optional.of(regle));

        // Act
        RegleDTO result = regleService.getRegleById(id);

        // Assert
        Assertions.assertNotNull(result, "La règle récupérée ne doit pas être null");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Le code règle doit correspondre");
        verify(regleRepository, times(1)).findById(id);
    }



    @Test
    public void testGetAllRegles_Success() {
        // Arrange
        List<Regle> regleList = new ArrayList<>();
        regleList.add(Regle.builder().id(1).coderegle("RULE123").build());
        regleList.add(Regle.builder().id(2).coderegle("RULE456").build());

        when(regleRepository.findAll()).thenReturn(regleList);

        // Act
        List<RegleDTO> result = regleService.getAllRegles();

        // Assert
        Assertions.assertEquals(2, result.size(), "La liste des règles doit contenir 2 éléments");
        verify(regleRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteRegle_Success() {
        // Arrange
        int id = 1;
        when(regleRepository.existsById(id)).thenReturn(true);
        doNothing().when(regleRepository).deleteById(id);

        // Act
        regleService.deleteRegle(String.valueOf(id));

        // Assert
        verify(regleRepository, times(1)).existsById(id);
        verify(regleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteRegle_NotFound() {
        // Arrange
        int id = 1;
        when(regleRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RegleNotFoundException.class, () -> regleService.deleteRegle(String.valueOf(id)));
        verify(regleRepository, times(1)).existsById(id);
    }
}
