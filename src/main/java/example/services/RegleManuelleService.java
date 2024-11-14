package example.services;

import example.entities.Formule;
import example.entities.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleManuelleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleManuelleService implements IRegleManuelleService {

    private final RegleManuelleRepository regleManuelleRepository;
    private final FormuleRepository formuleRepository;

    @Autowired
    public RegleManuelleService(RegleManuelleRepository regleManuelleRepository, FormuleRepository formuleRepository) {
        this.regleManuelleRepository = regleManuelleRepository;
        this.formuleRepository = formuleRepository;
    }

    @Override
    public RegleManuelle createRegleManuelle(RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            throw new IllegalArgumentException("Manual rule cannot be null");
        }

        System.out.println("RegleManuelle reçue : " + regleManuelle);
        System.out.println("Formule ID : " + (regleManuelle.getFormule() != null ? regleManuelle.getFormule().getId() : "Aucune Formule"));

        try {
            if (regleManuelle.getFormule() != null) {
                int formuleId = regleManuelle.getFormule().getId();
                Formule formule = formuleRepository.findById(formuleId)
                        .orElseThrow(() -> new EntityNotFoundException("Formule not found with ID: " + formuleId));
                regleManuelle.setFormule(formule);
            }

            return regleManuelleRepository.save(regleManuelle);
        } catch (Exception e) {
            e.printStackTrace(); // Affiche la trace complète de l'erreur
            throw new RuntimeException("Erreur lors de la création de la RegleManuelle", e);
        }
    }



    @Override
    public RegleManuelle updateRegleManuelle(int id, RegleManuelle regleManuelle) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new EntityNotFoundException("Manual Ruler not found with ID " + id);
        }
        regleManuelle.setId(id);
        return regleManuelleRepository.save(regleManuelle);
    }



    public void deleteRegleManuelle(int id) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new EntityNotFoundException("Manual Ruler with ID " + id + " not found");
        }
        regleManuelleRepository.deleteById(id);
    }


    @Override
    public RegleManuelle getRegleManuelle(int id) {
        return regleManuelleRepository.findById(id).orElse(null);
    }

    @Override
    public List<RegleManuelle> getRegleManuelles() {
        return regleManuelleRepository.findAll();
    }
}

