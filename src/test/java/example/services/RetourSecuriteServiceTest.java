package example.services;

import example.DTO.RetourSecuriteDTO;
import example.entity.RetourSecurite;
import example.repositories.RetourSecuriteRepository;
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
    public void testUpdateRetourSecurite_NotFound() {
        // Arrange
        int id = 1;
        RetourSecuriteDTO retourSecuriteDTO = new RetourSecuriteDTO();

        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> retourSecuriteService.updateRetourSecurite(id, retourSecuriteDTO)
        );

        Assertions.assertEquals("RetourSecurite introuvable avec l'ID : 1", exception.getMessage());
        verify(retourSecuriteRepository, times(1)).findById(id);
    }

    @Test
    public void testGetRetourSecurite_Success() {
        // Arrange
        int id = 1;
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .id(id)
                .numero(12345L)
                .mouvements(new ArrayList<>()) // Initialiser la liste des mouvements
                .build();

        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.of(retourSecurite));

        // Act
        RetourSecuriteDTO result = retourSecuriteService.getRetourSecurite(id);

        // Assert
        Assertions.assertNotNull(result, "RetourSecuriteDTO should not be null");
        Assertions.assertEquals(id, result.getId(), "ID should match");
        verify(retourSecuriteRepository, times(1)).findById(id);
    }


    @Test
    public void testGetRetourSecurite_NotFound() {
        // Arrange
        int id = 1;
        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> retourSecuriteService.getRetourSecurite(id)
        );

        Assertions.assertEquals("Retour Sécurité introuvable avec l'ID : 1", exception.getMessage());
        verify(retourSecuriteRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllRetourSecurites_Success() {
        // Arrange
        List<RetourSecurite> retourSecurites = new ArrayList<>();
        retourSecurites.add(RetourSecurite.builder().id(1).mouvements(new ArrayList<>()).build()); // Initialiser mouvements
        retourSecurites.add(RetourSecurite.builder().id(2).mouvements(new ArrayList<>()).build()); // Initialiser mouvements

        when(retourSecuriteRepository.findAll()).thenReturn(retourSecurites);

        // Act
        List<RetourSecuriteDTO> result = retourSecuriteService.getAllRetourSecurites();

        // Assert
        Assertions.assertFalse(result.isEmpty(), "List should not be empty");
        Assertions.assertEquals(2, result.size(), "List size should be 2");
        verify(retourSecuriteRepository, times(1)).findAll();
    }


    @Test
    public void testDeleteRetourSecurite_Success() {
        // Arrange
        int id = 1;
        when(retourSecuriteRepository.existsById(id)).thenReturn(true);
        doNothing().when(retourSecuriteRepository).deleteById(id);

        // Act
        retourSecuriteService.deleteRetourSecurite(id);

        // Assert
        verify(retourSecuriteRepository, times(1)).existsById(id);
        verify(retourSecuriteRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteRetourSecurite_NotFound() {
        // Arrange
        int id = 1;
        when(retourSecuriteRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> retourSecuriteService.deleteRetourSecurite(id)
        );

        Assertions.assertEquals("Retour Sécurité introuvable avec l'ID : " + id, exception.getMessage());
        verify(retourSecuriteRepository, times(1)).existsById(id);
    }
}
