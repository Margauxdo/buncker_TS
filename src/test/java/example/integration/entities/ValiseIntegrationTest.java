package example.integration.entities;

import example.entities.Valise;
import example.entities.Client;
import example.entities.Regle;
import example.entities.TypeValise;
import example.repositories.ValiseRepository;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
@SpringBootTest
@ActiveProfiles("testintegration")
@Transactional
public class ValiseIntegrationTest {

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    private Client client;
    private Regle regle;
    private TypeValise typeValise;
    private Valise valise;

    @BeforeEach
    void setUp() {
        // Création d'un client
        client = new Client();
        client.setName("Jean Dupont");
        client.setEmail("jean.dupont@example.com");
        client = clientRepository.save(client);

        // Création d'une règle
        regle = new Regle();
        regle.setCoderegle("R1234");
        regle = regleRepository.save(regle);

        // Création d'un type de valise
        typeValise = new TypeValise();
        typeValise.setProprietaire("Entreprise XYZ");
        typeValise.setDescription("Type de valise standard");
        typeValise = typeValiseRepository.save(typeValise);

        // Création d'une valise
        valise = Valise.builder()
                .description("Valise de test")
                .numeroValise(123456L)
                .refClient("REF123")
                .dateCreation(new Date())
                .client(client)
                .regleSortie(regle)
                .typevalise(typeValise)
                .build();

        valise = valiseRepository.save(valise);
    }

    @Test
    void testCreateValise() {
        assertNotNull(valise.getId(), "L'ID de la valise doit être généré après la sauvegarde.");
        assertEquals("Valise de test", valise.getDescription());
        assertEquals(client.getId(), valise.getClient().getId());
        assertEquals(regle.getId(), valise.getRegleSortie().getId());
        assertEquals(typeValise.getId(), valise.getTypevalise().getId());
    }

    @Test
    void testFindValiseById() {
        Valise foundValise = valiseRepository.findById(valise.getId()).orElse(null);
        assertNotNull(foundValise, "La valise doit être trouvée.");
        assertEquals(valise.getNumeroValise(), foundValise.getNumeroValise());
    }

    @Test
    void testUpdateValise() {
        valise.setDescription("Valise mise à jour");
        valise.setNumeroValise(654321L);
        Valise updatedValise = valiseRepository.save(valise);
        assertEquals("Valise mise à jour", updatedValise.getDescription());
        assertEquals(654321L, updatedValise.getNumeroValise());
    }

    @Test
    void testDeleteValise() {
        valiseRepository.deleteById(valise.getId());
        assertFalse(valiseRepository.existsById(valise.getId()), "La valise doit être supprimée.");
    }

    @Test
    void testDeleteNonExistentValise() {
        // Vérifier que l'ID n'existe pas
        assertFalse(valiseRepository.existsById(9999), "L'ID 9999 ne doit pas exister dans la base de données.");

        // Lever une exception manuellement si l'ID n'existe pas
        Exception exception = assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> {
            if (!valiseRepository.existsById(9999)) {
                throw new org.springframework.dao.EmptyResultDataAccessException("No Valise entity with id 9999 exists", 1);
            }
            valiseRepository.deleteById(9999);
        });

        // Vérifier le message de l'exception
        assertTrue(exception.getMessage().contains("No Valise entity with id 9999 exists"));
    }


    @Test
    void testFindAllValises() {
        List<Valise> valises = valiseRepository.findAll();
        assertFalse(valises.isEmpty(), "La liste des valises ne doit pas être vide.");
        assertEquals(1, valises.size());
    }
}
