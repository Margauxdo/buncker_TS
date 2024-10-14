package example.controller;

import example.entities.JourFerie;
import example.interfaces.IJourFerieService;
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

import static org.mockito.Mockito.when;

public class JourFerieControllerTest {

    @InjectMocks
    private JourFerieController jourFerieController;

    @Mock
    private IJourFerieService jourFerieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetJourFeries_Success() {
        List<JourFerie> jourFeries = new ArrayList<>();
        jourFeries.add(new JourFerie());
        when(jourFerieService.getJourFeries()).thenReturn(jourFeries);

        List<JourFerie> result = jourFerieController.getJourFeries();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(jourFeries.size(), result.size());
    }
    @Test
    public void testGetJourFeries_Failure() {
        when(jourFerieService.getJourFeries()).thenThrow(new RuntimeException("Erreur de la base de donnee"));
        Assertions.assertThrows(RuntimeException.class, () -> jourFerieController.getJourFeries());
    }
    @Test
    public void testGetJourFerie_Success() {
        JourFerie jourFerie = new JourFerie();
        when(jourFerieService.getJourFerie(1)).thenReturn(jourFerie);
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerie(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(jourFerie, result.getBody());
    }
    @Test
    public void testGetJourFerie_Failure() {
        when(jourFerieService.getJourFerie(1)).thenReturn(null);
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerie(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testGetJourFerie_NotFound() {
        when(jourFerieService.getJourFerie(99)).thenReturn(null); // Supposons que 99 n'existe pas

        ResponseEntity<JourFerie> result = jourFerieController.getJourFerie(99);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testGetJourFeries_Exception() {
        when(jourFerieService.getJourFeries()).thenThrow(new RuntimeException("Erreur inattendue"));

        Assertions.assertThrows(RuntimeException.class, () -> jourFerieController.getJourFeries());
    }




}
