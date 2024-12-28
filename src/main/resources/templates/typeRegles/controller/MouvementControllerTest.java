package templates.typeRegles.controller;

import example.DTO.MouvementDTO;
import example.controller.MouvementController;
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
        Mockito.when(mouvementService.getAllMouvements()).thenReturn(List.of(mouvementDTO));

        // Act
        String response = mouvementController.listAllMouvements(model);

        // Assert
        Assertions.assertEquals("mouvements/mouv_list", response);
        Assertions.assertTrue(model.containsAttribute("mouvements"));
        Assertions.assertEquals(1, ((List<?>) model.getAttribute("mouvements")).size());
        Mockito.verify(mouvementService, Mockito.times(1)).getAllMouvements();
    }



}
