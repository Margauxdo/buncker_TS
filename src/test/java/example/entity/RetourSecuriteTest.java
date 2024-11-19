package example.entity;

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

        Client client = new Client();
        client.setName("Client Test");
        client.setEmail("client.test@example.com");
        em.persist(client);
        em.flush();

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
        Assertions.assertNotNull(retourSecurite.getId(),
                "Security return ID must not be null after persistence");
    }

    @Test
    public void testRetourSecuriteNumero() {
        Assertions.assertEquals(25689568965L, retourSecurite.getNumero(),
                "The number must match the initialized value");
    }

    @Test
    public void testRetourSecuriteDateSecurite() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(sdf.parse("2020-02-25"), retourSecurite.getDatesecurite(),
                "The security date must match the initialized date");
    }

    @Test
    public void testRetourSecuriteDateCloture() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(sdf.parse("2020-02-25"), retourSecurite.getDateCloture(),
                "The closing date must match the initialized date");
    }

    @Test
    public void testRetourSecuriteCloture() {
        Assertions.assertEquals(Boolean.FALSE, retourSecurite.getCloture(),
                "The fence field must be equal to FALSE");
    }

    @Test
    public void testRetourSecuriteClientAssociation() {
        Assertions.assertNotNull(retourSecurite.getClient(),
                "The client associated with the Security return must not be null");
        Client expectedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals(expectedClient, retourSecurite.getClient(),
                "The associated client must match the persistent client");
    }

    @Test
    public void testNonNullNumero() {
        RetourSecurite retourSecuriteWithoutNumero = new RetourSecurite();
        retourSecuriteWithoutNumero.setClient(retourSecurite.getClient());
        retourSecuriteWithoutNumero.setDatesecurite(retourSecurite.getDatesecurite());

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(retourSecuriteWithoutNumero);
            em.flush();
        }, "Persistence should fail if number is null");
    }

    @Test
    public void testUpdateDateSecurite() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        retourSecurite.setDatesecurite(sdf.parse("2021-05-15"));
        em.merge(retourSecurite);
        em.flush();

        RetourSecurite updatedRetourSecurite = em.find(RetourSecurite.class, retourSecurite.getId());
        Assertions.assertEquals(sdf.parse("2021-05-15"), updatedRetourSecurite.getDatesecurite(),
                "The security date must be updated successfully.");
    }
}

