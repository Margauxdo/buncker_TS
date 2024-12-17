package example.controller;

import example.DTO.MouvementDTO;
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
    private LivreurService livreurService;

    @Mock
    private IMouvementService mouvementService;

    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new ConcurrentModel();
    }

    // Test: List all mouvements - Success
    @Test
    public void testListAllMouvements_Success() {
        // Arrange
        MouvementDTO mouvementDTO = MouvementDTO.builder().id(1).statutSortie("In Progress").build();
        when(mouvementService.getAllMouvements()).thenReturn(List.of(mouvementDTO));

        // Act
        String response = mouvementController.listAllMouvements(model);

        // Assert
        assertEquals("mouvements/mouv_list", response);
        assertTrue(model.containsAttribute("mouvements"));
        assertEquals(1, ((List<?>) model.getAttribute("mouvements")).size());
        verify(mouvementService, times(1)).getAllMouvements();
    }

    // Test: View mouvement by ID - Success
    /*@Test
    public void testViewMouvementById_Success() {
        // Arrange
        MouvementDTO mouvementDTO = MouvementDTO.builder().id(1).statutSortie("In Progress").build();
        when(mouvementService.getMouvementById(1)).thenReturn(mouvementDTO);

        // Act
        String response = mouvementController.viewMouvementDetails(1, model);

        // Assert
        assertEquals("mouvements/mouv_details", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertEquals(mouvementDTO, model.getAttribute("mouvement"));
        verify(mouvementService, times(1)).getMouvementById(1);
    }*/

    // Test: View mouvement by ID - Not Found
    /*@Test
    public void testViewMouvementById_NotFound() {
        // Arrange
        when(mouvementService.getMouvementById(1)).thenThrow(new EntityNotFoundException("Mouvement not found"));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            mouvementController.viewMouvementDetails(1, model);
        });

        assertEquals("Mouvement not found", exception.getMessage());
        verify(mouvementService, times(1)).getMouvementById(1);
    }*/

    // Test: Create mouvement form
    @Test
    public void testCreateMouvementForm() {
        // Arrange
        when(valiseService.getAllValises()).thenReturn(List.of());
        when(livreurService.getAllLivreurs()).thenReturn(List.of());

        // Act
        String response = mouvementController.createMouvementForm(model);

        // Assert
        assertEquals("mouvements/mouv_create", response);
        assertTrue(model.containsAttribute("mouvement"));
        assertTrue(model.containsAttribute("valises"));
        assertTrue(model.containsAttribute("allLivreurs"));
    }

    // Test: Create mouvement - Success
    @Test
    public void testCreateMouvement_Success() {
        // Arrange
        MouvementDTO mouvementDTO = MouvementDTO.builder().statutSortie("In Progress").build();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        //when(mouvementService.createMouvement(any(Mouvement.class))).thenReturn(mouvement);

        // Act
        //String response = mouvementController.createMouvement(mouvementDTO, result, model);

        // Assert
        //assertEquals("redirect:/mouvements/list", response);
        verify(mouvementService, times(1)).createMouvement(mouvementDTO);
    }

    // Test: Update mouvement - Success
    @Test
    public void testUpdateMouvement_Success() {
        // Arrange
        MouvementDTO mouvementDTO = MouvementDTO.builder().id(1).statutSortie("Updated").build();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(mouvementService.updateMouvement(1, mouvementDTO)).thenReturn(mouvementDTO);

        // Act
        //String response = mouvementController.updateMouvement(1, result);

        // Assert
        //assertEquals("redirect:/mouvements/list", response);
        verify(mouvementService, times(1)).updateMouvement(1, mouvementDTO);
    }

    // Test: Delete mouvement - Success
    @Test
    public void testDeleteMouvement_Success() {
        // Act
        String response = mouvementController.deleteMouvement(1, (ConcurrentModel) model);

        // Assert
        assertEquals("redirect:/mouvements/list", response);
        verify(mouvementService, times(1)).deleteMouvement(1);
    }
}
