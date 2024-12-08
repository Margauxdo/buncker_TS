package example.services;

import example.entity.Livreur;
import example.entity.Mouvement;
import example.exceptions.ConflictException;
import example.exceptions.RegleNotFoundException;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class    LivreurService implements ILivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    public LivreurService(LivreurRepository livreurRepository) {

        this.livreurRepository = livreurRepository;
        this.mouvementRepository = mouvementRepository;
    }
    @Transactional
    public void deleteLivreur(Long id) {
        Livreur livreur = livreurRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Livreur not found with ID: " + id));
        livreurRepository.delete(livreur);
    }


    @Override
    public Livreur createLivreur(Livreur livreur) {
        if (livreurRepository.existsByCodeLivreur(livreur.getCodeLivreur())) {
            throw new ConflictException("Livreur avec ce code existe déjà.");
        }

        if (livreur.getMouvement() != null && livreur.getMouvement().getId() != 0) {
            Mouvement mouvement = mouvementRepository.findById(livreur.getMouvement().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable."));
            livreur.setMouvement(mouvement);
        } else {
            livreur.setMouvement(null);
        }


        return livreurRepository.save(livreur);
    }



    @Override
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
            throw new EntityNotFoundException("delivery person not found with ID: " + id);
        }
        livreurRepository.deleteById(id);
    }


    @Override
    public Livreur getLivreurById(int id) {
        return livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur not found with ID " + id));
    }

    @Override
    public List<Livreur> getAllLivreurs() {

        return livreurRepository.findAll();
    }

    @Override
    public void saveLivreur(Livreur livreur) {
        livreurRepository.save(livreur);
    }

}
