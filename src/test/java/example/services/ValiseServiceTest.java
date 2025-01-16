package example.services;

import example.DTO.ValiseDTO;
import example.entity.*;
import example.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ValiseServiceTest {

    @Mock
    private ValiseRepository valiseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MouvementRepository mouvementRepository;

    @Mock
    private RegleRepository regleRepository;

    @Mock
    private TypeValiseRepository typeValiseRepository;

    @InjectMocks
    private ValiseService valiseService;




    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testDeleteValise_Success() {
        // Arrange
        int id = 1;
        Valise valise = new Valise();
        valise.setId(1);

        when(valiseRepository.findById(id)).thenReturn(Optional.of(valise));
        doNothing().when(valiseRepository).delete(valise);

        // Act
        valiseService.deleteValise(id);

        // Assert
        verify(valiseRepository, times(1)).findById(id);
        verify(valiseRepository, times(1)).delete(valise);
    }


}
