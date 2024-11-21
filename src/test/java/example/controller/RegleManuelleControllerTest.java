package example.controller;

import example.entity.RegleManuelle;
import example.exceptions.ConflictException;
import example.interfaces.IRegleManuelleService;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegleManuelleControllerTest {

    @InjectMocks
    private RegleManuelleController regleManuelleController;

    @Mock
    private IRegleManuelleService regleManuelleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetRegleManuelles_Success(){
        List<RegleManuelle> regleManuelles = new ArrayList<>();
        regleManuelles.add(new RegleManuelle());
        when(regleManuelleService.getRegleManuelles()).thenReturn(regleManuelles);
        List<RegleManuelle> result = regleManuelleController.getRegleManuelles();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(regleManuelles.size(), result.size());
    }
    @Test
    public void testGetRegleManuelles_Failure(){
        when(regleManuelleService.getRegleManuelles()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> regleManuelleController.getRegleManuelles());
    }
    @Test
    public void testGetRegleManuelleById_Succes(){
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(regleManuelle);
        ResponseEntity<RegleManuelle> result = regleManuelleController.getRegleManuelleById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetRegleManuelleById_Failure(){
        when(regleManuelleService.getRegleManuelle(1)).thenReturn(null);
        ResponseEntity<RegleManuelle> result = regleManuelleController.getRegleManuelleById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateRegleManuelle_Success(){
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.createRegleManuelle(regleManuelle)).thenReturn(regleManuelle);
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(regleManuelle);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(regleManuelle, result.getBody());
    }
    @Test
    public void testCreateRegleManuelle_Failure(){
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.createRegleManuelle(null)).thenThrow(new IllegalArgumentException("manual rule invalid"));
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateRegleManuelle_Success(){
        RegleManuelle updateRegleManuelle = new RegleManuelle();
        when(regleManuelleService.updateRegleManuelle(1, updateRegleManuelle)).thenReturn(updateRegleManuelle);
        ResponseEntity<RegleManuelle> result = regleManuelleController.updateRegleManuelle(1, updateRegleManuelle);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updateRegleManuelle, result.getBody());
    }
    @Test
    public void testUpdateRegleManuelle_Failure(){
        RegleManuelle updateRegleManuelle = new RegleManuelle();
        when(regleManuelleService.updateRegleManuelle(1,updateRegleManuelle)).thenReturn(null);
        ResponseEntity<RegleManuelle> result = regleManuelleController.updateRegleManuelle(1,updateRegleManuelle);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testDeleteRegleManuelle_Success(){
        doNothing().when(regleManuelleService).deleteRegleManuelle(1);
        ResponseEntity<Void> result = regleManuelleController.deleteRegleManuelle(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    @Test
    public void testDeleteRegleManuelle_Failure(){
        doThrow(new RuntimeException("Internal server error")).when(regleManuelleService).deleteRegleManuelle(1);
        ResponseEntity<Void> result = regleManuelleController.deleteRegleManuelle(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(regleManuelleService, times(1)).deleteRegleManuelle(1);
    }



    @Test
    public void testDeleteRegleManuelle_NotFound(){
        doThrow(new EntityNotFoundException("Manual rule not found")).when(regleManuelleService).deleteRegleManuelle(1);
        ResponseEntity<Void> result = regleManuelleController.deleteRegleManuelle(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }




    @Test
    public void testRegleManuelleController(){
        Assertions.assertNotNull(regleManuelleController);
        Assertions.assertNotNull(regleManuelleService);
    }

    @Test
    public void testCreateReglemanuelle_InternalServerError(){
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.createRegleManuelle(any(RegleManuelle.class)))
        .thenThrow(new RuntimeException("Internal server error"));
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(regleManuelle);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
    @Test
    public void testUpdateRegleManuelle_InternalServerError() {
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.updateRegleManuelle(anyInt(), any(RegleManuelle.class)))
                .thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<RegleManuelle> response = regleManuelleController.updateRegleManuelle(1, regleManuelle);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testDeleteRegleManuelle_InternalServerError(){
        doThrow(new RuntimeException("Internal server error")).when(regleManuelleService).deleteRegleManuelle(1);
        ResponseEntity<Void> response = regleManuelleController.deleteRegleManuelle(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateRegleManuelle_EmptyInput() {
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(regleManuelleService, never()).createRegleManuelle(any(RegleManuelle.class));
    }

    @Test
    public void testUpdateRegleManuelle_EmptyInput() {
        ResponseEntity<RegleManuelle> result = regleManuelleController.updateRegleManuelle(1, null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(regleManuelleService, never()).updateRegleManuelle(anyInt(), any(RegleManuelle.class));
    }
    @Test
    public void testCreateRegleManuelle_Conflict() {
        RegleManuelle regleManuelle = new RegleManuelle();
        when(regleManuelleService.createRegleManuelle(regleManuelle)).thenThrow(new ConflictException("Conflict occurred"));
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(regleManuelle);
        Assertions.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }
    @Test
    public void testCreateRegleManuelle_InvalidInput() {
        // Arrange
        RegleManuelle invalidRegleManuelle = new RegleManuelle();
        when(regleManuelleService.createRegleManuelle(invalidRegleManuelle))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<RegleManuelle> result = regleManuelleController.createRegleManuelle(invalidRegleManuelle);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Expected HTTP status 400 BAD_REQUEST");
        verify(regleManuelleService, times(1)).createRegleManuelle(any(RegleManuelle.class));
        verifyNoMoreInteractions(regleManuelleService);
    }


    @Test
    public void testDeleteRegleManuelle_EntityNotFound() {
        doThrow(new EntityNotFoundException("Manual rule not found"))
                .when(regleManuelleService).deleteRegleManuelle(999);

        ResponseEntity<Void> result = regleManuelleController.deleteRegleManuelle(999);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }





}
