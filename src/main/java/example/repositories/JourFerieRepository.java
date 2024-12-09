package example.repositories;

import example.entity.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {

    @Query("SELECT jf FROM JourFerie jf LEFT JOIN FETCH jf.regles WHERE jf.id = :id")
    Optional<JourFerie> findByIdWithRegles(@Param("id") int id);



}
