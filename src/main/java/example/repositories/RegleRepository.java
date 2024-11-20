package example.repositories;

import example.entity.Regle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleRepository extends JpaRepository<Regle, Integer> {


    boolean existsByCoderegle(String coderegle);
    Regle findByCoderegle(String coderegle);


}
