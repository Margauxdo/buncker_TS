package example.repositories;

import example.entity.RegleManuelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegleManuelleRepository extends JpaRepository<RegleManuelle, Integer> {

    // Rechercher par créateur
    List<RegleManuelle> findByCreateurRegle(String createurRegle);

    // Rechercher par description contenant un mot-clé
    List<RegleManuelle> findByDescriptionRegleContaining(String keyword);

    // Requête personnalisée pour une recherche avancée
    @Query("SELECT r FROM RegleManuelle r WHERE r.createurRegle = :createur AND r.descriptionRegle LIKE %:keyword%")
    List<RegleManuelle> findByCreateurAndKeyword(@Param("createur") String createur, @Param("keyword") String keyword);
}
