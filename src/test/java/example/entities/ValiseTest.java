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

        // Créer et persister un client pour la valise
        Client client1 = new Client();
        client1.setName("Doe");
        client1.setEmail("doe@example.com");
        em.persist(client1);

        // Initialiser TypeValise avec une chaîne pour le propriétaire
        TypeValise type1 = new TypeValise();
        type1.setProprietaire("Créateur de la règle");
        type1.setDescription("Description du type de valise");
        em.persist(type1);

        // Créer et persister une règle pour la valise avec un coderegle non nul
        Regle regle1 = new Regle();
        regle1.setCoderegle("CODE123");  // Initialisation de 'coderegle'
        em.persist(regle1);

        // Créer et persister une liste de mouvements pour la valise
        Mouvement mouvement1 = new Mouvement();
        Mouvement mouvement2 = new Mouvement();
        List<Mouvement> mouvementList = new ArrayList<>();
        mouvementList.add(mouvement1);
        mouvementList.add(mouvement2);
        em.persist(mouvement1);
        em.persist(mouvement2);

        // Initialiser et persister l'objet valise avec tous les champs nécessaires
        valise = new Valise();
        valise.setNumeroValise(1234L);
        valise.setDescription("description de la valise");
        valise.setClient(client1);
        valise.setTypevalise(type1);
        valise.setDateCreation(sdf.parse("2016-02-20"));
        valise.setDateDernierMouvement(sdf.parse("2024-02-20"));
        valise.setDateRetourPrevue(sdf.parse("2024-09-02"));
        valise.setDateSortiePrevue(sdf.parse("2024-10-02"));
        valise.setMouvementList(mouvementList);
        valise.setNumeroDujeu("numero du jeu");
        valise.setRefClient("ref client");
        valise.setRegleSortie(regle1);  // Associe la règle avec la valise
        valise.setSortie(sdf.parse("2016-02-20"));

        em.persist(valise);
        em.flush();
    }

    @Test
    public void testValisePersistence() {
        // Vérifie que l'objet valise a bien été persisté et a un ID non nul
        Assertions.assertNotNull(valise.getId(), "L'ID de la valise ne doit pas être nul après la persistance");
    }

    @Test
    public void testValiseDescription() {
        Assertions.assertEquals(valise.getDescription(), "description de la valise");
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
        // Format de date utilisé dans setUp()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2016-02-20");

        Assertions.assertNotNull(valise.getSortie(), "La date de sortie ne doit pas être nulle");
        Assertions.assertEquals(expectedDate, valise.getSortie(), "La date de sortie doit correspondre à la valeur initiale");
    }

    @Test
    public void testValiseDateDernierMouvement () throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-02-20");
        Assertions.assertNotNull(valise.getDateDernierMouvement(),"La date de dernier mouvement ne doit pas etre null");
        Assertions.assertEquals(expectedDate,valise.getDateDernierMouvement(),"La date de dernier mouvement doit correspondre à la valeure initiale");
    }
    @Test
    public void testValiseDateSortiePrevue() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-10-02");
        Assertions.assertNotNull(valise.getDateSortiePrevue(),"La date de sortie prevue ne doit pas être null");
        Assertions.assertEquals( expectedDate, valise.getDateSortiePrevue(),"La date de sortie prévue doit correspondre à la valeure initiale");
    }
    @Test
    public void testValiseDateRetourPrevue() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2024-09-02");
        Assertions.assertNotNull(valise.getDateRetourPrevue(),"La date de retour prevue ne doit pas être null");
        Assertions.assertEquals( expectedDate, valise.getDateRetourPrevue(),"La date de retour prévue doit correspondre à la valeure initiale");

    }
    @Test
    public void testValiseDateCreation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2016-02-20");
        Assertions.assertNotNull(valise.getDateCreation(),"La date de créationne doit pas être null");
        Assertions.assertEquals( expectedDate, valise.getDateCreation(),"La date de création doit correspondre à la valeure initiale");

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
        valiseWithoutClient.setDescription("Valise sans client");
        valiseWithoutClient.setTypevalise(valise.getTypevalise());
        valiseWithoutClient.setRegleSortie(valise.getRegleSortie());

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(valiseWithoutClient);
            em.flush();
        }, "La persistance devrait échouer si le client de la valise est nul");
    }


    @Test
    public void testUpdateClientAssociation() {
        // Créer et persister un nouveau client avec toutes les valeurs nécessaires
        Client newClient = new Client();
        newClient.setName("Smith");
        newClient.setEmail("smith@example.com");  // Ensure the email field is set
        newClient.setAdresse("rue de paris 75000 Paris");  // Add any other required fields
        em.persist(newClient);
        em.flush();

        // Récupérer la valise et vérifier son client actuel
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals("Doe", foundValise.getClient().getName(), "Le client initial devrait être 'Doe'");

        // Associer la valise au nouveau client
        foundValise.setClient(newClient);
        em.merge(foundValise);
        em.flush();

        // Vérifier que la valise est désormais associée au nouveau client
        Valise updatedValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals("Smith", updatedValise.getClient().getName(), "La valise devrait être associée au nouveau client 'Smith'");

        // Vérifier que l'ancien client n'a plus cette valise dans sa liste de valises
        Client oldClient = em.find(Client.class, clientRepository.findAll().get(0).getId());  // Fetch the old client from the repository
        Assertions.assertFalse(oldClient.getValises().contains(updatedValise), "L'ancien client ne devrait plus avoir cette valise associée");
    }

    @Test
    public void testValiseRegleAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getRegleSortie(), "La valise doit être associée à une règle.");
    }

    @Test
    public void testValiseRegleNull() {
        Valise valiseWithoutRegle = new Valise();
        valiseWithoutRegle.setClient(valise.getClient());
        valiseWithoutRegle.setTypevalise(valise.getTypevalise());
        valiseWithoutRegle.setDescription("Valise sans règle");

        em.persist(valiseWithoutRegle);
        em.flush();

        Valise foundValise = em.find(Valise.class, valiseWithoutRegle.getId());
        Assertions.assertNull(foundValise.getRegleSortie(), "La règle de la valise devrait être nulle.");
    }


    @Test
    public void testUpdateRegleAssociation() {
        Regle newRegle = new Regle();
        newRegle.setCoderegle("NEWCODE123");  // Ensure 'coderegle' is set
        em.persist(newRegle);
        em.flush();

        valise.setRegleSortie(newRegle);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(newRegle.getId(), foundValise.getRegleSortie().getId(), "La règle associée devrait être mise à jour.");
    }


    @Test
    public void testValiseTypeValiseAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getTypevalise(), "La valise doit être associée à un type de valise.");
    }

    @Test
    public void testValiseTypeValiseNull() {
        Valise valiseWithoutTypeValise = new Valise();
        valiseWithoutTypeValise.setClient(valise.getClient());
        valiseWithoutTypeValise.setDescription("Valise sans type de valise");

        em.persist(valiseWithoutTypeValise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valiseWithoutTypeValise.getId());
        Assertions.assertNull(foundValise.getTypevalise(), "Le type de valise de la valise devrait être nul.");
    }




    @Test
    public void testUpdateTypeValiseAssociation() {
        TypeValise newTypeValise = new TypeValise();
        newTypeValise.setProprietaire("Nouveau Propriétaire");
        newTypeValise.setDescription("Nouveau Type de Valise");
        em.persist(newTypeValise);
        em.flush();

        valise.setTypevalise(newTypeValise);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(newTypeValise.getId(), foundValise.getTypevalise().getId(), "Le type de valise associé devrait être mis à jour.");
    }


    @Test
    public void testValiseMouvementListAssociation() {
        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertNotNull(foundValise.getMouvementList(), "La liste des mouvements de la valise ne devrait pas être nulle.");
        Assertions.assertEquals(2, foundValise.getMouvementList().size(), "La valise devrait être associée à deux mouvements.");
    }

    @Test
    public void testCascadePersistMouvementList() {
        Mouvement newMouvement = new Mouvement();
        valise.getMouvementList().add(newMouvement);
        em.merge(valise);
        em.flush();

        Valise foundValise = em.find(Valise.class, valise.getId());
        Assertions.assertEquals(3, foundValise.getMouvementList().size(), "La liste des mouvements devrait contenir trois éléments.");
    }

    @Test
    public void testCascadeDeleteMouvementList() {
        em.remove(valise);
        em.flush();

        List<Mouvement> remainingMouvements = em.createQuery("SELECT m FROM Mouvement m", Mouvement.class).getResultList();
        Assertions.assertTrue(remainingMouvements.isEmpty(), "Tous les mouvements associés devraient être supprimés en cascade.");
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
        Assertions.assertEquals(1, foundValise.getMouvementList().size(), "La liste des mouvements devrait être mise à jour.");
        Assertions.assertEquals(newMouvement.getId(), foundValise.getMouvementList().get(0).getId(), "Le mouvement associé devrait être mis à jour.");
    }


}
