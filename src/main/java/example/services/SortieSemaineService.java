package example.services;

import example.entities.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import example.repositories.SortieSemaineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortieSemaineService implements ISortieSemaineService {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    public SortieSemaineService(SortieSemaineRepository sortieSemaineRepository) {
        this.sortieSemaineRepository = sortieSemaineRepository;
    }

    @Override
    public SortieSemaine createSortieSemaine(SortieSemaine semaine) {
        return sortieSemaineRepository.save(semaine);
    }

    @Override
    public SortieSemaine updateSortieSemaine(int id, SortieSemaine sortieSemaine) {
        if (sortieSemaineRepository.existsById(id)) {
            return sortieSemaineRepository.save(sortieSemaine);
        } else {
            throw new RuntimeException("week outing is not possible");
        }
    }


    @Override
    public void deleteSortieSemaine(int id) {
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
}
