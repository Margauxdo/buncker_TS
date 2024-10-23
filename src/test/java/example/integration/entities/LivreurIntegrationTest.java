package example.integration.entities;

import example.entities.Livreur;
import example.entities.Mouvement;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class LivreurIntegrationTest {

    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    public void setUp() {
        livreurRepository.deleteAll();
        mouvementRepository.deleteAll();
    }

    @Test
    public void testSaveLivreur() {
        Livreur livreur1 = new Livreur();
        livreur1.setNomLivreur("Dupond");
        livreur1.setCodeLivreur("1234");
        livreur1.setNumeroCartePro("123456");
        livreur1.setPrenomLivreur("jean");
        livreur1.setMotDePasse("1234");

        Mouvement mouvement1 = new Mouvement();
        mouvement1.setLivreur(livreur1);


        Livreur savedLivreur = livreurRepository.save(livreur1);

        Assertions.assertNotNull(savedLivreur.getId());
        Assertions.assertEquals("Dupond", savedLivreur.getNomLivreur());
        Assertions.assertEquals("1234", savedLivreur.getCodeLivreur());
        Assertions.assertEquals("123456", savedLivreur.getNumeroCartePro());
        Assertions.assertEquals("jean", savedLivreur.getPrenomLivreur());
        Assertions.assertEquals("1234", savedLivreur.getMotDePasse());
    }
    @Test
    public void testFindLivreurById(){
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupond");
        livreur.setCodeLivreur("1234");
        livreur.setNumeroCartePro("123456");
        livreur.setPrenomLivreur("jean");

        Livreur savedLivreur = livreurRepository.save(livreur);
        Livreur foundLivreur = livreurRepository.findById(savedLivreur.getId()).orElse(null);

        Assertions.assertNotNull(foundLivreur);
        Assertions.assertEquals("Dupond", foundLivreur.getNomLivreur());
        Assertions.assertEquals("1234", foundLivreur.getCodeLivreur());
        Assertions.assertEquals("123456", foundLivreur.getNumeroCartePro());
        Assertions.assertEquals("jean", foundLivreur.getPrenomLivreur());

    }
    @Test
    public void testFindLivreurByCodeLivreur(){
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupond");
        livreur.setCodeLivreur("1234");
        livreur.setNumeroCartePro("123456");
        livreur.setPrenomLivreur("jean");

        Livreur savedLivreur = livreurRepository.save(livreur);
        Livreur foundLivreurByCode = livreurRepository.findByCodeLivreur(savedLivreur.getCodeLivreur());

        Assertions.assertNotNull(foundLivreurByCode);
        Assertions.assertEquals("Dupond", foundLivreurByCode.getNomLivreur());
        Assertions.assertEquals("1234", foundLivreurByCode.getCodeLivreur());
        Assertions.assertEquals("123456", foundLivreurByCode.getNumeroCartePro());
        Assertions.assertEquals("jean", foundLivreurByCode.getPrenomLivreur());

    }
    @Test
    public void testFindAllLivreur(){
        Livreur livA = new Livreur();
        livA.setNomLivreur("Dupond");
        livA.setCodeLivreur("1234");
        livA.setNumeroCartePro("123456");
        livA.setPrenomLivreur("jean");
        livreurRepository.save(livA);
        Livreur livB = new Livreur();
        livB.setNomLivreur("Leon");
        livB.setCodeLivreur("4321");
        livB.setNumeroCartePro("134625");
        livB.setPrenomLivreur("Jules");
        livreurRepository.save(livB);

        List<Livreur> livreurs = livreurRepository.findAll();
        Assertions.assertEquals(2, livreurs.size());
        Assertions.assertTrue(livreurs.stream().anyMatch(livreur -> livreur.getNomLivreur().equals("Dupond")) );
        Assertions.assertTrue(livreurs.stream().anyMatch(livreur -> livreur.getNomLivreur().equals("Leon")) );
    }

    @Test
    public void testSaveLivreurWithNullNameThrowsException() {
        Livreur livreur = new Livreur();
        livreur.setCodeLivreur("1234");
        livreur.setNumeroCartePro("123456");
        livreur.setPrenomLivreur("jean");
        // Le nom n'est pas défini, ce qui provoque l'exception

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            livreurRepository.save(livreur);
        }, "Expected DataIntegrityViolationException due to null 'nomLivreur'");
    }

    @Test
    public void testUpdateLivreur() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupond");
        livreur.setCodeLivreur("1234");
        livreur.setNumeroCartePro("123456");
        livreur.setPrenomLivreur("jean");

        Livreur savedLivreur = livreurRepository.save(livreur);

        savedLivreur.setNomLivreur("henry");
        Livreur updatedLivreur = livreurRepository.save(savedLivreur);

        Assertions.assertEquals("henry", updatedLivreur.getNomLivreur(), "The delivery person's name should be updated to 'henry'");
    }

    @Test
    public void testCascadeDeleteLivreurDeleteMouvement() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupond");
        livreur.setCodeLivreur("1234");

        Mouvement mouvement1 = new Mouvement();
        mouvement1.setLivreur(livreur);

        livreur.setMouvements(new ArrayList<>());
        livreur.getMouvements().add(mouvement1);

        livreurRepository.saveAndFlush(livreur);

        livreurRepository.delete(livreur);

        Assertions.assertFalse(livreurRepository.findById(livreur.getId()).isPresent(), "The delivery person must be deleted");
        Assertions.assertFalse(mouvementRepository.findById(mouvement1.getId()).isPresent(), "The movement must be deleted in cascade");
    }




    @Test
    public void testDeleteLivreur() {
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Dupond");
        livreur.setCodeLivreur("1234");
        livreur.setNumeroCartePro("123456");
        livreur.setPrenomLivreur("jean");

        Livreur savedLivreur = livreurRepository.save(livreur);
        livreurRepository.deleteById(savedLivreur.getId());
        Assertions.assertFalse(livreurRepository.findById(savedLivreur.getId()).isPresent());
    }

}
