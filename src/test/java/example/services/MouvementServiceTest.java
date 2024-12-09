package example.services;

import example.DTO.MouvementDTO;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
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

        // Mock findById to return a valid optional object
        Mouvement mouvement = new Mouvement();
        mouvement.setId(id);

        when(mouvementRepository.findById(id)).thenReturn(Optional.of(mouvement));
        doNothing().when(mouvementRepository).deleteById(id);

        // Call the service method
        mouvementService.deleteMouvement(id);

        // Verify interactions
        verify(mouvementRepository, times(1)).findById(id);
        verify(mouvementRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }



    @Test
    public void testDeleteMouvement_Failure_Exception() {
        int id = 1;

        // Mock findById to return an empty Optional, simulating a missing entity
        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        // Assert that the correct exception is thrown
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.deleteMouvement(id);
        });

        // Validate exception message
        assertEquals("Mouvement not found with ID: " + id, exception.getMessage());

        // Verify repository interactions
        verify(mouvementRepository, times(1)).findById(id);
        verify(mouvementRepository, never()).deleteById(anyInt());
        verifyNoMoreInteractions(mouvementRepository);
    }



    @Test
    public void testGetMouvementById_Success() {
        int id = 1;
        Mouvement mouvement = new Mouvement();
        mouvement.setId(id);

        // Create a Valise and Client associated with the Mouvement
        Valise valise = new Valise();
        valise.setId(1);
        Client client = new Client();
        valise.setClient(client);

        mouvement.setValise(valise); // Associate the Valise with the Mouvement

        // Mock the repository to return the Mouvement
        when(mouvementRepository.findById(id)).thenReturn(Optional.of(mouvement));

        // Call the service method
        MouvementDTO result = mouvementService.getMouvementById(id);

        // Assertions
        assertNotNull(result, "MouvementDTO should not be null");
        assertEquals(id, result.getId(), "MouvementDTO ID should match");
        assertNotNull(result.getValiseId(), "MouvementDTO's ValiseId should not be null");

        // Verify repository interaction
        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testGetMouvementById_Failure_Exception() {
        int id = 1;

        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.getMouvementById(id);
        });

        assertEquals("Mouvement not found with ID: " + id, exception.getMessage());

        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testGetAllMouvements_Success() {
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(new Mouvement());
        mouvements.add(new Mouvement());

        when(mouvementRepository.findAll()).thenReturn(mouvements);

        List<MouvementDTO> result = mouvementService.getAllMouvements();

        assertEquals(2, result.size(), "The list of mouvements should contain 2 elements");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetAllMouvements_Failure_Exception() {
        when(mouvementRepository.findAll()).thenReturn(new ArrayList<>());

        List<MouvementDTO> result = mouvementService.getAllMouvements();

        Assertions.assertTrue(result.isEmpty(), "The list of mouvements should be empty");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testNoInteractionWithMouvementRepository_Success() {
        verifyNoInteractions(mouvementRepository);
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

