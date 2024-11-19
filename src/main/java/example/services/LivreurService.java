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

    @Override
    public Livreur createLivreur(Livreur livreur) {
        if (livreurRepository.existsByCodeLivreur(livreur.getCodeLivreur())) {
            throw new ConflictException("Delivery person with this code already exists.");
        }

        if (livreur.getMouvements() != null) {
            List<Mouvement> mouvements = livreur.getMouvements().stream()
                    .map(mouvement -> mouvementRepository.findById(mouvement.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + mouvement.getId())))
                    .toList();

            // Associe chaque mouvement au livreur
            mouvements.forEach(mouvement -> mouvement.setLivreur(livreur));
            livreur.setMouvements(mouvements);
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
        return livreurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }
}
