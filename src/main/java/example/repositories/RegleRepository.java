package example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleRepository extends JpaRepository<RegleRepository, Integer> {
}
