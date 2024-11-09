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
import java.util.Date;
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
        when(mouvementService.getAllMouvements()).thenThrow(new RuntimeException("Database error"));
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
        mouvement.setStatutSortie("open"); // Exemple de statut valide
        mouvement.setDateHeureMouvement(new Date()); // Date valide pour éviter une erreur de validation

        when(mouvementService.createMouvement(mouvement)).thenReturn(mouvement);

        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(mouvement, result.getBody());
    }

    @Test
    public void testCreateMouvement_Failure() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Movement invalid"));
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_Success() {
        Mouvement updatedMouvement = new Mouvement();
        updatedMouvement.setStatutSortie("open");
        updatedMouvement.setDateHeureMouvement(new Date());

        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(updatedMouvement);
        when(mouvementService.existsById(1)).thenReturn(true);

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
        when(mouvementService.existsById(1)).thenReturn(true);
        doNothing().when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

    @Test
    public void testDeleteMouvement_Failure() {
        when(mouvementService.existsById(1)).thenReturn(true);

        doThrow(new RuntimeException("Internal server error")).when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(mouvementService).existsById(1);
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

        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, invalidMouvement);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testDeleteMouvement_NotFound() {
        doThrow(new IllegalArgumentException("Mouvement not found")).when(mouvementService).deleteMouvement(1);
        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateMouvement_ShouldReturnBadRequest_OnConflictDetected() {
        Mouvement mouvement = new Mouvement();

        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
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

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreateMouvement_InternalServerError() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("valid");
        mouvement.setDateHeureMouvement(new Date());

        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdateMouvement_ShouldReturnNotFound_OnUnexpectedError() {
        Mouvement mouvement = new Mouvement();

        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, mouvement);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void testDeleteMouvement_InternalServerError() {
        when(mouvementService.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Unexpected error")).when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }



}
