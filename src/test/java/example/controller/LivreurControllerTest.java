package example.controller;

import example.entity.Livreur;
import example.interfaces.ILivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testListLivreurs() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getAllLivreurs()).thenReturn(List.of(livreur));

        Model model = new ConcurrentModel();
        String response = livreurController.listLivreurs(model);

        assertEquals("livreurs/livreur_list", response);
        assertTrue(model.containsAttribute("livreurs"));
        assertEquals(1, ((List) model.getAttribute("livreurs")).size());  // Vérifie que la liste contient un élément
    }

    @Test
    public void testViewLivreurById_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getLivreurById(1)).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        assertEquals("livreurs/livreur_details", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreur, model.getAttribute("livreur"));  // Vérifie que l'attribut est bien l'objet livreur
    }

    @Test
    public void testViewLivreurById_NotFound() {
        when(livreurService.getLivreurById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = livreurController.viewLivreurById(1, model);

        assertEquals("livreurs/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
        assertEquals("Livreur avec l'ID 1 non trouvé.", model.getAttribute("errorMessage"));
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
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John Doe");
        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.createLivreur(livreur);

        assertEquals("redirect:/livreurs/livreur_list", response);
    }

    @Test
    public void testEditLivreurForm_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        when(livreurService.getLivreurById(1)).thenReturn(livreur);

        Model model = new ConcurrentModel();
        String response = livreurController.editLivreurForm(1, model);

        assertEquals("livreurs/livreur_edit", response);
        assertTrue(model.containsAttribute("livreur"));
        assertEquals(livreur, model.getAttribute("livreur"));  // Vérifie que l'attribut est bien l'objet livreur
    }

    @Test
    public void testEditLivreurForm_NotFound() {
        when(livreurService.getLivreurById(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = livreurController.editLivreurForm(1, model);

        assertEquals("livreurs/error", response);
    }

    @Test
    public void testUpdateLivreur_Success() {
        Livreur livreur = new Livreur();
        livreur.setId(1);
        livreur.setNomLivreur("Updated Name");

        when(livreurService.updateLivreur(1, livreur)).thenReturn(livreur);

        String response = livreurController.updateLivreur(1, livreur);

        assertEquals("redirect:/livreurs/livreur_list", response);
    }

    @Test
    public void testDeleteLivreur_Success() {
        doNothing().when(livreurService).deleteLivreur(1);

        String response = livreurController.deleteLivreur(1);

        assertEquals("redirect:/livreurs/livreur_list", response);
    }
}
