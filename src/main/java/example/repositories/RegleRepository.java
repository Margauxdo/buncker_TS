package example.repositories;

import example.entity.Regle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegleRepository extends JpaRepository<Regle, Integer> {

    // Trouver des règles par type de règle
    List<Regle> findByTypeRegle_Id(Integer typeRegleId);

    // Trouver des règles associées à une valise
    List<Regle> findByValises_Id(Integer valiseId);

    // Trouver des règles par date
    List<Regle> findByDateRegleBetween(Date startDate, Date endDate);

    // Trouver des règles par formule
    List<Regle> findByFormule_Id(Integer formuleId);

    // Autres méthodes spécifiques

    List<Regle> findByJourFerieId(Integer jourFerieId);

    @Query("SELECT r FROM Regle r LEFT JOIN FETCH r.jourFerie WHERE r.id = :id")
    Optional<Regle> findByIdWithJourFerie(@Param("id") int id);



    @Query("SELECT r FROM Regle r WHERE r.typeRegle.id = :typeRegleId")
    List<Regle> findByTypeRegleId(@Param("typeRegleId") Integer typeRegleId);

    List<Regle> findByCoderegleIn(List<String> coderegles);

    @Query("SELECT r FROM Regle r WHERE r.formule.id = :formuleId")
    List<Regle> findByFormuleId(@Param("formuleId") Integer formuleId);


}
