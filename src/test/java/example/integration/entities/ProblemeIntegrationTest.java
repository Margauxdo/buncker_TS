package example.integration.entities;

import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class ProblemeIntegrationTest {

    @Autowired
    private ProblemeRepository problemeRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveProbleme() {
    }
    @Test
    public void testFindProblemeById() {

    }

    @Test
    public void testSaveProblemeWithDuplicateDescriptionThrowsException() {
    }

    @Test
    public void testUpdateProbleme() {
    }

    @Test
    public void testDeleteProbleme() {

    }
    @Test
    public void testFindAllProblemes() {

    }
    @Test
    public void testCascadeDeleteProblemeWithValise() {
    }
    }

