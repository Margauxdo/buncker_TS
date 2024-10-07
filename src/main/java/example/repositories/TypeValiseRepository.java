package example.repositories;

import example.entities.TypeValise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeValiseRepository extends JpaRepository<TypeValise, Integer> {
}
