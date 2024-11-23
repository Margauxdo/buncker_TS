package example.integration.repositories;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class MouvementRepositoryIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        livreurRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveMouvement_WithRelations() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Jean");
        livreur.setCodeLivreur("L123");
        livreur = livreurRepository.save(livreur);

        Valise valise = new Valise();
        valise.setNumeroValise(123L);
        valise = valiseRepository.save(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setLivreurs(Collections.singletonList(livreur));
        mouvement.setValise(valise);
        mouvement.setStatutSortie("En cours");
        mouvement.setDateHeureMouvement(new Date());

        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        assertNotNull(savedMouvement.getId());
        assertEquals(1, savedMouvement.getLivreurs().size());
        assertEquals(1, savedMouvement.getValise().getTypeValise());
        assertEquals("Jean", savedMouvement.getLivreurs().get(0).getNomLivreur());
        assertEquals(123L, savedMouvement.getValise().getNumeroValise());
    }

    @Test
    public void testFindByIdMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En transit");
        mouvement = mouvementRepository.save(mouvement);

        Optional<Mouvement> foundMouvement = mouvementRepository.findById(mouvement.getId());
        assertTrue(foundMouvement.isPresent());
        assertEquals("En transit", foundMouvement.get().getStatutSortie());
    }

    @Test
    public void testUpdateMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("Initial");
        mouvement.setDateHeureMouvement(new Date());
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        savedMouvement.setStatutSortie("Updated");
        Mouvement updatedMouvement = mouvementRepository.save(savedMouvement);

        assertNotNull(updatedMouvement);
        assertEquals("Updated", updatedMouvement.getStatutSortie());
    }

    @Test
    public void testDeleteMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setStatutSortie("En transit");
        mouvement = mouvementRepository.save(mouvement);

        mouvementRepository.deleteById(mouvement.getId());
        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(mouvement.getId());
        assertFalse(deletedMouvement.isPresent());
    }

    @Test
    public void testFindAllMouvements() {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("Sortie 1");
        mouvementRepository.save(mouvement1);

        Mouvement mouvement2 = new Mouvement();
        mouvement2.setStatutSortie("Sortie 2");
        mouvementRepository.save(mouvement2);

        List<Mouvement> mouvements = mouvementRepository.findAll();
        assertEquals(2, mouvements.size());
    }

    @Test
    public void testDeleteMouvement_WithRelations() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John");
        livreur.setCodeLivreur("L456");
        livreur = livreurRepository.save(livreur);

        Valise valise = new Valise();
        valise.setNumeroValise(456L);
        valise = valiseRepository.save(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setLivreurs(Collections.singletonList(livreur));
        mouvement.setValise(valise);
        mouvement.setStatutSortie("A supprimer");
        mouvement.setDateHeureMouvement(new Date());
        mouvement = mouvementRepository.save(mouvement);

        mouvementRepository.deleteById(mouvement.getId());

        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(mouvement.getId());
        assertFalse(deletedMouvement.isPresent());

        // Vérifie si les entités liées ne sont pas supprimées
        assertTrue(livreurRepository.findById(livreur.getId()).isPresent());
        assertTrue(valiseRepository.findById(valise.getId()).isPresent());
    }
}
