package example.repositories;

import example.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Integer> {

    Livreur findByCodeLivreur(String codeLivreur);

    List<Livreur> findByNomLivreur(String name);

    List<Livreur> findByNumeroCartePro(String numeroCartePro);
    boolean existsByCodeLivreur(String codeLivreur);
}
