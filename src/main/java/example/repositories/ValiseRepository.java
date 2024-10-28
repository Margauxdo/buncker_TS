package example.repositories;

import example.entities.Client;
import example.entities.Regle;
import example.entities.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {
    Valise findByNumeroValise(Long numeroValise);
    Valise findByRefClient(String refClient);
    List<Valise> findByClient(Client client);
    List<Valise> findByRegleSortie(Regle regle);  // Change to 'findByRegleSortie'
}
