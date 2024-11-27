package example.integration.repositories;

import example.entity.TypeRegle;
import example.entity.Regle;
import example.repositories.TypeRegleRepository;
import example.repositories.RegleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class TypeRegleRepositoryIntegrationTest {

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        typeRegleRepository.deleteAll();
        regleRepository.deleteAll();
    }

    @Test
    public void testSaveTypeRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("Code Regle A");
        regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type A")
                .regle(regle)
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        assertThat(savedTypeRegle.getId()).isNotNull();
        assertThat(savedTypeRegle.getNomTypeRegle()).isEqualTo("Type A");
        assertThat(savedTypeRegle.getRegle().getCoderegle()).isEqualTo("Code Regle A"); // Mise à jour ici
    }


    @Test
    public void testFindTypeRegleById() {
        Regle regle = new Regle();
        regle.setCoderegle("Code Regle B");
        regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type B")
                .regle(regle)
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());

        assertThat(foundTypeRegle).isPresent();
        assertThat(foundTypeRegle.get().getNomTypeRegle()).isEqualTo("Type B");
        assertThat(foundTypeRegle.get().getRegle().getCoderegle()).isEqualTo("Code Regle B"); // Mise à jour ici
    }


    @Test
    public void testFindByNomTypeRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("Code Regle C");
        regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type C")
                .regle(regle)
                .build();

        typeRegleRepository.save(typeRegle);

        List<TypeRegle> foundTypeRegles = typeRegleRepository.findByNomTypeRegle("Type C");

        // Vérifications
        assertThat(foundTypeRegles).hasSize(1);
        assertThat(foundTypeRegles.get(0).getNomTypeRegle()).isEqualTo("Type C");
        assertThat(foundTypeRegles.get(0).getRegle().getCoderegle()).isEqualTo("Code Regle C"); // Corrigé ici
    }


    @Test
    public void testFindByRegleId() {
        Regle regle = new Regle();
        regle.setCoderegle("Regle D");
        regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type D")
                .regle(regle)
                .build();

        typeRegleRepository.save(typeRegle);

        List<TypeRegle> foundTypeRegles = typeRegleRepository.findByRegle_Id(regle.getId());
        assertThat(foundTypeRegles).hasSize(1);
        assertThat(foundTypeRegles.get(0).getNomTypeRegle()).isEqualTo("Type D");
    }

    @Test
    public void testDeleteTypeRegle() {
        Regle regle = new Regle();
        regle.setCoderegle("Regle E");
        regleRepository.save(regle);

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type E")
                .regle(regle)
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        typeRegleRepository.delete(savedTypeRegle);

        Optional<TypeRegle> deletedTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());
        assertThat(deletedTypeRegle).isNotPresent();
    }
}
