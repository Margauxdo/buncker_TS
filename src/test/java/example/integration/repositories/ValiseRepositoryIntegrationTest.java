package example.integration.repositories;

import example.entities.Client;
import example.entities.Regle;
import example.entities.TypeValise;
import example.entities.Valise;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class ValiseRepositoryIntegrationTest {

    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
    }
    @Test
    public void testSaveValiseSuccess() {
        Client client = new Client();
        client.setName("Does");
        client.setEmail("does@email.com");
        clientRepository.save(client);

        Regle regle = new Regle();
        regle.setCoderegle("code regleA");
        regleRepository.save(regle);

        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Jean Provost");
        typeValise.setDescription("Description de la valise");
        typeValiseRepository.save(typeValise);

        Valise valise = new Valise();
        valise.setNumeroValise(2548L);
        valise.setRefClient("Aw5689");
        valise.setNumeroDujeu("254689");
        valise.setClient(client);
        valise.setRegleSortie(regle);
        valise.setTypevalise(typeValise);

        Valise savedVal = valiseRepository.save(valise);
        assertNotNull(savedVal.getId());
        assertEquals(2548L, savedVal.getNumeroValise());
        assertEquals("Aw5689", savedVal.getRefClient());
        assertEquals("254689", savedVal.getNumeroDujeu());
        assertEquals(regle.getCoderegle(), savedVal.getRegleSortie().getCoderegle());
    }


    @Test
    public void testFindValiseByIdSuccess(){
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        valiseRepository.save(val);

        Optional<Valise> foundVal = valiseRepository.findById(val.getId());
        assertTrue(foundVal.isPresent());
        assertEquals(2548L, foundVal.get().getNumeroValise());
        assertEquals("Aw5689", foundVal.get().getRefClient());
        assertEquals("254689", foundVal.get().getNumeroDujeu());
    }

    @Test
    public void testFindByNumeroValiseSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);

        valiseRepository.save(val);

        Valise foundVal = valiseRepository.findByNumeroValise(2548L);
        assertNotNull(foundVal);
        assertEquals(2548L, foundVal.getNumeroValise());
        assertEquals("Aw5689", foundVal.getRefClient());
        assertEquals("254689", foundVal.getNumeroDujeu());
    }



    @Test
    public void testFindByRefClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        valiseRepository.save(val);

        Valise foundVal = valiseRepository.findByRefClient("Aw5689");
        assertNotNull(foundVal);
        assertEquals("Aw5689", foundVal.getRefClient());
        assertEquals(2548L, foundVal.getNumeroValise());
        assertEquals("254689", foundVal.getNumeroDujeu());
    }


    @Test
    public void testFindByClient() {
        Client client = new Client();
        client.setName("Does");
        client.setEmail("does@email.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        valiseRepository.save(val);

        List<Valise> foundVals = valiseRepository.findByClient(client);
        assertNotNull(foundVals);
        assertFalse(foundVals.isEmpty());
        assertEquals(client.getName(), foundVals.get(0).getClient().getName());
    }

    @Test
    public void testFindByRegle() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Regle regle = new Regle();
        regle.setCoderegle("code regleA");
        regleRepository.save(regle);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        val.setRegleSortie(regle);
        valiseRepository.save(val);

        List<Valise> foundVals = valiseRepository.findByRegleSortie(regle);
        assertNotNull(foundVals);
        assertFalse(foundVals.isEmpty());
        assertEquals("code regleA", foundVals.get(0).getRegleSortie().getCoderegle());
    }


    @Test
    public void testDeleteValiseSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        Valise savedVal = valiseRepository.save(val);

        valiseRepository.delete(savedVal);

        Optional<Valise> foundVal = valiseRepository.findById(savedVal.getId());
        assertFalse(foundVal.isPresent());
    }

    @Test
    public void testUpdateValiseSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        clientRepository.save(client);

        Valise val = new Valise();
        val.setNumeroValise(2548L);
        val.setRefClient("Aw5689");
        val.setNumeroDujeu("254689");
        val.setClient(client);
        Valise savedVal = valiseRepository.save(val);

        savedVal.setNumeroValise(5678L);
        savedVal.setRefClient("Bw1234");
        savedVal.setNumeroDujeu("987654");
        valiseRepository.save(savedVal);

        Optional<Valise> updatedVal = valiseRepository.findById(savedVal.getId());
        assertTrue(updatedVal.isPresent());
        assertEquals(5678L, updatedVal.get().getNumeroValise());
        assertEquals("Bw1234", updatedVal.get().getRefClient());
        assertEquals("987654", updatedVal.get().getNumeroDujeu());
    }


    @Test
    public void testFindAllValiseSuccess() {
        Client client1 = new Client();
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Jane Doe");
        client2.setEmail("jane.doe@example.com");
        clientRepository.save(client2);

        Valise val1 = new Valise();
        val1.setNumeroValise(2548L);
        val1.setRefClient("Aw5689");
        val1.setNumeroDujeu("254689");
        val1.setClient(client1);

        Valise val2 = new Valise();
        val2.setNumeroValise(5678L);
        val2.setRefClient("Bw1234");
        val2.setNumeroDujeu("987654");
        val2.setClient(client2);

        valiseRepository.save(val1);
        valiseRepository.save(val2);

        List<Valise> allVals = valiseRepository.findAll();
        assertNotNull(allVals);
        assertEquals(2, allVals.size());
    }


    @Test
    public void testDeleteNonExistingValise() {
        valiseRepository.deleteById(9999);
        Optional<Valise> foundVal = valiseRepository.findById(9999);
        assertFalse(foundVal.isPresent());
    }

}
