package example.interfaces;

import example.entity.Regle;
import example.entity.Valise;

import java.util.List;

public interface IValiseService {
    Valise createValise(Valise valise);  // Laisse uniquement cette ligne
    Valise updateValise(int id, Valise valise);
    void deleteValise(int id);
    Valise getValiseById(int id);
    List<Valise> getAllValises();

    void persistValise(Valise valise);


    List<Regle> validateRegles(List<Regle> regleSortie);
    boolean existsById(int id);

    List<Valise> findAllWithClient();
}
