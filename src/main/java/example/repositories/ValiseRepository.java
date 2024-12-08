package example.repositories;

import example.entity.Client;
import example.entity.Regle;
import example.entity.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {
    Valise findByNumeroValise(Long numeroValise);
    Valise findByRefClient(String refClient);
    List<Valise> findByClient(Client client);
    List<Valise> findByRegleSortie(Regle regle);
    @Query("SELECT v FROM Valise v JOIN FETCH v.client")
    List<Valise> findAllWithClient();

}
