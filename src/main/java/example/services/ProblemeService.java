package example.services;

import example.entities.Probleme;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemeService implements IProblemeService {

    @Autowired
    private ProblemeRepository problemeRepository;

    public ProblemeService(ProblemeRepository problemeRepository) {
        this.problemeRepository = problemeRepository;
    }

    @Override
    public Probleme createProbleme(Probleme probleme) {
        return problemeRepository.save(probleme);
    }

    @Override
    public Probleme updateProbleme(int id, Probleme probleme) {
        if (probleme.getId() != id) {
            probleme.setId(id);
            return problemeRepository.save(probleme);
        }else{
            throw new RuntimeException("Problem not updated");
        }
    }

    @Override
    public void deleteProbleme(int id) {
        problemeRepository.deleteById(id);
    }

    @Override
    public Probleme getProbleme(int id) {
        return problemeRepository.findById(id).get();

    }

    @Override
    public List<Probleme> getAllProblemes() {
        return problemeRepository.findAll();
    }
}
