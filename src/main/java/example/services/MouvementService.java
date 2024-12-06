package example.services;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MouvementService implements IMouvementService {

    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private EntityManager entityManager;

    public MouvementService(MouvementRepository mouvementRepository) {
        this.mouvementRepository = mouvementRepository;
    } ;



    @Override
    public List<Mouvement> getAllMouvements() {

        return mouvementRepository.findAll();
    }

    @Override
    public Mouvement getMouvementById(int id) {
        Mouvement mouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + id));

        // Initialize lazy collections
        Hibernate.initialize(mouvement.getValise().getClient().getValises());
        return mouvement;
    }


    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

    @Override
    public void persistMouvement(Mouvement mouvement) {

    }

    @Override
    public Optional<Mouvement> findById(Long id) {
        return Optional.empty();
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


    @Override
    public Optional<Mouvement> findByIdWithValise(Long id) {
        return mouvementRepository.findByIdWithValise(id); // Call the repository method
    }

    @Override
    public List<Mouvement> getAllMouvementsWithValise() {
        return mouvementRepository.findAll();  // Assurez-vous que cela fonctionne comme prévu
    }










}

