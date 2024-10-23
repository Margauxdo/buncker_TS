package example.repositories;

import example.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Integer> {
Livreur findByCodeLivreur(String codeLivreur);

}
