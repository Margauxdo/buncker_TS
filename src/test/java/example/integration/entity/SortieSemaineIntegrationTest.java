package example.integration.entity;

import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class SortieSemaineIntegrationTest {

    @Autowired
    private SortieSemaineRepository semaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        semaineRepository.deleteAll();
        regleRepository.deleteAll();
    }


    @Test
    public void testFindSortieSemaineByIdNotFound() {
        Optional<SortieSemaine> foundSortie = semaineRepository.findById(9999);
        Assertions.assertFalse(foundSortie.isPresent(), "No output should be found");
    }


}
