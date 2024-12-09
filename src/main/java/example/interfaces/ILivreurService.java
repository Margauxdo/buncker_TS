package example.interfaces;

import example.DTO.LivreurDTO;
import example.entity.Livreur;

import java.util.List;

public interface ILivreurService {
    LivreurDTO createLivreur(LivreurDTO livreurDTO);
    LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO);

    Livreur createLivreur(Livreur livreur);

    Livreur updateLivreur(int id, Livreur livreur);

    void deleteLivreur(int id);
    LivreurDTO getLivreurById(int id);
    List<LivreurDTO> getAllLivreurs();

    void saveLivreur(Livreur livreur);
}
