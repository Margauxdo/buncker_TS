package example.integration.entities;

import example.entities.Regle;
import example.entities.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class TypeRegleIntegrationTest {

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        typeRegleRepository.deleteAll();
    }

    @Test
    public void testSaveTypeRegleSuccess() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Valid TypeRegle");

        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        assertNotNull(savedTypeRegle.getId());
        assertEquals("Valid TypeRegle", savedTypeRegle.getNomTypeRegle());
    }

    @Test
    public void testSaveTypeRegleFailure() {
        TypeRegle typeRegle = new TypeRegle();
        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle);
        });
    }

    @Test
    public void testFindTypeRegleByIdSuccess() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Test Find");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());
        assertTrue(foundTypeRegle.isPresent());
        assertEquals("Test Find", foundTypeRegle.get().getNomTypeRegle());
    }

    @Test
    public void testFindTypeRegleByIdNotFound() {
        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(-1);
        assertFalse(foundTypeRegle.isPresent());
    }

    @Test
    public void testUpdateTypeRegleSuccess() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Old Name");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        savedTypeRegle.setNomTypeRegle("New Name");
        TypeRegle updatedTypeRegle = typeRegleRepository.saveAndFlush(savedTypeRegle);

        assertEquals("New Name", updatedTypeRegle.getNomTypeRegle());
    }



    @Test
    public void testDeleteTypeRegleSuccess() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Delete Test");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        typeRegleRepository.deleteById(savedTypeRegle.getId());
        typeRegleRepository.flush();

        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());
        assertFalse(foundTypeRegle.isPresent());
    }
    @Test
    public void testUniqueConstraintOnTypeRegleName() {
        TypeRegle typeRegle1 = new TypeRegle();
        typeRegle1.setNomTypeRegle("SingleName");
        typeRegleRepository.saveAndFlush(typeRegle1);

        TypeRegle typeRegle2 = new TypeRegle();
        typeRegle2.setNomTypeRegle("SingleName");

        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle2);
        });
    }
    @Test
    public void testCascadeDeleteTypeRegleWithRegles() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type A");

        Regle regle1 = new Regle();
        regle1.setCoderegle("R001");
        regle1.setTypeRegle(typeRegle);

        Regle regle2 = new Regle();
        regle2.setCoderegle("R002");
        regle2.setTypeRegle(typeRegle);

        typeRegle.getListTypesRegles().add(regle1);
        typeRegle.getListTypesRegles().add(regle2);

        typeRegleRepository.saveAndFlush(typeRegle);

        typeRegleRepository.deleteById(typeRegle.getId());
        typeRegleRepository.flush();

        assertFalse(regleRepository.findById(regle1.getId()).isPresent());
        assertFalse(regleRepository.findById(regle2.getId()).isPresent());
    }













}
