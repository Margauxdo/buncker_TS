package example.services;

import example.entity.Livreur;
import example.entity.Mouvement;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MouvementServiceTest {

    @Mock
    private MouvementRepository mouvementRepository;

    @InjectMocks
    private MouvementService mouvementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        when(mouvementRepository.save(mouvement)).thenReturn(mouvement);

        Mouvement result = mouvementService.createMouvement(mouvement);

        assertNotNull(result, "Mouvement should not be null");
        verify(mouvementRepository, times(1)).save(mouvement);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testCreateMouvement_Failure_Exception() {
        Mouvement mouvement = new Mouvement();
        when(mouvementRepository.save(mouvement)).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            mouvementService.createMouvement(mouvement);
        });

        assertEquals("Database error", exception.getMessage(), "Exception message should match expected error");
        verify(mouvementRepository, times(1)).save(mouvement);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testUpdateMouvement_Success() {
        int id = 1;
        Mouvement existingMouvement = new Mouvement();
        existingMouvement.setId(id);
        existingMouvement.setDateHeureMouvement(new Date());
        existingMouvement.setStatutSortie("Initial");

        Mouvement updatedMouvement = new Mouvement();
        updatedMouvement.setDateHeureMouvement(new Date());
        updatedMouvement.setStatutSortie("Updated");

        when(mouvementRepository.findById(id)).thenReturn(Optional.of(existingMouvement));
        when(mouvementRepository.save(any(Mouvement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mouvement result = mouvementService.updateMouvement(id, updatedMouvement);

        assertNotNull(result, "Mouvement should not be null");
        assertEquals(id, result.getId(), "Mouvement ID should remain unchanged");
        assertEquals("Updated", result.getStatutSortie(), "StatutSortie should be updated");

        verify(mouvementRepository, times(1)).findById(id);
        verify(mouvementRepository, times(1)).save(existingMouvement);
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testUpdateMouvement_Failure_Exception() {
        int id = 1;
        Mouvement mouvement = new Mouvement();
        mouvement.setId(id);

        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.updateMouvement(id, mouvement);
        });

        assertEquals("Mouvement not found", exception.getMessage());

        verify(mouvementRepository, times(1)).findById(id);
        verify(mouvementRepository, never()).save(any(Mouvement.class));
        verifyNoMoreInteractions(mouvementRepository);
    }



    @Test
    public void testDeleteMouvement_Success() {
        int id = 1;

        when(mouvementRepository.existsById(id)).thenReturn(true);
        doNothing().when(mouvementRepository).deleteById(id);

        mouvementService.deleteMouvement(id);

        verify(mouvementRepository, times(1)).existsById(id);
        verify(mouvementRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testDeleteMouvement_Failure_Exception() {
        int id = 1;

        when(mouvementRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.deleteMouvement(id);
        });

        assertEquals("Mouvement not found with ID: " + id, exception.getMessage());
        verify(mouvementRepository, times(1)).existsById(id);
        verify(mouvementRepository, never()).deleteById(anyInt());
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testGetMouvementById_Success() {
        int id = 1;
        Mouvement mouvement = new Mouvement();
        mouvement.setId(id);

        when(mouvementRepository.findById(id)).thenReturn(Optional.of(mouvement));

        Mouvement result = mouvementService.getMouvementById(id);

        assertNotNull(result, "Mouvement should not be null");
        assertEquals(id, result.getId(), "Mouvement ID should match");

        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetMouvementById_Failure_Exception() {
        int id = 1;

        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        Mouvement result = mouvementService.getMouvementById(id);

        Assertions.assertNull(result, "Mouvement should be null if not found");
        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetAllMouvements_Success() {
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(new Mouvement());
        mouvements.add(new Mouvement());

        when(mouvementRepository.findAll()).thenReturn(mouvements);

        List<Mouvement> result = mouvementService.getAllMouvements();

        assertEquals(2, result.size(), "The list of mouvements should contain 2 elements");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetAllMouvements_Failure_Exception() {
        when(mouvementRepository.findAll()).thenReturn(new ArrayList<>());

        List<Mouvement> result = mouvementService.getAllMouvements();

        Assertions.assertTrue(result.isEmpty(), "The list of mouvements should be empty");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testNoInteractionWithMouvementRepository_Success() {
        verifyNoInteractions(mouvementRepository);
    }

    @Test
    public void testNoInteractionWithMouvementRepository_Failure_Exception() {
        mouvementService.getMouvementById(1);
        Assertions.assertThrows(AssertionError.class, () -> {
            verifyNoInteractions(mouvementRepository);
        });
    }

    @Test
    public void testCreateMouvement_WithLivreurs() {
        Mouvement mouvement = new Mouvement();
        mouvement.setLivreurs(Collections.singletonList(new Livreur()));

        when(mouvementRepository.save(any(Mouvement.class))).thenReturn(mouvement);

        Mouvement createdMouvement = mouvementService.createMouvement(mouvement);

        assertNotNull(createdMouvement);
        assertEquals(1, createdMouvement.getLivreurs().size());
    }

}

