package example.repositories;

import example.entities.TypeRegle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRegleRepository extends JpaRepository<TypeRegle, Integer> {
}
