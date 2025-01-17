package example.integration.controllers;

import example.DTO.TypeRegleDTO;
import example.interfaces.ITypeRegleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class TypeRegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ITypeRegleService typeRegleService;

    private TypeRegleDTO typeRegleDTO;

    @BeforeEach
    public void setUp() {
        // Set up a valid TypeRegleDTO for testing
        typeRegleDTO = TypeRegleDTO.builder()
                .nomTypeRegle("Test Rule")
                .description("Test Description")
                .build();

        // Save it using the service
        typeRegleDTO = typeRegleService.createTypeRegle(typeRegleDTO);
    }

    @Test
    public void testViewAllTypeRegles_Success() throws Exception {
        mockMvc.perform(get("/typeRegle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TR_list"))
                .andExpect(model().attributeExists("typeRegles"))
                .andExpect(model().attribute("typeRegles", hasItem(
                        hasProperty("nomTypeRegle", is("Test Rule"))
                )));
    }





    @Test
    public void testCreateTypeRegle_InvalidData() throws Exception {
        String invalidTypeRegleJson = "{\"nomTypeRegle\":\"\",\"description\":\"\"}";

        mockMvc.perform(post("/typeRegle/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTypeRegleJson))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TR_create"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", is("Le nom du type de règle ne peut pas être vide.")));
    }



    @Test
    public void testDeleteTypeRegle_NotFound() throws Exception {
        mockMvc.perform(post("/typeRegle/delete/99999"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("Erreur lors de la suppression")));
    }
}
