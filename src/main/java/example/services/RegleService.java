package example.services;

import example.entities.Regle;
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
            throw new IllegalArgumentException("Regle cannot be null");
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
            throw new IllegalArgumentException("Regle cannot be null");
        }

        return regleRepository.findById(id)
                .map(existingRegle -> {
                    regle.setId(id);
                    return regleRepository.save(regle);
                })
                .orElseThrow(() -> new RegleNotFoundException("Ruler with id " + id + " not found for update"));
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
}


