package example.repositories;

import example.entities.TypeValise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeValiseRepository extends JpaRepository<TypeValise, Integer> {
List<TypeValise> findByProprietaire(String proprietaire);
}
