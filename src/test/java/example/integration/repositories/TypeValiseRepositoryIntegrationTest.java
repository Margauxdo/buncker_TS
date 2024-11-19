package example.integration.repositories;

import example.entity.Client;
import example.entity.TypeValise;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        typeValiseRepository.deleteAll();
    }
    @Test
    public void testSaveTypeValise() {

        Client client = new Client();
        client.setName("Bernard");
        client.setEmail("bernard@gmail.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(25154L);
        valise.setClient(client);

        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");

        List<Valise> valises = new ArrayList<>();
        valises.add(valise);
        typeValise.setValises(valises);

        TypeValise savedTV = typeValiseRepository.save(typeValise);
        assertNotNull(savedTV.getId());
        assertEquals("Arthur Menart", savedTV.getProprietaire());
        assertEquals("Description de la valise", savedTV.getDescription());
    }
    @Test
    public void testFindTypeValiseById() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");
        TypeValise savedTV = typeValiseRepository.save(typeValise);
        Optional<TypeValise> foundTV = typeValiseRepository.findById(savedTV.getId());
        assertTrue(foundTV.isPresent());
        assertEquals("Arthur Menart", foundTV.get().getProprietaire());
    }
    @Test
    public void testDeleteTypeValise() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");
        typeValiseRepository.save(typeValise);
        typeValiseRepository.deleteById(typeValise.getId());
        Optional<TypeValise> foundTV = typeValiseRepository.findById(typeValise.getId());
        assertFalse(foundTV.isPresent());
    }
    @Test
    public void testFindByIdNotFound() {

        Optional<TypeValise> typeValise = typeValiseRepository.findById(9999);
        assertFalse(typeValise.isPresent(), "No Suitcase Type should be found for this non-existent ID");
    }

    @Test
    public void testUpdateTypeValise() {
        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");
        TypeValise savedTV = typeValiseRepository.save(typeValise);

        savedTV.setProprietaire("Jean Corotte");
        savedTV.setDescription("Description de la valise spécifié au client");
        TypeValise updatedTV = typeValiseRepository.save(savedTV);

        Optional<TypeValise> foundTV = typeValiseRepository.findById(updatedTV.getId());
        assertTrue(foundTV.isPresent());
        assertEquals("Jean Corotte", foundTV.get().getProprietaire());
        assertEquals("Description de la valise spécifié au client", foundTV.get().getDescription());
    }

    @Test
    public void testFindAllTypeValise() {
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setProprietaire("Arthur Menart");
        typeValiseA.setDescription("Description de la valise");
        typeValiseRepository.save(typeValiseA);
        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setProprietaire("Jean Corotte");
        typeValiseB.setDescription("Description de la valise");
        typeValiseRepository.save(typeValiseB);
        List<TypeValise> typeValises = typeValiseRepository.findAll();
        assertNotNull(typeValises);
        assertTrue(typeValises.size() >= 2);
        typeValises.sort(Comparator.comparing(TypeValise::getProprietaire));
        assertEquals("Arthur Menart", typeValises.get(0).getProprietaire());
        assertEquals("Jean Corotte", typeValises.get(1).getProprietaire());
        assertEquals("Description de la valise", typeValises.get(1).getDescription());


    }
    @Test
    public void testFindByProprietaire(){
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setProprietaire("Arthur Menart");
        typeValiseA.setDescription("Description de la valise");
        typeValiseRepository.save(typeValiseA);
        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setProprietaire("Jean Corotte");
        typeValiseB.setDescription("Description de la valise");
        typeValiseRepository.save(typeValiseB);
        List<TypeValise> typeValises = typeValiseRepository.findAll();
        assertNotNull(typeValises);
        assertEquals(2, typeValises.size());
        assertEquals("Arthur Menart", typeValises.get(0).getProprietaire());
        assertEquals("Jean Corotte", typeValises.get(1).getProprietaire());

    }
    @Test
    public void testCascadeDeleteWithValises() {
        Client client = new Client();
        client.setName("Bernard");
        client.setEmail("bernard@gmail.com");
        clientRepository.save(client);

        Valise valise = new Valise();
        valise.setNumeroValise(25154L);
        valise.setClient(client);

        TypeValise typeValise = new TypeValise();
        typeValise.setProprietaire("Arthur Menart");
        typeValise.setDescription("Description de la valise");

        List<Valise> valises = new ArrayList<>();
        valises.add(valise);
        typeValise.setValises(valises);

        typeValiseRepository.save(typeValise);
        typeValiseRepository.deleteById(typeValise.getId());

        Optional<TypeValise> foundTV = typeValiseRepository.findById(typeValise.getId());
        assertFalse(foundTV.isPresent(), "Le TypeValise devrait être supprimé");

        List<Valise> valisesAfterDelete = valiseRepository.findAll();
        assertTrue(valisesAfterDelete.isEmpty(), "Suitcases associated with SuitcaseType should be deleted in cascade");
    }

    @Test
    public void testUniqueConstraintViolation() {
        TypeValise typeValiseA = new TypeValise();
        typeValiseA.setProprietaire("Arthur Menart");
        typeValiseA.setDescription("Description de la valise");
        typeValiseRepository.save(typeValiseA);

        TypeValise typeValiseB = new TypeValise();
        typeValiseB.setProprietaire("Arthur Menart");
        typeValiseB.setDescription("Description de la valise");

        assertThrows(Exception.class, () -> {
            typeValiseRepository.save(typeValiseB);
        }, "An exception should be thrown due to the uniqueness constraint");
    }






}
