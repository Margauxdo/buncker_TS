package example.repositories;

import example.entity.RegleManuelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleManuelleRepository extends JpaRepository<RegleManuelle, Integer> {
}
