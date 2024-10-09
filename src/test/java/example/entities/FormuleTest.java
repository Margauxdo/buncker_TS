package example.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class FormuleTest {

    private Formule formule;
    private Regle regle;

    @BeforeEach
    public void setUp() {

        regle = new Regle();
        regle.setId(1);
        regle.setReglePourSortie("Test Rule");

        // Initialise la formule
        formule = new Formule();
        formule.setId(1);
        formule.setLibelle("Sample Libelle");
        formule.setFormule("Sample Formula");
        formule.setRegle(regle);
    }

    @Test
    public void testGetId() {
        // Test ID retrieval
        assertEquals(1, formule.getId(), "ID should match the set value");
    }

    @Test
    public void testGetLibelle() {
        // Test libelle retrieval
        assertEquals("Sample Libelle", formule.getLibelle(), "Libelle should match the set value");
    }

    @Test
    public void testGetFormule() {
        // Test formule retrieval
        assertEquals("Sample Formula", formule.getFormule(), "Formule should match the set value");
    }

    @Test
    public void testFormuleDependsOnUniqueRegle() {
        // Test if Formule has a unique associated Regle
        assertNotNull(formule.getRegle(), "Formule should have an associated Regle");
        assertEquals(1, formule.getRegle().getId(), "Regle ID should match the associated Regle");
        assertEquals("Test Rule", formule.getRegle().getReglePourSortie(), "Regle content should match the expected value");
    }
    @Test
    public void testCascadePersistValises(){

    }
    @Test
    public void testCascadeDeleteValises(){

    }
    @Test
    public void testNonNullName(){

    }
}

