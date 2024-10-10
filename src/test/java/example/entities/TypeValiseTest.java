package example.entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class TypeValiseTest {

    @Autowired
    private EntityManager em;
    private TypeValise typeValise;

    @BeforeEach
    void setUp() {
        Valise valise1 = new Valise();
        em.persist(valise1);

        List<Valise> valiseList = new ArrayList<>();
        valiseList.add(valise1);

        typeValise = new TypeValise();
        typeValise.setProprietaire("Nom du propriétaire");
        typeValise.setDescription("Description du type");
        typeValise.setValises(valiseList);

        em.persist(typeValise);
        em.flush();
    }

    @Test
    public void testTypeValisePersistence() {
        Assertions.assertNotNull(typeValise, "L'objet typeValise ne devrait pas être null.");
        Assertions.assertNotNull(typeValise.getId(), "L'ID de typeValise ne devrait pas être null après persistance.");
    }

    @Test
    public void testTypeValiseProprietaire() {
        Assertions.assertEquals("Nom du propriétaire", typeValise.getProprietaire(), "Le propriétaire devrait correspondre à la valeur initialisée.");
    }

    @Test
    public void testTypeValiseAssociationValise() {
        Assertions.assertEquals(1, typeValise.getValises().size(), "Le typeValise devrait être associé à une valise.");
    }

    @Test
    public void testCascadePersistValises() {
        TypeValise foundTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNotNull(foundTypeValise, "TypeValise devrait être persisté.");
        Assertions.assertEquals(1, foundTypeValise.getValises().size(), "Une valise devrait être persistée en cascade.");
        Assertions.assertNotNull(foundTypeValise.getValises().get(0).getId(), "La valise persistée devrait avoir un ID.");
    }

    @Test
    public void testCascadeDeleteValises() {
        em.remove(typeValise);
        em.flush();

        TypeValise deletedTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNull(deletedTypeValise, "TypeValise devrait être supprimé.");

        List<Valise> remainingValises = em.createQuery("SELECT v FROM Valise v", Valise.class).getResultList();
        Assertions.assertTrue(remainingValises.isEmpty(), "Les valises associées devraient également être supprimées.");
    }

    @Test
    public void testEmptyValiseList() {
        TypeValise emptyTypeValise = new TypeValise();
        emptyTypeValise.setProprietaire("Propriétaire sans valise");
        emptyTypeValise.setDescription("Type de valise sans valises");
        emptyTypeValise.setValises(new ArrayList<>());

        em.persist(emptyTypeValise);
        em.flush();

        TypeValise foundTypeValise = em.find(TypeValise.class, emptyTypeValise.getId());
        Assertions.assertNotNull(foundTypeValise, "TypeValise devrait être persisté.");
        Assertions.assertTrue(foundTypeValise.getValises().isEmpty(), "La liste des valises devrait être vide.");
    }

    @Test
    public void testNonNullProprietaire() {
        TypeValise typeValiseWithoutProprietaire = new TypeValise();
        typeValiseWithoutProprietaire.setDescription("Description sans propriétaire");

        Assertions.assertThrows(PersistenceException.class, () -> {
            em.persist(typeValiseWithoutProprietaire);
            em.flush();
        }, "Le propriétaire ne devrait pas être nul et devrait lancer une exception.");
    }
}

