package example.integration.repositories;

import example.entity.Livreur;
import example.entity.Mouvement;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class LivreurRepositoryIntegrationTest {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        livreurRepository.deleteAll();
    }

    @Test
    public void testSaveLivreurSuccess() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("John");
        livreur.setPrenomLivreur("Doe");
        livreur.setCodeLivreur("LIV123");

        Livreur savedLivreur = livreurRepository.save(livreur);

        assertNotNull(savedLivreur.getId());
        assertEquals("John", savedLivreur.getNomLivreur());
        assertEquals("Doe", savedLivreur.getPrenomLivreur());
        assertEquals("LIV123", savedLivreur.getCodeLivreur());
    }

    @Test
    public void testFindLivreurByCodeSuccess() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Jane");
        livreur.setPrenomLivreur("Doe");
        livreur.setCodeLivreur("LIV124");

        livreurRepository.save(livreur);

        Livreur foundLivreur = livreurRepository.findByCodeLivreur("LIV124");
        assertNotNull(foundLivreur);
        assertEquals("Jane", foundLivreur.getNomLivreur());
    }



    @Test
    public void testDeleteLivreur() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Emily");
        livreur.setPrenomLivreur("Clark");
        livreur.setCodeLivreur("LIV126");

        Livreur savedLivreur = livreurRepository.save(livreur);
        livreurRepository.deleteById(savedLivreur.getId());

        Optional<Livreur> deletedLivreur = livreurRepository.findById(savedLivreur.getId());
        assertFalse(deletedLivreur.isPresent());
    }

    @Test
    public void testFindLivreursByMouvementStatuses() {
        Livreur livreur1 = new Livreur();
        livreur1.setNomLivreur("Oliver");
        livreur1.setPrenomLivreur("Brown");
        livreur1.setCodeLivreur("LIV127");
        livreurRepository.save(livreur1);

        Mouvement mouvement1 = new Mouvement();
        mouvement1.setLivreur(livreur1);
        mouvement1.setStatutSortie("Delivered");
        mouvementRepository.save(mouvement1);

        Livreur livreur2 = new Livreur();
        livreur2.setNomLivreur("Sophia");
        livreur2.setPrenomLivreur("Johnson");
        livreur2.setCodeLivreur("LIV128");
        livreurRepository.save(livreur2);

        Mouvement mouvement2 = new Mouvement();
        mouvement2.setLivreur(livreur2);
        mouvement2.setStatutSortie("In Transit");
        mouvementRepository.save(mouvement2);

        List<Livreur> foundLivreurs = livreurRepository.findLivreursByMouvementStatuses(List.of("Delivered"));

        assertEquals(1, foundLivreurs.size());
        assertEquals("Oliver", foundLivreurs.get(0).getNomLivreur());
    }

    @Test
    public void testExistsByCodeLivreur() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Alice");
        livreur.setPrenomLivreur("Taylor");
        livreur.setCodeLivreur("LIV129");
        livreurRepository.save(livreur);

        boolean exists = livreurRepository.existsByCodeLivreur("LIV129");
        assertTrue(exists);
    }
}
