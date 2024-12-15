package example.repositories;

import example.entity.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Integer> {

    @Query("SELECT m FROM Mouvement m JOIN FETCH m.valise")
    List<Mouvement> findAllWithValise();


    @Query("SELECT m FROM Mouvement m JOIN FETCH m.valise WHERE m.id = :id")
    Optional<Mouvement> findByIdWithValise(@Param("id") Long id);

    @Query("SELECT m FROM Mouvement m LEFT JOIN FETCH m.retourSecurites WHERE m.id = :id")
        Optional<Mouvement> findByIdWithRetourSecurites(@Param("id") Integer id);


}
