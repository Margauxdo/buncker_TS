package example.services;

import example.DTO.MouvementDTO;
import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    private Mouvement createMouvement() {
        return Mouvement.builder()
                .id(1)
                .dateHeureMouvement(new Date())
                .statutSortie("En cours")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .valise(Valise.builder().id(1).build())
                .livreur(Livreur.builder().id(1).nomLivreur("Livreur Test").build())
                .build();
    }

    private MouvementDTO createMouvementDTO() {
        return MouvementDTO.builder()
                .id(1)
                .dateHeureMouvement(new Date())
                .statutSortie("En cours")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .valiseId(1)
                .livreurId(1)
                .build();
    }

    @Test
    public void testGetMouvementById_Success() {
        int id = 1;
        Mouvement mouvement = createMouvement();

        when(mouvementRepository.findById(id)).thenReturn(Optional.of(mouvement));

        MouvementDTO result = mouvementService.getMouvementById(id);

        assertNotNull(result, "Le MouvementDTO ne doit pas être null");
        assertEquals(id, result.getId(), "L'ID du MouvementDTO doit correspondre");
        assertEquals(mouvement.getStatutSortie(), result.getStatutSortie(), "Le statut du mouvement doit correspondre");
        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetMouvementById_NotFound() {
        int id = 1;

        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> mouvementService.getMouvementById(id)
        );

        assertEquals("Mouvement introuvable avec l'ID : 1", exception.getMessage());
        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetAllMouvements_Success() {
        List<Mouvement> mouvements = List.of(createMouvement(), createMouvement());

        when(mouvementRepository.findAll()).thenReturn(mouvements);

        List<MouvementDTO> result = mouvementService.getAllMouvements();

        assertNotNull(result, "La liste des mouvements ne doit pas être null");
        assertEquals(2, result.size(), "La liste des mouvements doit contenir 2 éléments");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testGetAllMouvements_EmptyList() {
        when(mouvementRepository.findAll()).thenReturn(Collections.emptyList());

        List<MouvementDTO> result = mouvementService.getAllMouvements();

        assertNotNull(result, "La liste des mouvements ne doit pas être null");
        assertTrue(result.isEmpty(), "La liste des mouvements doit être vide");
        verify(mouvementRepository, times(1)).findAll();
        verifyNoMoreInteractions(mouvementRepository);
    }

    @Test
    public void testDeleteMouvement_Success() {
        // Arrange
        int id = 1;
        Mouvement mouvement = createMouvement();

        // Initialiser la valise avec une liste de mouvements non nulle
        Valise valise = mouvement.getValise();
        if (valise != null) {
            valise.setMouvements(new ArrayList<>(List.of(mouvement)));
        }

        when(mouvementRepository.findById(id)).thenReturn(Optional.of(mouvement));
        doNothing().when(mouvementRepository).delete(mouvement);

        // Act
        mouvementService.deleteMouvement(id);

        // Assert
        verify(mouvementRepository, times(1)).findById(id);
        verify(mouvementRepository, times(1)).delete(mouvement);
        verifyNoMoreInteractions(mouvementRepository);
    }


    @Test
    public void testDeleteMouvement_NotFound() {
        int id = 1;

        when(mouvementRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> mouvementService.deleteMouvement(id)
        );

        assertEquals("Mouvement introuvable avec l'ID : 1", exception.getMessage());
        verify(mouvementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(mouvementRepository);
    }
}
