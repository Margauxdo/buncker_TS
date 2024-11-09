package example.entities;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MouvementTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    private Mouvement mouvement;
    private SimpleDateFormat sdf;

    @BeforeEach
    public void setUp() throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd");

        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setClient(client);
        valiseRepository.save(valise);

        Livreur livreur = new Livreur();
        livreur.setNomLivreur("Test Livreur");
        livreurRepository.save(livreur);

        mouvement = Mouvement.builder()
                .dateHeureMouvement(sdf.parse("2024-10-25"))
                .dateRetourPrevue(sdf.parse("2025-01-05"))
                .dateSortiePrevue(sdf.parse("2025-01-01"))
                .statutSortie("close")
                .valise(valise)
                .livreur(livreur)
                .build();

        em.persist(mouvement);
        em.flush();
    }

    @Test
    public void testMouvementPersistence() {
        Assertions.assertNotNull(mouvement.getId(), "Movement should have a non-null ID after persistence");
    }

    @Test
    public void testMouvementValiseAssociation() {
        Assertions.assertNotNull(mouvement.getValise(), "Movement should be associated with a Valise");
        Assertions.assertEquals(mouvement.getValise().getId(), valiseRepository.findAll().get(0).getId(), "Valise ID should match");
    }

    @Test
    public void testMouvementLivreurAssociation() {
        Assertions.assertNotNull(mouvement.getLivreur(), "Movement should be associated with a Livreur");
    }

    @Test
    public void testNonNullDateHeureMouvement() {
        Assertions.assertNotNull(mouvement.getDateHeureMouvement(), "date time movement should not be null");
    }

    @Test
    public void testUpdateMouvementStatutSortie() {
        mouvement.setStatutSortie("open");
        em.persist(mouvement);
        em.flush();
        Assertions.assertEquals("open", mouvement.getStatutSortie(), "Status Exit should be updated to 'open'");
    }
}
