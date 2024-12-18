package example.repositories;

import example.entity.TypeRegle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRegleRepository extends JpaRepository<TypeRegle, Integer> {

    // Recherche de TypeRegle par son nom
    List<TypeRegle> findByNomTypeRegle(String nomTypeRegle);


 List<TypeRegle> findByNomTypeRegleContaining(String nomTypeRegle);

}
