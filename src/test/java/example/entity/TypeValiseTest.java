package example.entity;

import jakarta.persistence.EntityManager;
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
        Client client = new Client();
        client.setName("Jean Martin");
        client.setEmail("jmartin@example.com");
        em.persist(client);

        typeValise = new TypeValise();
        typeValise.setProprietaire("John does");
        typeValise.setDescription("le type de la valise appartient à John Does");

        Valise valise1 = new Valise();
        valise1.setDescription("Description de la valise");
        valise1.setClient(client);
        valise1.setTypevalise(typeValise);

        List<Valise> valiseList = new ArrayList<>();
        valiseList.add(valise1);
        typeValise.setValises(valiseList);

        em.persist(typeValise);
        em.persist(valise1);
        em.flush();
    }

    @Test
    public void testTypeValisePersistence() {
        System.out.println("Testing TypeValise Persistence...");
        Assertions.assertNotNull(typeValise, "The Suitcase type object should not be null.");
        Assertions.assertNotNull(typeValise.getId(), "Suitcase type ID should not be null after persistence.");
    }

    @Test
    public void testCascadePersistValises() {
        System.out.println("Testing Cascade Persist for Valises...");
        TypeValise foundTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNotNull(foundTypeValise, "Suitcase type should be persisted.");
        Assertions.assertEquals(1, foundTypeValise.getValises().size(), "A suitcase should be persisted in cascade.");
        Assertions.assertNotNull(foundTypeValise.getValises().get(0).getId(), "The persisted suitcase should have an ID.");
    }

    @Test
    public void testCascadeDeleteValises() {
        System.out.println("Testing Cascade Delete for Valises...");
        em.remove(typeValise);
        em.flush();

        TypeValise deletedTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNull(deletedTypeValise, "Type Suitcase should be removed.");

        List<Valise> remainingValises = em.createQuery("SELECT v FROM Valise v", Valise.class).getResultList();
        Assertions.assertTrue(remainingValises.isEmpty(), "Associated suitcases should also be removed.");
    }
}




