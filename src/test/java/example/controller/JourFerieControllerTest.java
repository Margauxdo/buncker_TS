package example.controller;

import example.entity.Formule;
import example.entity.JourFerie;
import example.entity.Regle;
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

import static org.mockito.ArgumentMatchers.any;
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
    public void testCreateJourFerieWithFormule() {
        // Arrange
        Formule formule = new Formule();
        formule.setId(1);
        formule.setFormule("Formule Test");

        Regle regle = new Regle();
        regle.setCoderegle("R123");

        JourFerie jourFerie = new JourFerie();
        jourFerie.setRegles(List.of(regle)); // Correction ici
        jourFerie.setJoursFerieList(new ArrayList<>());

        when(jourFerieService.saveJourFerie(any(JourFerie.class))).thenReturn(jourFerie);

        // Act
        ResponseEntity<JourFerie> response = jourFerieController.createJourFerieApi(jourFerie);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().getRegles().size());
        Assertions.assertEquals("R123", response.getBody().getRegles().get(0).getCoderegle());
    }

    @Test
    public void testGetJourFerie_Success() {
        // Arrange
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(1);

        when(jourFerieService.getJourFerie(1)).thenReturn(jourFerie);

        // Act
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerieApi(1);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(jourFerie, result.getBody());
    }

    @Test
    public void testGetJourFerie_Failure() {
        // Arrange
        when(jourFerieService.getJourFerie(1)).thenReturn(null);

        // Act
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerieApi(1);

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetJourFerie_InvalidId() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> jourFerieController.getJourFerieApi(-1));
    }

    @Test
    public void testGetJourFeries_Exception() {
        // Arrange
        when(jourFerieService.getJourFeries()).thenThrow(new RuntimeException("Erreur inattendue"));

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> jourFerieController.getJourFeriesApi());
    }

    @Test
    public void testGetJourFerie_ServiceError() {
        // Arrange
        when(jourFerieService.getJourFerie(1)).thenThrow(new RuntimeException("Erreur de service"));

        // Act
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerieApi(1);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void testGetJourFerie_NotFound() {
        // Arrange
        when(jourFerieService.getJourFerie(9999)).thenReturn(null);

        // Act
        ResponseEntity<JourFerie> result = jourFerieController.getJourFerieApi(9999);

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testJourFerieControllerInitialization() {
        Assertions.assertNotNull(jourFerieController);
        Assertions.assertNotNull(jourFerieService);
    }
}
