package example.services;

import example.DTO.LivreurDTO;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LivreurServiceTest {

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private MouvementRepository mouvementRepository;

    @InjectMocks
    private LivreurService livreurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Livreur createLivreur() {
        return Livreur.builder()
                .id(1)
                .nomLivreur("John Doe")
                .codeLivreur("CODE123")
                .mouvements(new ArrayList<>())
                .build();
    }

    private LivreurDTO createLivreurDTO() {
        return LivreurDTO.builder()
                .id(1)
                .nomLivreur("John Doe")
                .codeLivreur("CODE123")
                .mouvementIds(List.of(1, 2))
                .build();
    }

    @Test
    public void testGetLivreurById_Success() {
        Livreur livreur = createLivreur();

        when(livreurRepository.findById(1)).thenReturn(Optional.of(livreur));

        LivreurDTO result = livreurService.getLivreurById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getNomLivreur());
        assertEquals("CODE123", result.getCodeLivreur());
        verify(livreurRepository, times(1)).findById(1);
    }

    @Test
    public void testGetLivreurById_NotFound() {
        when(livreurRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> livreurService.getLivreurById(1)
        );

        assertEquals("Livreur introuvable avec l'ID : 1", exception.getMessage());
        verify(livreurRepository, times(1)).findById(1);
    }



    @Test
    public void testDeleteLivreur_NotFound() {
        when(livreurRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> livreurService.deleteLivreur(1)
        );

        assertEquals("Livreur introuvable avec l'ID : 1", exception.getMessage());
        verify(livreurRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAllLivreurs_Success() {
        List<Livreur> livreurs = new ArrayList<>();
        livreurs.add(createLivreur());

        when(livreurRepository.findAll()).thenReturn(livreurs);

        List<LivreurDTO> result = livreurService.getAllLivreurs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getNomLivreur());
        verify(livreurRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllLivreurs_EmptyList() {
        when(livreurRepository.findAll()).thenReturn(new ArrayList<>());

        List<LivreurDTO> result = livreurService.getAllLivreurs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(livreurRepository, times(1)).findAll();
    }

    @Test
    public void testCreateLivreur_Success() {
        // Arrange
        LivreurDTO livreurDTO = createLivreurDTO();
        Livreur livreur = createLivreur();

        // Simuler la récupération des mouvements et la sauvegarde du livreur
        when(mouvementRepository.findAllById(livreurDTO.getMouvementIds())).thenReturn(new ArrayList<>());
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);

        // Act
        LivreurDTO result = livreurService.createLivreur(livreurDTO);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("John Doe", result.getNomLivreur(), "Le nom du livreur doit correspondre");
        verify(mouvementRepository, atLeastOnce()).findAllById(livreurDTO.getMouvementIds());
        verify(livreurRepository, times(1)).save(any(Livreur.class));
    }




}
