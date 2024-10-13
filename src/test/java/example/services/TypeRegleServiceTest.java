package example.services;

import example.entities.TypeRegle;
import example.entities.TypeValise;
import example.repositories.TypeRegleRepository;
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
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleRepository.save(typeRegle)).thenReturn(typeRegle);

        TypeRegle result = typeRegleService.createTypeRegle(typeRegle);

        Assertions.assertNotNull(result, "Le type de régle ne doit pas être null");
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testCreateTypeRegle_Failure_Exception() {
        TypeRegle typeRegle = new TypeRegle();
        when(typeRegleRepository.save(typeRegle)).thenThrow(new RuntimeException("Erreur de base de donnee"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.createTypeRegle(typeRegle);
        });

        Assertions.assertEquals("Erreur de base de donnee", exception.getMessage());
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testUpdateTypeRegle_Success() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);

        when(typeRegleRepository.existsById(id)).thenReturn(true);
        when(typeRegleRepository.save(any(TypeRegle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TypeRegle result = typeRegleService.updateTypeRegle(id, typeRegle);

        Assertions.assertNotNull(result, "Le type de régle ne doit pas être null");
        Assertions.assertEquals(id, result.getId(), "L'ID du type de la régle doit correspondre");

        verify(typeRegleRepository, times(1)).existsById(id);
        verify(typeRegleRepository, times(1)).save(typeRegle);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testUpdateTypeRegle_Failure_Exception() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(2); // ID différent pour déclencher l'exception

        // Pas besoin de simuler l'appel à existsById car l'exception sera levée avant
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            typeRegleService.updateTypeRegle(id, typeRegle);
        });

        Assertions.assertEquals("L'ID du type de régle ne correspond pas", exception.getMessage());

        // On vérifie qu'aucune interaction avec le repository n'a eu lieu, car l'exception est levée avant.
        verify(typeRegleRepository, never()).existsById(id);
        verify(typeRegleRepository, never()).save(any(TypeRegle.class));
        verifyNoMoreInteractions(typeRegleRepository);
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
        int id = 1;
        when(typeRegleRepository.existsById(id)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.deleteTypeRegle(id);
        });

        Assertions.assertEquals("Le type de régle n'existe pas", exception.getMessage());
        verify(typeRegleRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testGetTypeRegle_Success() {
        int id = 1;
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setId(id);

        when(typeRegleRepository.findById(id)).thenReturn(Optional.of(typeRegle));

        TypeRegle result = typeRegleService.getTypeRegle(id);

        Assertions.assertNotNull(result, "Le type de régle ne doit pas être null");
        Assertions.assertEquals(id, result.getId(), "L'ID doit correspondre");

        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testGetTypeRegle_Failure_Exception() {
        int id = 1;
        when(typeRegleRepository.findById(id)).thenReturn(Optional.empty());

        TypeRegle result = typeRegleService.getTypeRegle(id);

        Assertions.assertNull(result, "Le type de régle doit être null si non trouvée");
        verify(typeRegleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(typeRegleRepository);
    }
    @Test
    public void testGetTypeRegles_Success(){
        List<TypeRegle> typeRegles = List.of(new TypeRegle(), new TypeRegle());

        when(typeRegleRepository.findAll()).thenReturn(typeRegles);

        List<TypeRegle> result = typeRegleService.getTypeRegles();

        Assertions.assertEquals(2, result.size(), "La liste des types de regless doit contenir 2 éléments");
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }
    @Test
    public void testGetTypeRegles_Failure_Exception(){
        when(typeRegleRepository.findAll()).thenThrow(new RuntimeException("Erreur de base de données"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            typeRegleService.getTypeRegles();
        });

        Assertions.assertEquals("Erreur de base de données", exception.getMessage());
        verify(typeRegleRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRegleRepository);
    }

    @Test
    public void testNoInteractionWithTypeRegleRepository_Success() {
        verifyNoInteractions(typeRegleRepository);
    }
}

