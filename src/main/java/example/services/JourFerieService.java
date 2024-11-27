package example.services;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import example.repositories.JourFerieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JourFerieService implements IJourFerieService {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    public JourFerieService(JourFerieRepository jourFerieRepository){
        this.jourFerieRepository = jourFerieRepository;
    }

    @Override
    public JourFerie getJourFerie(int id) {
        return jourFerieRepository.findById(id).orElse(null);
    }

    @Override
    public List<JourFerie> getJourFeries() {
        return jourFerieRepository.findAll();
    }

    @Override
    public JourFerie saveJourFerie(JourFerie jourFerie) {
        if (jourFerie.getRegles().isEmpty()) {
            throw new IllegalStateException("A JourFerie must have at least one associated Regle");
        }
        return jourFerieRepository.save(jourFerie);
    }



    @Override
    public List<Date> getAllDateFerie() {
        return jourFerieRepository.findAll().stream()
                .map(JourFerie::getDate)
                .toList();
    }
    @Override
    public void persistJourFerie(JourFerie jourFerie) {
        jourFerieRepository.save(jourFerie);
    }


    public void deleteJourFerie(int id) {
        if (!jourFerieRepository.existsById(id)) {
            throw new EntityNotFoundException("JourFerie not found with id: " + id);
        }
        jourFerieRepository.deleteById(id);
    }


}
