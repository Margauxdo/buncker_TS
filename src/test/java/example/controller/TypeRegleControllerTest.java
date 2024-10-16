package example.controller;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    }@Test
    public void testGetTypeRegles_Failure() {

    }@Test
    public void testGetTypeRegle_Success() {

    }@Test
    public void testGetTypeRegle_Failure() {

    }@Test
    public void testCreateTypeRegle_Success() {

    }@Test
    public void testCreateTypeRegle_Failure() {

    }@Test
    public void testUpdateTypeRegle_Success() {

    }@Test
    public void testUpdateTypeRegle_Failure() {

    }@Test
    public void testDeleteTypeRegle_Success() {

    }@Test
    public void testDeleteTypeRegle_Failure() {

    }@Test
    public void testCreateTypeRegle_InvalidInput() {

    }@Test
    public void testUpdateTypeRegle_InvalidInput() {

    }@Test
    public void testDeleteTypeRegle_NotFound() {

    }@Test
    public void testCreateTypeRegle_NotFound() {

    }@Test
    public void testTypeRegleController() {

    }@Test
    public void testCreateTypeRegle_Conflict() {

    }@Test
    public void testCreateTypeRegle_InternalServerError() {

    }@Test
    public void testUpdateTypeRegle_Conflict() {

    }@Test
    public void testDeleteTypeRegle_Conflict() {

    }
}
