package example.integration.repositories;

import example.entities.Regle;
import example.entities.SortieSemaine;
import example.repositories.SortieSemaineRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class SortieSemaineRepositoryIntegrationTest {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
    }

    @Test
    public void testSaveSortieSemaine() {

    }


}
