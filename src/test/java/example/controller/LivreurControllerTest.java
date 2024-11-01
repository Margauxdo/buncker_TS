package example.controller;

import example.entities.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
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

        ResponseEntity<List<Livreur>> response = livreurController.getAllLivreurs();
        List<Livreur> result = response.getBody();

        // Vérifier les assertions
        Assertions.assertNotNull(result, "La liste des livreurs ne devrait pas être nulle");
        Assertions.assertEquals(livreurs.size(), result.size(), "La taille de la liste devrait correspondre");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Le statut HTTP devrait être OK");
    }

    @Test
    public void testGetAllLivreurs_Failure() {
        when(livreurService.getAllLivreurs()).thenThrow(new RuntimeException("Erreur de la base de donnee"));
        Assertions.assertThrows(RuntimeException.class, () -> livreurController.getAllLivreurs());
    }
    @Test
    public void testGetLivreurById_Success(){
        Livreur livreur = new Livreur();
        when(livreurService.getLivreurById(1)).thenReturn(livreur);
        ResponseEntity<Livreur> result = livreurController.getLivreurById(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(livreur, result.getBody());
    }
    @Test
    public void testGetLivreurById_Failure() {
        when(livreurService.getLivreurById(1)).thenReturn(null);
        ResponseEntity<Livreur> result = livreurController.getLivreurById(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateLivreur_Success() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John Doe");
        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        ResponseEntity<Livreur> result = livreurController.createLivreur(livreur);

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(livreur, result.getBody());
    }

    @Test
    public void testCreateLivreur_Failure() {
        Livreur livreur = new Livreur();
        when(livreurService.createLivreur(any(Livreur.class))).thenThrow(new IllegalArgumentException("Livreur invalide"));
        ResponseEntity<Livreur> result = livreurController.createLivreur(livreur);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateLivreur_Success() {
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setNomLivreur("NomValide");
        when(livreurService.updateLivreur(1, updatedLivreur)).thenReturn(updatedLivreur);

        ResponseEntity<Livreur> result = livreurController.updateLivreur(1, updatedLivreur);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(updatedLivreur, result.getBody());
    }

    @Test
    public void testUpdateLivreur_Failure() {
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setNomLivreur("NomValide");

        when(livreurService.updateLivreur(1, updatedLivreur)).thenReturn(null);

        ResponseEntity<Livreur> result = livreurController.updateLivreur(1, updatedLivreur);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteLivreur_Success() {
        doNothing().when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreur(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testCreateLivreur_InvalidInput(){
        Livreur invalidLivreur = new Livreur();
        when(livreurService.createLivreur(any(Livreur.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Livreur> result = livreurController.createLivreur(invalidLivreur);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteLivreur_NotFound(){
        doThrow(new IllegalArgumentException("Not found")).when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreur(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testCreateLivreur_Conflict() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("NomValide");

        when(livreurService.createLivreur(any(Livreur.class))).thenThrow(new ConflictException("conflict detected"));

        ResponseEntity<Livreur> result = livreurController.createLivreur(livreur);

        Assertions.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }



    @Test
    public void testDeleteLivreur_Failure() {
        doThrow(new IllegalArgumentException("Livreur non trouvé")).when(livreurService).deleteLivreur(1);
        ResponseEntity<Void> result = livreurController.deleteLivreur(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testUpdateLivreur_InvalidInput() {
        Livreur updatedLivreur = new Livreur();
        when(livreurService.updateLivreur(eq(1), any(Livreur.class))).thenThrow(new IllegalArgumentException("Invalid data"));
        ResponseEntity<Livreur> result = livreurController.updateLivreur(1, updatedLivreur);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void  testLivreurController(){
        Assertions.assertNotNull(livreurController);
        Assertions.assertNotNull(livreurService);
    }

}
