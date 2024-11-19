package example.integration.services;

import example.entity.Client;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.ValiseRepository;
import example.services.MouvementService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class MouvementServiceIntegrationTest {

    @Autowired
    private MouvementService mouvementService;
    @Autowired
    private MouvementRepository mouvementRepository;
    private Mouvement mouvement;
    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;


    @BeforeEach
    public void setUp() {
        mouvementRepository.deleteAll();

        Livreur livreurA = new Livreur();
        livreurA.setCodeLivreur("12569");
        livreurA.setNomLivreur("Dubois");
        livreurA.setNumeroCartePro("AW568951");
        livreurA = livreurRepository.save(livreurA);

        Client clientA = new Client();
        clientA.setName("Dutoit");
        clientA.setEmail("a.dutoit@gmail.com");
        clientA = clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setNumeroValise(1234L);
        val1 = valiseRepository.save(val1);

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateHM1 = dateTimeFormat.parse("2024-10-30 12:30:00");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateRP1 = dateFormat.parse("2024-10-01");
            Date dateSP1 = dateFormat.parse("2024-10-31");

            mouvement = Mouvement.builder()
                    .statutSortie("en cours")
                    .dateHeureMouvement(dateHM1)
                    .dateRetourPrevue(dateRP1)
                    .dateSortiePrevue(dateSP1)
                    .livreur(livreurA)
                    .valise(val1)
                    .build();

            mouvement = mouvementService.createMouvement(mouvement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateMouvement() {
        // Act
        Mouvement savedMvmt = mouvementService.createMouvement(mouvement);

        // Assert
        assertNotNull(savedMvmt);
        assertNotNull(savedMvmt.getLivreur());
        assertNotNull(savedMvmt.getValise());
        assertNotNull(savedMvmt.getDateHeureMouvement());
        assertNotNull(savedMvmt.getDateRetourPrevue());
        assertNotNull(savedMvmt.getDateSortiePrevue());
        assertEquals("en cours", savedMvmt.getStatutSortie());
        assertEquals("12569", savedMvmt.getLivreur().getCodeLivreur());
        assertEquals("Dutoit", savedMvmt.getValise().getClient().getName());
    }

    @Test
    public void testUpdateMouvement() {
        // Arrange
        Mouvement savedMvmt = mouvementService.createMouvement(mouvement);
        Date specificDate = new Date();

        // Act
        savedMvmt.setStatutSortie("finalisé");
        savedMvmt.setDateHeureMouvement(specificDate);
        savedMvmt.setDateRetourPrevue(specificDate);
        savedMvmt.setDateSortiePrevue(specificDate);
        Mouvement updatedMvmt = mouvementService.updateMouvement(savedMvmt.getId(), savedMvmt);

        // Assert
        assertNotNull(updatedMvmt);
        assertEquals(savedMvmt.getId(), updatedMvmt.getId());
        assertEquals("finalisé", updatedMvmt.getStatutSortie());
        assertEquals(specificDate, updatedMvmt.getDateHeureMouvement());
        assertEquals(specificDate, updatedMvmt.getDateRetourPrevue());
        assertEquals(specificDate, updatedMvmt.getDateSortiePrevue());
    }


    @Test
    public void testDeleteMouvement() {
        // Arrange
        Mouvement savedMvmt = mouvementService.createMouvement(mouvement);
        // Act
        mouvementService.deleteMouvement(savedMvmt.getId());
        // Assert
        Mouvement deletedMvmt = mouvementService.getMouvementById(savedMvmt.getId());
        Assertions.assertNull(deletedMvmt);
    }

    @Test
    public void testGetMouvementById() {
        // Arrange
        Mouvement savedMvmt = mouvementService.createMouvement(mouvement);

        // Act
        Mouvement mvmtId = mouvementService.getMouvementById(savedMvmt.getId());

        // Assert
        assertNotNull(mvmtId);
        assertEquals(savedMvmt.getId(), mvmtId.getId());

        // Compare les autres champs
        assertEquals("en cours", mvmtId.getStatutSortie());
        assertEquals(mouvement.getDateHeureMouvement(), mvmtId.getDateHeureMouvement());
        assertEquals(mouvement.getDateRetourPrevue(), mvmtId.getDateRetourPrevue());
        assertEquals(mouvement.getDateSortiePrevue(), mvmtId.getDateSortiePrevue());
    }


    @Test
    public void testGetAllMouvements() throws ParseException {
        // Arrange
        Livreur livreurB = new Livreur();
        livreurB.setCodeLivreur("12570");
        livreurB.setNomLivreur("Simon");
        livreurB.setNumeroCartePro("AW589632");
        livreurB = livreurRepository.save(livreurB);

        Client clientB = new Client();
        clientB.setName("Leurant");
        clientB.setEmail("t.leurant@gmail.com");
        clientB = clientRepository.save(clientB);

        Valise val2 = new Valise();
        val2.setClient(clientB);
        val2.setNumeroValise(1245L);
        val2 = valiseRepository.save(val2);

        Mouvement mouvement1 = null;

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateHM1 = dateTimeFormat.parse("2024-12-28 12:34:00");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateRP1 = dateFormat.parse("2024-08-21");
            Date dateSP1 = dateFormat.parse("2024-11-30");

            mouvement1 = Mouvement.builder()
                    .statutSortie("finalisé")
                    .dateHeureMouvement(dateHM1)
                    .dateRetourPrevue(dateRP1)
                    .dateSortiePrevue(dateSP1)
                    .livreur(livreurB)
                    .valise(val2)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mouvementService.createMouvement(mouvement);
        mouvementService.createMouvement(mouvement1);

        assertNotNull(mouvementService.getAllMouvements());
        assertEquals(2, mouvementService.getAllMouvements().size());
    }
}
