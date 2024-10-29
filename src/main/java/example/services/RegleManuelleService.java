package example.services;

import example.entities.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.RegleManuelleRepository;
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
            throw new IllegalArgumentException("La règle manuelle ne peut pas être null");
        }
        return regleManuelleRepository.save(regleManuelle);
    }

    @Override
    public RegleManuelle updateRegleManuelle(int id, RegleManuelle regleManuelle) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new RuntimeException("La règle manuelle n'existe pas");
        }

        if (regleManuelle == null || regleManuelle.getId() != id) {
            throw new IllegalArgumentException("L'ID de la règle manuelle ne correspond pas");
        }

        return regleManuelleRepository.save(regleManuelle);
    }


    @Override
    public void deleteRegleManuelle(int id) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new RuntimeException("La règle manuelle n'existe pas");
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

