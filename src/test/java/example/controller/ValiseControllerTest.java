package example.controller;

import example.entities.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IValiseService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ValiseControllerTest {

    @InjectMocks
    private ValiseController valiseController;

    @Mock
    private IValiseService valiseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllValises_Success() {
        List<Valise> valises = new ArrayList<>();
        valises.add(new Valise());
        when(valiseService.getAllValises()).thenReturn(valises);
        List<Valise> response = valiseController.getAllValises();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(valises.size(), response.size() );

    }
    @Test
    public void testGetAllValises_Failure() {
        when(valiseService.getAllValises()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> valiseController.getAllValises());
    }
    @Test
    public void testGetValiseById_Success() {
        Valise valise = new Valise();
        when(valiseService.getValiseById(1)).thenReturn(valise);
        ResponseEntity<Valise> response = valiseController.getValiseById(1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testGetValiseById_Failure() {
        when(valiseService.getValiseById(1)).thenReturn(null);
        ResponseEntity<Valise> response = valiseController.getValiseById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testCreateValise_Success() {
        Valise valise = new Valise();
        when(valiseService.createValise(valise)).thenReturn(valise);
        ResponseEntity<Valise> response = valiseController.createValise(valise);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(valise, response.getBody());
    }
    @Test
    public void testCreateValise_Failure() {
        Valise valise = new Valise();
        when(valiseService.createValise(valise)).thenThrow(new IllegalArgumentException("suitcase invalid"));
        ResponseEntity<Valise> response = valiseController.createValise(valise);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateValise_Success() {
        Valise updatedValise = new Valise();
        when(valiseService.updateValise(1, updatedValise)).thenReturn(updatedValise);
        ResponseEntity<Valise> response = valiseController.updateValise(updatedValise, 1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(updatedValise, response.getBody());
    }
    @Test
    public void testUpdateValise_Failure() {
        Valise updatedValise = new Valise();
        when(valiseService.updateValise(1, updatedValise)).thenReturn(null);
        ResponseEntity<Valise> response = valiseController.updateValise(updatedValise, 1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testDeleteValise_Success() {
        doNothing().when(valiseService).deleteValise(1);
        ResponseEntity<Valise> response = valiseController.deleteValise(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void testDeleteValise_Failure() {
        doThrow(new RuntimeException("Internal error")).when(valiseService).deleteValise(1);
        ResponseEntity<Valise> response = valiseController.deleteValise(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(valiseService).deleteValise(1);

    }
    @Test
    public void testCreateValise_InvalidInput() {
        Valise invalidValise = new Valise();
        when(valiseService.createValise(invalidValise)).thenThrow(new IllegalArgumentException("suitcase invalid"));
        ResponseEntity<Valise> response = valiseController.createValise(invalidValise);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateValise_InvalidInput() {
        Valise invalidValise = new Valise(); // Add invalid fields if needed
        when(valiseService.updateValise(anyInt(), any(Valise.class)))
                .thenThrow(new IllegalArgumentException("suitcase invalid"));
        ResponseEntity<Valise> response = valiseController.updateValise(invalidValise, 1);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteValise_NotFound() {
        doThrow(new IllegalArgumentException("suit case not found")).when(valiseService).deleteValise(1);
        ResponseEntity<Valise> response = valiseController.deleteValise(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testCreateValise_NotFound() {
        Valise valise = new Valise();
        when(valiseService.createValise(any(Valise.class)))
                .thenThrow(new ResourceNotFoundException("resource not found")); // Simulate not found
        ResponseEntity<Valise> response = valiseController.createValise(valise);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testValiseController() {
        assertNotNull(valiseController);
        assertNotNull(valiseService);

    }
    @Test
    public void testCreateValise_Conflict() {
        Valise valise = new Valise();
        when(valiseService.createValise(any(Valise.class)))
                .thenThrow(new IllegalStateException("conflict detected"));
        ResponseEntity<Valise> response = valiseController.createValise(valise);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    @Test
    public void testCreateValise_InternalServerError() {
        Valise valise = new Valise();
        when(valiseService.createValise(any(Valise.class)))
        .thenThrow(new RuntimeException("internal error"));
        ResponseEntity<Valise> response = valiseController.createValise(valise);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void testUpdateValise_Conflict() {
        Valise valise = new Valise();
        when(valiseService.updateValise(anyInt(), any(Valise.class)))
                .thenThrow(new IllegalStateException("conflict detected"));
        ResponseEntity<Valise> response = valiseController.updateValise(valise, 1);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateValise_InternalServerError() {
        Valise valise = new Valise();
        when(valiseService.updateValise(anyInt(), any(Valise.class)))
                .thenThrow(new RuntimeException("internal error"));
        ResponseEntity<Valise> response = valiseController.updateValise(valise, 1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }



}
