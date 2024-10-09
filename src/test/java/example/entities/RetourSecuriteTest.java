package example.entities;

import example.repositories.ClientRepository;
import jakarta.persistence.EntityManager;
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

        Client client=new Client();
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
    public void testRetourSecuritePersistence() throws ParseException {
        Assertions.assertNotNull(retourSecurite.getId());
    }
    @Test
    public void testRetourSecuriteNumero() {
        Assertions.assertEquals(25689568965L, retourSecurite.getNumero());
    }
    @Test
    public void testRetourSecuriteDateSecurite() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(retourSecurite.getDatesecurite(),(sdf.parse("2020-02-25")));
    }
    @Test
    public void testRetourSecuriteDateCloture() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals(retourSecurite.getDateCloture(),(sdf.parse("2020-02-25")));
    }
    @Test
    public void testRetourSecuriteCloture() {
        Assertions.assertEquals(retourSecurite.getCloture(), Boolean.FALSE);
    }
    @Test
    public void testRetourSecuriteClientAssociation() {
        Assertions.assertNotNull(retourSecurite.getClient());
        Client expectedClient = clientRepository.findAll().get(0);
        Assertions.assertEquals(expectedClient, retourSecurite.getClient());
    }
    @Test
    public void testNonNullNumero(){

    }
    @Test
    public void testUpdateDateSecurite(){

    }

}
