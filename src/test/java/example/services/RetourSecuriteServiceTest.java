package example.services;

import example.entities.RetourSecurite;
import example.repositories.RetourSecuriteRepository;
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

public class RetourSecuriteServiceTest {

    @Mock
    private RetourSecuriteRepository retourSecuriteRepository;

    @InjectMocks
    private RetourSecuriteService retourSecuriteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRetourSecurite_Success() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteRepository.save(retourSecurite)).thenReturn(retourSecurite);

        RetourSecurite result = retourSecuriteService.createRetourSecurite(retourSecurite);
        Assertions.assertNotNull(result, "Security should not be null");
        verify(retourSecuriteRepository, times(1)).save(retourSecurite);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }
    @Test
    public void testCreateRetourSecurite_Failure_Exception() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteRepository.save(retourSecurite)).thenThrow(new RuntimeException("database error"));
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            retourSecuriteService.createRetourSecurite(retourSecurite);
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(retourSecuriteRepository, times(1)).save(retourSecurite);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }

    @Test
    public void testUpdateRetourSecurite_Success() {
        int id = 1;
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(id); // L'ID doit correspondre ici

        // Simuler l'existence de l'entité avec cet ID
        when(retourSecuriteRepository.existsById(id)).thenReturn(true);

        // Simuler le comportement du save
        when(retourSecuriteRepository.save(any(RetourSecurite.class))).thenReturn(retourSecurite);

        // Appeler la méthode updateRetourSecurite
        RetourSecurite result = retourSecuriteService.updateRetourSecurite(id, retourSecurite);

        // Vérifications
        Assertions.assertNotNull(result, "Security should not be null");
        Assertions.assertEquals(id, result.getId(), "Wrong retour security id");

        // Vérification des interactions
        verify(retourSecuriteRepository, times(1)).existsById(id);
        verify(retourSecuriteRepository, times(1)).save(retourSecurite);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }


    @Test
    public void testUpdateRetourSecurite_Failure_Exception() {
        int id = 1;
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(id);

        // Simuler que l'ID n'existe pas
        when(retourSecuriteRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            retourSecuriteService.updateRetourSecurite(id, retourSecurite);
        });

        Assertions.assertEquals("Safety return does not exist", exception.getMessage(), "Exception message should match expected error");
        verify(retourSecuriteRepository, times(1)).existsById(id);
        verify(retourSecuriteRepository, never()).save(any(RetourSecurite.class));
        verifyNoMoreInteractions(retourSecuriteRepository);
    }

    @Test
    public void testDeleteRetourSecurite_Success() {
        int id = 1;
        doNothing().when(retourSecuriteRepository).deleteById(id);
        retourSecuriteService.deleteRetourSecurite(id);
        verify(retourSecuriteRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }
    @Test
    public void testDeleteRetourSecurite_Failure_Exception() {
        int id =1;
        doThrow(new RuntimeException("database error")).when(retourSecuriteRepository).deleteById(id);
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            retourSecuriteService.deleteRetourSecurite(id);
        });
        Assertions.assertEquals("database error", exception.getMessage(), "Exception message should match expected error");
        verify(retourSecuriteRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }
    @Test
    public void testGetRetourSecurite_Success() {
        int id = 1;
        RetourSecurite retourSecurite = new RetourSecurite();
        RetourSecurite retourSecurite1 = new RetourSecurite();
        retourSecurite.setId(id);
        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.of(retourSecurite));
        RetourSecurite result = retourSecuriteService.getRetourSecurite(id);
        Assertions.assertNotNull(result, "Security should not be null");
        Assertions.assertEquals(id, result.getId(), "Wrong retour security id");
        verify(retourSecuriteRepository, times(1)).findById(id);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }
    @Test
    public void testGetRetourSecurite_Failure_Exception() {
        int id = 1;
        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.empty());

        RetourSecurite result = retourSecuriteService.getRetourSecurite(id);
        Assertions.assertNull(result, "Security should be null");
        verify(retourSecuriteRepository, times(1)).findById(id);
        verifyNoMoreInteractions(retourSecuriteRepository);
    }

    @Test
    public void testGetAllRetourSecurites_Success() {
        List<RetourSecurite> retourSecurites = new ArrayList<>();
        retourSecurites.add(new RetourSecurite());
        when(retourSecuriteRepository.findAll()).thenReturn(retourSecurites);

        List<RetourSecurite> result = retourSecuriteService.getAllRetourSecurites();
        Assertions.assertFalse(result.isEmpty(), "Security list should not be empty"); // Vérifier que la liste n'est pas vide
        verify(retourSecuriteRepository, times(1)).findAll();
        verifyNoMoreInteractions(retourSecuriteRepository);
    }

    @Test
    public void testGetAllRetourSecurites_Failure_Exception() {
        when(retourSecuriteRepository.findAll()).thenReturn(new ArrayList<>());
        List<RetourSecurite> result = retourSecuriteService.getAllRetourSecurites();
        Assertions.assertTrue(result.isEmpty(), "Security should not be null");
        verify(retourSecuriteRepository, times(1)).findAll();
        verifyNoMoreInteractions(retourSecuriteRepository);
    }
    @Test
    public void testNoInteractionWithRetourSecuriteRepository_Success(){
        verifyNoInteractions(retourSecuriteRepository);
    }
    @Test
    public void testNoInteractionWithRetourSecuriteRepository_Failure_Exception() {}

}
