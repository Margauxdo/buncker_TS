package example.repositories;

import example.DTO.ClientDTO;
import example.entity.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByName(String name);

    List<Client> findByEmail(String email);

    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = {"valises", "problemes"})
    List<Client> findAll();



    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.valises WHERE c.id = :id")
    Optional<Client> findByIdWithValises(@Param("id") Integer id);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.valises LEFT JOIN FETCH c.problemes WHERE c.id = :id")
    Optional<Client> findClientWithRelationsById(@Param("id") int id);


}
