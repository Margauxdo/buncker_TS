package example.entities;

import example.repositories.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        valise.setTypevalise(type1);
        valise.setDateCreation(sdf.parse("2016-02-20"));
        valise.setDateDernierMouvement(sdf.parse("2024-02-20"));
        valise.setDateRetourPrevue(sdf.parse("2024-09-02"));
        valise.setDateSortiePrevue(sdf.parse("2024-10-02"));
        valise.setMouvementList(mouvementList);
        valise.setNumeroDujeu("numero du jeu");
        valise.setRefClient("ref client");
        valise.setRegleSortie(regle1);
        valise.setSortie(sdf.parse("2016-02-20"));

        em.persist(valise);
        em.flush();
    }

    @Test
    public void testValisePersistence() {
        Assertions.assertNotNull(valise.getId(), "Suitcase ID must not be null after persistence");
    }

    @Test
    public void testValiseDescription() {
        Assertions.assertEquals(valise.getDescription(), "la valise a pour numero 1234L");
    }

    @Test
    public void testValiseNumeroValise() {
        Assertions.assertEquals(valise.getNumeroValise(), 1234L);
    }
    @Test
    public void testValiseRefClient() {
        Assertions.assertEquals(valise.getRefClient(), "ref client");
    }
    @Test
    public void testValiseSortie() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2016-02-20");

        Assertions.assertNotNull(valise.getSortie(), "Release date must not be zero");
        Assertions.assertEquals(expectedDate, valise.getSortie(), "The release date must match the initial value");
    }

    @Test
    public void testValiseDateDernierMouvement () throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-02-20");
        Assertions.assertNotNull(valise.getDateDernierMouvement(),"Last movement date must not be null");
        Assertions.assertEquals(expectedDate,valise.getDateDernierMouvement(),"The last movement date must match the initial value");
    }
    @Test
    public void testValiseDateSortiePrevue() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-10-02");
        Assertions.assertNotNull(valise.getDateSortiePrevue(),"The expected release date must not be null");
        Assertions.assertEquals( expectedDate, valise.getDateSortiePrevue(),"The expected release date must match the initial value");
    }
    @Test
    public void testValiseDateRetourPrevue() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-09-02");
        Assertions.assertNotNull(valise.getDateRetourPrevue(),"The expected return date must not be null");
        Assertions.assertEquals( expectedDate, valise.getDateRetourPrevue(),"The expected return date must match the initial value");

    }
    @Test
    public void testValiseDateCreation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2016-02-20");
        Assertions.assertNotNull(valise.getDateCreation(),"Creation date must not be null");
        Assertions.assertEquals( expectedDate, valise.getDateCreation(),"The creation date must match the initial value");

    }
    @Test
    public void testValiseClientAssociation() {
        Assertions.assertNotNull(valise.getClient());
        Client expectedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals(expectedClient.getId(), valise.getClient().getId());
    }
    @Test
    public void testValiseClientNull() {
        Valise valiseWithoutClient = new Valise();
        valiseWithoutClient.setDescription("Suitcase without customer");
        valiseWithoutClient.setTypevalise(valise.getTypevalise());
        valiseWithoutClient.setRegleSortie(valise.getRegleSortie());

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(valiseWithoutClient);
            em.flush();
        }, "Persistence should fail if the suitcase client is null");
    }


    @Test
    public void testUpdateClientAssociation() {
        Client newClient = new Client();
        newClient.setName("Smith");
        newClient.setEmail("smith@example.com");
        newClient.setAdresse("rue de paris 75000 Paris");
        em.persist(newClient);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals("Doe", foundValise.getClient().getName(), "The initial client should be 'Doe'");

        foundValise.setClient(newClient);
        em.merge(foundValise);
        em.flush();

        Valise updatedValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals("Smith", updatedValise.getClient().getName(), "The suitcase should be associated with the new client 'Smith'");

        Client oldClient = em.find(Client.class, clientRepository.findAll().get(0).getId());
        Assertions.assertFalse(oldClient.getValises().contains(updatedValise), "The old customer should no longer have this suitcase associated");
    }

    @Test
    public void testValiseRegleAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getRegleSortie(), "The suitcase must be associated with a rule");
    }

    @Test
    public void testValiseRegleNull() {
        Valise valiseWithoutRegle = new Valise();
        valiseWithoutRegle.setClient(valise.getClient());
        valiseWithoutRegle.setTypevalise(valise.getTypevalise());
        valiseWithoutRegle.setDescription("Suitcase without ruler");

        em.persist(valiseWithoutRegle);
        em.flush();

        Valise foundValise = em.find(Valise.class, valiseWithoutRegle.getId());
        Assertions.assertNull(foundValise.getRegleSortie(), "The suitcase rule should be null");
    }


    @Test
    public void testUpdateRegleAssociation() {
        Regle newRegle = new Regle();
        newRegle.setCoderegle("NEWCODE123");
        em.persist(newRegle);
        em.flush();

        valise.setRegleSortie(newRegle);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(newRegle.getId(), foundValise.getRegleSortie().getId(), "The associated rule should be updated.");
    }


    @Test
    public void testValiseTypeValiseAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getTypevalise(), "The suitcase must be associated with a suitcase type.");
    }

    @Test
    public void testValiseTypeValiseNull() {
        Valise valiseWithoutTypeValise = new Valise();
        valiseWithoutTypeValise.setClient(valise.getClient());
        valiseWithoutTypeValise.setDescription("Suitcase without suitcase type");

        em.persist(valiseWithoutTypeValise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valiseWithoutTypeValise.getId());
        Assertions.assertNull(foundValise.getTypevalise(), "The suitcase type of the suitcase should be zero.");
    }




    @Test
    public void testUpdateTypeValiseAssociation() {
        TypeValise newTypeValise = new TypeValise();
        newTypeValise.setProprietaire("New Owner");
        newTypeValise.setDescription("New Type of Suitcase");
        em.persist(newTypeValise);
        em.flush();

        valise.setTypevalise(newTypeValise);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(newTypeValise.getId(), foundValise.getTypevalise().getId(), "The associated suitcase type should be updated.");
    }


    @Test
    public void testValiseMouvementListAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getMouvementList(), "The suitcase movement list should not be zero.");
        Assertions.assertEquals(2, foundValise.getMouvementList().size(), "The suitcase should be associated with two movements.");
    }

    @Test
    public void testCascadePersistMouvementList() {
        Mouvement newMouvement = new Mouvement();
        valise.getMouvementList().add(newMouvement);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(3, foundValise.getMouvementList().size(), "The movement list should contain three items.");
    }

    @Test
    public void testCascadeDeleteMouvementList() {
        em.createQuery("DELETE FROM Probleme p WHERE p.valise.id = :valiseId")
                .setParameter("valiseId", valise.getId())
                .executeUpdate();

        em.remove(valise);
        em.flush();

        List<Mouvement> remainingMouvements = em.createQuery("SELECT m FROM Mouvement m", Mouvement.class).getResultList();
        Assertions.assertTrue(remainingMouvements.isEmpty(), "All associated movements should be deleted in cascade.");
    }



    @Test
    public void testUpdateMouvementListAssociation() {
        Mouvement newMouvement = new Mouvement();
        em.persist(newMouvement);
        valise.getMouvementList().clear();
        valise.getMouvementList().add(newMouvement);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(1, foundValise.getMouvementList().size(), "The list of movements should be updated.");
        Assertions.assertEquals(newMouvement.getId(), foundValise.getMouvementList().get(0).getId(), "The associated movement should be updated.");
    }


}
