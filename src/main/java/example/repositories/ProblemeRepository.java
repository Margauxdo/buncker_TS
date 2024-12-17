package example.repositories;

import example.entity.Probleme;
import example.entity.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemeRepository extends JpaRepository<Probleme, Integer> {

    // Trouver tous les problèmes associés à une valise
    List<Probleme> findByValise(Valise valise);

    // Vérifier l'existence d'un problème par description et détails
    boolean existsByDescriptionProblemeAndDetailsProbleme(String descriptionProbleme, String detailsProbleme);

    // Supprimer tous les problèmes associés à une valise
    void deleteAllByValise(Valise valise);

    // Rechercher tous les problèmes liés à un client par son ID
    @Query("SELECT p FROM Probleme p JOIN p.clients c WHERE c.id = :clientId")
    List<Probleme> findByClientId(@Param("clientId") int clientId);
}

