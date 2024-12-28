package example.services;

import example.DTO.LivreurDTO;
import example.entity.Livreur;
import example.exceptions.RegleNotFoundException;
import example.repositories.LivreurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LivreurServiceTest {

    @Mock
    private LivreurRepository livreurRepository;

    @InjectMocks
    private LivreurService livreurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetLivreurById_Success() {
        Livreur livreur = Livreur.builder().id(1).nomLivreur("John Doe").build();

        when(livreurRepository.findById(1)).thenReturn(Optional.of(livreur));

        LivreurDTO result = livreurService.getLivreurById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getNomLivreur());
    }

    @Test
    public void testDeleteLivreur_NotFound() {
        when(livreurRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> livreurService.deleteLivreur(1));
    }








    @Test
    public void testGetAllLivreurs_Success(){
        List<Livreur> livreurs = new ArrayList<>();
        livreurs.add(new Livreur());
        when(livreurRepository.findAll()).thenReturn(livreurs);
        List<LivreurDTO> result = livreurService.getAllLivreurs();
        Assertions.assertEquals(1,result.size(),"la liste des livreurs devrait contenir un element");
        verify(livreurRepository, times(1)).findAll();
        verifyNoMoreInteractions(livreurRepository);
    }
    @Test
    public void testGetAllLivreurs_Failure_Exception(){
        when(livreurRepository.findAll()).thenThrow(new RuntimeException("database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            livreurService.getAllLivreurs();
        });
        Assertions.assertEquals("database error",exception.getMessage(),"Exception message should match expected error");
        verify(livreurRepository, times(1)).findAll();
        verifyNoMoreInteractions(livreurRepository);
    }
    @Test
    public void testNoInteractionWithLivreurRepository_Success(){
        verifyNoInteractions(livreurRepository);
    }


}
