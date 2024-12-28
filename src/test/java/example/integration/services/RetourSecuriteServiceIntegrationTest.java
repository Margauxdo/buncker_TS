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
                    .dateCloture(dateCloture)
                    .datesecurite(dateSecurite)
                    .cloture(true)
                    .build();

            retourSecuriteDTO = retourSecuriteService.createRetourSecurite(retourSecuriteDTO);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
