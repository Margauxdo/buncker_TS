package example.repositories;

import example.entity.TypeValise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeValiseRepository extends JpaRepository<TypeValise, Integer> {

    // Find by owner (proprietaire)
    List<TypeValise> findByProprietaire(String proprietaire);

    // Find by description
    List<TypeValise> findByDescription(String description);

    // Find by owner and description (unique constraint)
    List<TypeValise> findByProprietaireAndDescription(String proprietaire, String description);

    // Check existence by owner and description
    boolean existsByProprietaireAndDescription(String proprietaire, String description);
}
