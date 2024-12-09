package example.integration.services;

import example.DTO.RetourSecuriteDTO;
import example.entity.Client;
import example.repositories.ClientRepository;
import example.repositories.RetourSecuriteRepository;
import example.services.RetourSecuriteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class RetourSecuriteServiceIntegrationTest {

    @Autowired
    private RetourSecuriteService retourSecuriteService;
    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;
    @Autowired
    private ClientRepository clientRepository;

    private RetourSecuriteDTO retourSecuriteDTO;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
        clientRepository.deleteAll();

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateCloture = dateTimeFormat.parse("2024-10-30");
            Date dateSecurite = dateTimeFormat.parse("2024-10-28");

            retourSecuriteDTO = RetourSecuriteDTO.builder()
                    .numero(12345L)
                    .dateCloture(dateCloture)
                    .datesecurite(dateSecurite)
                    .cloture(true)
                    .build();

            retourSecuriteDTO = retourSecuriteService.createRetourSecurite(retourSecuriteDTO);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateRetourSecurite_Success() {
        RetourSecuriteDTO newRS = RetourSecuriteDTO.builder()
                .numero(123456L)
                .cloture(false)
                .build();

        RetourSecuriteDTO savedRS = retourSecuriteService.createRetourSecurite(newRS);

        assertNotNull(savedRS);
        assertNotNull(savedRS.getId());
        assertEquals(123456L, savedRS.getNumero());
        assertFalse(savedRS.getCloture());
    }

    @Test
    public void testUpdateRetourSecurite_Success() {
        retourSecuriteDTO.setNumero(54321L);
        retourSecuriteDTO.setCloture(false);

        RetourSecuriteDTO updatedRS = retourSecuriteService.updateRetourSecurite(retourSecuriteDTO.getId(), retourSecuriteDTO);

        assertNotNull(updatedRS);
        assertEquals(54321L, updatedRS.getNumero());
        assertFalse(updatedRS.getCloture());
    }

    @Test
    public void testUpdateRetourSecurite_Failure_EntityNotFound() {
        RetourSecuriteDTO nonExistentRS = RetourSecuriteDTO.builder().numero(99999L).build();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            retourSecuriteService.updateRetourSecurite(9999, nonExistentRS);
        });

        assertTrue(exception.getMessage().contains("RetourSecurite not found with id:"));
    }

    @Test
    public void testDeleteRetourSecurite_Success() {
        retourSecuriteService.deleteRetourSecurite(retourSecuriteDTO.getId());

        RetourSecuriteDTO deletedRS = retourSecuriteService.getRetourSecurite(retourSecuriteDTO.getId());
        assertNull(deletedRS);
    }

    @Test
    public void testGetRetourSecurite_Success() {
        RetourSecuriteDTO fetchedRS = retourSecuriteService.getRetourSecurite(retourSecuriteDTO.getId());

        assertNotNull(fetchedRS);
        assertEquals(retourSecuriteDTO.getId(), fetchedRS.getId());
    }

    @Test
    public void testGetRetourSecurite_Failure_NotFound() {
        RetourSecuriteDTO fetchedRS = retourSecuriteService.getRetourSecurite(9999);

        assertNull(fetchedRS);
    }

    @Test
    public void testGetAllRetourSecurites() {
        retourSecuriteService.createRetourSecurite(RetourSecuriteDTO.builder().numero(54321L).build());

        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();

        assertNotNull(retourSecurites);
        assertTrue(retourSecurites.size() >= 2);
    }

    @Test
    public void testCreateRetourSecurite_Failure_ConstraintViolation() {
        RetourSecuriteDTO invalidRS = new RetourSecuriteDTO();

        assertThrows(DataIntegrityViolationException.class, () -> {
            retourSecuriteService.createRetourSecurite(invalidRS);
        });
    }
}
