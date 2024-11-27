package example.integration.services;

import example.entity.Client;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.RetourSecuriteRepository;
import example.repositories.ValiseRepository;
import example.services.RetourSecuriteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class RetourSecuriteServiceIntegrationTest {

    @Autowired
    private RetourSecuriteService retourSecuriteService;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;
    private RetourSecurite retourSecurite;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();

        Client clientA = new Client();
        clientA.setName("Dutoit");
        clientA.setEmail("a.dutoit@gmail.com");
        clientA = clientRepository.save(clientA);

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateClo = dateTimeFormat.parse("2024-10-30");
            Date dateSecu = dateTimeFormat.parse("2024-10-28");
            retourSecurite = RetourSecurite.builder()
                    .clients(new ArrayList<>())
                    .cloture(true)
                    .dateCloture(dateClo)
                    .datesecurite(dateSecu)
                    .numero(12345L)
                    .build();
            retourSecurite = retourSecuriteService.createRetourSecurite(retourSecurite);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testUpdateRetourSecurite_Failure_NullNumero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            retourSecurite.setNumero(null);
            retourSecuriteService.updateRetourSecurite(retourSecurite.getId(), retourSecurite);
        });
        assertEquals("Invalid data: 'numero' cannot be null.", exception.getMessage());
    }

    @Test
    public void testUpdateRetourSecurite_Failure_EntityNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            RetourSecurite nonExistentRS = RetourSecurite.builder().numero(99999L).build();
            retourSecuriteService.updateRetourSecurite(9999, nonExistentRS);
        });
        assertTrue(exception.getMessage().contains("RetourSecurite not found with id:"));
    }

    @Test
    public void testGetRetourSecurite_NonExistent() {
        RetourSecurite rs = retourSecuriteService.getRetourSecurite(9999);
        assertNull(rs);
    }

    @Test
    public void testCreateRetourSecurite_PartialData() {
        RetourSecurite partialRS = RetourSecurite.builder().numero(123456L).build();
        RetourSecurite savedRS = retourSecuriteService.createRetourSecurite(partialRS);

        assertNotNull(savedRS);
        assertEquals(123456L, savedRS.getNumero());
        assertNull(savedRS.getDatesecurite());
        assertNull(savedRS.getCloture());
    }



    @Test
    public void testDeleteRetourSecurite() {
        // Arrange
        RetourSecurite savedRS = retourSecuriteService.createRetourSecurite(retourSecurite);
        //Act
        retourSecuriteService.deleteRetourSecurite(savedRS.getId());
        //Assert
        RetourSecurite deletedRS = retourSecuriteService.getRetourSecurite(savedRS.getId());
        assertNull(deletedRS);
    }




    @Test
    public void testGetAllRetourSecurites() {
        // Arrange
        retourSecuriteRepository.deleteAll();

        Client clientB = new Client();
        clientB.setName("Duterte");
        clientB.setEmail("dduterte@gmail.com");
        clientB = clientRepository.save(clientB);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateClo = dateTimeFormat.parse("2024-10-30");
            Date dateSecu = dateTimeFormat.parse("2024-10-28");
            RetourSecurite rs1 = RetourSecurite.builder()
                    .clients(new ArrayList<>())
                    .cloture(true)
                    .dateCloture(dateClo)
                    .datesecurite(dateSecu)
                    .numero(52645L)
                    .build();
            retourSecuriteService.createRetourSecurite(rs1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Client clientC = new Client();
        clientC.setName("Williamson");
        clientC.setEmail("williamson@gmail.com");
        clientC = clientRepository.save(clientC);

        try {
            Date dateClo = dateTimeFormat.parse("2024-08-10");
            Date dateSecu = dateTimeFormat.parse("2023-12-28");
            RetourSecurite rs2 = RetourSecurite.builder()
                    .clients(new ArrayList<>())
                    .cloture(false)
                    .dateCloture(dateClo)
                    .datesecurite(dateSecu)
                    .numero(55865L)
                    .build();
            retourSecuriteService.createRetourSecurite(rs2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Act
        List<RetourSecurite> listRS = retourSecuriteService.getAllRetourSecurites();

        // Assert
        assertNotNull(listRS);
        assertEquals(2, listRS.size());
    }


    @Test
    @Transactional
    public void testCreateRetourSecurite_Failure_ConstraintViolation() {
        RetourSecurite invalidRetourSecurite = new RetourSecurite();

        assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteService.createRetourSecurite(invalidRetourSecurite);
        }, "Une DataIntegrityViolationException devrait être levée lorsque le numéro est nul");
    }


}
