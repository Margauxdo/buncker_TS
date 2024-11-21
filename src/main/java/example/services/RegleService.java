package example.services;

import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleService implements IRegleService {

    private final RegleRepository regleRepository;
    private final FormuleRepository formuleRepository;

    public RegleService(RegleRepository regleRepository, FormuleRepository formuleRepository) {
        this.regleRepository = regleRepository;
        this.formuleRepository = formuleRepository;
    }

    @Override
    public Regle createRegle(Regle regle) {
        if (regle == null) {
            throw new IllegalArgumentException("La règle ne peut pas être nulle.");
        }
        // Validation de la relation avec Formule
        if (regle.getFormule() != null && regle.getFormule().getId() != 0) {
            Formule formule = formuleRepository.findById(regle.getFormule().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Formule non trouvée."));
            regle.setFormule(formule);
        }
        return regleRepository.save(regle);
    }

    @Override
    public Regle updateRegle(int id, Regle regle) {
        if (regle == null) {
            throw new RegleNotFoundException("Règle non trouvée avec l'ID " + id);
        }

        return regleRepository.findById(id)
                .map(existingRegle -> {
                    existingRegle.setCoderegle(regle.getCoderegle());
                    existingRegle.setDateRegle(regle.getDateRegle());
                    existingRegle.setNombreJours(regle.getNombreJours());
                    if (regle.getFormule() != null && regle.getFormule().getId() != 0) {
                        Formule formule = formuleRepository.findById(regle.getFormule().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Formule non trouvée."));
                        existingRegle.setFormule(formule);
                    }
                    return regleRepository.save(existingRegle);
                })
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID " + id));
    }



    @Override
    public Regle readRegle(int id) {
        return regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Ruler with id " + id + " not found"));
    }

    @Override
    public void deleteRegle(int id) {
        if (!regleRepository.existsById(id)) {
            throw new RegleNotFoundException("Ruler with id " + id + " not found, cannot delete");
        }
        regleRepository.deleteById(id);
    }

    @Override
    public List<Regle> readAllRegles() {
        return regleRepository.findAll();
    }
    @Override
    public boolean regleExists(String coderegle) {
        return regleRepository.existsByCoderegle("code regle");
    }


}


