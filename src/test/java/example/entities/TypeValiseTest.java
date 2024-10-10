package example.entities;

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
        // Création d'un client pour chaque valise
        Client client = new Client();
        client.setName("Client Test");
        em.persist(client);  // Persister le client

        // Création d'une instance de TypeValise
        typeValise = new TypeValise();
        typeValise.setProprietaire("Nom du propriétaire");
        typeValise.setDescription("Description du type");

        // Création et association d'une Valise avec un Client et un TypeValise
        Valise valise1 = new Valise();
        valise1.setDescription("Description de la valise");
        valise1.setClient(client);  // Association de la valise avec le client
        valise1.setTypevalise(typeValise);  // Association de la valise avec le typeValise

        List<Valise> valiseList = new ArrayList<>();
        valiseList.add(valise1);
        typeValise.setValises(valiseList);

        // Persister les entités
        em.persist(typeValise);  // Persist the parent first
        em.persist(valise1);  // Persist the child explicitly
        em.flush();  // Force Hibernate to synchronize data
    }

    @Test
    public void testTypeValisePersistence() {
        System.out.println("Testing TypeValise Persistence...");
        Assertions.assertNotNull(typeValise, "L'objet typeValise ne devrait pas être null.");
        Assertions.assertNotNull(typeValise.getId(), "L'ID de typeValise ne devrait pas être null après persistance.");
    }

    @Test
    public void testCascadePersistValises() {
        System.out.println("Testing Cascade Persist for Valises...");
        TypeValise foundTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNotNull(foundTypeValise, "TypeValise devrait être persisté.");
        Assertions.assertEquals(1, foundTypeValise.getValises().size(), "Une valise devrait être persistée en cascade.");
        Assertions.assertNotNull(foundTypeValise.getValises().get(0).getId(), "La valise persistée devrait avoir un ID.");
    }

    @Test
    public void testCascadeDeleteValises() {
        System.out.println("Testing Cascade Delete for Valises...");
        em.remove(typeValise);
        em.flush();

        TypeValise deletedTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNull(deletedTypeValise, "TypeValise devrait être supprimé.");

        List<Valise> remainingValises = em.createQuery("SELECT v FROM Valise v", Valise.class).getResultList();
        Assertions.assertTrue(remainingValises.isEmpty(), "Les valises associées devraient également être supprimées.");
    }
}




