package example.services;

import example.entities.Livreur;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreurService implements ILivreurService {

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

        // Vérification de l'ID du livreur pour lever l'exception
        if (livreur.getId() == id) {
            throw new RuntimeException("Expression not valid");
        }

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
