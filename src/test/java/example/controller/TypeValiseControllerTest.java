package example.controller;

import example.entities.TypeValise;
import example.interfaces.ITypeRegleService;
import example.interfaces.ITypeValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeValiseControllerTest {

    @InjectMocks
    private TypeValiseController typeValiseController;

    @Mock
    private ITypeValiseService typeValiseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTypeValises_Success() {

    }@Test
    public void testGetTypeValises_Failure() {

    }@Test
    public void testGetTypeValise_Success() {

    }@Test
    public void testGetTypeValise_Failure() {

    }@Test
    public void testCreateTypeValise_Success() {

    }@Test
    public void testCreateTypeValise_Failure() {

    }@Test
    public void testUpdateTypeValise_Success() {

    }@Test
    public void testUpdateTypeValise_Failure() {

    }@Test
    public void testDeleteTypeValise_Success() {

    }@Test
    public void testDeleteTypeValise_Failure() {

    }@Test
    public void testCreateTypeValise_InvalidInput() {

    }@Test
    public void testUpdateTypeValise_InvalidInput() {

    }@Test
    public void testDeleteTypeValise_NotFound() {

    }@Test
    public void testCreateTypeValise_NotFound() {

    }@Test
    public void testTypeValiseController() {

    }@Test
    public void testCreateTypeValise_Conflict() {

    }@Test
    public void testCreateTypeValise_InternalServerError() {

    }@Test
    public void testUpdateTypeValise_Conflict() {

    }@Test
    public void testDeleteTypeValise_Conflict() {

    }
}
