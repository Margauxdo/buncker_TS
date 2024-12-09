package example.interfaces;

import example.DTO.MouvementDTO;
import example.entity.Mouvement;

import java.util.List;
import java.util.Optional;

public interface IMouvementService {
    List<MouvementDTO> getAllMouvements();
    MouvementDTO getMouvementById(int id);

    void persistMouvement(Mouvement mouvement);

    Optional<Mouvement> findByIdWithValise(Long id);

    List<Mouvement> getAllMouvementsWithValise();

    MouvementDTO createMouvement(MouvementDTO mouvementDTO);
    MouvementDTO updateMouvement(int id, MouvementDTO mouvementDTO);

    Optional<Mouvement> findById(Long id);

    Mouvement createMouvement(Mouvement mouvement);

    Mouvement updateMouvement(int id, Mouvement mouvement);

    void deleteMouvement(int id);
    boolean existsById(int id);
}
