package example.integration.repositories;

import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeValise;
import example.entity.Valise;
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

    private Client client;
    private TypeValise typeValise;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        typeValiseRepository.deleteAll();
        clientRepository.deleteAll();

        client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client = clientRepository.save(client);

        typeValise = new TypeValise();
        typeValise.setProprietaire("John Doe");
        typeValise.setDescription("Standard suitcase");
        typeValise = typeValiseRepository.save(typeValise);
    }

    @Test
    public void testSaveValise() {
        Valise valise = new Valise();
        valise.setNumeroValise("VAL123");
        valise.setDescription("A sample suitcase");
        valise.setClient(client);
        valise.setTypeValise(typeValise);

        Valise savedValise = valiseRepository.save(valise);

        assertNotNull(savedValise.getId());
        assertEquals("VAL123", savedValise.getNumeroValise());
        assertEquals("A sample suitcase", savedValise.getDescription());
        assertEquals(client.getId(), savedValise.getClient().getId());
        assertEquals(typeValise.getId(), savedValise.getTypeValise().getId());
    }

    @Test
    public void testFindValiseById() {
        Valise valise = new Valise();
        valise.setNumeroValise("VAL123");
        valise.setDescription("A sample suitcase");
        valise.setClient(client);
        valise.setTypeValise(typeValise);

        Valise savedValise = valiseRepository.save(valise);

        Optional<Valise> foundValise = valiseRepository.findByIdWithDetails(savedValise.getId());
        assertTrue(foundValise.isPresent());
        assertEquals("VAL123", foundValise.get().getNumeroValise());
        assertEquals("A sample suitcase", foundValise.get().getDescription());
    }

    @Test
    public void testFindAllValises() {
        Valise valise1 = new Valise();
        valise1.setNumeroValise("VAL123");
        valise1.setDescription("First suitcase");
        valise1.setClient(client);
        valise1.setTypeValise(typeValise);
        valiseRepository.save(valise1);

        Valise valise2 = new Valise();
        valise2.setNumeroValise("VAL456");
        valise2.setDescription("Second suitcase");
        valise2.setClient(client);
        valise2.setTypeValise(typeValise);
        valiseRepository.save(valise2);

        List<Valise> valises = valiseRepository.findAllWithDetails();

        assertNotNull(valises);
        assertEquals(2, valises.size());
    }

    @Test
    public void testDeleteValise() {
        Valise valise = new Valise();
        valise.setNumeroValise("VAL123");
        valise.setDescription("A sample suitcase");
        valise.setClient(client);
        valise.setTypeValise(typeValise);

        Valise savedValise = valiseRepository.save(valise);
        valiseRepository.deleteById(savedValise.getId());

        Optional<Valise> foundValise = valiseRepository.findById(savedValise.getId());
        assertFalse(foundValise.isPresent());
    }

    @Test
    public void testFindValiseByClientId() {
        Valise valise = new Valise();
        valise.setNumeroValise("VAL123");
        valise.setDescription("A sample suitcase");
        valise.setClient(client);
        valise.setTypeValise(typeValise);

        valiseRepository.save(valise);

        List<Valise> valises = valiseRepository.findByClientId(client.getId());

        assertNotNull(valises);
        assertEquals(1, valises.size());
        assertEquals("VAL123", valises.get(0).getNumeroValise());
    }
}
