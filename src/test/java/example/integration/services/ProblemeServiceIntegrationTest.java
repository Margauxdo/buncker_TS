package example.integration.services;

import example.entities.Client;
import example.entities.Probleme;
import example.entities.Valise;
import example.repositories.ProblemeRepository;
import example.services.ProblemeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ProblemeServiceIntegrationTest {
    @Autowired
    private ProblemeService problemeService;
    @Autowired
    private ProblemeRepository problemeRepository;
    private Probleme probleme;

    @BeforeEach
    public void setUp() {
        problemeRepository.deleteAll();

        Client clientA = new Client();
        clientA.setName("Dutoit");
        clientA.setEmail("a.dutoit@gmail.com");
        Valise val = new Valise();
        val.setClient(clientA);
        val.setNumeroValise(1234L);

        probleme = Probleme.builder()
                .detailsProbleme("details du probleme de categorie A")
                .client(clientA)
                .descriptionProbleme("decrire mon probleme")
                .valise(val)
                .build();
    }
    @Test
    public void testCreateProbleme(){
        }
    @Test
    public void testUpdateProbleme(){

    }
    @Test
    public void testDeleteProbleme(){

    }
    @Test
    public void testGetProblemeById(){

    }
    @Test
    public void testGetAllProblemes(){

    }
}
