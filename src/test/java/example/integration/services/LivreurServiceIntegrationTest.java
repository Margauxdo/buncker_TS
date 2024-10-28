package example.integration.services;

import example.entities.Livreur;
import example.repositories.LivreurRepository;
import example.services.LivreurService;
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
public class LivreurServiceIntegrationTest {
    @Autowired
    private LivreurService livreurService;
    @Autowired
    private LivreurRepository livreurRepository;
    private Livreur livreur;
    @BeforeEach
    public void setUp() {
        livreurRepository.deleteAll();
        livreur = Livreur.builder()
                .codeLivreur("12345A")
                .prenomLivreur("Jean")
                .nomLivreur("Bernard")
                .telephoneKobby("0789603526")
                .telephonePortable("0652458785")
                .telephoneAlphapage("0963562154")
                .numeroCartePro("1456932")
                .build();
    }
    @Test
    public void testCreateLivreur() {
        //Act
        Livreur savedLivreur = livreurService.createLivreur(livreur);

        // Assert
        assertNotNull(savedLivreur);
        assertNotNull(savedLivreur.getId());
        assertEquals("12345A", savedLivreur.getCodeLivreur());
        assertEquals("Jean", savedLivreur.getPrenomLivreur());
        assertEquals("Bernard", savedLivreur.getNomLivreur());
        assertEquals("0789603526", savedLivreur.getTelephoneKobby());
        assertEquals("0652458785", savedLivreur.getTelephonePortable());
        assertEquals("0963562154", savedLivreur.getTelephoneAlphapage());
        assertEquals("1456932", savedLivreur.getNumeroCartePro());
    }
    @Test
    public void testUpdateLivreur() {
        // Arrange
        Livreur savedLivreur = livreurService.createLivreur(livreur);

        //Act
        savedLivreur.setCodeLivreur("12346A1");
        savedLivreur.setPrenomLivreur("Quentin");
        savedLivreur.setNomLivreur("Dutois");
        savedLivreur.setTelephoneKobby("0789603527");
        savedLivreur.setTelephonePortable("0652458788");
        savedLivreur.setTelephoneAlphapage("0963562254");
        savedLivreur.setNumeroCartePro("1456933");
        Livreur updatedLivreur = livreurService.updateLivreur(savedLivreur.getId(), savedLivreur);

        // Assert
        assertNotNull(updatedLivreur);
        assertEquals("12346A1", updatedLivreur.getCodeLivreur());
        assertEquals("Quentin", updatedLivreur.getPrenomLivreur());
        assertEquals("Dutois", updatedLivreur.getNomLivreur());
        assertEquals("0789603527", updatedLivreur.getTelephoneKobby());
        assertEquals("0652458788", updatedLivreur.getTelephonePortable());
        assertEquals("0963562254", updatedLivreur.getTelephoneAlphapage());
        assertEquals("1456933", updatedLivreur.getNumeroCartePro());

    }
    @Test
    public void testDeleteLivreur() {
        // Arrange
        Livreur savedLivreur = livreurService.createLivreur(livreur);

        //Act
        livreurService.deleteLivreur(savedLivreur.getId());

        // Assert
        Livreur deletedLivreur = livreurService.getLivreurById(savedLivreur.getId());
        assertNull(deletedLivreur);
    }
    @Test
    public void testGetLivreurById() {
        // Arrange
        Livreur savedLivreur = livreurService.createLivreur(livreur);

        // Act
        Livreur foundLivreur = livreurService.getLivreurById(savedLivreur.getId());

        //Assert
        assertNotNull(foundLivreur);
        assertEquals(savedLivreur.getId(), foundLivreur.getId());
        assertEquals("12345A", foundLivreur.getCodeLivreur());
        assertEquals("Jean", foundLivreur.getPrenomLivreur());
        assertEquals("Bernard", foundLivreur.getNomLivreur());

    }
    @Test
    public void testGetAllLivreurs() {
        //Arrange
        Livreur livreur2 = Livreur.builder()
                .codeLivreur("125689")
                .prenomLivreur("Louis")
                .nomLivreur("Decroix")
                .telephoneKobby("0785693654")
                .telephonePortable("0654256985")
                .telephoneAlphapage("0911254587")
                .numeroCartePro("1451658")
                .build();

        livreurService.createLivreur(livreur);
        livreurService.createLivreur(livreur2);

        //Act
        List<Livreur> livreurs = livreurService.getAllLivreurs();

        //Assert
        assertNotNull(livreurs);
        assertEquals(2, livreurs.size());
    }
}
