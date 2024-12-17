package example.repositories;

import example.entity.RetourSecurite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RetourSecuriteRepository extends JpaRepository<RetourSecurite, Integer> {
    // Recherche par numéro
    Optional<RetourSecurite> findByNumero(Long numero);

    // Recherche par ID du client
    List<RetourSecurite> findByClientId(Integer clientId);

    // Recherche par plage de dates
    List<RetourSecurite> findByDatesecuriteBetween(Date startDate, Date endDate);

    // Recherche des retours non clôturés
    List<RetourSecurite> findByClotureFalse();
}
