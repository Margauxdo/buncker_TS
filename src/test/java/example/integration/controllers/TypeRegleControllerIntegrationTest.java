package example.integration.controllers;

import example.entity.TypeRegle;
import example.repositories.TypeRegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TypeRegleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    private TypeRegle testTypeRegle;

    @BeforeEach
    public void setUp() {
        testTypeRegle = TypeRegle.builder()
                .nomTypeRegle("TypeRegle Test")
                .build();
        typeRegleRepository.save(testTypeRegle);
    }

    @Test
    public void testViewAllTypeRegles() throws Exception {
        mockMvc.perform(get("/typeRegle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TR_list"))
                .andExpect(model().attributeExists("typeRegle"))
                .andExpect(model().attribute("typeRegle", hasItem(hasProperty("libelle", is("TypeRegle Test")))));
    }

    @Test
    public void testViewTypeRegleById_Success() throws Exception {
        mockMvc.perform(get("/typeRegle/view/{id}", testTypeRegle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TP_details"))
                .andExpect(model().attributeExists("typeRegle"))
                .andExpect(model().attribute("typeRegle", hasProperty("libelle", is("TypeRegle Test"))));
    }

    @Test
    public void testViewTypeRegleById_NotFound() throws Exception {
        mockMvc.perform(get("/typeRegle/view/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("non trouve")));
    }

    @Test
    public void testCreateTypeRegleForm() throws Exception {
        mockMvc.perform(get("/typeRegle/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TP_create"))
                .andExpect(model().attributeExists("typeRegle"));
    }

    @Test
    public void testCreateTypeRegle() throws Exception {
        mockMvc.perform(post("/typeRegle/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("libelle", "New TypeRegle"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeRegles/TR_list"));

        TypeRegle createdTypeRegle = typeRegleRepository.findAll().stream()
                .filter(tr -> tr.getNomTypeRegle().equals("New TypeRegle"))
                .findFirst()
                .orElse(null);

        assert createdTypeRegle != null;
    }

    @Test
    public void testEditTypeRegleForm() throws Exception {
        mockMvc.perform(get("/typeRegle/edit/{id}", testTypeRegle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("typeRegles/TR_edit"))
                .andExpect(model().attributeExists("typeRegle"))
                .andExpect(model().attribute("typeRegle", hasProperty("libelle", is("TypeRegle Test"))));
    }

    @Test
    public void testUpdateTypeRegle() throws Exception {
        mockMvc.perform(post("/typeRegle/edit/{id}", testTypeRegle.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("libelle", "Updated TypeRegle"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeRegles/TR_list"));

        TypeRegle updatedTypeRegle = typeRegleRepository.findById(testTypeRegle.getId()).orElse(null);

        assert updatedTypeRegle != null;
        assert updatedTypeRegle.getNomTypeRegle().equals("Updated TypeRegle");
    }

    @Test
    public void testDeleteTypeRegle() throws Exception {
        mockMvc.perform(post("/typeRegle/delete/{id}", testTypeRegle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/typeRegles/TR_list"));

        boolean exists = typeRegleRepository.existsById(testTypeRegle.getId());
        assert !exists;
    }
}

