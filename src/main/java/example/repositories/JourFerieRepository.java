package example.repositories;

import example.entity.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {

    // Custom query to find holidays by a specific date
    @Query("SELECT jf FROM JourFerie jf WHERE jf.date = :date")
    List<JourFerie> findByDate(@Param("date") Date date);

    // Check if a holiday exists by a specific date
    boolean existsByDate(Date date);
}

