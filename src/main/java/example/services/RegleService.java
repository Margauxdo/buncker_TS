package example.services;

import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.RegleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleService implements IRegleService {

    private final RegleRepository regleRepository;

    public RegleService(RegleRepository regleRepository) {
        this.regleRepository = regleRepository;
    }

    @Override
    public Regle createRegle(Regle regle) {
        if (regle == null) {
            throw new IllegalArgumentException("Ruler cannot be null");
        }
        return regleRepository.save(regle);
    }

    @Override
    public Regle readRegle(int id) {
        return regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Ruler with id " + id + " not found"));
    }

    @Override
    public Regle updateRegle(int id, Regle regle) {
        if (regle == null) {
            throw new RegleNotFoundException("Ruler with id " + id + " not found for update");

        }

        return regleRepository.findById(id)
                .map(existingRegle -> {
                    existingRegle.setCoderegle(regle.getCoderegle());
                    existingRegle.setDateRegle(regle.getDateRegle());
                    existingRegle.setNombreJours(regle.getNombreJours());
                    return regleRepository.save(existingRegle);
                })
                .orElseThrow(() -> new RegleNotFoundException("Ruler withID " + id + " not found"));
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
        return regleRepository.existsByCoderegle(coderegle);
    }


}


