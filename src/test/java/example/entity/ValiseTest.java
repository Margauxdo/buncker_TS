package example.entity;

import example.repositories.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ValiseTest {

    @Autowired
    private EntityManager em;

    private Valise valise;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Client client1 = new Client();
        client1.setName("Doe");
        client1.setEmail("doe@example.com");
        em.persist(client1);

        TypeValise type1 = new TypeValise();
        type1.setProprietaire("ClientA");
        type1.setDescription("La valise appartient au Client A");
        em.persist(type1);

        Regle regle1 = new Regle();
        regle1.setCoderegle("CODE123");
        em.persist(regle1);

        Mouvement mouvement1 = new Mouvement();
        Mouvement mouvement2 = new Mouvement();
        List<Mouvement> mouvementList = new ArrayList<>();
        mouvementList.add(mouvement1);
        mouvementList.add(mouvement2);
        em.persist(mouvement1);
        em.persist(mouvement2);

        valise = new Valise();
        valise.setNumeroValise(1234L);
        valise.setDescription("la valise a pour numero 1234L");
        valise.setClient(client1);
        valise.setTypeValise(type1);  // Ajout d'un seul TypeValise à la Valise
        valise.setDateCreation(sdf.parse("2016-02-20"));
        valise.setDateDernierMouvement(sdf.parse("2024-02-20"));
        valise.setDateRetourPrevue(sdf.parse("2024-09-02"));
        valise.setDateSortiePrevue(sdf.parse("2024-10-02"));
        valise.setMouvementList(mouvementList);
        valise.setNumeroDujeu("numero du jeu");
        valise.setRefClient("ref client");
        valise.setRegleSortie(new ArrayList<>());
        valise.getRegleSortie().add(regle1);
        valise.setSortie(sdf.parse("2016-02-20"));

        em.persist(valise);
        em.flush();
    }

    @Test
    public void testValiseTypeValiseAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getTypeValise(), "La valise doit être associée à un type de valise.");
        Assertions.assertEquals("ClientA", foundValise.getTypeValise().getProprietaire(), "Le type de la valise doit être associé à ClientA.");
    }
}
