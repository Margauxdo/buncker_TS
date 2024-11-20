package example.integration.services;

import example.entity.*;
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
import java.util.ArrayList;
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
    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

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

            List<TypeRegle> typeRegles = new ArrayList<>();
            typeRegles.add(tRegle); // Ajout du TypeRegle à la liste

            regle = Regle.builder()
                    .dateRegle(dateRegle)
                    .coderegle("code regleA")
                    .nombreJours(25)
                    .reglePourSortie("regle A1")
                    .typeEntree("entree test")
                    .typeRegles(typeRegles) // Définition de la liste des TypeRegles
                    .formule(form)
                    .valise(val1)
                    .build();
            regle = regleService.createRegle(regle);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateRegle() {
        // Act
        Regle savedR = regleService.createRegle(regle);

        // Assert
        assertNotNull(savedR);
        assertFalse(savedR.getTypeRegles().isEmpty(), "La liste des TypeRegles ne doit pas être vide");
        assertEquals("Type AB", savedR.getTypeRegles().get(0).getNomTypeRegle(), "Le nom du TypeRegle doit correspondre");
        assertNotNull(savedR.getFormule(), "La Formule ne doit pas être null");
        assertNotNull(savedR.getValise(), "La Valise ne doit pas être null");
        assertEquals("regle A1", savedR.getReglePourSortie(), "Le contenu de la règle doit correspondre");
    }

    @Test
    public void testReadRegle() {
        // Arrange
        Regle savedR = regleService.createRegle(regle);

        // Act
        Regle regleId = regleService.readRegle(savedR.getId());

        // Assert
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
        savedR.setReglePourSortie("regle B1");
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
        assertNotNull(allRegles, "La liste des règles ne doit pas être null");
        assertEquals(2, allRegles.size(), "La liste doit contenir 2 règles");
        assertTrue(allRegles.stream().anyMatch(r -> "codeA".equals(r.getCoderegle()) && "type1".equals(r.getTypeEntree())));
        assertTrue(allRegles.stream().anyMatch(r -> "codeB".equals(r.getCoderegle()) && "type2".equals(r.getTypeEntree())));
    }

    @Test
    public void testDeleteRegleCascade() {
        // Arrange
        Regle regle = new Regle();
        regle.setCoderegle("CascadeTest");
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(new Date());
        sortieSemaine.setRegle(regle);
        regle.getSortieSemaine().add(sortieSemaine);
        regleRepository.saveAndFlush(regle);

        // Act
        regleService.deleteRegle(regle.getId());

        // Assert
        Assertions.assertFalse(sortieSemaineRepository.findById(sortieSemaine.getId()).isPresent(),
                "SortieSemaine devrait être supprimé en cascade");
        Assertions.assertFalse(regleRepository.findById(regle.getId()).isPresent(),
                "Regle devrait être supprimée avec succès");
    }
}
