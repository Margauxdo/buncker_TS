package templates.typeRegles.controller;

import example.DTO.TypeRegleDTO;
import example.controller.TypeRegleController;
import example.interfaces.ITypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    // **Test: Lister tous les TypeRegles (Thymeleaf)**
    @Test
    public void testViewAllTypeRegles_Success() {
        // Arrange
        List<TypeRegleDTO> expectedTypeRegles = List.of(
                TypeRegleDTO.builder().id(1).nomTypeRegle("TypeRegle1").build(),
                TypeRegleDTO.builder().id(2).nomTypeRegle("TypeRegle2").build()
        );
        Mockito.when(typeRegleService.getTypeRegles()).thenReturn(expectedTypeRegles);
        Model model = new ConcurrentModel();

        // Act
        String response = typeRegleController.viewAllTypeRegles(model);

        // Assert
        Assertions.assertEquals("typeRegles/TR_list", response);
        Assertions.assertTrue(model.containsAttribute("typeRegles"));
        Assertions.assertEquals(expectedTypeRegles, model.getAttribute("typeRegles"));
        Mockito.verify(typeRegleService, Mockito.times(1)).getTypeRegles();
    }





}
