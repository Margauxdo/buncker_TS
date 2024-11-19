package example.controller;

import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RetourSecuriteControllerTest {

    @InjectMocks
    private RetourSecuriteController RScontroller;

    @Mock
    private IRetourSecuriteService retourSecuriteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllRetourSecurites_Succes() {
        List<RetourSecurite> retourSecurites = new ArrayList<>();
        retourSecurites.add(new RetourSecurite());
        when(retourSecuriteService.getAllRetourSecurites()).thenReturn(retourSecurites);
        List<RetourSecurite> result = RScontroller.getAllRetourSecurites();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(retourSecurites.size(), result.size());
    }
    @Test
    public void testGetAllRetourSecurites_Failure() {
        when(retourSecuriteService.getAllRetourSecurites()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> RScontroller.getAllRetourSecurites());
    }
    @Test
    public void testGetRetourSecuriteById_Succes() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(retourSecurite);
        ResponseEntity<RetourSecurite> result = RScontroller.getRetourSecuriteById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }
    @Test
    public void testGetRetourSecuriteById_Failure() {
        when(retourSecuriteService.getRetourSecurite(1)).thenReturn(null);
        ResponseEntity<RetourSecurite> result = RScontroller.getRetourSecuriteById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateRetourSecurite_Succes() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteService.createRetourSecurite(retourSecurite)).thenReturn(retourSecurite);
        ResponseEntity<RetourSecurite> result = RScontroller.createRetourSecurite(retourSecurite);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(retourSecurite, result.getBody());
    }

    @Test
    public void testUpdateRetourSecurite_Succes() {
        RetourSecurite updateRetourSecurite = new RetourSecurite();
        when(retourSecuriteService.updateRetourSecurite(1,updateRetourSecurite)).thenReturn(updateRetourSecurite);
        ResponseEntity<RetourSecurite> result = RScontroller.updateRetourSecurite(1,updateRetourSecurite);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updateRetourSecurite, result.getBody());
    }
    @Test
    public void testUpdateRetourSecurite_Failure() {
        int id = 1;
        RetourSecurite updateRetourSecurite = new RetourSecurite();
        when(retourSecuriteService.updateRetourSecurite(id, updateRetourSecurite))
                .thenThrow(new EntityNotFoundException("Safety return does not exist"));

        ResponseEntity<RetourSecurite> response = RScontroller.updateRetourSecurite(id, updateRetourSecurite);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be 404 NOT_FOUND");
    }

    @Test
    public void testDeleteRetourSecurite_Succes() {
        doNothing().when(retourSecuriteService).deleteRetourSecurite(1);
        ResponseEntity<Void> result = RScontroller.deleteRetourSecurite(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    @Test
    public void testDeleteRetourSecurite_Failure() {
        doThrow(new RuntimeException("Internal server error")).when(retourSecuriteService).deleteRetourSecurite(1);
        ResponseEntity<Void> result = RScontroller.deleteRetourSecurite(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(retourSecuriteService).deleteRetourSecurite(1);
    }
    @Test
    public void testRetourSecuriteController() {
        assertNotNull(RScontroller);
        assertNotNull(retourSecuriteService);
    }
    @Test
    public void testCreateRetourSecurite_Failure() {
        RetourSecurite retourSecurite = new RetourSecurite();

        when(retourSecuriteService.createRetourSecurite(any(RetourSecurite.class)))
                .thenThrow(new DataIntegrityViolationException("security invalid"));

        ResponseEntity<RetourSecurite> result = RScontroller.createRetourSecurite(retourSecurite);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }



    @Test
    public void testUpdateRetourSecurite_InternalServerError() {
        RetourSecurite retourSecurite = new RetourSecurite();
        when(retourSecuriteService.updateRetourSecurite(1, retourSecurite))
                .thenThrow(new RuntimeException("Internal server error"));
        ResponseEntity<RetourSecurite> result = RScontroller.updateRetourSecurite(1, retourSecurite);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void testDeleteRetourSecurite_InternalServerError() {
        doThrow(new RuntimeException("Internal server error")).when(retourSecuriteService).deleteRetourSecurite(1);
        ResponseEntity<Void> result = RScontroller.deleteRetourSecurite(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void testCreateRetourSecurite_NullInput() {
        when(retourSecuriteService.createRetourSecurite(null)).thenThrow(new DataIntegrityViolationException("Null input not allowed"));

        ResponseEntity<RetourSecurite> result = RScontroller.createRetourSecurite(null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }


}
