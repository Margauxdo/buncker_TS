package example.entity;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class LivreurTest {

    @Autowired
    private EntityManager em;

    private Livreur livreur;

    @BeforeEach
    public void setUp() {
        livreur = Livreur.builder()
                .nomLivreur("Henry")
                .prenomLivreur("Martin")
                .codeLivreur("1234Henry")
                .motDePasse("root")
                .numeroCartePro("1234")
                .telephoneAlphapage("+3333333")
                .telephoneKobby("+0303030303")
                .telephonePortable("0625062506")
                .build();
    }

    @Test
    public void testLivreurPersistence() {
        em.persist(livreur);
        em.flush();
        assertNotNull(livreur.getId(), "The id should not be null after persistence");
    }

    @Test
    public void testLivreurAttributes() {
        em.persist(livreur);
        em.flush();
        Livreur retrievedLivreur = em.find(Livreur.class, livreur.getId());
        assertNotNull(retrievedLivreur, "Livreur should be retrieved from the database");
        assertEquals("Henry", retrievedLivreur.getNomLivreur());
        assertEquals("Martin", retrievedLivreur.getPrenomLivreur());
        assertEquals("1234Henry", retrievedLivreur.getCodeLivreur());
    }

    @Test
    public void testLivreurMouvementRelation() {
        Mouvement mouvement = new Mouvement();
        mouvement.setLivreurs((List<Livreur>) livreur);

        em.persist(mouvement);
        em.persist(livreur);
        em.flush();

        Livreur retrievedLivreur = em.find(Livreur.class, livreur.getId());
        assertNotNull(retrievedLivreur.getMouvement());
        assertEquals(mouvement.getId(), retrievedLivreur.getMouvement().getId());
    }


    @Test
    public void testLivreurAttribute(){
        em.persist(livreur);
        em.flush();
        Livreur retrievedLivreur = em.find(Livreur.class, livreur.getId());
        assertNotNull(retrievedLivreur, "The id must be retrieved from the database");
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
    public void testCascadeDeleteMouvements() {
        em.persist(livreur);
        em.flush();

        int livreurId = livreur.getId();
        int mouvement1Id = livreur.getMouvement().getLivreurs().size();
        int mouvement2Id = livreur.getMouvement().getLivreurs().size();

        em.remove(livreur);
        em.flush();

        Livreur deletedLivreur = em.find(Livreur.class, livreurId);
        Mouvement deletedMouvement1 = em.find(Mouvement.class, mouvement1Id);
        Mouvement deletedMouvement2 = em.find(Mouvement.class, mouvement2Id);

        assertNull(deletedLivreur, "The delivery man must be removed.");
        assertNull(deletedMouvement1, "Movement 1 must be deleted in cascade.");
        assertNull(deletedMouvement2, "Movement 2 must be deleted in cascade..");
    }





}
