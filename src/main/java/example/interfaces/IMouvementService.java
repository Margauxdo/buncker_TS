package example.interfaces;

import example.entities.Mouvement;

import java.util.List;

public interface IMouvementService {
    Mouvement createMouvement(Mouvement mouvement);
    Mouvement updateMouvement(int id,Mouvement mouvement);
    void deleteMouvement(int id);
    List<Mouvement> getAllMouvements();
    Mouvement getMouvementById(int id);
}
