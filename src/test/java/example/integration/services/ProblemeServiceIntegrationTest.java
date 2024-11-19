package example.integration.services;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import example.services.ProblemeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ProblemeServiceIntegrationTest {
    @Autowired
    private ProblemeService problemeService;
    @Autowired
    private ProblemeRepository problemeRepository;
    private Probleme probleme;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();

        Client clientA = new Client();
        clientA.setName("Dutoit");
        clientA.setEmail("a.dutoit@gmail.com");
        clientA = clientRepository.save(clientA);

        Valise val = new Valise();
        val.setClient(clientA);
        val.setNumeroValise(1234L);
        val = valiseRepository.save(val);

        probleme = Probleme.builder()
                .detailsProbleme("details du probleme de categorie A")
                .descriptionProbleme("decrire mon probleme")
                .client(clientA)
                .valise(val)
                .build();
    }



    @Test
    public void testCreateProbleme(){
        // Act
        Probleme savedPb = problemeService.createProbleme(probleme);

        // Assert
        assertNotNull(savedPb);
        assertNotNull(savedPb.getClient());
        assertNotNull(savedPb.getValise());
        assertEquals("details du probleme de categorie A", savedPb.getDetailsProbleme());
        assertEquals("decrire mon probleme", savedPb.getDescriptionProbleme());
    }

    @Test
    public void testUpdateProbleme(){
        // Arrange
        Probleme savedPb = problemeService.createProbleme(probleme);

        // Act
        savedPb.setDetailsProbleme("details du probleme standard");
        savedPb.setDescriptionProbleme("description du probleme de catégorie B1");
        Probleme updatedPb = problemeService.updateProbleme(savedPb.getId(), savedPb);

        // Assert
        assertNotNull(updatedPb);
        assertEquals(savedPb.getId(), updatedPb.getId());
        assertEquals("details du probleme standard", updatedPb.getDetailsProbleme());
        assertEquals("description du probleme de catégorie B1", updatedPb.getDescriptionProbleme());
    }
    @Test
    public void testDeleteProbleme() {
        // Arrange
        Probleme savedPb = problemeService.createProbleme(probleme);

        // Act
        problemeService.deleteProbleme(savedPb.getId());

        // Assert
        Probleme deletedPb = problemeService.getProblemeById(savedPb.getId());
        assertNull(deletedPb);
    }

    @Test
    public void testGetProblemeById() {
        // Arrange
        Probleme savedPb = problemeService.createProbleme(probleme);

        // Act
        Probleme pbById = problemeService.getProblemeById(savedPb.getId());

        // Assert
        assertNotNull(pbById);
        assertEquals(savedPb.getId(), pbById.getId());
        assertEquals("details du probleme de categorie A", pbById.getDetailsProbleme());
        assertEquals("decrire mon probleme", pbById.getDescriptionProbleme());
    }

    @Test
    public void testGetAllProblemes(){

        Client clientB = new Client();
        clientB.setName("duterte");
        clientB.setEmail("a.duterte@gmail.com");
        clientB = clientRepository.saveAndFlush(clientB);

        Valise valB = new Valise();
        valB.setClient(clientB);
        valB.setNumeroValise(1236L);
        valB = valiseRepository.saveAndFlush(valB);

        Probleme pb1 = Probleme.builder()
                .detailsProbleme("details du probleme standard")
                .descriptionProbleme("description standard")
                .client(clientB)
                .valise(valB)
                .build();
        problemeService.createProbleme(pb1);

        Probleme probleme = Probleme.builder()
                .detailsProbleme("autre detail du probleme")
                .descriptionProbleme("autre description")
                .client(clientB)
                .valise(valB)
                .build();
        problemeService.createProbleme(probleme);

        // Act
        List<Probleme> allProblemes = problemeService.getAllProblemes();

        // Assert
        assertNotNull(allProblemes);
        assertEquals(2, allProblemes.size());
    }

}
