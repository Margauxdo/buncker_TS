package example.integration.repositories;

import example.entity.TypeValise;
import example.repositories.TypeValiseRepository;
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
public class TypeValiseRepositoryIntegrationTest {

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @BeforeEach
    public void setUp() {
        typeValiseRepository.deleteAll();
    }

    @Test
    public void testSaveTypeValise() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        assertNotNull(savedTypeValise.getId(), "L'ID ne doit pas être null après l'enregistrement.");
        assertEquals("Arthur Menart", savedTypeValise.getProprietaire(), "Le propriétaire doit correspondre.");
        assertEquals("Description de la valise", savedTypeValise.getDescription(), "La description doit correspondre.");
    }

    @Test
    public void testFindTypeValiseById() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        Optional<TypeValise> foundTypeValise = typeValiseRepository.findById(savedTypeValise.getId());

        assertTrue(foundTypeValise.isPresent(), "Le type de valise devrait être trouvé par ID.");
        assertEquals("Arthur Menart", foundTypeValise.get().getProprietaire(), "Le propriétaire doit correspondre.");
    }

    @Test
    public void testUpdateTypeValise() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description initiale");

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        savedTypeValise.setProprietaire("Jean Dupont");
        savedTypeValise.setDescription("Description mise à jour");

        TypeValise updatedTypeValise = typeValiseRepository.save(savedTypeValise);

        assertEquals("Jean Dupont", updatedTypeValise.getProprietaire(), "Le propriétaire doit être mis à jour.");
        assertEquals("Description mise à jour", updatedTypeValise.getDescription(), "La description doit être mise à jour.");
    }

    @Test
    public void testDeleteTypeValise() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        typeValiseRepository.delete(savedTypeValise);

        Optional<TypeValise> deletedTypeValise = typeValiseRepository.findById(savedTypeValise.getId());
        assertFalse(deletedTypeValise.isPresent(), "Le type de valise devrait être supprimé.");
    }

    @Test
    public void testFindAllTypeValises() {
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setProprietaire("Arthur Menart");
        typeValiseA.setDescription("Description A");
        typeValiseRepository.save(typeValiseA);

        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setProprietaire("Jean Dupont");
        typeValiseB.setDescription("Description B");
        typeValiseRepository.save(typeValiseB);

        List<TypeValise> typeValises = typeValiseRepository.findAll();

        assertEquals(2, typeValises.size(), "Le nombre de types de valises doit être 2.");
        typeValises.sort(Comparator.comparing(TypeValise::getProprietaire));
        assertEquals("Arthur Menart", typeValises.get(0).getProprietaire(), "Le premier propriétaire doit être Arthur Menart.");
        assertEquals("Jean Dupont", typeValises.get(1).getProprietaire(), "Le deuxième propriétaire doit être Jean Dupont.");
    }

    @Test
    public void testUniqueConstraintViolation() {
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setProprietaire("Arthur Menart");
        typeValiseA.setDescription("Description unique");
        typeValiseRepository.save(typeValiseA);

        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setProprietaire("Arthur Menart");
        typeValiseB.setDescription("Description unique");

        assertThrows(Exception.class, () -> {
            typeValiseRepository.save(typeValiseB);
        }, "Une exception devrait être levée en raison de la contrainte d'unicité.");
    }
}
