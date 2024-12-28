package example.services;

import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ValiseServiceTest {

    @Mock
    private ValiseRepository valiseRepository;

    @Mock
    private ClientRepository clientRepository;

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
        when(valiseRepository.existsById(id)).thenReturn(true);

        // Act
        valiseService.deleteValise(id);

        // Assert
        verify(valiseRepository, times(1)).existsById(id);
        verify(valiseRepository, times(1)).deleteById(id);
    }




}

