package example.repositories;

import example.entity.Formule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormuleRepository extends JpaRepository<Formule, Integer> {
}
