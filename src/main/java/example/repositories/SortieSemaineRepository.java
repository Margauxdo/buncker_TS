package example.repositories;

import example.entities.SortieSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortieSemaineRepository extends JpaRepository<SortieSemaine, Integer> {
}
