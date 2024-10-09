package example.entities;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ValiseTest {
    @Autowired
    private EntityManager em;
    private Valise valise;
    @BeforeEach
    public void setUp() {

    }
    @Test
    public void testValisePersistence () {

    }
    @Test
    public void testValiseDescription() {

    }
    @Test
    public void testValiseNumeroValise() {

    }
    @Test
    public void testValiseRefClient() {

    }
    @Test
    public void testValiseSortie() {

    }
    @Test
    public void testValiseDateDernierMouvement () {

    }
    @Test
    public void testValiseDateSortiePrevue() {

    }
    @Test
    public void testValiseDateRetourPrevue() {

    }
    @Test
    public void testValiseDateCreation() {

    }
    @Test
    public void testValiseClientAssociation() {

    }
    @Test
    public void testValiseClientNull() {

    }
    @Test
    public void testDeleteClientCascadeEffect() {

    }
    @Test
    public void testUpdateClientAssociation () {

    }
    @Test
    public void testValiseRegleAssociation() {

    }
    @Test
    public void testValiseRegleNull() {

    }
    @Test
    public void testDeleteRegleCascadeEffect () {

    }
    @Test
    public void testUpdateRegleAssociation() {

    }
    @Test
    public void testValiseTypeValiseAssociation() {

    }
    @Test
    public void testValiseTypeValiseNull() {

    }
    @Test
    public void testDeleteTypeValiseCascadeEffect() {

    }
        @Test
        public void testUpdateTypeValiseAssociation() {

        }
        @Test
        public void testValiseMouvementListAssociation() {

        }
    @Test
    public void testCascadePersistMouvementList () {

    }
    @Test
    public void testCascadeDeleteMouvementList() {

    }
    @Test
    public void testUpdateMouvementListAssociation(){

    }

}
