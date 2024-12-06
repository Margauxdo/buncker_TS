package example.controller;

import example.entity.Mouvement;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
import example.services.LivreurService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MouvementControllerTest {

    @InjectMocks
    private MouvementController mouvementController;

    @Mock
    private ValiseService valiseService;
    @Mock
    private MouvementRepository mouvementRepository;

    @Mock
    private LivreurService livreurService;

    @Mock
    private IMouvementService mouvementService;
    private Model model;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        model = new ConcurrentModel();
    }

    // Test : Lister tous les mouvements - Succès
    @Test
    public void testListAllMouvements_Success() {
        // Création d'un mouvement avec une valise null
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        mouvement.setValise(null); // Simuler une valise null pour ce mouvement

        // Simulation du comportement du service
        when(mouvementService.getAllMouvements()).thenReturn(List.of(mouvement));

        // Création du modèle pour tester la méthode du contrôleur
        Model model = new ConcurrentModel();
        String response = mouvementController.listAllMouvements(model);

        // Vérifications
        assertEquals("mouvements/mouv_list", response);
        assertTrue(model.containsAttribute("mouvements"));
        assertEquals(1, ((List<?>) model.getAttribute("mouvements")).size()); // Vérification du nombre d'éléments dans la liste
        verify(mouvementService, times(1)).getAllMouvements(); // Vérification que la méthode a bien été appelée
    }





    @Test
    public void testGetAllMouvementsWithValise_Success() {
        // Création d'un mouvement et d'une valise associée
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);

        Valise valise = new Valise();
        valise.setId(1);
        mouvement.setValise(valise);  // Associer une valise au mouvement

        // Mock du service pour renvoyer la liste avec valise
        when(mouvementService.getAllMouvementsWithValise()).thenReturn(List.of(mouvement));

        // Appel de la méthode dans le service
        List<Mouvement> result = mouvementService.getAllMouvementsWithValise();

        // Vérifications
        assertEquals(1, result.size());  // Vérifier que la taille est 1
        assertEquals(mouvement, result.get(0)); // Vérifier que le bon mouvement est retourné
        assertNotNull(result.get(0).getValise()); // Vérifier que la valise est bien présente
    }


    // Test : Voir un mouvement par ID - Succès
    @Test
    public void testViewMouvementById_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.findByIdWithValise(1L)).thenReturn(Optional.of(mouvement)); // Mock service call

        Model model = new ConcurrentModel();

        // Act
        String response = mouvementController.viewMouvementDetails(1L, model); // Pass Long here too

        // Assert
        assertEquals("mouvements/mouv_details", response); // Check the returned view name
        assertTrue(model.containsAttribute("mouvement")); // Verify the model contains the "mouvement" attribute
        assertEquals(mouvement, model.getAttribute("mouvement")); // Verify the model's "mouvement" attribute value
        verify(mouvementService, times(1)).findByIdWithValise(1L); // Verify interaction with the service
    }



    // Test : Voir un mouvement par ID - Non trouvé
    @Test
    public void testViewMouvementById_NotFound() {
        // Arrange
        when(mouvementService.findByIdWithValise(1L)).thenReturn(Optional.empty()); // Simulate not found

        Model model = new ConcurrentModel();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            mouvementController.viewMouvementDetails(1L, model);
        });

        // Verify no model attributes were added
        assertFalse(model.containsAttribute("mouvement"));

        // Verify the service was called once
        verify(mouvementService, times(1)).findByIdWithValise(1L);
    }



    // Test : Formulaire de création de mouvement
    @Test
    public void testCreateMouvementForm() {
        // Arrange
        when(valiseService.getAllValises()).thenReturn(List.of());
        when(livreurService.getAllLivreurs()).thenReturn(List.of());
        Model model = new ConcurrentModel();

        // Act
        String response = mouvementController.createMouvementForm(model);

        // Assert
        assertEquals("mouvements/mouv_create", response, "The view name should be 'mouvements/mouv_create'");
        assertTrue(model.containsAttribute("mouvement"), "Model should contain attribute 'mouvement'");
        assertTrue(model.containsAttribute("valises"), "Model should contain attribute 'valises'");
        assertTrue(model.containsAttribute("allLivreurs"), "Model should contain attribute 'allLivreurs'");
        assertNotNull(model.getAttribute("mouvement"), "Attribute 'mouvement' should not be null");

        // Verify interactions
        verify(valiseService, times(1)).getAllValises();
        verify(livreurService, times(1)).getAllLivreurs();
        verifyNoMoreInteractions(valiseService, livreurService);
    }


    // Test : Création de mouvement - Succès
    @Test
    public void testCreateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new java.util.Date());

        // Mocking dependencies
        when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = mock(Model.class);

        // Act
        String response = mouvementController.createMouvementThymeleaf(mouvement, bindingResult, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Assert redirection
        verify(mouvementService, times(1)).createMouvement(any(Mouvement.class)); // Verify service call
        verify(bindingResult, times(1)).hasErrors(); // Verify validation check
    }



    // Test : Formulaire de modification de mouvement - Succès
    @Test
    public void testEditMouvementForm_Success() {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1);
        when(mouvementService.getMouvementById(1)).thenReturn(mouvement);

        Model model = new ConcurrentModel();
        String response = mouvementController.editMouvementForm(1, model);

        assertEquals("mouvements/mouv_edit", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertEquals(mouvement, model.getAttribute("mouvement"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }

    // Test : Formulaire de modification de mouvement - Non trouvé
    @Test
    public void testEditMouvementForm_NotFound() {
        // Arrange
        when(mouvementService.getMouvementById(1)).thenReturn(null);
        Model model = new ConcurrentModel();

        // Act
        String response = mouvementController.editMouvementForm(1, model);

        // Assert
        assertEquals("mouvements/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Mouvement avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
        verify(mouvementService, times(1)).getMouvementById(1);
        verifyNoMoreInteractions(mouvementService);
    }


    // Test : Mise à jour de mouvement - Succès
    @Test
    public void testUpdateMouvement_Success() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setId(1); // Utilisation de Long pour l'ID
        mouvement.setStatutSortie("Finalisé");

        // Mock d'une valise associée
        Valise valise = new Valise();
        valise.setId(10); // Assigner un ID fictif à la valise
        mouvement.setValise(valise);

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);

        // Simuler l'absence d'erreurs de validation
        when(result.hasErrors()).thenReturn(false);

        // Simuler le Mouvement existant récupéré par le service
        Mouvement existingMouvement = new Mouvement();
        existingMouvement.setId(1);
        existingMouvement.setValise(valise); // Associer une valise au mouvement existant
        when(mouvementService.getMouvementById(1)).thenReturn(existingMouvement);

        // Simuler la récupération de la Valise par le service ValiseService
        when(valiseService.getValiseById(10)).thenReturn(valise);

        // Simuler la mise à jour réussie
        when(mouvementService.updateMouvement(1, existingMouvement)).thenReturn(mouvement);

        // Act
        String response = mouvementController.updateMouvement(1, mouvement, result, model);

        // Assert
        assertEquals("redirect:/mouvements/list", response); // Vérifier l'URL de redirection
        verify(mouvementService, times(1)).getMouvementById(1); // Vérifier que le mouvement existant a été récupéré
        verify(mouvementService, times(1)).updateMouvement(1, existingMouvement); // Vérifier l'appel à la méthode de mise à jour
        verify(result, times(1)).hasErrors(); // Vérifier la validation
        verify(model, never()).addAttribute(eq("errorMessage"), anyString()); // Vérifier qu'aucun message d'erreur n'a été ajouté
    }




    // Test : Suppression de mouvement - Succès
    @Test
    public void testDeleteMouvement_Success() {
        doNothing().when(mouvementService).deleteMouvement(1);

        String response = mouvementController.deleteMouvement(1, new ConcurrentModel());

        assertEquals("redirect:/mouvements/list", response);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }

}
