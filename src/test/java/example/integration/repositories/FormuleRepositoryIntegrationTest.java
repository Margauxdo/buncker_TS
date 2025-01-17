package example.integration.repositories;

import example.entity.Formule;
import example.entity.Regle;
import example.repositories.FormuleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class FormuleRepositoryIntegrationTest {

    @Autowired
    private FormuleRepository formuleRepository;

    @BeforeEach
    public void setUp() {
        formuleRepository.deleteAll();
    }

    @Test
    public void testSaveAndDeleteFormule() {
        Formule formule = new Formule();
        formule.setLibelle("libelle 1234");
        formule.setFormule("formule test");

        Formule savedFormule = formuleRepository.save(formule);
        assertNotNull(savedFormule.getId());

        formuleRepository.deleteById(savedFormule.getId());
        Optional<Formule> foundFormule = formuleRepository.findById(savedFormule.getId());
        assertFalse(foundFormule.isPresent());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Formule> foundFormule = formuleRepository.findById(-1);
        assertFalse(foundFormule.isPresent());
    }

    @Test
    public void testUpdateFormuleSuccess() {
        Formule formule = new Formule();
        formule.setLibelle("libelle 1234");
        formule.setFormule("formule test");

        Formule savedFormule = formuleRepository.save(formule);
        savedFormule.setLibelle("libelle updated");
        savedFormule.setFormule("formule updated");

        Formule updatedFormule = formuleRepository.save(savedFormule);
        Optional<Formule> foundFormule = formuleRepository.findById(updatedFormule.getId());
        assertTrue(foundFormule.isPresent());
        assertEquals("libelle updated", foundFormule.get().getLibelle());
        assertEquals("formule updated", foundFormule.get().getFormule());
    }

    @Test
    public void testFindAllFormules() {
        Formule formule1 = new Formule();
        formule1.setLibelle("libelle 1234");
        formule1.setFormule("formule test1");
        formuleRepository.save(formule1);

        Formule formule2 = new Formule();
        formule2.setLibelle("libelle 5678");
        formule2.setFormule("formule test2");
        formuleRepository.save(formule2);

        List<Formule> foundFormules = formuleRepository.findAll();
        assertNotNull(foundFormules);
        assertTrue(foundFormules.size() >= 2);

        foundFormules.sort(Comparator.comparing(Formule::getLibelle));

        assertEquals("libelle 1234", foundFormules.get(0).getLibelle());
        assertEquals("libelle 5678", foundFormules.get(1).getLibelle());
    }

    @Test
    public void testFindByLibelle() {
        Formule formule = new Formule();
        formule.setLibelle("unique libelle");
        formule.setFormule("formule test");
        formuleRepository.save(formule);

        List<Formule> foundFormules = formuleRepository.findByLibelle("unique libelle");
        assertNotNull(foundFormules);
        assertEquals(1, foundFormules.size());
        assertEquals("unique libelle", foundFormules.get(0).getLibelle());
    }

    @Test
    public void testFindByIdWithRegles() {
        Formule formule = new Formule();
        formule.setLibelle("libelle with regles");
        formule.setFormule("formule with regles");

        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regle.setFormule(formule);

        formule.setRegles(List.of(regle));
        Formule savedFormule = formuleRepository.save(formule);

        Formule foundFormule = formuleRepository.findByIdWithRegles(savedFormule.getId());
        assertNotNull(foundFormule);
        assertEquals(1, foundFormule.getRegles().size());
        assertEquals("R001", foundFormule.getRegles().get(0).getCoderegle());
    }


}
