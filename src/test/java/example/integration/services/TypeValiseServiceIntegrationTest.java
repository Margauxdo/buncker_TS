package example.integration.services;

import example.entities.TypeValise;
import example.repositories.TypeValiseRepository;
import example.services.TypeValiseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class TypeValiseServiceIntegrationTest {

    @Autowired
    private TypeValiseService typeValiseService;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    private TypeValise typeValise;

    @BeforeEach
    void setUp() {
        typeValise = TypeValise.builder()
                .description("description du type de ma valise")
                .proprietaire("John Does")
                .build();
        typeValise = typeValiseService.createTypeValise(typeValise);
    }
    @Test
    public void testCreateTypeValise() {
        //Act
        TypeValise savedTV = typeValiseService.createTypeValise(typeValise);
        // Assert
        assertNotNull(savedTV);
        assertEquals("description du type de ma valise", savedTV.getDescription());
        assertEquals("John Does", savedTV.getProprietaire());
    }
    @Test
    public void testUpdateTypeValise() {
        // Arrange
        TypeValise savedTV = typeValiseService.createTypeValise(typeValise);
        // Act
        savedTV.setDescription("new description");
        savedTV.setProprietaire("new proprietaire");
        //Assert
        assertNotNull(savedTV);
        assertEquals("new description", savedTV.getDescription());
        assertEquals("new proprietaire", savedTV.getProprietaire());
    }
    @Test
    public void testDeleteTypeValise() {
        // Arrange
        TypeValise savedTV = typeValiseService.createTypeValise(typeValise);

        // Act
        typeValiseService.deleteTypeValise(savedTV.getId());

        // Assert
        boolean isDeleted = !typeValiseRepository.existsById(savedTV.getId());
        assertTrue(isDeleted, "Type Suitcase should be deleted from the database");
    }

    @Test
    public void testGetTypeValise() {
        //Arrange
        TypeValise savedTV = typeValiseService.createTypeValise(typeValise);
        //Act
        TypeValise retrievedTV = typeValiseService.getTypeValise(savedTV.getId());
        //Assert
        assertNotNull(retrievedTV);
        assertEquals("description du type de ma valise", retrievedTV.getDescription());
        assertEquals("John Does", retrievedTV.getProprietaire());
    }
    @Test
    public void testGetTypeValises() {
        //Arrange
        typeValiseRepository.deleteAll();
        TypeValise typeVal1 = TypeValise.builder()
                .description("description Val1")
                .proprietaire("Marie Georges")
                .build();
        TypeValise typeVal2 = TypeValise.builder()
                .description("description Val2")
                .proprietaire("Georges Dunois")
                .build();
        typeValiseService.createTypeValise(typeVal1);
        typeValiseService.createTypeValise(typeVal2);
        //Act
        List<TypeValise> typeValises = typeValiseService.getTypeValises();
        //Assert
        assertNotNull(typeValises);
        assertEquals(2, typeValises.size());
    }
}
