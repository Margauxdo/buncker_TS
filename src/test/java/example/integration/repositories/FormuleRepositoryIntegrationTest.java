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
    public void testDeleteFormule(){

        Formule formule = new Formule();
        formule.setLibelle("libelle 1234");
        formule.setFormule("formule test");
        Formule savedF = formuleRepository.save(formule);
        formuleRepository.deleteById(savedF.getId());
        Optional<Formule> foundF = formuleRepository.findById(savedF.getId());
        assertFalse(foundF.isPresent());
    }
    @Test
    public void testFindByIdNotFound(){
        List<Formule> foundF = formuleRepository.findAll();
        assertTrue(foundF.isEmpty());
    }
    @Test
    public void testUpdateFormuleSuccess(){
        Formule formule = new Formule();
        formule.setLibelle("libelle 1234");
        formule.setFormule("formule test");

        Formule savedF = formuleRepository.save(formule);
        savedF.setLibelle("libelle updated");
        savedF.setFormule("formule updated");
        Formule updatedF = formuleRepository.save(savedF);
        Optional<Formule> foundF = formuleRepository.findById(updatedF.getId());
        assertTrue(foundF.isPresent());
        assertEquals("libelle updated", foundF.get().getLibelle());
        assertEquals("formule updated", foundF.get().getFormule());


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

        List<Formule> foundF = formuleRepository.findAll();
        assertNotNull(foundF);
        assertTrue(foundF.size() >= 2);

        foundF.sort(Comparator.comparing(Formule::getLibelle));

        assertEquals("libelle 1234", foundF.get(0).getLibelle());
        assertEquals("formule test1", foundF.get(0).getFormule());

        assertEquals("libelle 5678", foundF.get(1).getLibelle());
        assertEquals("formule test2", foundF.get(1).getFormule());

        foundF.forEach(formule -> System.out.println(formule.getFormule()));
    }

    @Test
    public void testSaveFormuleInvalidInput() {
        Formule formule = new Formule();
        // Ne pas définir les champs obligatoires comme "libelle"
        assertThrows(Exception.class, () -> {
            formuleRepository.save(formule);
        });
    }
    











}
