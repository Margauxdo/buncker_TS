package example.integration.repositories;

import example.entity.Regle;
import example.entity.TypeRegle;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
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
        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type A")
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        assertThat(savedTypeRegle.getId()).isNotNull();
        assertThat(savedTypeRegle.getNomTypeRegle()).isEqualTo("Type A");
    }

    @Test
    public void testFindTypeRegleById() {
        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type B")
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        Optional<TypeRegle> foundTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());

        assertThat(foundTypeRegle).isPresent();
        assertThat(foundTypeRegle.get().getNomTypeRegle()).isEqualTo("Type B");
    }

    @Test
    public void testFindByNomTypeRegle() {
        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type C")
                .build();

        typeRegleRepository.save(typeRegle);

        List<TypeRegle> foundTypeRegles = typeRegleRepository.findByNomTypeRegle("Type C");

        assertThat(foundTypeRegles).hasSize(1);
        assertThat(foundTypeRegles.get(0).getNomTypeRegle()).isEqualTo("Type C");
    }

    @Test
    public void testFindByNomTypeRegleContaining() {
        TypeRegle typeRegle1 = TypeRegle.builder()
                .nomTypeRegle("Type D")
                .build();

        TypeRegle typeRegle2 = TypeRegle.builder()
                .nomTypeRegle("Type DE")
                .build();

        typeRegleRepository.save(typeRegle1);
        typeRegleRepository.save(typeRegle2);

        List<TypeRegle> foundTypeRegles = typeRegleRepository.findByNomTypeRegleContaining("Type D");

        assertThat(foundTypeRegles).hasSize(2);
    }

    @Test
    public void testDeleteTypeRegle() {
        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle("Type E")
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        typeRegleRepository.delete(savedTypeRegle);

        Optional<TypeRegle> deletedTypeRegle = typeRegleRepository.findById(savedTypeRegle.getId());
        assertThat(deletedTypeRegle).isNotPresent();
    }
}
