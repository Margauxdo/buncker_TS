package example.repositories;

import example.entity.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {

    // Find a valise by its unique number
    Valise findByNumeroValise(Integer numeroValise);

    // Find valises by client reference
    List<Valise> findByRefClient(String refClient);

    // Find valises by their type (TypeValise ID)
    List<Valise> findByTypeValise_Id(Integer typeValiseId);

    // Find valises by their description
    List<Valise> findByDescriptionContainingIgnoreCase(String description);

    // Check if a valise exists by its unique number
    boolean existsByNumeroValise(Integer numeroValise);

    // Custom query to find valises with specific conditions (e.g., date filters)
    List<Valise> findByDateSortiePrevueBetweenAndDateRetourPrevueBetween(
            java.util.Date dateSortieStart,
            java.util.Date dateSortieEnd,
            java.util.Date dateRetourStart,
            java.util.Date dateRetourEnd
    );
}
