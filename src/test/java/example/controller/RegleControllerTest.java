package example.controller;

import example.entities.Regle;
import example.exceptions.GlobalExceptionHandler;
import example.interfaces.IRegleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(GlobalExceptionHandler.class)
public class RegleControllerTest {

    @InjectMocks
    private RegleController regleController;

    @Mock
    private IRegleService regleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testReadRegles_Success() {
        List<Regle> regles = new ArrayList<>();
        regles.add(new Regle());

        when(regleService.readAllRegles()).thenReturn(regles);

        ResponseEntity<List<Regle>> response = regleController.readRegles();

        List<Regle> result = response.getBody();

        Assertions.assertNotNull(result, "The result should not be null");
        Assertions.assertEquals(regles.size(), result.size(), "The size of the result list should match the mock data");
    }

    @Test
    public void testReadRegles_Failure(){
        when(regleService.readAllRegles()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> regleController.readRegles());
    }
    @Test
    public void testReadRegleById_Success(){
        Regle regle = new Regle();
        when(regleService.readRegle(1)).thenReturn(regle);
        ResponseEntity<Regle> result = regleController.readRegleById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testReadRegleById_Failure(){
        when(regleService.readRegle(1)).thenReturn(null);
        ResponseEntity<Regle> result = regleController.readRegleById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateRegle_Success(){
        Regle regle = new Regle();
        when(regleService.createRegle(regle)).thenReturn(regle);
        ResponseEntity<Regle> result = (ResponseEntity<Regle>) regleController.createRegle(regle);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(regle, result.getBody());
    }
    @Test
    public void testCreateRegle_Failure() {
        Regle regle = new Regle();

        when(regleService.createRegle(regle)).thenThrow(new IllegalArgumentException("Rule invalid"));

        ResponseEntity<Regle> result = (ResponseEntity<Regle>) regleController.createRegle(regle);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Expected BAD_REQUEST status");
    }


    @Test
    public void testUpdateRegle_Success(){
        Regle updatedRegle = new Regle();
        when(regleService.updateRegle(1,updatedRegle)).thenReturn(updatedRegle);
        ResponseEntity<Regle> result = regleController.updateRegle(1,updatedRegle);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedRegle, result.getBody());
    }
    @Test
    public void testUpdateRegle_Failure() {
        Regle updatedRegle = new Regle();
        when(regleService.updateRegle(1, updatedRegle)).thenReturn(null);

        ResponseEntity<Regle> result = regleController.updateRegle(1, updatedRegle);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "Expected NOT_FOUND status");
    }

    @Test
    public void testDeleteRegle_Success(){
        doNothing().when(regleService).deleteRegle(1);
        ResponseEntity<Void> result = regleController.deleteRegle(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    @Test
    public void testDeleteRegle_Failure(){
        doThrow(new RuntimeException("Internal server error")).when(regleService).deleteRegle(1);
        ResponseEntity<Void> result = regleController.deleteRegle(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(regleService, times(1)).deleteRegle(1);
    }





    @Test
    public void testUpdateRegle_InvalidInput() {
        Regle invalidRegle = new Regle();

        when(regleService.updateRegle(anyInt(), any(Regle.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Regle> result = regleController.updateRegle(1, invalidRegle);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Expected BAD_REQUEST status");
    }

    @Test
    public void testDeleteRegle_NotFound(){
        doThrow(new IllegalArgumentException("Rule not found")).when(regleService).deleteRegle(1);
        ResponseEntity<Void> result = regleController.deleteRegle(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }



    @Test
    public void testRegleController(){
        Assertions.assertNotNull(regleController);
        Assertions.assertNotNull(regleService);
    }





    @Test
    public void testCreateRegle_InternalServerError(){
        Regle regle = new Regle();
        when(regleService.createRegle(any(Regle.class)))
                .thenThrow(new RuntimeException("Internal server error"));
        ResponseEntity<Regle> result = (ResponseEntity<Regle>) regleController.createRegle(regle);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void testUpdateRegle_InternalServerError() {
        Regle regle = new Regle();
        when(regleService.updateRegle(anyInt(), any(Regle.class)))
                .thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<Regle> result = regleController.updateRegle(1, regle);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }



    @Test
    public void testDeleteRegle_InternalServerError() {
        doThrow(new RuntimeException("Internal server error")).when(regleService).deleteRegle(1);

        ResponseEntity<Void> result = regleController.deleteRegle(1);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Expected INTERNAL_SERVER_ERROR status");
    }



}
