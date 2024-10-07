package example.services;

import example.entities.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.RegleManuelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleManuelleService implements IRegleManuelleService {

    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    public RegleManuelleService(RegleManuelleRepository regleManuelleRepository){
        this.regleManuelleRepository = regleManuelleRepository;
    }

    @Override
    public RegleManuelle createRegleManuelle(RegleManuelle regleManuelle) {
        return regleManuelleRepository.save(regleManuelle);
    }

    @Override
    public RegleManuelle updateRegleManuelle(int id, RegleManuelle regleManuelle) {
        if (regleManuelleRepository.findById(id).isPresent()) {
            regleManuelle.setId(id);
            return regleManuelleRepository.save(regleManuelle);
        }else{
            throw  new RuntimeException("Manual rule is not exists");
        }
    }

    @Override
    public void deleteRegleManuelle(int id) {
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
