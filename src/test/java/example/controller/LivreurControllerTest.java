package example.controller;

import example.DTO.LivreurDTO;
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
        when(livreurService.getAllLivreurs()).thenReturn(List.of(livreurDTO));

        Model model = new ConcurrentModel();
        String response = livreurController.listLivreurs(model);

        assertEquals("livreurs/livreur_list", response);
        assertTrue(model.containsAttribute("livreurs"));
        assertEquals(1, ((List<?>) model.getAttribute("livreurs")).size());
        verify(livreurService, times(1)).getAllLivreurs();
    }

    @Test
    public void testViewLivreurById_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).build();
        when(livreurService.getLivreurById(1)).thenReturn(livreurDTO);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        assertEquals("livreurs/livreur_details", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreurDTO, model.getAttribute("livreur"));
        verify(livreurService, times(1)).getLivreurById(1);
    }

    @Test
    public void testCreateLivreurForm() {
        Model model = new ConcurrentModel();
        String response = livreurController.createLivreurForm(model);

        assertEquals("livreurs/livreur_create", response);
        assertTrue(model.containsAttribute("livreur"));
        assertNotNull(model.getAttribute("livreur"));
    }

    @Test
    public void testCreateLivreur_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().nomLivreur("John Doe").build();
        when(bindingResult.hasErrors()).thenReturn(false);

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreurDTO, bindingResult, model);

        assertEquals("redirect:/livreurs/list", response);
        verify(livreurService, times(1)).createLivreur(livreurDTO);
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    public void testCreateLivreur_Conflict() {
        LivreurDTO livreurDTO = LivreurDTO.builder().build();

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new ConflictException("Conflict")).when(livreurService).createLivreur(any(LivreurDTO.class));

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreurDTO, bindingResult, model);

        assertEquals("livreurs/livreur_create", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Un livreur avec ce code existe déjà.", model.getAttribute("errorMessage"));
        verify(livreurService, times(1)).createLivreur(livreurDTO);
    }

    @Test
    public void testEditLivreurForm_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).build();
        when(livreurService.getLivreurById(1)).thenReturn(livreurDTO);

        Model model = new ConcurrentModel();
        String response = livreurController.editLivreurForm(1, model);

        assertEquals("livreurs/livreur_edit", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreurDTO, model.getAttribute("livreur"));
        verify(livreurService, times(1)).getLivreurById(1);
    }

    @Test
    public void testUpdateLivreur_Success() {
        LivreurDTO livreurDTO = LivreurDTO.builder().id(1).nomLivreur("Updated Name").build();
        when(bindingResult.hasErrors()).thenReturn(false);

        Model model = new ConcurrentModel();
        String response = livreurController.updateLivreur(1, livreurDTO, bindingResult, model);

        assertEquals("redirect:/livreurs/list", response);
        verify(livreurService, times(1)).updateLivreur(1, livreurDTO);
    }

    @Test
    public void testDeleteLivreur_Success() {
        doNothing().when(livreurService).deleteLivreur(1);

        Model model = new ConcurrentModel();
        String response = livreurController.deleteLivreur(1, model);

        assertEquals("redirect:/livreurs/list", response);
        verify(livreurService, times(1)).deleteLivreur(1);
    }


    @Test
    public void testDeleteLivreur_Error() {
        doThrow(new RuntimeException("Delete Error")).when(livreurService).deleteLivreur(1);

        Model model = new ConcurrentModel();
        String response = livreurController.deleteLivreur(1, model);

        assertEquals("livreurs/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Une erreur inattendue s'est produite lors de la suppression.", model.getAttribute("errorMessage"));
    }

}
