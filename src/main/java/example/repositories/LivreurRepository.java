package example.repositories;

import example.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Integer> {

    // Check if a Livreur exists by its unique code
    boolean existsByCodeLivreur(String codeLivreur);

    // Find a Livreur by its code
    Livreur findByCodeLivreur(String codeLivreur);

    // Custom query to fetch Livreur with their movements
    @Query("SELECT l FROM Livreur l LEFT JOIN FETCH l.mouvements WHERE l.id = :id")
    Livreur findByIdWithMouvements(@Param("id") Integer id);

    // Fetch all Livreurs with specific movement statuses
    @Query("SELECT l FROM Livreur l JOIN l.mouvements m WHERE m.statutSortie IN :statuses")
    List<Livreur> findLivreursByMouvementStatuses(@Param("statuses") List<String> statuses);
}
