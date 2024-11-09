package example.controller;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
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

public class TypeRegleControllerTest {

    @InjectMocks
    private TypeRegleController typeRegleController;

    @Mock
    private ITypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetTypeRegles_Success() {
        List<TypeRegle> typeRegles = new ArrayList<>();
        typeRegles.add(new TypeRegle());
        when(typeRegleService.getTypeRegles()).thenReturn(typeRegles);
        List<TypeRegle> result = typeRegleController.getTypeRegles();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(typeRegles.size(), result.size());
    }
    @Test
    public void testGetTypeRegles_Failure() {
        when(typeRegleService.getTypeRegles()).thenThrow(new RuntimeException("error database"));
        assertThrows(RuntimeException.class, () -> typeRegleController.getTypeRegles());
    }
    @Test
    public void testGetTypeRegle_Success() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleService.getTypeRegle(1)).thenReturn(typeRegle);
        ResponseEntity<TypeRegle> result = typeRegleController.getTypeRegle(1);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetTypeRegle_Failure() {
        when(this.typeRegleService.getTypeRegle(1)).thenReturn(null);
        ResponseEntity<TypeRegle> result = typeRegleController.getTypeRegle(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    public void testCreateTypeRegle_Success() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleService.createTypeRegle(typeRegle)).thenReturn(typeRegle);
        ResponseEntity<TypeRegle> result = typeRegleController.createTypeRegle(typeRegle);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals(typeRegle, result.getBody());
    }
    @Test
    public void testCreateTypeRegle_Failure() {
        TypeRegle typeRegle = new TypeRegle();
        when(this.typeRegleService.createTypeRegle(typeRegle)).thenThrow(new IllegalArgumentException("Type of rule invalid"));
        ResponseEntity<TypeRegle> result = typeRegleController.createTypeRegle(typeRegle);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    @Test
    public void testUpdateTypeRegle_Success() {
        TypeRegle updateTypeRegle = new TypeRegle();
        when(typeRegleService.updateTypeRegle(1, updateTypeRegle)).thenReturn(updateTypeRegle);
        ResponseEntity<TypeRegle> response = typeRegleController.updateTypeRegle(1, updateTypeRegle);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testUpdateTypeRegle_Failure() {
        TypeRegle updateTypeRegle = new TypeRegle();

        doThrow(new EntityNotFoundException("Le type de règle n'existe pas"))
                .when(this.typeRegleService).updateTypeRegle(1, updateTypeRegle);

        ResponseEntity<TypeRegle> response = typeRegleController.updateTypeRegle(1, updateTypeRegle);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteTypeRegle_Success() {
        doNothing().when(typeRegleService).deleteTypeRegle(1);
        ResponseEntity<TypeRegle> response = typeRegleController.deleteTypeRegle(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void testDeleteTypeRegle_Failure() {
        doThrow(new RuntimeException("Internal error")).when(typeRegleService).deleteTypeRegle(1);
        ResponseEntity<TypeRegle> response = typeRegleController.deleteTypeRegle(1);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(typeRegleService, times(1)).deleteTypeRegle(1);
    }
    @Test
    public void testCreateTypeRegle_InvalidInput() {
        TypeRegle invalidTypeRegle = new TypeRegle();
        when(typeRegleService.createTypeRegle(invalidTypeRegle)).thenThrow(new IllegalArgumentException("Type of rule invalid"));
        ResponseEntity<TypeRegle> response = typeRegleController.createTypeRegle(invalidTypeRegle);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testUpdateTypeRegle_InvalidInput() {
        TypeRegle invalidTypeRegle = new TypeRegle();
        when(typeRegleService.updateTypeRegle(1, invalidTypeRegle)).thenThrow(new IllegalArgumentException("Type de règle invalide"));
        ResponseEntity<TypeRegle> response = typeRegleController.updateTypeRegle(1, invalidTypeRegle);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testDeleteTypeRegle_NotFound() {
        doThrow(new EntityNotFoundException("Type de règle introuvable")).when(typeRegleService).deleteTypeRegle(1);
        ResponseEntity<TypeRegle> response = typeRegleController.deleteTypeRegle(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testTypeRegleController() {
        assertNotNull(typeRegleController);
        assertNotNull(typeRegleService);
    }
    @Test
    public void testCreateTypeRegle_Conflict() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleService.createTypeRegle(any(TypeRegle.class)))
                .thenThrow(new IllegalStateException("Conflit lors de la création du type de règle"));
        ResponseEntity<TypeRegle> response = typeRegleController.createTypeRegle(typeRegle);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
