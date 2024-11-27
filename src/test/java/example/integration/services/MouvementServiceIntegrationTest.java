package example.integration.services;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import example.services.MouvementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MouvementServiceIntegrationTest {

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Valise valise;
    private Mouvement mouvement;

    @BeforeEach
    public void setUp() {
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        client = clientRepository.save(client);

        valise = Valise.builder()
                .description("Valise de test")
                .numeroValise(123456L)
                .refClient("RefClientTest")
                .client(client)
                .build();
        valise = valiseRepository.save(valise);

        mouvement = Mouvement.builder()
                .dateHeureMouvement(new Date())
                .statutSortie("EN_TRANSIT")
                .dateSortiePrevue(new Date())
                .dateRetourPrevue(new Date(System.currentTimeMillis() + 86400000L)) // +1 jour
                .valise(valise)
                .build();
    }

    @Test
    void testCreateMouvement_Success() {
        // Act
        Mouvement createdMouvement = mouvementService.createMouvement(mouvement);

        // Assert
        assertNotNull(createdMouvement.getId());
        assertEquals(mouvement.getDateHeureMouvement(), createdMouvement.getDateHeureMouvement());
        assertEquals(mouvement.getStatutSortie(), createdMouvement.getStatutSortie());
        assertEquals(mouvement.getValise().getId(), createdMouvement.getValise().getId());
    }

    @Test
    void testUpdateMouvement_Success() {
        // Arrange
        Mouvement createdMouvement = mouvementService.createMouvement(mouvement);
        createdMouvement.setStatutSortie("LIVRE");

        // Act
        Mouvement updatedMouvement = mouvementService.updateMouvement(createdMouvement.getId(), createdMouvement);

        // Assert
        assertNotNull(updatedMouvement);
        assertEquals("LIVRE", updatedMouvement.getStatutSortie());
    }

    @Test
    void testUpdateMouvement_Failure_NotFound() {
        // Arrange
        mouvement.setId(999);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.updateMouvement(999, mouvement);
        });

        assertEquals("Mouvement not found", exception.getMessage());
    }

    @Test
    void testDeleteMouvement_Success() {
        // Arrange
        Mouvement createdMouvement = mouvementService.createMouvement(mouvement);

        // Act
        mouvementService.deleteMouvement(createdMouvement.getId());

        // Assert
        assertFalse(mouvementService.existsById(createdMouvement.getId()));
    }

    @Test
    void testDeleteMouvement_Failure_NotFound() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            mouvementService.deleteMouvement(999);
        });

        assertEquals("Mouvement not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllMouvements() {
        // Arrange
        mouvementService.createMouvement(mouvement);

        // Act
        List<Mouvement> mouvements = mouvementService.getAllMouvements();

        // Assert
        assertFalse(mouvements.isEmpty());
        assertEquals(1, mouvements.size());
    }

    @Test
    void testGetMouvementById_Success() {
        // Arrange
        Mouvement createdMouvement = mouvementService.createMouvement(mouvement);

        // Act
        Mouvement fetchedMouvement = mouvementService.getMouvementById(createdMouvement.getId());

        // Assert
        assertNotNull(fetchedMouvement);
        assertEquals(createdMouvement.getStatutSortie(), fetchedMouvement.getStatutSortie());
    }

    @Test
    void testGetMouvementById_Failure_NotFound() {
        // Act
        Mouvement fetchedMouvement = mouvementService.getMouvementById(999);

        // Assert
        assertNull(fetchedMouvement);
    }


    @Test
    void testGetAllMouvements_LargeDataset() {
        // Arrange
        for (int i = 0; i < 100; i++) {
            mouvementService.createMouvement(
                    Mouvement.builder()
                            .dateHeureMouvement(new Date())
                            .statutSortie("EN_TRANSIT_" + i)
                            .dateSortiePrevue(new Date())
                            .dateRetourPrevue(new Date(System.currentTimeMillis() + 86400000L))
                            .valise(valise)
                            .build()
            );
        }

        // Act
        List<Mouvement> mouvements = mouvementService.getAllMouvements();

        // Assert
        assertEquals(100, mouvements.size());
    }



}
