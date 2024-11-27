package example.integration.repositories;

import example.entity.*;
import example.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class MouvementRepositoryIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        livreurRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveMouvement_WithRelations() {
        // Create and save RetourSecurite
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(2525L)
                .build();
        retourSecurite = retourSecuriteRepository.save(retourSecurite);

        // Create and save Client
        Client client = Client.builder()
                .name("bernard")
                .email("bernard@gmail.com")
                .build();
        client = clientRepository.save(client);

        // Create and save Valise
        Valise valise = Valise.builder()
                .description("description")
                .numeroValise(123L)
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        // Create the Mouvement instance
        Mouvement mouvement = Mouvement.builder()
                .valise(valise)
                .statutSortie("finished")
                .dateHeureMouvement(new Date())
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date())
                .livreurs(new ArrayList<>()) // Ensure livreurs is initialized
                .build();

        // Add Livreurs to the Mouvement
        Livreur livreur = Livreur.builder()
                .nomLivreur("jean")
                .build();

        mouvement.addLivreur(livreur); // Add livreur to mouvement
        mouvement = mouvementRepository.save(mouvement);

        // Assertions
        assertNotNull(mouvement.getId(), "Mouvement ID should not be null");
        assertEquals("finished", mouvement.getStatutSortie(), "StatutSortie should match");
        assertEquals(1, mouvement.getLivreurs().size(), "Mouvement should have one Livreur");
        assertEquals(valise.getId(), mouvement.getValise().getId(), "Valise ID should match");
        assertEquals("jean", mouvement.getLivreurs().get(0).getNomLivreur(), "Livreur name should match");
    }




    @Test
    public void testFindByIdMouvement() {
        Client client = Client.builder()
                .name("Test Client")
                .email("test.client@example.com")
                .build();
        client = clientRepository.save(client);

        Valise valise = Valise.builder()
                .numeroValise(12345L)
                .description("Valise de test")
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .statutSortie("en cours")
                .dateHeureMouvement(new Date())
                .valise(valise)
                .build();
        mouvement = mouvementRepository.save(mouvement);

        Optional<Mouvement> foundMouvement = mouvementRepository.findById(mouvement.getId());

        assertTrue(foundMouvement.isPresent());
        assertEquals("en cours", foundMouvement.get().getStatutSortie());
        assertNotNull(foundMouvement.get().getValise());
        assertEquals(valise.getNumeroValise(), foundMouvement.get().getValise().getNumeroValise());
        assertEquals(client.getName(), foundMouvement.get().getValise().getClient().getName());
    }


    @Test
    public void testUpdateMouvement() {
        Client client = Client.builder()
                .name("bernard")
                .email("bernard@gmail.com")
                .build();
        client = clientRepository.save(client);

        Valise valise = Valise.builder()
                .description("description")
                .numeroValise(123L)
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        Mouvement mouvement = Mouvement.builder()
                .statutSortie("initial")
                .dateHeureMouvement(new Date())
                .valise(valise)
                .build();
        mouvement = mouvementRepository.save(mouvement);

        mouvement.setStatutSortie("modifié");
        Mouvement updatedMouvement = mouvementRepository.save(mouvement);

        assertEquals("modifié", updatedMouvement.getStatutSortie());
    }


    @Test
    public void testDeleteMouvement_WithRelations() {
        Client client = clientRepository.save(Client.builder().name("client").email("client@mail.com").build());
        Valise valise = valiseRepository.save(Valise.builder().description("valise").client(client).build());
        Mouvement mouvement = mouvementRepository.save(Mouvement.builder().valise(valise).build());

        mouvementRepository.delete(mouvement);

        // Vérifications
        assertFalse(mouvementRepository.findById(mouvement.getId()).isPresent());
    }

    @Test
    public void testFindAllMouvements() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setDescription("Valise pour test");
        valise.setNumeroValise(12345L);
        valise.setClient(client);
        valiseRepository.save(valise);


        Mouvement mouvement1 = new Mouvement();
        mouvement1.setStatutSortie("1");
        mouvement1.setValise(valise);
        mouvementRepository.save(mouvement1);

        Mouvement mouvement2 = new Mouvement();
        mouvement2.setStatutSortie("2");
        mouvement2.setValise(valise);
        mouvementRepository.save(mouvement2);

        // Retrieve all Mouvements
        List<Mouvement> mouvements = mouvementRepository.findAll();

        // Assertions
        assertEquals(2, mouvements.size());
        assertEquals("1", mouvements.get(0).getStatutSortie());
        assertEquals("2", mouvements.get(1).getStatutSortie());
        assertNotNull(mouvements.get(0).getValise());
        assertEquals("Valise pour test", mouvements.get(0).getValise().getDescription());
    }




}
