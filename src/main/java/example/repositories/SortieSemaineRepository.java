package example.repositories;

import example.entity.SortieSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SortieSemaineRepository extends JpaRepository<SortieSemaine, Integer> {
 List<SortieSemaine> findByDateSortieSemaine(Date dateSortieSemaine);
}
