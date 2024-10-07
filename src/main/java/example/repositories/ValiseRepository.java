package example.repositories;

import example.entities.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValiseRepository extends JpaRepository<Valise, Integer> {
}
