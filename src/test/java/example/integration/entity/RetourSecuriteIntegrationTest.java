package example.integration.entity;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RetourSecuriteIntegrationTest {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();
    }


    @Test
    public void testSaveRetourSecuriteWithoutNumero() {
        Client client = new Client();
        client.setEmail("test@test.com");
        client.setName("Albert");
        clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        //retourSecurite.setClient(new ArrayList<>());
        retourSecurite.setDatesecurite(new Date());


        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteRepository.save(retourSecurite);
        }, "Expected DataIntegrityViolationException due to missing 'number'");
    }

    @Test
    public void testFindRetourSecuriteByIdNotFound(){
        Optional<RetourSecurite> foundRS = retourSecuriteRepository.findById(9999);
        Assertions.assertFalse(foundRS.isPresent(), "The safety return id must be generated");
    }

    @Test
    public void testFindById_ReturnSecuriteDoesNotExist() {
        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(99999);

        Assertions.assertFalse(foundRetourSecurite.isPresent(), "safety return should not be found");
    }



}
