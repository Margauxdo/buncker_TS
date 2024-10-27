package example.integration.repositories;
import example.entities.Livreur;
import example.repositories.LivreurRepository;
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
    @BeforeEach
    public void setUp() {
        livreurRepository.deleteAll();
    }
    @Test
    public void testSaveLivreurSuccess(){
        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Harry");
        livreur.setPrenomLivreur("Jean");
        livreur.setNumeroCartePro("carte 1234");
        livreur.setCodeLivreur("code 456");
        Livreur savedLivreur = livreurRepository.save(livreur);

        assertNotNull(savedLivreur.getId());
        assertEquals("Harry", savedLivreur.getNomLivreur());
        assertEquals("Jean", savedLivreur.getPrenomLivreur());
        assertEquals("carte 1234", savedLivreur.getNumeroCartePro());
        assertEquals("code 456", savedLivreur.getCodeLivreur());
    }
    @Test
    public void testFindByIdSuccess(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        livreurRepository.save(livreurA);

        Optional<Livreur> foundL = livreurRepository.findById(livreurA.getId());
        assertTrue(foundL.isPresent());
        assertEquals("Harry", foundL.get().getNomLivreur());
        assertEquals("Jean", foundL.get().getPrenomLivreur());
        assertEquals("carte 1234", foundL.get().getNumeroCartePro());
        assertEquals("code 456", foundL.get().getCodeLivreur());

    }
    @Test
    public void testFindbyNomLivreurSuccess() {
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        livreurRepository.save(livreurA);

        Livreur livreurB = new Livreur();
        livreurB.setNomLivreur("Bernard");
        livreurB.setPrenomLivreur("Louis");
        livreurB.setNumeroCartePro("carte 4987");
        livreurRepository.save(livreurB);

        List<Livreur> livreurs = livreurRepository.findByNomLivreur("Harry");

        assertEquals(1, livreurs.size());

         assertEquals("Harry", livreurs.get(0).getNomLivreur());  // Corrigé ici
    }

    @Test
    public void testFindByCarteProLivreur(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        livreurRepository.save(livreurA);

        Livreur livreurB = new Livreur();
        livreurB.setNomLivreur("Bernard");
        livreurB.setPrenomLivreur("Louis");
        livreurB.setNumeroCartePro("carte 4987");
        livreurRepository.save(livreurB);

        List<Livreur> livreurs = livreurRepository.findByNumeroCartePro("carte 4987");
        assertEquals(1, livreurs.size());
        assertEquals("carte 4987", livreurs.get(0).getNumeroCartePro());
    }

    @Test
    public void testFindByCodeLivreur(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        livreurRepository.save(livreurA);

        Livreur livreurB = new Livreur();
        livreurB.setNomLivreur("Bernard");
        livreurB.setPrenomLivreur("Louis");
        livreurB.setNumeroCartePro("carte 4987");
        livreurB.setCodeLivreur("code 789");
        livreurRepository.save(livreurB);

        Livreur livreur = livreurRepository.findByCodeLivreur("code 456");

        assertNotNull(livreur);
        assertEquals("code 456", livreur.getCodeLivreur());
        assertEquals("Harry", livreur.getNomLivreur());
    }


    @Test
    public void testDeleteLivreurSuccess(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        Livreur savedLivreur = livreurRepository.save(livreurA);
         livreurRepository.deleteById(livreurA.getId());
         Optional<Livreur> foundL = livreurRepository.findById(livreurA.getId());
         assertFalse(foundL.isPresent());
    }
    @Test
    public void testUpdateLivreurSuccess(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurA.setNumeroCartePro("carte 1234");
        livreurA.setCodeLivreur("code 456");
        Livreur savedLivreur = livreurRepository.save(livreurA);

        savedLivreur.setNomLivreur("Leon");
        savedLivreur.setPrenomLivreur("Arnauld");
        savedLivreur.setNumeroCartePro("carte 5678");
        savedLivreur.setCodeLivreur("code 789");
        livreurRepository.save(savedLivreur);

        Optional<Livreur> foundL = livreurRepository.findById(savedLivreur.getId());
        assertTrue(foundL.isPresent());
        assertEquals("Leon", foundL.get().getNomLivreur());
        assertEquals("Arnauld", foundL.get().getPrenomLivreur());
        assertEquals("carte 5678", foundL.get().getNumeroCartePro());
        assertEquals("code 789", foundL.get().getCodeLivreur());
    }


    @Test
    public void findAllLivreursSuccess(){
        Livreur livreurA = new Livreur();
        livreurA.setNomLivreur("Harry");
        livreurA.setPrenomLivreur("Jean");
        livreurRepository.save(livreurA);

        Livreur livreurB = new Livreur();
        livreurB.setNomLivreur("Bernard");
        livreurB.setPrenomLivreur("Louis");
        livreurRepository.save(livreurB);

        List<Livreur> livreurs = livreurRepository.findAll();
        assertEquals(2, livreurs.size());

        assertEquals("Harry", livreurs.get(0).getNomLivreur());
        assertEquals("Bernard", livreurs.get(1).getNomLivreur());
    }
    @Test
    public void testFindByCodeLivreurNotFound() {
        Livreur livreur = livreurRepository.findByCodeLivreur("nonexistent_code");

        assertNull(livreur);
    }
    @Test
    public void testDeleteNonExistingLivreur() {
        livreurRepository.deleteById(999);

        assertEquals(0, livreurRepository.count());
    }






}
