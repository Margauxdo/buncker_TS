package example.controller;

import example.interfaces.ITypeRegleService;
import example.interfaces.IValiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ValiseControllerTest {

    @InjectMocks
    private ValiseController valiseController;

    @Mock
    private IValiseService valiseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllValises_Success() {

    }@Test
    public void testGetAllValises_Failure() {

    }@Test
    public void testGetValiseById_Success() {

    }@Test
    public void testGetValiseById_Failure() {

    }@Test
    public void testCreateValise_Success() {

    }@Test
    public void testCreateValise_Failure() {

    }@Test
    public void testUpdateValise_Success() {

    }@Test
    public void testUpdateValise_Failure() {

    }@Test
    public void testDeleteValise_Success() {

    }@Test
    public void testDeleteValise_Failure() {

    }@Test
    public void testCreateValise_InvalidInput() {

    }@Test
    public void testUpdateValise_InvalidInput() {

    }@Test
    public void testDeleteValise_NotFound() {

    }@Test
    public void testCreateValise_NotFound() {

    }@Test
    public void testValiseController() {

    }@Test
    public void testCreateValise_Conflict() {

    }@Test
    public void testCreateValise_InternalServerError() {

    }@Test
    public void testUpdateValise_Conflict() {

    }
    @Test
    public void testUpdateValise_InternalServerError() {

    }
    @Test
    public void testDeleteValise_Conflict() {

    }
    @Test
    public void testDeleteValise_InternalServerError() {

    }
}
