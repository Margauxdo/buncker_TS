package example.services;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.TypeRegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TypeRegleServiceTest {

    @Mock
    private TypeRegleRepository typeRegleRepository;

    @InjectMocks
    private TypeRegleService typeRegleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTypeRegle_Success() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setRegle(regle);
        when(typeRegleRepository.save(typeRegle)).thenReturn(typeRegle);

        // Act
        TypeRegle result = typeRegleService.createTypeRegle(typeRegle);

        // Assert
        Assertions.assertNotNull(result, "Rule type must not be null");
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }


    @Test
    public void testCreateTypeRegle_Failure_Exception() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setRegle(regle);

        when(typeRegleRepository.save(typeRegle)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.createTypeRegle(typeRegle);
        });

        // Assert
        Assertions.assertEquals("Database error", exception.getMessage(),
                "Exception message should match the expected error message.");
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }


    @Test
    public void testUpdateTypeRegle_Success() {
        // Arrange
        int id = 1;

        // Créer un objet Regle valide
        Regle regle = new Regle();
        regle.setId(100); // ID arbitraire pour la règle

        // Créer un TypeRegle existant
        TypeRegle existingTypeRegle = new TypeRegle();
        existingTypeRegle.setId(id);
        existingTypeRegle.setNomTypeRegle("Ancien TypeRegle");
        existingTypeRegle.setRegle(regle);

        // Créer un TypeRegle pour mise à jour
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);
        typeRegle.setNomTypeRegle("TypeRegle Test");
        typeRegle.setRegle(regle);

        when(typeRegleRepository.existsById(id)).thenReturn(true);
        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(existingTypeRegle));
        when(typeRegleRepository.save(any(TypeRegle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TypeRegle result = typeRegleService.updateTypeRegle(id, typeRegle);

        // Assert
        Assertions.assertNotNull(result, "Rule type must not be null");
        Assertions.assertEquals(id, result.getId(), "Rule type ID must match");
        Assertions.assertEquals("TypeRegle Test", result.getNomTypeRegle(), "Rule type name must match");
        Assertions.assertNotNull(result.getRegle(), "Rule type must have an associated rule");
        Assertions.assertEquals(100, result.getRegle().getId(), "Associated rule ID must match");

        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).findById(id);
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }



    @Test
    public void testUpdateTypeRegle_Failure_Exception() {
        // Arrange
        int id = 1;
        Regle regle = new Regle();
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(2);
        typeRegle.setNomTypeRegle("Nom valide");
        typeRegle.setRegle(regle);

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            typeRegleService.updateTypeRegle(id, typeRegle);
        });

        // Vérification du message de l'exception
        Assertions.assertEquals("TypeRegle ID mismatch", exception.getMessage(),
                "Exception message should match the expected error message.");
    }



    @Test
    public void testDeleteTypeRegle_Success() {
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(true);

        typeRegleService.deleteTypeRegle(id);

        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testDeleteTypeRegle_Failure_Exception() {
        // Arrange
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            typeRegleService.deleteTypeRegle(id);
        });

        // Vérification du message exact de l'exception
        Assertions.assertEquals("TypeRegle avec l'ID 1 est introuvable", exception.getMessage(),
                "Exception message should match the expected error message.");

        verify(typeRegleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }




    @Test
    public void testGetTypeRegle_Success() {
        // Arrange
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(typeRegle));

        // Act
        Optional<TypeRegle> result = typeRegleService.getTypeRegle(id);

        // Assert
        Assertions.assertTrue(result.isPresent(), "Rule type must be present");
        Assertions.assertEquals(id, result.get().getId(), "Rule type ID must match");

        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }


    @Test
    public void testGetTypeRegle_Failure_Exception() {
        // Arrange
        int id = 1;
        when(typeRegleRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<TypeRegle> result = typeRegleService.getTypeRegle(id);

        // Assert
        Assertions.assertTrue(result.isEmpty(), "Result should be Optional.empty if the type rule is not found");
        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testGetTypeRegles_Success(){
        List<TypeRegle> typeRegles = List.of(new TypeRegle(), new TypeRegle());

        when(typeRegleRepository.findAll()).thenReturn(typeRegles);

        List<TypeRegle> result = typeRegleService.getTypeRegles();

        Assertions.assertEquals(2, result.size(), "Rule type list must contain 2 elements");
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }
    @Test
    public void testGetTypeRegles_Failure_Exception(){
        when(typeRegleRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.getTypeRegles();
        });

        Assertions.assertEquals("Database error", exception.getMessage());
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testNoInteractionWithTypeRegleRepository_Success() {

        verifyNoInteractions(typeRegleRepository);
    }
    @Test
    public void testCreateTypeRegle_WithRegle() {
        // Arrange
        Regle regle = new Regle();
        regle.setId(1);
        regle.setCoderegle("Regle1");

        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle("Type B");
        typeRegle.setRegle(regle);

        TypeRegle savedTypeRegle = new TypeRegle();
        savedTypeRegle.setId(100);
        savedTypeRegle.setNomTypeRegle("Type B");
        savedTypeRegle.setRegle(regle);

        when(typeRegleRepository.save(typeRegle)).thenReturn(savedTypeRegle);

        // Act
        TypeRegle result = typeRegleService.createTypeRegle(typeRegle);

        // Assert
        Assertions.assertNotNull(result.getId(), "ID should not be null after saving");
        Assertions.assertEquals(100, result.getId(), "ID should match the saved entity");
        Assertions.assertEquals("Type B", result.getNomTypeRegle(), "NomTypeRegle should match");
        Assertions.assertNotNull(result.getRegle(), "Associated Regle should not be null");
        Assertions.assertEquals("Regle1", result.getRegle().getCoderegle(), "Coderegle should match");
    }




}


