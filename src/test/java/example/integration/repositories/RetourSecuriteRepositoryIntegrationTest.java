package example.integration.repositories;

import example.entity.RetourSecurite;
import example.repositories.RetourSecuriteRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class RetourSecuriteRepositoryIntegrationTest {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @BeforeEach
    public void setUp() {
        retourSecuriteRepository.deleteAll();
    }

    @Test
    public void testSaveRetourSecuriteSuccess() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(12345L)
                .datesecurite(new Date())
                .cloture(false)
                .build();

        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);

        assertNotNull(savedRetourSecurite.getId());
        assertEquals(12345L, savedRetourSecurite.getNumero());
        assertFalse(savedRetourSecurite.getCloture());
    }

    @Test
    public void testFindByIdSuccess() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(67890L)
                .datesecurite(new Date())
                .cloture(true)
                .build();

        retourSecurite = retourSecuriteRepository.save(retourSecurite);

        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(retourSecurite.getId());

        assertTrue(foundRetourSecurite.isPresent());
        assertEquals(67890L, foundRetourSecurite.get().getNumero());
    }

    @Test
    public void testFindByNumeroSuccess() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(54321L)
                .datesecurite(new Date())
                .cloture(true)
                .build();

        retourSecuriteRepository.save(retourSecurite);

        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findByNumero(54321L);

        assertTrue(foundRetourSecurite.isPresent());
        assertEquals(54321L, foundRetourSecurite.get().getNumero());
    }

    @Test
    public void testDeleteRetourSecuriteSuccess() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(98765L)
                .datesecurite(new Date())
                .cloture(false)
                .build();

        retourSecurite = retourSecuriteRepository.save(retourSecurite);
        retourSecuriteRepository.deleteById(retourSecurite.getId());

        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(retourSecurite.getId());
        assertFalse(foundRetourSecurite.isPresent());
    }

    @Test
    @Transactional
    public void testUpdateRetourSecuriteSuccess() {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .numero(13579L)
                .datesecurite(new Date())
                .cloture(false)
                .clients(new ArrayList<>())
                .build();

        retourSecurite = retourSecuriteRepository.save(retourSecurite);

        // Find the saved RetourSecurite
        Optional<RetourSecurite> foundRetourSecurite = retourSecuriteRepository.findById(retourSecurite.getId());
        assertTrue(foundRetourSecurite.isPresent());

        RetourSecurite existingRetourSecurite = foundRetourSecurite.get();
        existingRetourSecurite.setCloture(true);
        existingRetourSecurite.setDateCloture(new Date());
        existingRetourSecurite.getClients().clear();

        retourSecuriteRepository.save(existingRetourSecurite);

        Optional<RetourSecurite> updatedRetourSecurite = retourSecuriteRepository.findById(existingRetourSecurite.getId());
        assertTrue(updatedRetourSecurite.isPresent());
        assertTrue(updatedRetourSecurite.get().getCloture());
        assertNotNull(updatedRetourSecurite.get().getDateCloture());
    }




}

