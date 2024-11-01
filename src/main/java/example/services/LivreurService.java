package example.services;

import example.entities.Livreur;
import example.exceptions.ConflictException;
import example.exceptions.RegleNotFoundException;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import jakarta.persistence.EntityNotFoundException;
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
        if (livreurRepository.existsByCodeLivreur(livreur.getCodeLivreur())) {
            throw new ConflictException("Livreur with this code already exists.");
        }
        return livreurRepository.save(livreur);
    }


    @Override
    // In LivreurService.java

    public Livreur updateLivreur(int id, Livreur livreur) {
        if (!livreurRepository.existsById(id)) {
            throw new RegleNotFoundException("Delivery person not found with ID " + id);
        }
        livreur.setId(id);
        return livreurRepository.save(livreur);
    }




    @Override
    public void deleteLivreur(int id) {
        if (!livreurRepository.existsById(id)) {
            throw new EntityNotFoundException("Livreur not found with ID: " + id);
        }
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
