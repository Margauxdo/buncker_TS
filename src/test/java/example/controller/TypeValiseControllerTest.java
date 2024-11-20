package example.controller;

import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

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

    @Test
    public void testGetTypeValises_Success() {
        List<TypeValise> typeValises = new ArrayList<>();
        typeValises.add(new TypeValise());
        when(typeValiseService.getTypeValises()).thenReturn(typeValises);
        List<TypeValise> result = typeValiseController.getTypeValises();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(typeValises.size(), result.size());

    }
    @Test
    public void testGetTypeValises_Failure() {
        when(this.typeValiseService.getTypeValises()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> typeValiseController.getTypeValises());
    }
    @Test
    public void testGetTypeValise_Success() {
        TypeValise typeValise = new TypeValise();
        when(typeValiseService.getTypeValise(1)).thenReturn(typeValise);
        ResponseEntity<TypeValise> response = typeValiseController.getTypeValise(1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testGetTypeValise_Failure() {
        when(this.typeValiseService.getTypeValise(1)).thenReturn(null);
        ResponseEntity<TypeValise> response = typeValiseController.getTypeValise(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testCreateTypeValise_Success() {
        // Arrange
        Valise valise = new Valise();
        valise.setId(1); // Simulez une valise avec un ID valide

        TypeValise typeValise = new TypeValise();
        typeValise.setValise(valise); // Associez la valise au type de valise

        when(valiseService.getValiseById(1)).thenReturn(valise); // Simulez la récupération de la valise
        when(typeValiseService.createTypeValise(any(TypeValise.class))).thenReturn(typeValise);

        // Act
        ResponseEntity<TypeValise> response = typeValiseController.createTypeValise(typeValise);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(typeValise, response.getBody());
    }

    @Test
    public void testCreateTypeValise_Failure() {
        TypeValise typeValise = new TypeValise();
        when(typeValiseService.createTypeValise(typeValise)).thenThrow(new IllegalArgumentException("Type of suitcase invalid"));
        ResponseEntity<TypeValise> response = typeValiseController.createTypeValise(typeValise);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateTypeValise_Success() {
        TypeValise updateTypeValise = new TypeValise();
        when(typeValiseService.updateTypeValise(1, updateTypeValise)).thenReturn(updateTypeValise);
        ResponseEntity<TypeValise> response = typeValiseController.updateTypeValise(1, updateTypeValise);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testUpdateTypeValise_Failure() {
        TypeValise updatedTypeValise = new TypeValise();

        when(typeValiseService.updateTypeValise(1, updatedTypeValise)).thenThrow(new EntityNotFoundException("TypeValise not found"));

        ResponseEntity<TypeValise> response = typeValiseController.updateTypeValise(1, updatedTypeValise);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void testDeleteTypeValise_Success() {
        doNothing().when(typeValiseService).deleteTypeValise(1);
        ResponseEntity<TypeValise> response = typeValiseController.deleteTypeValise(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




    @Test
    public void testCreateTypeValise_InvalidInput() {
        TypeValise invalidTypeValise = new TypeValise();
        when(typeValiseService.createTypeValise(invalidTypeValise)).thenThrow(new IllegalArgumentException("type of suitcase invalid"));
        ResponseEntity<TypeValise> response = typeValiseController.createTypeValise(invalidTypeValise);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateTypeValise_InvalidInput() {
        TypeValise invalidTypeValise = new TypeValise();
        when(typeValiseService.updateTypeValise(anyInt(), any(TypeValise.class)))
                .thenThrow(new IllegalArgumentException("suitcase invalid"));
        ResponseEntity<TypeValise> response = typeValiseController.updateTypeValise(1, invalidTypeValise);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testDeleteTypeValise_NotFound() {
        doThrow(new IllegalArgumentException("suit case not found")).when(typeValiseService).deleteTypeValise(1);
        ResponseEntity<TypeValise> response = typeValiseController.deleteTypeValise(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testTypeValiseController() {
        assertNotNull(typeValiseController);
        assertNotNull(typeValiseService);

    }

    @Test
    public void testCreateTypeValise_InternalServerError() {
        // Arrange
        Valise valise = new Valise();
        valise.setId(1);

        TypeValise typeValise = new TypeValise();
        typeValise.setValise(valise);

        when(valiseService.getValiseById(1)).thenReturn(valise);
        when(typeValiseService.createTypeValise(any(TypeValise.class)))
                .thenThrow(new RuntimeException("internal error"));

        // Act
        ResponseEntity<TypeValise> response = typeValiseController.createTypeValise(typeValise);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }





}
