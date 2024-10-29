package example.integration.services;

import example.entities.*;
import example.exceptions.RegleNotFoundException;
import example.repositories.*;
import example.services.RegleService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class RegleServiceIntegrationTest {

    @Autowired
    private RegleService regleService;
    @Autowired
    private RegleRepository regleRepository;
    private Regle regle;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private TypeRegleRepository typeRegleRepository;
    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    void setUp() {
        regleRepository.deleteAll();

        Client clientA = new Client();
        clientA.setName("Dutoit");
        clientA.setEmail("a.dutoit@gmail.com");
        clientA = clientRepository.save(clientA);

        Valise val1 = new Valise();
        val1.setClient(clientA);
        val1.setNumeroValise(1234L);
        val1 = valiseRepository.save(val1);

        TypeRegle tRegle = new TypeRegle();
        tRegle.setNomTypeRegle("Type AB");
        tRegle = typeRegleRepository.save(tRegle);

        Formule form = new Formule();
        form.setLibelle("libelle test");
        form = formuleRepository.save(form);

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateRegle = dateTimeFormat.parse("2024-10-30");

            regle = Regle.builder()
                    .dateRegle(dateRegle)
                    .coderegle("code regleA")
                    .nombreJours(25)
                    .reglePourSortie("regle A1")
                    .typeEntree("entree test")
                    .typeRegle(tRegle)
                    .formule(form)
                    .valise(val1)
                    .build();
            regle = regleService.createRegle(regle);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateRegle(){
        // Act
        Regle savedR = regleService.createRegle(regle);

        // Assert
        assertNotNull(savedR);
        assertNotNull(savedR.getTypeRegle());
        assertNotNull(savedR.getFormule());
        assertNotNull(savedR.getValise());
        assertEquals("regle A1", savedR.getReglePourSortie());
    }


@Test
    public void testReadRegle(){
        //Arrange
        Regle savedR = regleService.createRegle(regle);
        //Act
        Regle regleId = regleService.readRegle(savedR.getId());
        //Assert
        assertNotNull(regleId);
        assertEquals("regle A1", regleId.getReglePourSortie());

    }
    @Test
    public void testUpdateRegle() {
        // Arrange
        Regle savedR = regleService.createRegle(regle);

        // Act
        savedR.setCoderegle("code regle B1");
        savedR.setTypeEntree("entree testB");
        savedR.setReglePourSortie("regle B1");  // Ensure this field is updated
        Regle updatedRegle = regleService.updateRegle(savedR.getId(), savedR);

        // Assert
        assertNotNull(updatedRegle);
        assertEquals("regle B1", updatedRegle.getReglePourSortie());
    }

    @Test
    public void testDeleteRegle() {
        // Arrange
        Regle savedR = regleService.createRegle(regle);

        // Act
        regleService.deleteRegle(savedR.getId());

        // Assert
        Assertions.assertThrows(RegleNotFoundException.class, () -> {
            regleService.readRegle(savedR.getId());
        });
    }

    @Test
    public void testReadAllRegles() {
        // Arrange
        regleRepository.deleteAll();

        Regle regle1 = Regle.builder()
                .coderegle("codeA")
                .typeEntree("type1")
                .reglePourSortie("sortie1")
                .build();

        Regle regle2 = Regle.builder()
                .coderegle("codeB")
                .typeEntree("type2")
                .reglePourSortie("sortie2")
                .build();

        regleService.createRegle(regle1);
        regleService.createRegle(regle2);

        // Act
        List<Regle> allRegles = regleService.readAllRegles();

        // Assert
        assertNotNull(allRegles, "The list of regles should not be null");
        assertEquals(2, allRegles.size(), "The list should contain 2 regles");

        assertTrue(allRegles.stream().anyMatch(r -> "codeA".equals(r.getCoderegle()) && "type1".equals(r.getTypeEntree())));
        assertTrue(allRegles.stream().anyMatch(r -> "codeB".equals(r.getCoderegle()) && "type2".equals(r.getTypeEntree())));
    }



}
