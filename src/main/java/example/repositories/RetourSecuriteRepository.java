package example.repositories;

import example.entities.RetourSecurite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetourSecuriteRepository extends JpaRepository<RetourSecurite, Integer> {
}
