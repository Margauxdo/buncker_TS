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
    public void testCreateRetourSecurite_Success() {
        RetourSecuriteDTO retourSecuriteDTO = new RetourSecuriteDTO();
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteRepository.save(any(RetourSecurite.class))).thenReturn(retourSecurite);

        RetourSecuriteDTO result = retourSecuriteService.createRetourSecurite(retourSecuriteDTO);

        Assertions.assertNotNull(result, "Security should not be null");
        verify(retourSecuriteRepository, times(1)).save(any(RetourSecurite.class));
    }

    @Test
    public void testUpdateRetourSecurite_Success() {
        int id = 1;
        RetourSecuriteDTO retourSecuriteDTO = new RetourSecuriteDTO();
        retourSecuriteDTO.setId(id);

        RetourSecurite existingRetourSecurite = new RetourSecurite();
        existingRetourSecurite.setId(id);

        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.of(existingRetourSecurite));
        when(retourSecuriteRepository.save(any(RetourSecurite.class))).thenReturn(existingRetourSecurite);

        RetourSecuriteDTO result = retourSecuriteService.updateRetourSecurite(id, retourSecuriteDTO);

        Assertions.assertNotNull(result, "RetourSecuriteDTO should not be null");
        Assertions.assertEquals(id, result.getId(), "Wrong retour security ID");
        verify(retourSecuriteRepository, times(1)).findById(id);
        verify(retourSecuriteRepository, times(1)).save(any(RetourSecurite.class));
    }

    @Test
    public void testDeleteRetourSecurite_Success() {
        int id = 1;
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(id);

        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.of(retourSecurite));
        doNothing().when(retourSecuriteRepository).delete(retourSecurite);

        retourSecuriteService.deleteRetourSecurite(id);

        verify(retourSecuriteRepository, times(1)).findById(id);
        verify(retourSecuriteRepository, times(1)).delete(retourSecurite);
    }

    @Test
    public void testGetRetourSecurite_Success() {
        int id = 1;
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(id);

        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.of(retourSecurite));

        RetourSecuriteDTO result = retourSecuriteService.getRetourSecurite(id);

        Assertions.assertNotNull(result, "RetourSecuriteDTO should not be null");
        Assertions.assertEquals(id, result.getId(), "Wrong retour security ID");
        verify(retourSecuriteRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllRetourSecurites_Success() {
        List<RetourSecurite> retourSecurites = new ArrayList<>();
        retourSecurites.add(new RetourSecurite());

        when(retourSecuriteRepository.findAll()).thenReturn(retourSecurites);

        List<RetourSecuriteDTO> result = retourSecuriteService.getAllRetourSecurites();

        Assertions.assertFalse(result.isEmpty(), "Security list should not be empty");
        verify(retourSecuriteRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteRetourSecurite_NotFound() {
        int id = 1;
        when(retourSecuriteRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            retourSecuriteService.deleteRetourSecurite(id);
        });

        verify(retourSecuriteRepository, times(1)).findById(id);
        verify(retourSecuriteRepository, never()).delete(any(RetourSecurite.class));
    }
}
