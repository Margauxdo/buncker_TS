package example.controller;

import example.entities.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SortieSemaineControllerTest {

    @InjectMocks
    private SortieSemaineController sortieSemaineController;

    @Mock
    private ISortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSortieSemaine_Success() {
        List<SortieSemaine> sortieSemaines = new ArrayList<>();
        sortieSemaines.add(new SortieSemaine());
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(sortieSemaines);
        ResponseEntity<List<SortieSemaine>> response = sortieSemaineController.getAllSortieSemaine();
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sortieSemaines.size(), response.getBody().size());
    }


    @Test
    public void testGetAllSortieSemaine_Fail() {
        when(sortieSemaineService.getAllSortieSemaine()).thenThrow(new RuntimeException("error database"));

        ResponseEntity<List<SortieSemaine>> response = sortieSemaineController.getAllSortieSemaine();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }




    @Test
    public void testGetSortieSemaine_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);
        ResponseEntity<SortieSemaine> result = sortieSemaineController.getSortieSemaine(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetSortieSemaine_Fail() {
        when(sortieSemaineService.getSortieSemaine(1)).thenReturn(null);
        ResponseEntity<SortieSemaine> result = sortieSemaineController.getSortieSemaine(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testCreateSortieSemaine_Success() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        when(sortieSemaineService.createSortieSemaine(sortieSemaine)).thenReturn(sortieSemaine);
        ResponseEntity<SortieSemaine> result = sortieSemaineController.createSortieSemaine(sortieSemaine);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(sortieSemaine, result.getBody());
    }

    @Test
    public void testCreateSortieSemaine_Fail() {
        SortieSemaine sortieSemaine = new SortieSemaine();
        when(sortieSemaineService.createSortieSemaine(sortieSemaine)).thenThrow(new IllegalArgumentException("week invalid"));
        ResponseEntity<SortieSemaine> response = sortieSemaineController.createSortieSemaine(sortieSemaine);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testUpdateSortieSemaine_Success() {
        SortieSemaine updateSortieSemaine = new SortieSemaine();
        when(sortieSemaineService.updateSortieSemaine(1, updateSortieSemaine)).thenReturn(updateSortieSemaine);
        ResponseEntity<SortieSemaine> response = sortieSemaineController.updateSortieSemaine(1, updateSortieSemaine);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateSortieSemaine, response.getBody());
    }

    @Test
    public void testUpdateSortieSemaine_Fail() {
        SortieSemaine updateSortieSemaine = new SortieSemaine();

        when(sortieSemaineService.updateSortieSemaine(1, updateSortieSemaine)).thenReturn(null);
        ResponseEntity<SortieSemaine> response = sortieSemaineController.updateSortieSemaine(1, updateSortieSemaine);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void testDeleteSortieSemaine_Success() {
        doNothing().when(sortieSemaineService).deleteSortieSemaine(1);
        ResponseEntity<Void> response = sortieSemaineController.deleteSortieSemaine(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteSortieSemaine_Fail() {
        doThrow(new RuntimeException("Internal server error")).when(sortieSemaineService).deleteSortieSemaine(1);

        ResponseEntity<Void> response = sortieSemaineController.deleteSortieSemaine(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(sortieSemaineService, times(1)).deleteSortieSemaine(1);
    }


    @Test
    public void testUpdateSortieSemaine_InvalidInput() {
        SortieSemaine invalidSortieSemaine = new SortieSemaine();
        when(sortieSemaineService.updateSortieSemaine(anyInt(), any(SortieSemaine.class)))
                .thenThrow(new IllegalArgumentException("invalid data"));

        ResponseEntity<SortieSemaine> response = sortieSemaineController.updateSortieSemaine(1, invalidSortieSemaine);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testSortieSemaine_EmptyList() {
        when(sortieSemaineService.getAllSortieSemaine()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SortieSemaine>> response = sortieSemaineController.getAllSortieSemaine();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testSortieSemaineController() {
        assertNotNull(sortieSemaineController);
        assertNotNull(sortieSemaineService);
    }


}
