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
    public void testCreateValise_Success() {
        // Arrange
        Client client = new Client();
        client.setId(1);
        client.setName("name client");
        client.setEmail("testclient@gmail.com");

        ValiseDTO valiseDTO = ValiseDTO.builder()
                .description("Valise de transport spécialisé")
                .numeroValise(12565657L)
                .refClient("REF-7952")
                .clientId(client.getId())
                .build();

        Valise valise = Valise.builder()
                .description("Valise de transport spécialisé")
                .numeroValise(12565657L)
                .refClient("REF-7952")
                .client(client)
                .build();

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(valiseRepository.save(any(Valise.class))).thenReturn(valise);

        // Act
        ValiseDTO result = valiseService.createValise(valiseDTO);

        // Assert
        assertNotNull(result, "The suitcase DTO must not be null");
        assertEquals("Valise de transport spécialisé", result.getDescription());
        verify(valiseRepository, times(1)).save(any(Valise.class));
        verifyNoMoreInteractions(valiseRepository);
    }

    @Test
    public void testCreateValise_Failure_Exception() {
        // Arrange
        ValiseDTO valiseDTO = ValiseDTO.builder()
                .description("Valise de transport spécialisé")
                .numeroValise(12565657L)
                .refClient("REF-7952")
                .clientId(1)
                .build();

        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.createValise(valiseDTO);
        });

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findById(1);
        verifyNoInteractions(valiseRepository);
    }

    @Test
    public void testUpdateValise_Success() {
        // Arrange
        int id = 1;
        Valise valise = Valise.builder()
                .id(id)
                .description("Initial Description")
                .build();

        ValiseDTO valiseDTO = ValiseDTO.builder()
                .id(id)
                .description("Updated Description")
                .build();

        when(valiseRepository.findById(id)).thenReturn(Optional.of(valise));
        when(valiseRepository.save(any(Valise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ValiseDTO result = valiseService.updateValise(id, valiseDTO);

        // Assert
        assertNotNull(result, "The suitcase DTO must not be null");
        assertEquals("Updated Description", result.getDescription());
        verify(valiseRepository, times(1)).findById(id);
        verify(valiseRepository, times(1)).save(any(Valise.class));
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

    @Test
    public void testGetValiseById_Success() {
        // Arrange
        int id = 1;
        Valise valise = Valise.builder()
                .id(id)
                .description("Test Valise")
                .build();

        when(valiseRepository.findById(id)).thenReturn(Optional.of(valise));

        // Act
        ValiseDTO result = valiseService.getValiseById(id);

        // Assert
        assertNotNull(result, "The suitcase DTO must not be null");
        assertEquals("Test Valise", result.getDescription());
        verify(valiseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllValises_Success() {
        // Arrange
        List<Valise> valises = List.of(
                Valise.builder().id(1).description("Valise 1").build(),
                Valise.builder().id(2).description("Valise 2").build()
        );

        when(valiseRepository.findAll()).thenReturn(valises);

        // Act
        List<ValiseDTO> result = valiseService.getAllValises();

        // Assert
        assertEquals(2, result.size(), "The suitcase DTO list must contain 2 items");
        verify(valiseRepository, times(1)).findAll();
    }
}

