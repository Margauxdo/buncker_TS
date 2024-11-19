package example.services;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LivreurServiceTest {

    @Mock
    private LivreurRepository livreurRepository;

    @InjectMocks
    private LivreurService livreurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateLivreur_Success(){
        Livreur livreur = new Livreur();
        when(livreurRepository.save(livreur)).thenReturn(livreur);
        Livreur result = livreurService.createLivreur(livreur);
        Assertions.assertNotNull(result, "Livreur should not be null");
        verify(livreurRepository, times(1)).save(livreur);

    }

    @Test
    public void testCreateLivreur_Failure_Exception() {
        Livreur livreur = new Livreur();
        when(livreurRepository.save(livreur)).thenThrow(new RuntimeException("database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            livreurService.createLivreur(livreur);
        });

        Assertions.assertEquals("database error", exception.getMessage());
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    public void testUpdateLivreur_Success(){
        int id = 1;
        Livreur livreur = new Livreur();
        livreur.setId(2);
        when(livreurRepository.existsById(id)).thenReturn(true);
        when(livreurRepository.save(any(Livreur.class))).thenAnswer(invocationOnMock -> {
            Livreur savedLivreur = invocationOnMock.getArgument(0);
            savedLivreur.setId(id);
            return savedLivreur;
        });
        Livreur result = livreurService.updateLivreur(id, livreur);
        Assertions.assertNotNull(result, "Livreur should not be null");
        Assertions.assertEquals(livreur.getId(), result.getId(), "Livreur id should be the same");
        verify(livreurRepository, times(1)).existsById(id);
        verify(livreurRepository, times(1)).save(livreur);
        verifyNoMoreInteractions(livreurRepository);

    }
    @Test
    public void testUpdateLivreur_Failure_Exception() {
        int id = 1;
        Livreur livreur = new Livreur();
        livreur.setId(id);

        when(livreurRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RegleNotFoundException.class, () -> {
            livreurService.updateLivreur(id, livreur);
        });

        Assertions.assertEquals("Delivery person not found with ID " + id, exception.getMessage(),
                "Exception message should match expected error");

        verify(livreurRepository, times(1)).existsById(id);
        verify(livreurRepository, never()).save(any(Livreur.class));
        verifyNoMoreInteractions(livreurRepository);
    }



    @Test
    public void testDeleteLivreur_Success(){
        int id = 1;
        when(livreurRepository.existsById(id)).thenReturn(true);

        livreurService.deleteLivreur(id);

        verify(livreurRepository, times(1)).existsById(id);
        verify(livreurRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(livreurRepository);
    }


    @Test
    public void testDeleteLivreur_Failure_Exception() {
        int id = 1;

        when(livreurRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            livreurService.deleteLivreur(id);
        });

        Assertions.assertEquals("delivery person not found with ID: " + id, exception.getMessage(),
                "Exception message should match expected error");

        verify(livreurRepository, times(1)).existsById(id);
        verify(livreurRepository, never()).deleteById(id);
    }





    @Test
    public void testGetLivreurById_Success(){
        int id = 1;
        Livreur livreur = new Livreur();
        livreur.setId(id);
        when(livreurRepository.findById(id)).thenReturn(Optional.of(livreur));
        Livreur result = livreurService.getLivreurById(id);
        Assertions.assertNotNull(result, "Livreur should not be null");
        Assertions.assertEquals(livreur.getId(), result.getId(), "Livreur id should be the same");
        verify(livreurRepository, times(1)).findById(id);
        verifyNoMoreInteractions(livreurRepository);
    }
    @Test
    public void testGetLivreurById_Failure_Exception(){
        int id = 1;
        when(livreurRepository.findById(id)).thenReturn(Optional.empty());
        Livreur result = livreurService.getLivreurById(id);
        Assertions.assertNull(result,"Livreur should not be null");
        verify(livreurRepository, times(1)).findById(id);
        verifyNoMoreInteractions(livreurRepository);
    }
    @Test
    public void testGetAllLivreurs_Success(){
        List<Livreur> livreurs = new ArrayList<>();
        livreurs.add(new Livreur());
        when(livreurRepository.findAll()).thenReturn(livreurs);
        List<Livreur> result = livreurService.getAllLivreurs();
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
    @Test
    public void testNoInteractionWithLivreurRepository_Failure_Exception() {
        int id = 1;
        Livreur livreur = new Livreur();
        livreur.setId(id);

        when(livreurRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RegleNotFoundException.class, () -> {
            livreurService.updateLivreur(id, livreur);
        });

        Assertions.assertEquals("Delivery person not found with ID " + id, exception.getMessage());
        verify(livreurRepository, times(1)).existsById(id);
        verify(livreurRepository, never()).save(any(Livreur.class));
    }

    @Test
    public void testUpdateLivreur_LivreurNotFound() {
        when(livreurRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RegleNotFoundException.class, () -> livreurService.updateLivreur(1, new Livreur()));
    }

}
