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




}

