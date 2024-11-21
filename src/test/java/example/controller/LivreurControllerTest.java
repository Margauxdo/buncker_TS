package example.controller;

import example.entity.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LivreurControllerTest {

    @InjectMocks
    private LivreurController livreurController;

    @Mock
    private ILivreurService livreurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllLivreurs_Success() {
        List<Livreur> livreurs = new ArrayList<>();
        livreurs.add(new Livreur());

        when(livreurService.getAllLivreurs()).thenReturn(livreurs);

        ResponseEntity<List<Livreur>> response = livreurController.getAllLivreursApi();
        List<Livreur> result = response.getBody();

        // Vérifier les assertions
        Assertions.assertNotNull(result, "The list of delivery people should not be zero");
        Assertions.assertEquals(livreurs.size(), result.size(), "The size of the list should match");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
    }

    @Test
    public void testGetAllLivreurs_Failure() {
        when(livreurService.getAllLivreurs()).thenThrow(new RuntimeException("Database error"));
        Assertions.assertThrows(RuntimeException.class, () -> livreurController.getAllLivreursApi());
    }
    @Test
    public void testGetLivreurById_Success(){
        Livreur livreur = new Livreur();
        when(livreurService.getLivreurById(1)).thenReturn(livreur);
        ResponseEntity<Livreur> result = livreurController.getLivreurByIdApi(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(livreur, result.getBody());
    }
    @Test
    public void testGetLivreurById_Failure() {
        when(livreurService.getLivreurById(1)).thenReturn(null);
        ResponseEntity<Livreur> result = livreurController.getLivreurByIdApi(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateLivreur_Success() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John Doe");
        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        ResponseEntity<Livreur> result = livreurController.createLivreurApi(livreur);

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(livreur, result.getBody());
    }

    @Test
    public void testCreateLivreur_Failure() {
        // Arrange
        Livreur livreur = new Livreur();
        when(livreurService.createLivreur(any(Livreur.class)))
                .thenThrow(new IllegalArgumentException("Disabled delivery person"));

        // Act
        ResponseEntity<Livreur> result = livreurController.createLivreurApi(livreur);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(),
                "Expected HTTP status 400 BAD_REQUEST for disabled delivery person");
        verify(livreurService, times(1)).createLivreur(any(Livreur.class));
        verifyNoMoreInteractions(livreurService);
    }



    @Test
    public void testUpdateLivreur_Success() {
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setNomLivreur("Valid Name");
        when(livreurService.updateLivreur(1, updatedLivreur)).thenReturn(updatedLivreur);

        ResponseEntity<Livreur> result = livreurController.updateLivreurApi(1, updatedLivreur);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedLivreur, result.getBody());
    }

    @Test
    public void testUpdateLivreur_Failure() {
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setNomLivreur("Valid Name");

        when(livreurService.updateLivreur(1, updatedLivreur)).thenReturn(null);

        ResponseEntity<Livreur> result = livreurController.updateLivreurApi(1, updatedLivreur);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteLivreur_Success() {
        doNothing().when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreurApi(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testCreateLivreur_InvalidInput() {
        // Arrange
        Livreur invalidLivreur = new Livreur();
        when(livreurService.createLivreur(any(Livreur.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<Livreur> result = livreurController.createLivreurApi(invalidLivreur);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(),
                "Expected HTTP status 400 BAD_REQUEST for invalid input");
        verify(livreurService, times(1)).createLivreur(any(Livreur.class));
        verifyNoMoreInteractions(livreurService);
    }


    @Test
    public void testDeleteLivreur_NotFound(){
        doThrow(new IllegalArgumentException("Not found")).when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreurApi(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testCreateLivreur_Conflict() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Valid Name");

        when(livreurService.createLivreur(any(Livreur.class))).thenThrow(new ConflictException("conflict detected"));

        ResponseEntity<Livreur> result = livreurController.createLivreurApi(livreur);

        Assertions.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }



    @Test
    public void testDeleteLivreur_Failure() {
        doThrow(new IllegalArgumentException("Delivery person not found")).when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreurApi(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testUpdateLivreur_InvalidInput() {
        Livreur updatedLivreur = new Livreur();
        when(livreurService.updateLivreur(eq(1), any(Livreur.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Livreur> result = livreurController.updateLivreurApi(1, updatedLivreur);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void  testLivreurController(){
        Assertions.assertNotNull(livreurController);
        Assertions.assertNotNull(livreurService);
    }

    @Test
    public void testCreateLivreurApi_Success() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John Doe");
        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        ResponseEntity<Livreur> result = livreurController.createLivreurApi(livreur);  // Updated method call

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(livreur, result.getBody());
    }

    @Test
    public void testCreateLivreurApi_Failure() {
        Livreur livreur = new Livreur();
        when(livreurService.createLivreur(any(Livreur.class)))
                .thenThrow(new IllegalArgumentException("Disabled delivery person"));

        ResponseEntity<Livreur> result = livreurController.createLivreurApi(livreur);  // Updated method call

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(),
                "Expected HTTP status 400 BAD_REQUEST for disabled delivery person");
        verify(livreurService, times(1)).createLivreur(any(Livreur.class));
        verifyNoMoreInteractions(livreurService);
    }

    @Test
    public void testDeleteLivreurApi_NotFound() {
        doThrow(new EntityNotFoundException("Livreur not found")).when(livreurService).deleteLivreur(1);

        ResponseEntity<Void> result = livreurController.deleteLivreurApi(1);  // Updated method call

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteLivreurApi_Success() {
        doNothing().when(livreurService).deleteLivreur(1);

        ResponseEntity<Void> result = livreurController.deleteLivreurApi(1);  // Updated method call

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }


}
