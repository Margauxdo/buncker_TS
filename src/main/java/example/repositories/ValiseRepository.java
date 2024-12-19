package example.repositories;

import example.entity.TypeValise;
import example.entity.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {
    boolean existsByNumeroValise(String numeroValise);
    List<Valise> findByTypeValise(TypeValise typeValise);
}

