package example.integration.entity;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.RetourSecuriteRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MouvementIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        livreurRepository.deleteAll();
        retourSecuriteRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testCreateMouvement() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En cours");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());

        // Act
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Assert
        assertNotNull(savedMouvement.getId());
        assertEquals("En cours", savedMouvement.getStatutSortie());
    }

    @Test
    void testReadMouvement() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Terminé");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Act
        Optional<Mouvement> foundMouvement = mouvementRepository.findById(savedMouvement.getId());

        // Assert
        assertTrue(foundMouvement.isPresent());
        assertEquals("Terminé", foundMouvement.get().getStatutSortie());
    }

    @Test
    void testUpdateMouvement() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En attente");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Act
        savedMouvement.setStatutSortie("Livré");
        Mouvement updatedMouvement = mouvementRepository.saveAndFlush(savedMouvement);

        // Assert
        assertEquals("Livré", updatedMouvement.getStatutSortie());
    }

    @Test
    void testDeleteMouvement() {
        // Arrange
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Annulé");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Act
        mouvementRepository.delete(savedMouvement);
        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(savedMouvement.getId());

        // Assert
        assertTrue(deletedMouvement.isEmpty());
    }


    @Test
    void testMouvementWithLivreur() {
        // Arrange
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupont");
        Livreur savedLivreur = livreurRepository.saveAndFlush(livreur);

        Mouvement mouvement = new Mouvement();
        mouvement.setLivreur(savedLivreur);
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("En préparation");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Act
        Optional<Mouvement> foundMouvement = mouvementRepository.findById(savedMouvement.getId());

        // Assert
        assertTrue(foundMouvement.isPresent());
        assertEquals("Dupont", foundMouvement.get().getLivreur().getNomLivreur());
    }


}
