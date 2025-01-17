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

import java.util.Date;
import java.util.Optional;

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
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();
        valiseRepository.deleteAll();
        livreurRepository.deleteAll();
        retourSecuriteRepository.deleteAll();
    }



    @Test
    public void testFindMouvementById() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("Completed");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        Optional<Mouvement> foundMouvement = mouvementRepository.findById(savedMouvement.getId());
        assertTrue(foundMouvement.isPresent());
        assertEquals("Completed", foundMouvement.get().getStatutSortie());
    }

    @Test
    public void testDeleteMouvement() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("To Delete");
        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);

        mouvementRepository.delete(savedMouvement);
        Optional<Mouvement> deletedMouvement = mouvementRepository.findById(savedMouvement.getId());
        assertFalse(deletedMouvement.isPresent());
    }

    @Test
    public void testSaveMouvementWithoutValise() {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateHeureMouvement(new Date());
        mouvement.setStatutSortie("No Valise");

        Mouvement savedMouvement = mouvementRepository.saveAndFlush(mouvement);
        assertNotNull(savedMouvement.getId());
        assertEquals("No Valise", savedMouvement.getStatutSortie());
        assertNull(savedMouvement.getValise());
    }

    @Test
    public void testFindAllMouvements() {
        Mouvement mouvement1 = new Mouvement();
        mouvement1.setDateHeureMouvement(new Date());
        mouvement1.setStatutSortie("First");
        mouvementRepository.saveAndFlush(mouvement1);

        Mouvement mouvement2 = new Mouvement();
        mouvement2.setDateHeureMouvement(new Date());
        mouvement2.setStatutSortie("Second");
        mouvementRepository.saveAndFlush(mouvement2);

        assertEquals(2, mouvementRepository.findAll().size());
    }
}
