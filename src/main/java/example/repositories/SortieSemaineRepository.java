package example.repositories;

import example.entity.SortieSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SortieSemaineRepository extends JpaRepository<SortieSemaine, Integer> {

 // Find by the specific date of the sortie semaine
 List<SortieSemaine> findByDateSortieSemaine(Date dateSortieSemaine);

 // Find all sortie semaine records that have associated rules
 List<SortieSemaine> findByReglesIsNotNull();

 // Custom query to find sortie semaine by rule ID
 List<SortieSemaine> findByRegles_Id(Integer regleId);
}
