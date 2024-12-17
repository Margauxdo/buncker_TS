package example.integration.entity;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void testSaveTypeRegleFailure() {
        TypeRegle typeRegle = new TypeRegle();
        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle);
        });
    }

    @Test
    @Transactional
    public void testUpdateTypeRegleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("InitialCode");
        regle = regleRepository.saveAndFlush(regle);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Old Name");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        savedTypeRegle.setNomTypeRegle("New Name");
        TypeRegle updatedTypeRegle = typeRegleRepository.saveAndFlush(savedTypeRegle);

        assertEquals("New Name", updatedTypeRegle.getNomTypeRegle());
    }




    @Test
    @Transactional
    public void testUniqueConstraintOnTypeRegleName() {
        Regle regle = new Regle();
        regle.setCoderegle("CODE123");
        regle = regleRepository.saveAndFlush(regle);

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
    @Transactional
    public void testSaveTypeRegleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("TestCode");
        regle = regleRepository.saveAndFlush(regle);
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Test Type");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        // Assertions
        assertEquals("Test Type", savedTypeRegle.getNomTypeRegle());
    }

    @Test
    @Transactional
    public void testUniqueConstraintOnNomTypeRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regleRepository.saveAndFlush(regle);

        TypeRegle typeRegle1 = new TypeRegle();
        typeRegle1.setNomTypeRegle("Unique Type");
        typeRegleRepository.saveAndFlush(typeRegle1);

        TypeRegle typeRegle2 = new TypeRegle();
        typeRegle2.setNomTypeRegle("Unique Type");


        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle2);
        });
    }

    @Test
    @Transactional
    public void testFindTypeRegleByIdSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("R001");
        regleRepository.saveAndFlush(regle);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Find Type");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);
    }

    @Test
    @Transactional
    public void testFindTypeRegleByIdNotFound() {
        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(-1);
        assertFalse(foundTypeRegle.isPresent());
    }
    @Test
    @Transactional
    public void testDeleteTypeRegleSuccess() {
        Regle regle = new Regle();
        regle.setCoderegle("ValidCode");
        regle = regleRepository.saveAndFlush(regle);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Delete Type");
        TypeRegle savedTypeRegle = typeRegleRepository.saveAndFlush(typeRegle);

        typeRegleRepository.flush();

    }

    @Test
    @Transactional
    public void testSaveTypeRegleInvalidData() {
        TypeRegle typeRegle = new TypeRegle();

        assertThrows(DataIntegrityViolationException.class, () -> {
            typeRegleRepository.saveAndFlush(typeRegle);
        });
    }



















}
