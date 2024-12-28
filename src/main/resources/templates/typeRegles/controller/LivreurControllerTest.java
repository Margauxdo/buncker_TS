package templates.typeRegles.controller;

import example.DTO.LivreurDTO;
import example.controller.LivreurController;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LivreurControllerTest {

    @InjectMocks
    private LivreurController livreurController;

    @Mock
    private ILivreurService livreurService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListLivreurs_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).build();
        Mockito.when(livreurService.getAllLivreurs()).thenReturn(List.of(livreurDTO));

        Model model = new ConcurrentModel();
        String response = livreurController.listLivreurs(model);

        Assertions.assertEquals("livreurs/livreur_list", response);
        Assertions.assertTrue(model.containsAttribute("livreurs"));
        Assertions.assertEquals(1, ((List<?>) model.getAttribute("livreurs")).size());
        Mockito.verify(livreurService, Mockito.times(1)).getAllLivreurs();
    }

    @Test
    public void testViewLivreurById_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).build();
        Mockito.when(livreurService.getLivreurById(1)).thenReturn(livreurDTO);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        Assertions.assertEquals("livreurs/livreur_details", response);
        Assertions.assertTrue(model.containsAttribute("livreur"));
        Assertions.assertEquals(livreurDTO, model.getAttribute("livreur"));
        Mockito.verify(livreurService, Mockito.times(1)).getLivreurById(1);
    }



    @Test
    public void testCreateLivreur_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().nomLivreur("John Doe").build();
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreurDTO, bindingResult, model);

        Assertions.assertEquals("redirect:/livreurs/list", response);
        Mockito.verify(livreurService, Mockito.times(1)).createLivreur(livreurDTO);
        Mockito.verify(bindingResult, Mockito.times(1)).hasErrors();
    }




    @Test
    public void testUpdateLivreur_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).nomLivreur("Updated Name").build();
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        Model model = new ConcurrentModel();
        String response = livreurController.updateLivreur(1, livreurDTO, bindingResult, model);

        Assertions.assertEquals("redirect:/livreurs/list", response);
        Mockito.verify(livreurService, Mockito.times(1)).updateLivreur(1, livreurDTO);
    }

    @Test
    public void testDeleteLivreur_Success() {
        Mockito.doNothing().when(livreurService).deleteLivreur(1);

        Model model = new ConcurrentModel();
        String response = livreurController.deleteLivreur(1, model);

        Assertions.assertEquals("redirect:/livreurs/list", response);
        Mockito.verify(livreurService, Mockito.times(1)).deleteLivreur(1);
    }



}
