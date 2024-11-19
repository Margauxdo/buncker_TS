package example.services;

import example.entity.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import example.repositories.SortieSemaineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortieSemaineService implements ISortieSemaineService {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Override
    public SortieSemaine createSortieSemaine(SortieSemaine sortieSemaine) {
        if (sortieSemaine.getRegle() == null) {
            throw new IllegalArgumentException("The associated rule is mandatory");
        }
        return sortieSemaineRepository.save(sortieSemaine);
    }

    @Override
    public SortieSemaine updateSortieSemaine(int id, SortieSemaine sortieSemaine) {
        if (sortieSemaineRepository.existsById(id)) {
            sortieSemaine.setId(id);
            return sortieSemaineRepository.save(sortieSemaine);
        } else {
            throw new IllegalArgumentException("Week Output Not Found for ID " + id);
        }
    }

    @Override
    public void deleteSortieSemaine(int id) {
        if (!sortieSemaineRepository.existsById(id)) {
            throw new IllegalArgumentException("Week Output Not Found for ID " + id);
        }
        sortieSemaineRepository.deleteById(id);
    }

    @Override
    public SortieSemaine getSortieSemaine(int id) {
        return sortieSemaineRepository.findById(id).orElse(null);
    }

    @Override
    public List<SortieSemaine> getAllSortieSemaine() {
        return sortieSemaineRepository.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return false;
    }
}
