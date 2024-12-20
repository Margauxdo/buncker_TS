package example.repositories;

import example.entity.TypeValise;
import example.entity.Valise;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {
    boolean existsByNumeroValise(String numeroValise);
    List<Valise> findByTypeValise(TypeValise typeValise);

    @EntityGraph(attributePaths = {"mouvements", "reglesSortie"})
    List<Valise> findAll();


    @Query("SELECT v FROM Valise v " +
            "LEFT JOIN FETCH v.typeValise " +
            "LEFT JOIN FETCH v.mouvements")
    List<Valise> findAllWithDetails();

    @Query("SELECT v FROM Valise v " +
            "LEFT JOIN FETCH v.typeValise " +
            "LEFT JOIN FETCH v.mouvements WHERE v.id = :id")
    Optional<Valise> findByIdWithDetails(@Param("id") Integer id);

}

