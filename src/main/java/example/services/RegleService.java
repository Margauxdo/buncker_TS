package example.services;

import example.entities.Regle;
import example.interfaces.IRegleService;
import example.repositories.RegleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegleService implements IRegleService {

    @Autowired
    private RegleRepository regleRepository;
    public RegleService(RegleRepository regleRepository) {
        this.regleRepository = regleRepository;
    }

    @Override
    public Regle createRegle(Regle regle) {
        return regleRepository.save(regle);
    }

    @Override
    public Regle readRegle(int id) {
        return regleRepository.findById(id).orElse(null);

    }

    @Override
    public Regle updateRegle(int id, Regle regle) {
        if (regleRepository.findById(id).isPresent()) {
            regle.setId(id);
            return regleRepository.save(regle);
        }else {
            throw new RuntimeException("Ruler not found");
        }
    }

    @Override
    public void deleteRegle(int id) {
            regleRepository.deleteById(id);
    }

    @Override
    public List<Regle> readAllRegles() {
        return regleRepository.findAll();
    }
}
