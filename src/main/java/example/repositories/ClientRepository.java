package example.repositories;

import example.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // Recherche par nom
    List<Client> findByName(String name);

    // Recherche par email
    Optional<Client> findByEmail(String email);

    // Vérification d'existence par email
    boolean existsByEmail(String email);

    // Chargement de tous les clients avec leurs relations associées
    @Query("SELECT c FROM Client c " +
            "LEFT JOIN FETCH c.retourSecurites " +
            "LEFT JOIN FETCH c.regle " +
            "LEFT JOIN FETCH c.valises " +
            "LEFT JOIN FETCH c.probleme")
    List<Client> findAllWithAssociations();

    // Recherche par ID avec relations associées
    @Query("SELECT c FROM Client c " +
            "LEFT JOIN FETCH c.retourSecurites " +
            "LEFT JOIN FETCH c.regle " +
            "LEFT JOIN FETCH c.valises " +
            "LEFT JOIN FETCH c.probleme " +
            "WHERE c.id = :id")
    Optional<Client> findByIdWithAssociations(@Param("id") Integer id);

    // Recherche des clients associés à un problème spécifique
    List<Client> findByProbleme_Id(Integer problemeId);

    // Recherche des clients associés à une règle spécifique
    List<Client> findByRegle_Id(Integer regleId);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.retourSecurites WHERE c.id = :id")
    Optional<Client> findByIdWithRetourSecurites(@Param("id") int id);


}
