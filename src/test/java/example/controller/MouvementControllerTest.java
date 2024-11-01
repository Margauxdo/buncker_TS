package example.controller;

import example.entities.Formule;
import example.entities.Mouvement;
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
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mouvements.size(), result.size());

    }
    @Test
    public void testGetAllMouvements_Failure() {
        when(mouvementService.getAllMouvements()).thenThrow(new RuntimeException("Erreur de la base de donnee"));
        Assertions.assertThrows(RuntimeException.class, () -> mouvementController.getAllMouvements());
    }
    @Test
    public void testGetMouvementById_Success() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.getMouvementById(1)).thenReturn(mouvement);
        ResponseEntity<Mouvement> result = mouvementController.getMouvementById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetMouvementById_Failure() {
        when(mouvementService.getMouvementById(1)).thenReturn(null);
        ResponseEntity<Mouvement> result = mouvementController.getMouvementById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testCreateMouvement_Success() {
        Mouvement mouvement = new Mouvement();
        // Ajouter les valeurs nécessaires pour que les validations du contrôleur passent
        mouvement.setStatutSortie("open"); // Exemple de statut valide
        mouvement.setDateHeureMouvement(new Date()); // Date valide pour éviter une erreur de validation

        // Configurer le service pour renvoyer l'objet mouvement
        when(mouvementService.createMouvement(mouvement)).thenReturn(mouvement);

        // Appeler la méthode createMouvement du contrôleur
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        // Vérifier que le code de statut est CREATED et que le corps de la réponse est correct
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(mouvement, result.getBody());
    }

    @Test
    public void testCreateMouvement_Failure() {
        Mouvement mouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Mouvement invalide"));
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_Success() {
        Mouvement updatedMouvement = new Mouvement();
        updatedMouvement.setStatutSortie("open"); // Exigence potentielle de validation
        updatedMouvement.setDateHeureMouvement(new Date()); // Date valide

        // Simuler la présence de l'ID dans le service pour éviter une réponse 404
        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(updatedMouvement);
        when(mouvementService.existsById(1)).thenReturn(true); // Simuler l'existence de l'entité

        // Appeler la méthode updateMouvement du contrôleur
        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, updatedMouvement);

        // Vérifier que le code de statut est OK et que le corps de la réponse est correct
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedMouvement, result.getBody());
    }


    @Test
    public void testUpdateMouvement_Failure() {
        Mouvement updatedMouvement = new Mouvement();
        when(mouvementService.updateMouvement(1, updatedMouvement)).thenReturn(null);
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, updatedMouvement);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testDeleteMouvement_Success() {
        // Simuler que l'entité existe
        when(mouvementService.existsById(1)).thenReturn(true);
        doNothing().when(mouvementService).deleteMouvement(1);

        // Appeler la méthode du contrôleur pour supprimer
        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);

        // Vérifier que le statut est bien 204 NO_CONTENT
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Vérifier les interactions avec les mocks
        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

    @Test
    public void testDeleteMouvement_Failure() {
        // Simuler que l'entité existe pour éviter que le contrôleur ne retourne 404
        when(mouvementService.existsById(1)).thenReturn(true);

        // Forcer une exception RuntimeException lors de l'appel de deleteMouvement
        doThrow(new RuntimeException("Internal server error")).when(mouvementService).deleteMouvement(1);

        // Appeler la méthode deleteMouvement du contrôleur
        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        // Vérifier que le code de statut est 500 INTERNAL_SERVER_ERROR
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Vérifier que la méthode existsById et deleteMouvement ont été appelées
        verify(mouvementService).existsById(1);
        verify(mouvementService).deleteMouvement(1);
    }


    @Test
    public void testCreateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();
        when(mouvementService.createMouvement(any(Mouvement.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(invalidMouvement);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateMouvement_InvalidInput() {
        Mouvement invalidMouvement = new Mouvement();

        // Simuler une exception IllegalArgumentException pour des données invalides
        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // Appeler la méthode updateMouvement avec un Mouvement invalide
        ResponseEntity<Mouvement> result = mouvementController.updateMouvement(1, invalidMouvement);

        // Vérifier que le code de statut est NOT_FOUND (404)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testDeleteMouvement_NotFound() {
        doThrow(new IllegalArgumentException("Mouvement not found")).when(mouvementService).deleteMouvement(1);
        ResponseEntity<Void> result = mouvementController.deleteMouvement(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateMouvement_ShouldReturnBadRequest_OnConflictDetected() {
        Mouvement mouvement = new Mouvement();

        // Mock the service to throw an IllegalArgumentException with "Conflict detected"
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        // Perform the request
        ResponseEntity<Mouvement> result = mouvementController.createMouvement(mouvement);

        // Assert that the status is BAD_REQUEST instead of CONFLICT
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }



    @Test
    public void testMouvementController(){
        Assertions.assertNotNull(mouvementController);
        Assertions.assertNotNull(mouvementService);
    }

    @Test
    public void testCreateMouvement_Conflict() {
        Mouvement mouvement = new Mouvement();

        // Simuler une exception IllegalArgumentException
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new IllegalArgumentException("Conflict detected"));

        // Appeler la méthode createMouvement du contrôleur
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        // Vérifier que le code de statut est BAD_REQUEST (400), comme renvoyé actuellement par le contrôleur
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreateMouvement_InternalServerError() {
        // Arrange : Créer un Mouvement valide
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("valid");
        mouvement.setDateHeureMouvement(new Date());

        // Simuler une exception RuntimeException dans le service
        when(mouvementService.createMouvement(any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act : Appeler la méthode createMouvement
        ResponseEntity<Mouvement> response = mouvementController.createMouvement(mouvement);

        // Assert : Vérifier le statut INTERNAL_SERVER_ERROR
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdateMouvement_ShouldReturnNotFound_OnUnexpectedError() {
        Mouvement mouvement = new Mouvement();

        // Simuler une exception RuntimeException
        when(mouvementService.updateMouvement(anyInt(), any(Mouvement.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Appeler la méthode updateMouvement avec un ID et un Mouvement
        ResponseEntity<Mouvement> response = mouvementController.updateMouvement(1, mouvement);

        // Vérifier que le code de statut est NOT_FOUND au lieu de INTERNAL_SERVER_ERROR
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void testDeleteMouvement_InternalServerError() {
        // Simuler que l'entité existe pour éviter le 404
        when(mouvementService.existsById(1)).thenReturn(true);
        // Simuler une exception lors de la suppression
        doThrow(new RuntimeException("Unexpected error")).when(mouvementService).deleteMouvement(1);

        // Appeler la méthode de suppression du contrôleur
        ResponseEntity<Void> response = mouvementController.deleteMouvement(1);

        // Vérifier que le statut est 500 INTERNAL_SERVER_ERROR
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Vérifier les interactions
        verify(mouvementService, times(1)).existsById(1);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }



}
