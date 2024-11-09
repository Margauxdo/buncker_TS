package example.services;

import example.entities.Mouvement;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MouvementService implements IMouvementService {

    @Autowired
    private MouvementRepository mouvementRepository;

    public MouvementService(MouvementRepository mouvementRepository) {
        this.mouvementRepository = mouvementRepository;
    } ;


    @Override
    public Mouvement createMouvement(Mouvement mouvement) {

        return mouvementRepository.save(mouvement);
    }

    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        if (!mouvementRepository.existsById(id)) {
            throw new EntityNotFoundException("Mouvement not found"); //404
        }
        return mouvementRepository.save(mouvement);
    }



    @Override
    public void deleteMouvement(int id) {
            mouvementRepository.deleteById(id);
    }

    @Override
    public List<Mouvement> getAllMouvements() {

        return mouvementRepository.findAll();
    }

    @Override
    public Mouvement getMouvementById(int id) {
        return mouvementRepository.findById(id).orElse(null);

    }
    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

}
