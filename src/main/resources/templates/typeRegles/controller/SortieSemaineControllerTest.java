package templates.typeRegles.controller;

import example.DTO.SortieSemaineDTO;
import example.controller.SortieSemaineController;
import example.interfaces.ISortieSemaineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SortieSemaineControllerTest {

    @InjectMocks
    private SortieSemaineController sortieSemaineController;

    @Mock
    private ISortieSemaineService sortieSemaineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // Test: Voir une semaine spécifique - Succès
    @Test
    public void testViewSortieSemaineById_Success() {
        // Arrange
        SortieSemaineDTO sortieSemaine = new SortieSemaineDTO();
        Mockito.when(sortieSemaineService.getSortieSemaine(1)).thenReturn(sortieSemaine);
        Model model = new ConcurrentModel();

        // Act
        String response = sortieSemaineController.viewSortieSemaine(1, model);

        // Assert
        Assertions.assertEquals("sortieSemaines/SS_details", response);
        Assertions.assertTrue(model.containsAttribute("sortieSemaine"));
        Assertions.assertEquals(sortieSemaine, model.getAttribute("sortieSemaine"));
        Mockito.verify(sortieSemaineService, Mockito.times(1)).getSortieSemaine(1);
    }


}
