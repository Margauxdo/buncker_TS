package example.controller;

import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TypeValiseControllerTest {

    @InjectMocks
    private TypeValiseController typeValiseController;

    @Mock
    private IValiseService valiseService;

    @Mock
    private ITypeValiseService typeValiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // API REST Tests
    @Test
    public void testGetTypeValises_Success() {
        List<TypeValise> typeValises = new ArrayList<>();
        typeValises.add(new TypeValise());
        when(typeValiseService.getTypeValises()).thenReturn(typeValises);

        List<TypeValise> result = typeValiseController.getTypeValisesApi();

        assertNotNull(result);
        assertEquals(typeValises.size(), result.size());
    }

    @Test
    public void testGetTypeValises_Failure() {
        when(typeValiseService.getTypeValises()).thenThrow(new RuntimeException("error database"));

        assertThrows(RuntimeException.class, () -> typeValiseController.getTypeValisesApi());
    }

    @Test
    public void testGetTypeValise_Success() {
        TypeValise typeValise = new TypeValise();
        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);

        ResponseEntity<TypeValise> response = typeValiseController.getTypeValiseApi(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTypeValise_Failure() {
        when(typeValiseService.getTypeValise(1)).thenReturn(null);

        ResponseEntity<TypeValise> response = typeValiseController.getTypeValiseApi(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateTypeValise_Success() {
        Valise valise = new Valise();
        valise.setId(1);

        TypeValise typeValise = new TypeValise();
        typeValise.setValise(valise);

        when(valiseService.getValiseById(1)).thenReturn(valise);
        when(typeValiseService.createTypeValise(any(TypeValise.class))).thenReturn(typeValise);

        ResponseEntity<TypeValise> response = typeValiseController.createTypeValiseApi(typeValise);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(typeValise, response.getBody());
    }

    @Test
    public void testCreateTypeValise_Failure() {
        TypeValise typeValise = new TypeValise();

        when(typeValiseService.createTypeValise(typeValise))
                .thenThrow(new IllegalArgumentException("Type of suitcase invalid"));

        ResponseEntity<TypeValise> response = typeValiseController.createTypeValiseApi(typeValise);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateTypeValise_Success() {
        TypeValise updateTypeValise = new TypeValise();

        when(typeValiseService.updateTypeValise(1, updateTypeValise)).thenReturn(updateTypeValise);

        ResponseEntity<TypeValise> response = typeValiseController.updateTypeValiseApi(1, updateTypeValise);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateTypeValise_Failure() {
        TypeValise updateTypeValise = new TypeValise();

        when(typeValiseService.updateTypeValise(1, updateTypeValise))
                .thenThrow(new EntityNotFoundException("TypeValise not found"));

        ResponseEntity<TypeValise> response = typeValiseController.updateTypeValiseApi(1, updateTypeValise);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteTypeValise_Success() {
        doNothing().when(typeValiseService).deleteTypeValise(1);

        ResponseEntity<TypeValise> response = typeValiseController.deleteTypeValiseApi(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteTypeValise_NotFound() {
        doThrow(new IllegalArgumentException("suitcase not found")).when(typeValiseService).deleteTypeValise(1);

        ResponseEntity<TypeValise> response = typeValiseController.deleteTypeValiseApi(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testTypeValiseControllerInitialization() {
        assertNotNull(typeValiseController);
        assertNotNull(typeValiseService);
    }

    // Thymeleaf Tests
    @Test
    public void testViewTypeValises_Success() {
        when(typeValiseService.getTypeValises()).thenReturn(List.of(new TypeValise()));

        Model model = new ConcurrentModel();
        String response = typeValiseController.viewTypeValises(model);

        assertEquals("typeValises/TV_list", response);
        assertTrue(model.containsAttribute("typeValises"));
    }

    @Test
    public void testViewTypeValiseById_Success() {
        TypeValise typeValise = new TypeValise();

        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);

        Model model = new ConcurrentModel();
        String response = typeValiseController.viewTypeValiseById(1, model);

        assertEquals("typeValises/TV_details", response);
        assertTrue(model.containsAttribute("typeValise"));
        assertEquals(typeValise, model.getAttribute("typeValise"));
    }

    @Test
    public void testViewTypeValiseById_NotFound() {
        when(typeValiseService.getTypeValise(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = typeValiseController.viewTypeValiseById(1, model);

        assertEquals("typeValises/error", response);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    public void testCreateTypeValiseForm_Success() {
        Model model = new ConcurrentModel();
        String response = typeValiseController.createTypeValiseForm(model);

        assertEquals("typeValises/TV_create", response);
        assertTrue(model.containsAttribute("typeValise"));
    }

    @Test
    public void testEditTypeValiseForm_Success() {
        TypeValise typeValise = new TypeValise();

        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);

        Model model = new ConcurrentModel();
        String response = typeValiseController.editTypeValiseForm(1, model);

        assertEquals("typeValises/TV_edit", response);
        assertTrue(model.containsAttribute("typeValise"));
        assertEquals(typeValise, model.getAttribute("typeValise"));
    }

    @Test
    public void testEditTypeValiseForm_NotFound() {
        when(typeValiseService.getTypeValise(1)).thenReturn(null);

        Model model = new ConcurrentModel();
        String response = typeValiseController.editTypeValiseForm(1, model);

        assertEquals("typeValises/error", response);
    }

    @Test
    public void testDeleteTypeValise_Success_Thymeleaf() {
        // Arrange
        doNothing().when(typeValiseService).deleteTypeValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.deleteTypeValise(1, model);

        // Assert
        assertEquals("redirect:/typeValises/TV_list", response, "Expected redirect to 'typeValises/TV_list'");
        verify(typeValiseService, times(1)).deleteTypeValise(1);
    }


    @Test
    public void testDeleteTypeValise_NotFound_Thymeleaf() {
        // Arrange
        doThrow(new EntityNotFoundException("TypeValise not found")).when(typeValiseService).deleteTypeValise(1);

        Model model = new ConcurrentModel();

        // Act
        String response = typeValiseController.deleteTypeValise(1, model);

        // Assert
        assertEquals("typeValises/error", response, "Expected view name is 'typeValises/error'");
        assertTrue(model.containsAttribute("errorMessage"), "Model should contain 'errorMessage' attribute");
        assertEquals("TypeValise avec l'ID 1 non trouvé !", model.getAttribute("errorMessage"));
        verify(typeValiseService, times(1)).deleteTypeValise(1);
    }


}
