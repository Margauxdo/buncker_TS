package example.controller;

import example.entity.Formule;
import example.interfaces.IFormuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;

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

        List<Formule> result = formuleController.getAllFormulesApi();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllFormules_Failure() {
        when(formuleService.getAllFormules()).thenThrow(new RuntimeException("Erreur de la base de données"));

        assertThrows(RuntimeException.class, () -> formuleController.getAllFormulesApi());
    }

    @Test
    public void testGetFormuleById_Success() {
        Formule formule = new Formule();
        when(formuleService.getFormuleById(1)).thenReturn(formule);

        ResponseEntity<Formule> response = formuleController.getFormuleByIdApi(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(formule, response.getBody());
    }

    @Test
    public void testGetFormuleById_Failure() {
        when(formuleService.getFormuleById(1)).thenReturn(null);

        ResponseEntity<Formule> response = formuleController.getFormuleByIdApi(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateFormule_Success() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenReturn(formule);

        String response = formuleController.createFormule(formule, new ConcurrentModel());

        assertEquals("redirect:/formules/list", response);
    }
    @Test
    public void testCreateFormuleApi_Success() {
        Formule formule = new Formule();
        when(formuleService.createFormule(any(Formule.class))).thenReturn(formule);

        ResponseEntity<Formule> response = formuleController.createFormuleApi(formule);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(formule, response.getBody());
    }

    @Test
    public void testCreateFormuleApi_Failure() {
        // Cas d'échec avec l'API REST
        when(formuleService.createFormule(any(Formule.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Formule> response = formuleController.createFormuleApi(new Formule());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



    @Test
    public void testCreateFormule_Failure() {
        doThrow(new IllegalArgumentException("Formule invalide"))
                .when(formuleService).createFormule(any(Formule.class));

        ConcurrentModel model = new ConcurrentModel();

        String response = formuleController.createFormule(new Formule(), model);

        assertEquals("formule_create", response);

        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Formule invalide", model.getAttribute("errorMessage"));
    }




    @Test
    public void testUpdateFormule_Success() {
        Formule updatedFormule = new Formule();
        when(formuleService.updateFormule(1, updatedFormule)).thenReturn(updatedFormule);

        ResponseEntity<Formule> response = formuleController.updateFormuleApi(1, updatedFormule);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFormule, response.getBody());
    }

    @Test
    public void testUpdateFormule_Failure() {
        Formule updatedFormule = new Formule();
        when(formuleService.updateFormule(1, updatedFormule)).thenReturn(null);

        ResponseEntity<Formule> response = formuleController.updateFormuleApi(1, updatedFormule);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteFormule_Success() {
        doNothing().when(formuleService).deleteFormule(1);

        ResponseEntity<Formule> response = formuleController.deleteFormuleApi(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteFormule_Failure() {
        doThrow(new RuntimeException("Formule non trouvée")).when(formuleService).deleteFormule(1);

        assertThrows(RuntimeException.class, () -> formuleController.deleteFormuleApi(1));
    }

    @Test
    public void testCreateFormule_InvalidInput() {
        doThrow(new IllegalArgumentException("Invalid data"))
                .when(formuleService).createFormule(any(Formule.class));

        ConcurrentModel model = new ConcurrentModel();

        String response = formuleController.createFormule(new Formule(), model);

        assertEquals("formule_create", response);

        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Invalid data", model.getAttribute("errorMessage"));
    }




    @Test
    public void testUpdateFormule_InvalidInput() {
        Formule invalidFormule = new Formule();
        when(formuleService.updateFormule(eq(1), any(Formule.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Formule> response = formuleController.updateFormuleApi(1, invalidFormule);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testDeleteFormule_NotFound() {
        doThrow(new IllegalArgumentException("Formule not found")).when(formuleService).deleteFormule(1);

        ResponseEntity<Formule> response = formuleController.deleteFormuleApi(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateFormuleApi_Conflict() {
        when(formuleService.createFormule(any(Formule.class))).thenThrow(new IllegalStateException("Conflict detected"));

        ResponseEntity<Formule> response = formuleController.createFormuleApi(new Formule());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }




    @Test
    public void testFormuleController(){
        assertNotNull(formuleController);
        assertNotNull(formuleController);
    }

}

