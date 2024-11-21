package example.controller;

import example.entity.Mouvement;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertNotNull(result);
        assertEquals(mouvements.size(), result.size());

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
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetMouvementById_Failure() {
        when(mouvementService.getMouvementById(1)).thenReturn(null);
        ResponseEntity<Mouvement> result = mouvementController.getMouvementById(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }



    @Test
    public void testCreateMouvement_Failure() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Movement invalid"));
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



    @Test
    public void testUpdateMouvement_Failure() {
        Mouvement updatedMouvement = new Mouvement();
        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(null);
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, updatedMouvement);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testDeleteMouvement_Success() {
        when(mouvementService.existsById(1)).thenReturn(true);
        doNothing().when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

    @Test
    public void testDeleteMouvement_Failure() {
        when(mouvementService.existsById(1)).thenReturn(true);

        doThrow(new RuntimeException("Internal server error")).when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(mouvementService).existsById(1);
        verify(mouvementService).deleteMouvement(1);
    }


    @Test
    public void testCreateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(invalidMouvement);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();

        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, invalidMouvement);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testDeleteMouvement_NotFound() {
        doThrow(new IllegalArgumentException("Mouvement not found")).when(mouvementService).deleteMouvement(1);
        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateMouvement_ShouldReturnBadRequest_OnConflictDetected() {
        Mouvement mouvement = new Mouvement();

        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }



    @Test
    public void testMouvementController(){
        assertNotNull(mouvementController);
        assertNotNull(mouvementService);
    }

    @Test
    public void testCreateMouvement_Conflict() {
        Mouvement mouvement = new Mouvement();

        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreateMouvement_InternalServerError() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("valid");
        mouvement.setDateHeureMouvement(new Date());

        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdateMouvement_ShouldReturnNotFound_OnUnexpectedError() {
        Mouvement mouvement = new Mouvement();

        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, mouvement);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void testDeleteMouvement_InternalServerError() {
        when(mouvementService.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Unexpected error")).when(mouvementService).deleteMouvement(1);

        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

    @Test
    public void testCreateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new Date());

        when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);

        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("En cours", response.getBody().getStatutSortie());
    }

    @Test
    public void testUpdateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        mouvement.setStatutSortie("Finalisé");

        when(mouvementService.existsById(1)).thenReturn(true); // Mock existence check
        when(mouvementService.updateMouvement(eq(1), any(Mouvement.class))).thenReturn(mouvement);

        // Act
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, mouvement);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertNotNull(response.getBody(), "Response body must not be null");
        assertEquals("Finalisé", response.getBody().getStatutSortie(), "StatutSortie should match the updated value");
    }


}
