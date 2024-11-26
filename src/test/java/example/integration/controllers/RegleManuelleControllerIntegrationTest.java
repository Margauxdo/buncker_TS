
package example.integration.controllers;

import example.entity.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegleManuelleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    @BeforeEach
    public void setup() {
        regleManuelleRepository.deleteAll();
    }

    @Test
    public void testListRegleManuelles() throws Exception {
        // Arrange
        RegleManuelle regleManuelle = RegleManuelle.builder()
                .descriptionRegle("Test Description")
                .createurRegle("Admin")
                .build();
        regleManuelleRepository.save(regleManuelle);

        // Act & Assert
        mockMvc.perform(get("/reglemanuelle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("reglesManuelles/RM_list"))
                .andExpect(model().attributeExists("regleManuelles"))
                .andExpect(model().attribute("regleManuelles", hasSize(1)));
    }

    @Test
    public void testViewRegleManuelleById() throws Exception {
        // Arrange
        RegleManuelle regleManuelle = RegleManuelle.builder()
                .descriptionRegle("Test View")
                .createurRegle("Admin")
                .build();
        regleManuelle = regleManuelleRepository.save(regleManuelle);

        // Act & Assert
        mockMvc.perform(get("/reglemanuelle/view/{id}", regleManuelle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("reglesManuelles/RM_details"))
                .andExpect(model().attributeExists("regleManuelle"))
                .andExpect(model().attribute("regleManuelle", hasProperty("descriptionRegle", is("Test View"))));
    }

    @Test
    public void testCreateRegleManuelleForm() throws Exception {
        mockMvc.perform(get("/reglemanuelle/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("reglesManuelles/RM_create"))
                .andExpect(model().attributeExists("regleManuelle"));
    }

    @Test
    public void testCreateRegleManuelle() throws Exception {
        mockMvc.perform(post("/reglemanuelle/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("descriptionRegle", "New Regle")
                        .param("createurRegle", "Admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reglesManuelles/RM_list"));

        // Verify the RegleManuelle is saved
        RegleManuelle saved = regleManuelleRepository.findAll().get(0);
        assert saved.getDescriptionRegle().equals("New Regle");
    }

    @Test
    public void testEditRegleManuelleForm() throws Exception {
        // Arrange
        RegleManuelle regleManuelle = RegleManuelle.builder()
                .descriptionRegle("Edit Test")
                .createurRegle("Admin")
                .build();
        regleManuelle = regleManuelleRepository.save(regleManuelle);

        // Act & Assert
        mockMvc.perform(get("/reglemanuelle/edit/{id}", regleManuelle.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("reglesManuelles/RM_edit"))
                .andExpect(model().attributeExists("regleManuelle"))
                .andExpect(model().attribute("regleManuelle", hasProperty("descriptionRegle", is("Edit Test"))));
    }

    @Test
    public void testUpdateRegleManuelle() throws Exception {
        // Arrange
        RegleManuelle regleManuelle = RegleManuelle.builder()
                .descriptionRegle("Old Description")
                .createurRegle("Admin")
                .build();
        regleManuelle = regleManuelleRepository.save(regleManuelle);

        // Act
        mockMvc.perform(post("/reglemanuelle/edit/{id}", regleManuelle.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("descriptionRegle", "Updated Description")
                        .param("createurRegle", "Admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reglesManuelles/RM_list"));

        // Assert
        RegleManuelle updated = regleManuelleRepository.findById(regleManuelle.getId()).orElse(null);
        assert updated != null;
        assert updated.getDescriptionRegle().equals("Updated Description");
    }

    @Test
    public void testDeleteRegleManuelle() throws Exception {
        // Arrange
        RegleManuelle regleManuelle = RegleManuelle.builder()
                .descriptionRegle("Delete Test")
                .createurRegle("Admin")
                .build();
        regleManuelle = regleManuelleRepository.save(regleManuelle);

        // Act
        mockMvc.perform(post("/reglemanuelle/delete/{id}", regleManuelle.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reglesManuelles/RM_list"));

        // Assert
        assert regleManuelleRepository.findById(regleManuelle.getId()).isEmpty();
    }
}
