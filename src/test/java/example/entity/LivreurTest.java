package example.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
    @DataJpaTest
    @ActiveProfiles("test")
    public class LivreurTest {

        @Autowired
        private EntityManager em;

        private Livreur livreur;
        private Mouvement mouvement;

        @BeforeEach
        public void setUp() {
            // Créer un Mouvement
            mouvement = Mouvement.builder()
                    .dateHeureMouvement(new Date())
                    .dateRetourPrevue(new Date())
                    .dateSortiePrevue(new Date())
                    .build();

            // Persist le Mouvement avant d'associer un Livreur
            em.persist(mouvement);
            em.flush(); // Assure-toi que le Mouvement est bien persisté avant de l'utiliser

            // Créer un Livreur et l'associer à Mouvement
            livreur = Livreur.builder()
                    .nomLivreur("Henry")
                    .prenomLivreur("Martin")
                    .codeLivreur("1234Henry")
                    .motDePasse("root")
                    .numeroCartePro("1234")
                    .telephoneAlphapage("+3333333")
                    .telephoneKobby("+0303030303")
                    .telephonePortable("0625062506")
                    .mouvement(mouvement) // Associe le Mouvement
                    .build();
        }

        @Test
        public void testCreateLivreur() {
            em.persist(livreur);
            em.flush();

            Livreur foundLivreur = em.find(Livreur.class, livreur.getId());

            assertNotNull(foundLivreur);
            assertEquals("Henry", foundLivreur.getNomLivreur());
            assertNotNull(foundLivreur.getMouvement(), "Le mouvement associé ne devrait pas être nul");
        }

        @Test
        public void testUpdateLivreur() {
            em.persist(livreur);
            em.flush();

            Livreur foundLivreur = em.find(Livreur.class, livreur.getId());
            foundLivreur.setNomLivreur("UpdatedName");

            em.merge(foundLivreur);
            em.flush();

            Livreur updatedLivreur = em.find(Livreur.class, livreur.getId());
            assertEquals("UpdatedName", updatedLivreur.getNomLivreur(), "Le nom du livreur devrait être mis à jour");
        }

        @Test
        public void testDeleteLivreur() {
            em.persist(livreur);
            em.flush();

            Livreur foundLivreur = em.find(Livreur.class, livreur.getId());
            em.remove(foundLivreur);
            em.flush();

            Livreur deletedLivreur = em.find(Livreur.class, livreur.getId());
            assertNull(deletedLivreur, "Le livreur devrait avoir été supprimé");
        }

        @Test
        public void testLivreurWithMouvement() {
            em.persist(livreur);
            em.flush();

            Livreur foundLivreur = em.find(Livreur.class, livreur.getId());
            assertNotNull(foundLivreur.getMouvement(), "Le mouvement associé ne devrait pas être nul");
            assertEquals(mouvement.getId(), foundLivreur.getMouvement().getId(), "L'ID du mouvement associé devrait correspondre");
        }
    }
