package example.integration.entity;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Regle;
import example.entity.RetourSecurite;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.RegleRepository;
import example.repositories.RetourSecuriteRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MouvementIntegrationTest {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        livreurRepository.deleteAll();
        retourSecuriteRepository.deleteAll();
        valiseRepository.deleteAll();
    }

    @Test
    public void testSaveMouvement() {
        // Create client and valise
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        // Create mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Test Statut");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);

        mouvement = mouvementRepository.saveAndFlush(mouvement);

        // Assertions
        assertTrue(mouvement.getId() > 0);
        assertEquals("Test Statut", mouvement.getStatutSortie());
        assertEquals(valise.getId(), mouvement.getValise().getId());
    }

    @Test
    public void testFindMouvementById() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Test Statut");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);

        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Optional<Mouvement> foundMouvement = mouvementRepository.findById(mouvement.getId());

        // Assertions
        assertTrue(foundMouvement.isPresent());
        assertEquals("Test Statut", foundMouvement.get().getStatutSortie());
    }

    @Test
    public void testUpdateMouvement() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Old Statut");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);

        mouvement = mouvementRepository.saveAndFlush(mouvement);

        // Update the mouvement
        mouvement.setStatutSortie("Updated Statut");
        mouvement = mouvementRepository.saveAndFlush(mouvement);

        // Assertions
        assertEquals("Updated Statut", mouvement.getStatutSortie());
    }

    @Test
    public void testDeleteMouvement() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Test Statut");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);

        mouvement = mouvementRepository.saveAndFlush(mouvement);

        mouvementRepository.deleteById(mouvement.getId());

        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(mouvement.getId());
        assertTrue(deletedMouvement.isEmpty());
    }


    @Test
    public void testMouvementWithLivreurAndRetourSecurite() {

        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("client@test.com");
        client = clientRepository.saveAndFlush(client);

        Valise valise = new Valise();
        valise.setDescription("Test Valise");
        valise.setNumeroValise(123456L);
        valise.setClient(client);
        valise = valiseRepository.saveAndFlush(valise);

        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Test Statut");
        mouvement.setDateSortiePrevue(new Date());
        mouvement.setDateRetourPrevue(new Date());
        mouvement.setValise(valise);

        mouvement = mouvementRepository.saveAndFlush(mouvement);

        Livreur livreur = new Livreur();
        livreur.setNomLivreur("NomLivreur");
        livreurRepository.saveAndFlush(livreur);

        mouvement.addLivreur(livreur);
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(12345L);
        retourSecurite.setMouvement(mouvement);
        retourSecuriteRepository.saveAndFlush(retourSecurite);

        mouvement.getRetourSecurites().add(retourSecurite);
        mouvement = mouvementRepository.findById(mouvement.getId()).orElseThrow();

        assertEquals(1, mouvement.getRetourSecurites().size());

    }



}









