package example.repositories;

import example.entity.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Integer> {

    // Example query methods
    List<Mouvement> findByRetourSecurite_Id(Integer retourSecuriteId);

    List<Mouvement> findByValise_Id(Integer valiseId);

    List<Mouvement> findByDateHeureMouvementBetween(Date startDate, Date endDate);

    List<Mouvement> findByValiseId(Integer valiseId);

}
