package example.integration.services;

import example.DTO.ProblemeDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import example.services.ProblemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProblemeServiceIntegrationTest {

    @Autowired
    private ProblemeService problemeService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;
    private Valise valise;
    private Probleme probleme;

    @BeforeEach
    public void setUp() {
        client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        client = clientRepository.save(client);

        valise = Valise.builder()
                .description("Valise de test")
                .numeroValise(123456L)
                .refClient("RefClientTest")
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        probleme = Probleme.builder()
                .descriptionProbleme("Test description")
                .detailsProbleme("Test details")
                .client(client)
                .valise(valise)
                .build();
    }

    @Test
    void testCreateProbleme_Success() {
        // Act
        Probleme createdProbleme = problemeService.createProbleme(probleme);

        // Assert
        assertNotNull(createdProbleme.getId());
        assertEquals(probleme.getDescriptionProbleme(), createdProbleme.getDescriptionProbleme());
        assertEquals(probleme.getDetailsProbleme(), createdProbleme.getDetailsProbleme());
        assertNotNull(createdProbleme.getValise());
        assertEquals(valise.getId(), createdProbleme.getValise().getId());
    }

    @Test
    void testCreateProbleme_Failure_Duplicate() {
        // Arrange
        problemeService.createProbleme(probleme);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            problemeService.createProbleme(probleme);
        });

        assertEquals("Un problème avec cette description et ces détails existe déjà.", exception.getMessage());
    }

    @Test
    void testUpdateProbleme_Success() {
        // Arrange
        Probleme createdProbleme = problemeService.createProbleme(probleme);
        createdProbleme.setDetailsProbleme("Updated details");

        // Act
        Probleme updatedProbleme = problemeService.updateProbleme(createdProbleme.getId(), createdProbleme);

        // Assert
        assertNotNull(updatedProbleme);
        assertEquals("Updated details", updatedProbleme.getDetailsProbleme());
    }

    @Test
    void testUpdateProbleme_Failure_NotFound() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            problemeService.updateProbleme(999, probleme);
        });

        assertEquals("Problem not found with ID: 999", exception.getMessage());
    }

    @Test
    void testDeleteProbleme_Success() {
        // Arrange
        Probleme createdProbleme = problemeService.createProbleme(probleme);

        // Act
        problemeService.deleteProbleme(createdProbleme.getId());

        // Assert
        assertFalse(problemeService.getAllProblemes().stream()
                .anyMatch(p -> p.getId() == createdProbleme.getId()));
    }

    @Test
    void testDeleteProbleme_Failure_NotFound() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            problemeService.deleteProbleme(999);
        });

        assertEquals("Problem not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllProblemes() {
        // Arrange
        problemeService.createProbleme(probleme);

        // Act
        List<ProblemeDTO> problemes = problemeService.getAllProblemes();

        // Assert
        assertFalse(problemes.isEmpty());
        assertEquals(1, problemes.size());
    }

    @Test
    void testGetProblemeById_Success() {
        // Arrange
        Probleme createdProbleme = problemeService.createProbleme(probleme);

        // Act
        ProblemeDTO fetchedProbleme = problemeService.getProblemeById(createdProbleme.getId());

        // Assert
        assertNotNull(fetchedProbleme);
        assertEquals(createdProbleme.getDescriptionProbleme(), fetchedProbleme.getDescriptionProbleme());
    }

    @Test
    void testGetProblemeById_Failure_NotFound() {
        // Act
        ProblemeDTO fetchedProbleme = problemeService.getProblemeById(999);

        // Assert
        assertNull(fetchedProbleme);
    }
}
