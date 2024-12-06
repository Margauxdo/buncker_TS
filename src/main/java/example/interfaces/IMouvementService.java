package example.interfaces;

import example.entity.Mouvement;

import java.util.List;
import java.util.Optional;

public interface IMouvementService {
    Optional<Mouvement> findById(Long id);

    Mouvement createMouvement(Mouvement mouvement);
    Mouvement updateMouvement(int id,Mouvement mouvement);
    void deleteMouvement(int id);
    List<Mouvement> getAllMouvements();
    Mouvement getMouvementById(int id);
    boolean existsById(int id);
    void persistMouvement(Mouvement mouvement);
    Optional<Mouvement> findByIdWithValise(Long id);
    List<Mouvement> getAllMouvementsWithValise();


}
