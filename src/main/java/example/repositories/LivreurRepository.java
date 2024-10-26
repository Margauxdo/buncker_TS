package example.repositories;

import example.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Integer> {

    Livreur findByCodeLivreur(String codeLivreur);  // Garder cette méthode

    List<Livreur> findByNomLivreur(String name);  // Trouver par nom

    List<Livreur> findByNumeroCartePro(String numeroCartePro);  // Trouver par carte pro
}
