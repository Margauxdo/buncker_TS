package example.repositories;

import example.entities.Probleme;
import example.entities.Valise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemeRepository extends JpaRepository<Probleme, Integer> {
    List<Probleme> findByValise(Valise valise);
}
