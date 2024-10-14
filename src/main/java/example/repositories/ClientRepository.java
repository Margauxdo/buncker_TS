package example.repositories;

import example.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Client entities.
 * This interface extends JpaRepository, which provides
 * standard CRUD operations for the Client entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    // Method to find clients by their name
    List<Client> findByName(String name);

    // Method to find clients by their email
    List<Client> findByEmail(String email);
}
