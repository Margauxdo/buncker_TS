package example.controller;

import example.controller.FormuleController;
import example.entities.Formule;
import example.entities.JourFerie;
import example.interfaces.IFormuleService;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FormuleControllerTest {

    @InjectMocks
    private FormuleController formuleController; ;

    @Mock
    private IFormuleService formuleService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFormules_Success() {
        List<Formule> formules = new ArrayList<>();
        formules.add(new Formule());
        when(formuleService.getAllFormules()).thenReturn(formules);

        List<Formule> result = formuleController.getAllFormules();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllFormules_Failure() {
        when(formuleService.getAllFormules()).thenThrow(new RuntimeException("Erreur de la base de données"));

        assertThrows(RuntimeException.class, () -> formuleController.getAllFormules());
    }

    @Test
    public void testGetFormuleById_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        ResponseEntity<Formule> response = formuleController.getFormuleById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(formule, response.getBody());
    }

    @Test
    public void testGetFormuleById_Failure() {
        when(formuleService.getFormuleById(1)).thenReturn(null);

        ResponseEntity<Formule> response = formuleController.getFormuleById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateFormule_Success() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenReturn(formule);

        ResponseEntity<Formule> response = formuleController.createFormule(formule);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(formule, response.getBody());
    }

    @Test
    public void testCreateFormule_Failure() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenThrow(new IllegalArgumentException("Formule invalide"));

        ResponseEntity<Formule> response = formuleController.createFormule(formule);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateFormule_Success() {
        Formule updatedFormule = new Formule();
        when(formuleService.updateFormule(1, updatedFormule)).thenReturn(updatedFormule);

        ResponseEntity<Formule> response = formuleController.updateFormule(1, updatedFormule);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFormule, response.getBody());
    }

    @Test
    public void testUpdateFormule_Failure() {
        Formule updatedFormule = new Formule();
        when(formuleService.updateFormule(1, updatedFormule)).thenReturn(null);

        ResponseEntity<Formule> response = formuleController.updateFormule(1, updatedFormule);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteFormule_Success() {
        doNothing().when(formuleService).deleteFormule(1);

        ResponseEntity<Formule> response = formuleController.deleteFormule(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteFormule_Failure() {
        doThrow(new RuntimeException("Formule non trouvée")).when(formuleService).deleteFormule(1);

        assertThrows(RuntimeException.class, () -> formuleController.deleteFormule(1));
    }

    @Test
    public void testCreateFormule_InvalidInput() {
        Formule invalidFormule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Formule> response = formuleController.createFormule(invalidFormule);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateFormule_InvalidInput() {
        Formule invalidFormule = new Formule();
        when(formuleService.updateFormule(eq(1), any(Formule.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Formule> response = formuleController.updateFormule(1, invalidFormule);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testDeleteFormule_NotFound() {
        doThrow(new IllegalArgumentException("Formule not found")).when(formuleService).deleteFormule(1);

        ResponseEntity<Formule> response = formuleController.deleteFormule(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateFormule_Conflict() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenThrow(new IllegalStateException("Conflict detected"));

        ResponseEntity<Formule> response = formuleController.createFormule(formule);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }



    @Test
    public void testFormuleController(){
        assertNotNull(formuleController);
        assertNotNull(formuleController);
    }

}

