package example.repositories;

import example.entities.Formule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormuleRepository extends JpaRepository<Formule,Integer> {
}
