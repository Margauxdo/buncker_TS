package example.services;

import example.entity.Client;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
    @ExtendWith(MockitoExtension.class)
    public void testCreateValise_Success() {
        Client client = new Client();
        client.setId(1);
        client.setName("name client");
        client.setEmail("testclient@gmail.com");

        Valise valise = new Valise();
        valise.setClient(client);
        valise.setDescription("Valise de transport spécialisé");
        valise.setNumeroValise(12565657L);
        valise.setRefClient("REF-7952");

        when(clientRepository.findById(client.getId())).thenReturn(java.util.Optional.of(client)); // Mock du clientRepository
        when(valiseRepository.save(valise)).thenReturn(valise); // Mock du valiseRepository

        Valise result = valiseService.createValise(valise);

        assertNotNull(result, "The suitcase must not be null");
        verify(valiseRepository, times(1)).save(valise); // Vérifie que save a bien été appelé une fois
        verifyNoMoreInteractions(valiseRepository); // Vérifie qu'il n'y a pas d'autres appels aux mocks
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void testCreateValise_Failure_Exception() {
        Client client = new Client();
        client.setId(1);

        Valise valise = new Valise();
        valise.setClient(client);  // Assigner le client valide
        valise.setDescription("Valise de transport spécialisé");
        valise.setNumeroValise(12565657L);
        valise.setRefClient("REF-7952");

        when(clientRepository.findById(client.getId())).thenReturn(java.util.Optional.of(client));

        when(valiseRepository.save(valise)).thenThrow(new RuntimeException("Erreur interne lors de la création de la valise"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.createValise(valise);
        });

        assertEquals("Erreur interne lors de la création de la valise", exception.getMessage());

        verify(valiseRepository, times(1)).save(valise);
        verifyNoMoreInteractions(valiseRepository);
    }





    @Test
    public void testUpdateValise_Success(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(id);

        when(valiseRepository.existsById(id)).thenReturn(true);
        when(valiseRepository.save(any(Valise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Valise result = valiseService.updateValise( id, valise);

        assertNotNull(result, "The suitcase must not be null");
        assertEquals(id, result.getId(), "Suitcase ID must match");

        verify(valiseRepository, times(1)).existsById(id);
        verify(valiseRepository, times(1)).save(valise);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testUpdateValise_Failure_Exception(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(2);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            valiseService.updateValise( id, valise);
        });

        assertEquals("Suitcase ID does not match", exception.getMessage());

        verify(valiseRepository, never()).existsById(id);
        verify(valiseRepository, never()).save(any(Valise.class));
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testDeleteValise_Success(){
        int id = 1;
        when(valiseRepository.existsById(id)).thenReturn(true);

        valiseService.deleteValise(id);

        verify(valiseRepository, times(1)).existsById(id);
        verify(valiseRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testDeleteValise_Failure_Exception(){
        int id = 1;
        when(valiseRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.deleteValise(id);
        });

        assertEquals("The suitcase does not exist", exception.getMessage());
        verify(valiseRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetValiseById_Success(){
        int id = 1;
        Valise valise = new Valise();
        valise.setId(id);

        when(valiseRepository.findById(id)).thenReturn(Optional.of(valise));

        Valise result = valiseService.getValiseById(id);

        assertNotNull(result, "The suitcase must not be null");
        assertEquals(id, result.getId(), "ID must match");

        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetValiseById_Failure_Exception() {
        // Arrange
        int id = 1;
        when(valiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.getValiseById(id);
        }, "Expected ResourceNotFoundException to be thrown");

        // Vérifications
        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }



    @Test
    public void testGetAllValises_Success(){
        List<Valise> valiseList = List.of(new Valise(), new Valise());

        when(valiseRepository.findAll()).thenReturn(valiseList);

        List<Valise> result = valiseService.getAllValises();

        assertEquals(2, result.size(), "The suitcase list must contain 2 items");
        verify(valiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testGetAllValises_Failure_Exception(){
        when(valiseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            valiseService.getAllValises();
        });

        assertEquals("Database error", exception.getMessage());
        verify(valiseRepository, times(1)).findAll();
        verifyNoMoreInteractions(valiseRepository);
    }
    @Test
    public void testNoInteractionWithValiseRepository_Success() {
        verifyNoInteractions(valiseRepository);
    }
    @Test
    public void testNoInteractionTypeValiseRepository_Failure_Exception() {
        // Arrange
        int id = 1;
        when(valiseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            valiseService.getValiseById(id);
        }, "Expected ResourceNotFoundException to be thrown when the suitcase is not found");

        // Vérifications
        verify(valiseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(valiseRepository);
    }





}
