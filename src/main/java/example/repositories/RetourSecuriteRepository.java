package example.repositories;

import example.entity.RetourSecurite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetourSecuriteRepository extends JpaRepository<RetourSecurite, Integer> {
    Optional<RetourSecurite> findByNumero(Long numero);
}

