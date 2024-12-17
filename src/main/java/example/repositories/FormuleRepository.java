package example.repositories;

import example.entity.Formule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormuleRepository extends JpaRepository<Formule, Integer> {

    // Find a Formule by its libelle
    List<Formule> findByLibelle(String libelle);

    @Query("SELECT f FROM Formule f LEFT JOIN FETCH f.regles WHERE f.id = :id")
    Formule findByIdWithRegles(@Param("id") Integer id);


    // Find all Formules with a specific Regle code
    @Query("SELECT f FROM Formule f JOIN f.regles r WHERE r.coderegle = :code")
    List<Formule> findAllByRegleCode(@Param("code") String code);

    // Find all Formules with at least one Regle
    @Query("SELECT f FROM Formule f WHERE SIZE(f.regles) > 0")
    List<Formule> findAllWithRegles();

    @Query("SELECT f FROM Formule f LEFT JOIN FETCH f.regles WHERE f.id = :id")
    Formule findFormuleWithRegles(@Param("id") Integer id);

}
