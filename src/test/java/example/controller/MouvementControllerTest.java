package example.controller;

import example.entities.Formule;
import example.entities.Mouvement;
import example.interfaces.IMouvementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MouvementControllerTest {

    @InjectMocks
    private MouvementController mouvementController;

    @Mock
    private IMouvementService mouvementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllMouvements_Success() {
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(new Mouvement());
        when(mouvementService.getAllMouvements()).thenReturn(mouvements);
        List<Mouvement> result = mouvementController.getAllMouvements();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mouvements.size(), result.size());

    }
    @Test
    public void testGetAllMouvements_Failure() {
        when(mouvementService.getAllMouvements()).thenThrow(new RuntimeException("Erreur de la base de donnee"));
        Assertions.assertThrows(RuntimeException.class, () -> mouvementController.getAllMouvements());
    }
    @Test
    public void testGetMouvementById_Success() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.getMouvementById(1)).thenReturn(mouvement);
        ResponseEntity<Mouvement> result = mouvementController.getMouvementById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetMouvementById_Failure() {
        when(mouvementService.getMouvementById(1)).thenReturn(null);
        ResponseEntity<Mouvement> result = mouvementController.getMouvementById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(mouvement)).thenReturn(mouvement);
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(mouvement, result.getBody());
    }
    @Test
    public void testCreateMouvement_Failure() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Mouvement invalide"));
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_Success() {
        Mouvement updatedMouvement = new Mouvement();
        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(updatedMouvement);
        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, updatedMouvement);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedMouvement, result.getBody());
    }
    @Test
    public void testUpdateMouvement_Failure() {
        Mouvement updatedMouvement = new Mouvement();
        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(null);
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, updatedMouvement);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testDeleteMouvement_Success() {
        doNothing().when(mouvementService).deleteMouvement(1);
        ResponseEntity<Mouvement> result = mouvementController.deleteMouvement(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    @Test
    public void testDeleteMouvement_Failure() {
        // Mock the service to throw a RuntimeException when deleteMouvement is called
        doThrow(new RuntimeException("Internal server error")).when(mouvementService).deleteMouvement(1);

        // Perform the delete request and check the response
        ResponseEntity<Mouvement> response = mouvementController.deleteMouvement(1);

        // Verify that the correct status is returned
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verify that the service was called with the correct argument
        verify(mouvementService).deleteMouvement(1);
    }

    @Test
    public void testCreateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(invalidMouvement);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();

        // Corrige en utilisant `anyInt()` pour l'ID et `any()` pour le Mouvement
        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, invalidMouvement);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteMouvement_NotFound() {
        doThrow(new IllegalArgumentException("Mouvement not found")).when(mouvementService).deleteMouvement(1);
        ResponseEntity<Mouvement> result = mouvementController.deleteMouvement(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateMouvement_NotFound() {
        Mouvement mouvement = new Mouvement();

        // Mock the service to throw an IllegalArgumentException with "Conflict detected"
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        // Perform the request
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        // Assert that the status is CONFLICT
        Assertions.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }


    @Test
    public void testMouvementController(){
        Assertions.assertNotNull(mouvementController);
        Assertions.assertNotNull(mouvementService);
    }

    @Test
    public void testCreateMouvement_Conflict() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateMouvement_InternalServerError() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateMouvement_InternalServerError() {
        Mouvement mouvement = new Mouvement();

        // Simuler une exception RuntimeException
        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Appeler la méthode updateMouvement avec un ID et un Mouvement
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, mouvement);

        // Vérifier que le code de statut est INTERNAL_SERVER_ERROR
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testDeleteMouvement_InternalServerError() {
        doThrow(new RuntimeException("Unexpected error")).when(mouvementService).deleteMouvement(1);

        ResponseEntity<Mouvement> response = mouvementController.deleteMouvement(1);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}
