
package example.integration.controllers;

import example.entity.Regle;
import example.entity.SortieSemaine;

import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SortieSemaineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    private Regle regle;

    @BeforeEach
    public void setUp() {
        // Create and save a Regle entity for relationships
        regle = Regle.builder()
                .coderegle("R001")
                .dateRegle(new Date())
                .build();
        regle = regleRepository.save(regle);
    }

    @Test
    public void testViewSortieSemaineList() throws Exception {
        mockMvc.perform(get("/sortieSemaine/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_list"))
                .andExpect(model().attributeExists("sortieSemaine"));
    }

    @Test
    public void testViewSortieSemaineById_Success() throws Exception {
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regle(regle)
                .build();
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        mockMvc.perform(get("/sortieSemaine/view/{id}", sortieSemaine.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_details"))
                .andExpect(model().attributeExists("sortieSemaine"))
                .andExpect(model().attribute("sortieSemaine", hasProperty("id", is(sortieSemaine.getId()))));
    }

    @Test
    public void testViewSortieSemaineById_NotFound() throws Exception {
        mockMvc.perform(get("/sortieSemaine/view/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/error"))
                .andExpect(model().attributeExists("errormessage"));
    }

    @Test
    public void testCreateSortieSemaineForm() throws Exception {
        mockMvc.perform(get("/sortieSemaine/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_create"))
                .andExpect(model().attributeExists("sortieSemaine"));
    }

    @Test
    public void testCreateSortieSemaine() throws Exception {
        mockMvc.perform(post("/sortieSemaine/create")
                        .param("dateSortieSemaine", "2024-11-30")
                        .param("regle.id", String.valueOf(regle.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sortieSemaines/SS_list"));
    }

    @Test
    public void testEditSortieSemaineForm_Success() throws Exception {
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regle(regle)
                .build();
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        mockMvc.perform(get("/sortieSemaine/edit/{id}", sortieSemaine.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/SS_edit"))
                .andExpect(model().attributeExists("sortieSemaine"))
                .andExpect(model().attribute("sortieSemaine", hasProperty("id", is(sortieSemaine.getId()))));
    }

    @Test
    public void testEditSortieSemaineForm_NotFound() throws Exception {
        mockMvc.perform(get("/sortieSemaine/edit/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("sortieSemaines/error"));
    }

    @Test
    public void testUpdateSortieSemaine() throws Exception {
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regle(regle)
                .build();
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        mockMvc.perform(post("/sortieSemaine/edit/{id}", sortieSemaine.getId())
                        .param("dateSortieSemaine", "2024-12-01")
                        .param("regle.id", String.valueOf(regle.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sortieSemaines/SS_list"));
    }

    @Test
    public void testDeleteSortieSemaine() throws Exception {
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(new Date())
                .regle(regle)
                .build();
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        mockMvc.perform(post("/sortieSemaine/delete/{id}", sortieSemaine.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sortieSemaines/SS_list"));
    }
}
