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
    public void testCreateRegle_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("RULE123");

        RegleDTO regleDTO = RegleDTO.builder()
                .coderegle("RULE123")
                .build();

        when(regleRepository.save(any(Regle.class))).thenReturn(regle);

        // Act
        Regle result = regleService.createRegle(regleDTO);

        // Assert
        Assertions.assertNotNull(result, "La règle ne devrait pas être nulle");
        Assertions.assertEquals("RULE123", result.getCoderegle(), "Le code règle devrait correspondre");
        verify(regleRepository, times(1)).save(any(Regle.class));
    }




}
