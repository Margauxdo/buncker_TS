package example.repositories;

import example.entities.TypeRegle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRegleRepository extends JpaRepository<TypeRegle, Integer> {
    List<TypeRegle> findByNomTypeRegle(String nomTypeRegle);

}
