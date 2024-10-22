package example.integration.entities;

import example.entities.Client;
import example.entities.Mouvement;
import example.entities.Valise;
import example.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class ValiseIntegrationTest {

    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    @Autowired
    private MouvementRepository mouvementRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        regleRepository.deleteAll();
        clientRepository.deleteAll();
        typeValiseRepository.deleteAll();
        mouvementRepository.deleteAll();
    }

    @Test
    public void testSaveValise() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientA.setAdresse("bd de turin 59000 Lille");
        clientRepository.save(clientA);

        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(clientA);

        Valise savedValise = valiseRepository.save(valise);
        Assertions.assertNotNull(savedValise.getId());
        Assertions.assertEquals(252525L, savedValise.getNumeroValise());
        Assertions.assertEquals("1234L", savedValise.getRefClient());
        Assertions.assertEquals("1234", savedValise.getNumeroDujeu());
        Assertions.assertEquals(clientA, savedValise.getClient());
    }

    @Test
    public void testFindValisebyId() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientA.setAdresse("bd de turin 59000 Lille");
        clientRepository.save(clientA);

        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(clientA);
        Valise savedValise = valiseRepository.save(valise);

        Valise foundValise = valiseRepository.findById(savedValise.getId()).orElse(null);
        Assertions.assertNotNull(foundValise);
        Assertions.assertEquals(252525L, foundValise.getNumeroValise());
        Assertions.assertEquals("1234L", foundValise.getRefClient());
        Assertions.assertEquals("1234", foundValise.getNumeroDujeu());

        Assertions.assertEquals(clientA.getId(), foundValise.getClient().getId());
    }


    @Test
    public void testUpdateValise() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientA.setAdresse("bd de turin 59000 Lille");
        Client savedClientA = clientRepository.save(clientA);

        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(savedClientA);
        Valise savedValise = valiseRepository.save(valise);

        Assertions.assertNotNull(savedValise.getId());
        Assertions.assertEquals(252525L, savedValise.getNumeroValise());
        Assertions.assertEquals("1234L", savedValise.getRefClient());
        Assertions.assertEquals("1234", savedValise.getNumeroDujeu());
        Assertions.assertEquals(savedClientA.getId(), savedValise.getClient().getId());

        Client clientB = new Client();
        clientB.setName("Marc");
        clientB.setEmail("marc@gmail.com");
        clientB.setAdresse("rue de Paris 75000 Paris");
        Client savedClientB = clientRepository.save(clientB);

        savedValise.setClient(savedClientB);
        Valise updatedValise = valiseRepository.save(savedValise);

        Assertions.assertEquals(savedClientB.getId(), updatedValise.getClient().getId());
        Assertions.assertEquals(savedValise.getId(), updatedValise.getId());
        Assertions.assertEquals("1234L", updatedValise.getRefClient());
        Assertions.assertEquals("1234", updatedValise.getNumeroDujeu());
        Assertions.assertEquals(252525L, updatedValise.getNumeroValise());
    }
    @Test
    public void testDeleteValise() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientA.setAdresse("bd de turin 59000 Lille");
        clientRepository.save(clientA);

        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(clientA);
        Valise savedValise = valiseRepository.save(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setValise(savedValise);
        mouvementRepository.save(mouvement);

        valiseRepository.deleteById(savedValise.getId());

        Assertions.assertFalse(valiseRepository.findById(savedValise.getId()).isPresent());
    }

    @Test
    public void testFindAllValise() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");

        clientA.setAdresse("bd de turin 59000 Lille");
        clientRepository.save(clientA);
        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(clientA);
        Valise savedValise = valiseRepository.save(valise);
        Mouvement mouvement = new Mouvement();
        mouvement.setValise(savedValise);
        mouvementRepository.save(mouvement);
        valiseRepository.deleteById(savedValise.getId());
        Assertions.assertFalse(valiseRepository.findById(savedValise.getId()).isPresent());

    }
    @Test
    public void testSaveValiseWithoutClient() {
        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            valiseRepository.save(valise);
        });
    }

    @Test
    public void testSaveMultipleValises() {

        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientRepository.save(clientA);

        Valise valise1 = new Valise();
        valise1.setNumeroValise(252525L);
        valise1.setRefClient("1234L");
        valise1.setNumeroDujeu("1234");
        valise1.setClient(clientA);
        valiseRepository.save(valise1);

        Valise valise2 = new Valise();
        valise2.setNumeroValise(353535L);
        valise2.setRefClient("5678L");
        valise2.setNumeroDujeu("5678");
        valise2.setClient(clientA);
        valiseRepository.save(valise2);

        List<Valise> valises = valiseRepository.findAll();
        Assertions.assertEquals(2, valises.size(), "There should be two valises in the database.");
    }
    @Test
    public void testCascadeDeleteMouvement() {
        Client clientA = new Client();
        clientA.setName("Henri");
        clientA.setEmail("henri@gmail.com");
        clientRepository.save(clientA);

        Valise valise = new Valise();
        valise.setNumeroValise(252525L);
        valise.setRefClient("1234L");
        valise.setNumeroDujeu("1234");
        valise.setClient(clientA);
        Valise savedValise = valiseRepository.save(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setValise(savedValise);
        mouvementRepository.save(mouvement);

        Assertions.assertFalse(mouvementRepository.findAll().isEmpty(), "Mouvement should be saved.");

        valiseRepository.deleteById(savedValise.getId());

        List<Mouvement> mouvements = mouvementRepository.findAll();
        Assertions.assertTrue(mouvements.isEmpty(), "Associated mouvements should also be deleted when the valise is deleted.");
    }









}
