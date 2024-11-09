package example.integration.repositories;

import example.entities.Regle;
import example.entities.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
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
public class TypeRegleRepositoryIntegrationTest {
    @Autowired
    private TypeRegleRepository typeRegleRepository;


    @BeforeEach
    public void setUp() {
        typeRegleRepository.deleteAll();
    }
    @Test
    public void testSaveTypeRegle(){
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("type A");
        TypeRegle tR = typeRegleRepository.save(typeRegle);
        assertNotNull(tR.getId());
        assertEquals("type A", tR.getNomTypeRegle());

    }
    @Test
    public void testFindTypeRegleById(){
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("type A");
        TypeRegle savedTR = typeRegleRepository.save(typeRegle);
        Optional<TypeRegle> foundTR = typeRegleRepository.findById(savedTR.getId());
        assertTrue(foundTR.isPresent());
        assertEquals("type A", foundTR.get().getNomTypeRegle());

    }
    @Test
    public void testDeleteTypeRegle(){
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("type A");
        typeRegleRepository.save(typeRegle);
        typeRegleRepository.deleteById(typeRegle.getId());
        Optional<TypeRegle> foundTR = typeRegleRepository.findById(typeRegle.getId());
        assertFalse(foundTR.isPresent());
    }
    @Test
    public void testFindByIdNotFound(){
        List<TypeRegle> typeRegles = typeRegleRepository.findAll();
        assertTrue(typeRegles.isEmpty());
    }
    @Test
    public void testUpdateTypeRegle(){
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("type A");
        TypeRegle savedTR= typeRegleRepository.save(typeRegle);
        savedTR.setNomTypeRegle("type B");
        TypeRegle updatedTR = typeRegleRepository.save(savedTR);
        Optional<TypeRegle> foundTR = typeRegleRepository.findById(updatedTR.getId());
        assertTrue(foundTR.isPresent());
        assertEquals("type B", foundTR.get().getNomTypeRegle());

    }
    @Test
    public void testFindAllTypeRegle(){
        TypeRegle typeRegleA = new TypeRegle();
        typeRegleA.setNomTypeRegle("type A");
        typeRegleRepository.save(typeRegleA);
        TypeRegle typeRegleB = new TypeRegle();
        typeRegleB.setNomTypeRegle("type B");
        typeRegleRepository.save(typeRegleB);
        List<TypeRegle> typeRegles = typeRegleRepository.findAll();
        assertNotNull(typeRegles);
        assertTrue(typeRegles.size() >= 2);
        typeRegles.sort(Comparator.comparing(TypeRegle::getNomTypeRegle));
        assertEquals("type A", typeRegles.get(0).getNomTypeRegle());
        assertEquals("type B", typeRegles.get(1).getNomTypeRegle());

    }
    @Test
    public void testFindByNomTypeRegle() {
        TypeRegle typeRegleA = new TypeRegle();
        typeRegleA.setNomTypeRegle("type A");
        typeRegleRepository.save(typeRegleA);

        TypeRegle typeRegleB = new TypeRegle();
        typeRegleB.setNomTypeRegle("type B");
        typeRegleRepository.save(typeRegleB);

        List<TypeRegle> foundTypeRegles = typeRegleRepository.findByNomTypeRegle("type A");
        assertNotNull(foundTypeRegles);
        assertEquals(1, foundTypeRegles.size());
        assertEquals("type A", foundTypeRegles.get(0).getNomTypeRegle());
    }
    @Test
    public void testSaveTypeRegleWithInvalidData() {
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle(null);

        assertThrows(Exception.class, () -> {
            typeRegleRepository.save(typeRegle);
        }, "An exception should be thrown if the name is null");
    }


    @Test
    public void testSaveDuplicateTypeRegle() {
        TypeRegle typeRegleA = new TypeRegle();
        typeRegleA.setNomTypeRegle("type A");
        typeRegleRepository.save(typeRegleA);

        TypeRegle typeRegleB = new TypeRegle();
        typeRegleB.setNomTypeRegle("type A");

        assertThrows(Exception.class, () -> {
            typeRegleRepository.save(typeRegleB);
        }, "An exception should be thrown if a duplicate name is inserted");
    }




}
