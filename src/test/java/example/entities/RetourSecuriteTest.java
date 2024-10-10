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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class RetourSecuriteTest {

    @Autowired
    private EntityManager em;

    private RetourSecurite retourSecurite;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Création et persistance d'un client avec un nom obligatoire
        Client client = new Client();
        client.setName("Client Test"); // Initialisation du champ obligatoire 'name'
        em.persist(client);
        em.flush();

        // Initialisation de RetourSecurite avec les valeurs nécessaires
        retourSecurite = new RetourSecurite();
        retourSecurite.setClient(client);
        retourSecurite.setCloture(Boolean.FALSE);
        retourSecurite.setNumero(25689568965L);
        retourSecurite.setDatesecurite(sdf.parse("2020-02-25"));
        retourSecurite.setDateCloture(sdf.parse("2020-02-25"));

        em.persist(retourSecurite);
        em.flush();
    }

    @Test
    public void testRetourSecuritePersistence() {
        Assertions.assertNotNull(retourSecurite.getId(), "L'ID de retourSecurite ne doit pas être nul après la persistance");
    }

    @Test
    public void testRetourSecuriteNumero() {
        Assertions.assertEquals(25689568965L, retourSecurite.getNumero(), "Le numéro doit correspondre à la valeur initialisée");
    }

    @Test
    public void testRetourSecuriteDateSecurite() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(sdf.parse("2020-02-25"), retourSecurite.getDatesecurite(), "La date de sécurité doit correspondre à la date initialisée");
    }

    @Test
    public void testRetourSecuriteDateCloture() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(sdf.parse("2020-02-25"), retourSecurite.getDateCloture(), "La date de clôture doit correspondre à la date initialisée");
    }

    @Test
    public void testRetourSecuriteCloture() {
        Assertions.assertEquals(Boolean.FALSE, retourSecurite.getCloture(), "Le champ cloture doit être égal à FALSE");
    }

    @Test
    public void testRetourSecuriteClientAssociation() {
        Assertions.assertNotNull(retourSecurite.getClient(), "Le client associé à retourSecurite ne doit pas être nul");
        Client expectedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals(expectedClient, retourSecurite.getClient(), "Le client associé doit correspondre au client persistant");
    }

    @Test
    public void testNonNullNumero() {
        RetourSecurite retourSecuriteWithoutNumero = new RetourSecurite();
        retourSecuriteWithoutNumero.setClient(retourSecurite.getClient());
        retourSecuriteWithoutNumero.setDatesecurite(retourSecurite.getDatesecurite());

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(retourSecuriteWithoutNumero);
            em.flush();
        }, "La persistance doit échouer si le numéro est nul");
    }

    @Test
    public void testUpdateDateSecurite() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        retourSecurite.setDatesecurite(sdf.parse("2021-05-15"));
        em.merge(retourSecurite);
        em.flush();

        RetourSecurite updatedRetourSecurite = em.find(RetourSecurite.class, retourSecurite.getId());
        Assertions.assertEquals(sdf.parse("2021-05-15"), updatedRetourSecurite.getDatesecurite(), "La date de sécurité doit être mise à jour avec succès");
    }
}

