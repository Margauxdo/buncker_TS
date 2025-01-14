package example.integration.entity;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
public class TypeValiseIntegrationTest {

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        typeValiseRepository.deleteAll();
        clientRepository.deleteAll();



    }

    @Test
    public void testSaveTypeValiseSuccess() {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("This is a test");
        typeValise.setProprietaire("Jean Bernard");

        TypeValise savedTV = typeValiseRepository.saveAndFlush(typeValise);

        Assertions.assertNotNull(savedTV.getId());
        Assertions.assertEquals("This is a test", savedTV.getDescription());
        Assertions.assertEquals("Jean Bernard", savedTV.getProprietaire());
    }

    @Test
    public void testSaveTypeValiseFailure() {
        TypeValise typeValise = new TypeValise();
        assertThrows(DataIntegrityViolationException.class, () -> {
            typeValiseRepository.saveAndFlush(typeValise);
        });
    }

    @Test
    public void testFindTypeValiseByIdSuccess() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Jean Bernard");
        typeValise.setDescription("This is a test");

        TypeValise savedTV = typeValiseRepository.saveAndFlush(typeValise);

        Optional<TypeValise> foundTV = typeValiseRepository.findById(savedTV.getId());
        assertTrue(foundTV.isPresent());
        Assertions.assertEquals("Jean Bernard", foundTV.get().getProprietaire());
    }

    @Test
    public void testFindTypeValiseByIdNotFound() {
        Optional<TypeValise> foundTV = typeValiseRepository.findById(-1);
        assertFalse(foundTV.isPresent());
    }

    @Test
    public void testUpdateTypeValiseSuccess() {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("This is a test");
        typeValise.setProprietaire("Jean Bernard");
        TypeValise savedTV = typeValiseRepository.saveAndFlush(typeValise);
        savedTV.setDescription("This is a new test");
        savedTV.setProprietaire("Toto");
        TypeValise updatedTV = typeValiseRepository.saveAndFlush(savedTV);
        Assertions.assertEquals("Toto", updatedTV.getProprietaire());
        Assertions.assertEquals("This is a new test", updatedTV.getDescription());
    }

    @Test
    public void testUpdateTypeValiseFailure() {
        Optional<TypeValise> foundTV = typeValiseRepository.findById(-1);
        assertFalse(foundTV.isPresent());
    }

    @Test
    public void testDeleteTypeValiseSuccess() {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Delete this a test");
        typeValise.setProprietaire("Delete Jean Bernard");
        TypeValise savedTV = typeValiseRepository.saveAndFlush(typeValise);

        typeValiseRepository.deleteById(savedTV.getId());
        typeValiseRepository.flush();

        Optional<TypeValise> foundTV = typeValiseRepository.findById(savedTV.getId());
        assertFalse(foundTV.isPresent());
    }

    @Test
    public void testDeleteTypeValiseFailure() {
        Optional<TypeValise> foundTV = typeValiseRepository.findById(-1);
        assertFalse(foundTV.isPresent());
    }

    @Test
    public void testUniqueConstraintOnTypeValise() {
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setDescription("This is a test unique constraint");
        typeValiseA.setProprietaire("Jean Bernard");
        typeValiseRepository.saveAndFlush(typeValiseA);

        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setDescription("This is a test unique constraint");
        typeValiseB.setProprietaire("Jean Bernard");

        assertThrows(DataIntegrityViolationException.class, () -> {
            typeValiseRepository.saveAndFlush(typeValiseB);
        });
    }

    @Test
    public void testSaveTypeValiseWithoutProprietaireShouldFail() {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("Test without owner");

        assertThrows(DataIntegrityViolationException.class, () -> {
            typeValiseRepository.saveAndFlush(typeValise);
        });
    }

    @Test
    public void testSaveTypeValiseWithoutDescriptionShouldFail() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Test Owner");

        assertThrows(DataIntegrityViolationException.class, () -> {
            typeValiseRepository.saveAndFlush(typeValise);
        });
    }

    @Test
    public void testDeleteTypeValiseWithoutValisesShouldSucceed() {
        TypeValise typeValise = new TypeValise();
        typeValise.setDescription("TypeValise without valises");
        typeValise.setProprietaire("Proprietaire Test");

        typeValiseRepository.saveAndFlush(typeValise);

        typeValiseRepository.deleteById(typeValise.getId());
        typeValiseRepository.flush();

        Optional<TypeValise> foundTypeValise = typeValiseRepository.findById(typeValise.getId());
        assertFalse(foundTypeValise.isPresent());
    }

    @Test
    public void testUniqueConstraintOnDescription() {
        TypeValise typeValise1 = new TypeValise();
        typeValise1.setDescription("Single Description");
        typeValise1.setProprietaire("Owner A");
        typeValiseRepository.saveAndFlush(typeValise1);

        TypeValise typeValise2 = new TypeValise();
        typeValise2.setDescription("Single Description");
        typeValise2.setProprietaire("Owner B");
        typeValiseRepository.saveAndFlush(typeValise2);

    }




}
