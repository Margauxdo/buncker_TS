package example.entities;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class LivreurTest {

    @Autowired
    private EntityManager em;

    private Livreur livreur;
    @BeforeEach
    public void setUp() {
        livreur = new Livreur();
        livreur.setNomLivreur("Henry");
        livreur.setPrenomLivreur("Martin");
        livreur.setCodeLivreur("1234Henry");
        livreur.setMotDePasse("root");
        livreur.setNumeroCartePro("1234");
        livreur.setTelephoneAlphapage("+3333333");
        livreur.setTelephoneKobby("+0303030303");
        livreur.setTelephonePortable("0625062506");

        Mouvement mouvement1 = new Mouvement();
        Mouvement mouvement2 = new Mouvement();
        List<Mouvement> mouvements = new ArrayList<>();
        mouvements.add(mouvement1);
        mouvements.add(mouvement2);
        livreur.setMouvements(mouvements);
    }
    @Test
    public void testLivreurPersistence(){
        em.persist(livreur);
        em.flush();
        assertNotNull(livreur.getId(), "L'id n'existe pas");


    }
    @Test
    public void testLivreurAttribute(){
        em.persist(livreur);
        em.flush();
        Livreur retrievedLivreur = em.find(Livreur.class, livreur.getId());
        assertNotNull(retrievedLivreur, "L'id doit être récupéré depuis la base de donnée");
        assertEquals("Henry", retrievedLivreur.getNomLivreur());
        assertEquals("Martin", retrievedLivreur.getPrenomLivreur());
        assertEquals("1234Henry", retrievedLivreur.getCodeLivreur());
        assertEquals("root", retrievedLivreur.getMotDePasse());
        assertEquals("1234", retrievedLivreur.getNumeroCartePro());
        assertEquals("+3333333", retrievedLivreur.getTelephoneAlphapage());
        assertEquals("+0303030303", retrievedLivreur.getTelephoneKobby());
        assertEquals("0625062506", retrievedLivreur.getTelephonePortable());
    }

    @Test
    public void testLivreurMouvementsRelation() {
        // Persist le livreur et ses mouvements
        em.persist(livreur);
        em.flush();

        // Récupère le livreur depuis la base de données
        Livreur retrievedLivreur = em.find(Livreur.class, livreur.getId());

        // Vérifie que la liste des mouvements est bien enregistrée
        assertNotNull(retrievedLivreur.getMouvements());
        assertEquals(2, retrievedLivreur.getMouvements().size());
    }
    @Test
    public void testCascadeDeleteMouvements(){

    }
    @Test
    public void testNonNullCodeLivreur(){

    }


}
