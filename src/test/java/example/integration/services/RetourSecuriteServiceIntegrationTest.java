package example.integration.services;

import example.entity.Client;
import example.entity.RetourSecurite;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import example.services.RetourSecuriteService;
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
    public void testCreateRetourSecurite() {
        // Act
        RetourSecurite savedRetourSecu = retourSecuriteService.createRetourSecurite(retourSecurite);

        // Assert
        assertNotNull(savedRetourSecu);
        assertNotNull(savedRetourSecu.getNumero());
        assertNotNull(savedRetourSecu.getCloture());
        assertNotNull(savedRetourSecu.getDateCloture());
        assertNotNull(savedRetourSecu.getDatesecurite());
        assertNotNull(savedRetourSecu.getClients());
        assertEquals("Dutoit", savedRetourSecu.getClients().get(0).getName());
        assertEquals("a.dutoit@gmail.com", savedRetourSecu.getClients().get(0).getEmail());

    }

    @Test
    public void testUpdateRetourSecurite() {
        // Arrange
        RetourSecurite savedRS = retourSecuriteService.createRetourSecurite(retourSecurite);
        // Act
        savedRS.setNumero(4856L);
        savedRS.setCloture(false);
        savedRS.setDateCloture(new Date());
        savedRS.setDatesecurite(new Date());
        RetourSecurite updatedRS = retourSecuriteService.updateRetourSecurite(savedRS.getId(), savedRS);
        //Assert
        assertNotNull(updatedRS);
        assertEquals("Dutoit", updatedRS.getClients().get(0).getName());
        assertEquals("a.dutoit@gmail.com", updatedRS.getClients().get(0).getEmail());

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
    public void testGetRetourSecurite() {
        //Arrange
        RetourSecurite savedRS = retourSecuriteService.createRetourSecurite(retourSecurite);
        //Act
        RetourSecurite rsById = retourSecuriteService.getRetourSecurite(savedRS.getId());
        //Assert
        assertNotNull(rsById);
        assertEquals("Dutoit", rsById.getClients().get(0).getName());
        assertEquals("a.dutoit@gmail.com", rsById.getClients().get(0).getEmail());

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
    public void testCascadeDeleteClient() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client = clientRepository.save(client);

        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setClients(new ArrayList<>());
        retourSecurite.setNumero(12345L);
        retourSecurite = retourSecuriteRepository.save(retourSecurite);

        retourSecuriteRepository.delete(retourSecurite);

        Optional<Client> foundClient = clientRepository.findById(client.getId());
        Assertions.assertTrue(foundClient.isEmpty(), "Client should be deleted due to cascade.");
    }


    @Test
    public void testCreateRetourSecurite_Failure_ConstraintViolation() {
        RetourSecurite invalidRetourSecurite = new RetourSecurite(); // Entité sans numéro

        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteService.createRetourSecurite(invalidRetourSecurite);
        });

        Assertions.assertTrue(exception.getMessage().contains("could not execute statement"));
    }



}
