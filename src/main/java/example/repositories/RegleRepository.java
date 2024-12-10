package example.repositories;

import example.entity.Regle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegleRepository extends JpaRepository<Regle, Integer> {


    boolean existsByCoderegle(String coderegle);
    Regle findByCoderegle(String coderegle);

    @Query("SELECT r FROM Regle r LEFT JOIN FETCH r.valise WHERE r.id = :id")
    Optional<Regle> findByIdWithValise(@Param("id") int id);

    @Query("SELECT r FROM Regle r LEFT JOIN FETCH r.valise")
    List<Regle> findAllWithValise();




}
