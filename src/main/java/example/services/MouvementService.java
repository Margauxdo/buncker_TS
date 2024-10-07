package example.services;

import example.entities.Mouvement;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
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

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        if (mouvementRepository.existsById(id)) {
            mouvement.setId(id);
            return mouvementRepository.save(mouvement);
        }else{
            throw  new RuntimeException("movement not found");
        }
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
}
