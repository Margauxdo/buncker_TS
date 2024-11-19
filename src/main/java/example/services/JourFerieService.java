package example.services;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import example.repositories.JourFerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return jourFerieRepository.save(jourFerie);

    }

    }
