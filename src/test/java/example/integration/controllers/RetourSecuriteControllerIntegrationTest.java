
package example.integration.controllers;

import example.entity.RetourSecurite;
import example.repositories.RetourSecuriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RetourSecuriteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    private RetourSecurite savedRetourSecurite;

    @BeforeEach
    public void setUp() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(123456L)
                .datesecurite(new Date())
                .cloture(false)
                .build();
        savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
    }

    // Test: View all RetourSecurites
    @Test
    public void testViewAllRetourSecurites() throws Exception {
        mockMvc.perform(get("/retourSecurite/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/RS_list"))
                .andExpect(model().attributeExists("retourSecurites"))
                .andExpect(model().attribute("retourSecurites", hasSize(greaterThan(0))));
    }

    // Test: View a RetourSecurite by ID - Success
    @Test
    public void testViewRetourSecuriteById_Success() throws Exception {
        mockMvc.perform(get("/retourSecurite/view/{id}", savedRetourSecurite.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/RS_details"))
                .andExpect(model().attributeExists("retourSecurite"))
                .andExpect(model().attribute("retourSecurite", hasProperty("numero", is(savedRetourSecurite.getNumero()))));
    }

    // Test: View a RetourSecurite by ID - Not Found
    @Test
    public void testViewRetourSecuriteById_NotFound() throws Exception {
        mockMvc.perform(get("/retourSecurite/view/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("non trouve")));
    }

    // Test: Create RetourSecurite Form
    @Test
    public void testCreateRetourSecuriteForm() throws Exception {
        mockMvc.perform(get("/retourSecurite/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/RS_create"))
                .andExpect(model().attributeExists("retourSecurite"));
    }

    // Test: Create RetourSecurite - Success
    @Test
    public void testCreateRetourSecurite_Success() throws Exception {
        mockMvc.perform(post("/retourSecurite/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("numero", "987654")
                        .param("cloture", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retourSecurites/RS_list"));

        RetourSecurite createdRetourSecurite = retourSecuriteRepository.findAll().stream()
                .filter(rs -> rs.getNumero().equals(987654L))
                .findFirst()
                .orElse(null);

        assert createdRetourSecurite != null;
    }

    // Test: Edit RetourSecurite Form
    @Test
    public void testEditRetourSecuriteForm() throws Exception {
        mockMvc.perform(get("/retourSecurite/edit/{id}", savedRetourSecurite.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("retourSecurites/RS_edit"))
                .andExpect(model().attributeExists("retourSecurite"))
                .andExpect(model().attribute("retourSecurite", hasProperty("numero", is(savedRetourSecurite.getNumero()))));
    }

    // Test: Edit RetourSecurite - Success
    @Test
    public void testEditRetourSecurite_Success() throws Exception {
        mockMvc.perform(post("/retourSecurite/edit/{id}", savedRetourSecurite.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("numero", "111111")
                        .param("cloture", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retourSecurites/RS_list"));

        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.findById(savedRetourSecurite.getId()).orElse(null);
        assert updatedRetourSecurite != null;
        assert updatedRetourSecurite.getNumero().equals(111111L);
        assert updatedRetourSecurite.getCloture();
    }

    // Test: Delete RetourSecurite
    @Test
    public void testDeleteRetourSecurite() throws Exception {
        mockMvc.perform(post("/retourSecurite/delete/{id}", savedRetourSecurite.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retourSecurites/RS_list"));

        assert retourSecuriteRepository.findById(savedRetourSecurite.getId()).isEmpty();
    }
}
