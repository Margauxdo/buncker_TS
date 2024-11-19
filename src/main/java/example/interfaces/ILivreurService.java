package example.interfaces;

import example.entity.Livreur;

import java.util.List;

public interface ILivreurService {
    Livreur createLivreur(Livreur livreur);
    Livreur updateLivreur(int id, Livreur livreur);
    void deleteLivreur(int id);
    Livreur getLivreurById(int id);
    List<Livreur> getAllLivreurs();
}
