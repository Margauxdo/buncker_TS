package example.services;

import example.entities.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.RegleManuelleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleManuelleService implements IRegleManuelleService {

    private final RegleManuelleRepository regleManuelleRepository;

    public RegleManuelleService(RegleManuelleRepository regleManuelleRepository) {
        this.regleManuelleRepository = regleManuelleRepository;
    }

    @Override
    public RegleManuelle createRegleManuelle(RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            throw new IllegalArgumentException("Manual rule cannot be null");
        }
        return regleManuelleRepository.save(regleManuelle);
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

