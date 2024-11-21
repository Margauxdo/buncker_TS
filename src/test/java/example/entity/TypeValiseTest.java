package example.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
        // Créer un client
        Client client = new Client();
        client.setName("Jean Martin");
        client.setEmail("jmartin@example.com");
        em.persist(client);

        // Créer une valise et l'associer à un client
        Valise valise1 = new Valise();
        valise1.setDescription("Description de la valise");
        valise1.setClient(client);
        em.persist(valise1);

        // Créer un TypeValise et l'associer à la valise
        typeValise = new TypeValise();
        typeValise.setProprietaire("John Does");
        typeValise.setDescription("Type de la valise");
        typeValise.setValise(valise1);  // Associe valise1 à TypeValise

        em.persist(typeValise);
        em.flush();
    }

    @Test
    public void testTypeValisePersistence() {
        Assertions.assertNotNull(typeValise.getId(), "TypeValise ID must not be null after persistence");
    }

    @Test
    public void testCascadePersistValises() {
        TypeValise foundTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNotNull(foundTypeValise, "TypeValise should be persisted.");
        Assertions.assertEquals("Description de la valise", foundTypeValise.getValise().getDescription(), "The associated Valise description should match.");
    }

    @Test
    public void testCascadeDeleteValises() {
        em.remove(typeValise);  // Supprime TypeValise
        em.flush();

        TypeValise deletedTypeValise = em.find(TypeValise.class, typeValise.getId());
        Assertions.assertNull(deletedTypeValise, "TypeValise should be removed.");

        // Vérifie que la valise associée existe encore dans la base de données
        Valise remainingValise = em.find(Valise.class, typeValise.getValise().getId());
        Assertions.assertNotNull(remainingValise, "The associated Valise should still exist.");
    }
}
