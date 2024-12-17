package example.repositories;

import example.entity.TypeRegle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRegleRepository extends JpaRepository<TypeRegle, Integer> {
    // Retrieve TypeRegle by its name
    List<TypeRegle> findByNomTypeRegle(String nomTypeRegle);

    // Retrieve TypeRegle by associated Regle ID
    List<TypeRegle> findByRegles_Id(Integer regleId); // Adjusted to match the 'regles' field in TypeRegle entity
}
