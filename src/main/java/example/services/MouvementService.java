package example.services;

import example.entity.Mouvement;
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
    public List<Mouvement> getAllMouvements() {

        return mouvementRepository.findAll();
    }

    @Override
    public Mouvement getMouvementById(int id) {
        return mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + id));
    }

    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

    @Override
    public void persistMouvement(Mouvement mouvement) {

    }


    @Override
        public Mouvement createMouvement(Mouvement mouvement) {
            return mouvementRepository.save(mouvement);
        }

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        Mouvement existingMouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found"));

        existingMouvement.setDateHeureMouvement(mouvement.getDateHeureMouvement());
        existingMouvement.setStatutSortie(mouvement.getStatutSortie());
        existingMouvement.setDateSortiePrevue(mouvement.getDateSortiePrevue());
        existingMouvement.setDateRetourPrevue(mouvement.getDateRetourPrevue());

        return mouvementRepository.save(existingMouvement);
    }


    @Override
    public void deleteMouvement(int id) {
        mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + id));
        mouvementRepository.deleteById(id);
    }


}

