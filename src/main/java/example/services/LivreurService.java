package example.services;

import example.entities.Livreur;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class    LivreurService implements ILivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    public LivreurService(LivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }

    @Override
    public Livreur createLivreur(Livreur livreur) {

        return livreurRepository.save(livreur);
    }

    @Override
    public Livreur updateLivreur(int id, Livreur livreur) {
        if (!livreurRepository.existsById(id)) {
            throw new RuntimeException("delivery person not found");
        }

        // Mettre à jour l'ID de l'objet `livreur` pour correspondre à l'ID fourni
        livreur.setId(id);
        return livreurRepository.save(livreur);
    }


    @Override
    public void deleteLivreur(int id) {
        livreurRepository.deleteById(id);
    }

    @Override
    public Livreur getLivreurById(int id) {
        return livreurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }
}
