package example.repositories;

import example.entities.Probleme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemeRepository extends JpaRepository<Probleme, Integer> {
}
