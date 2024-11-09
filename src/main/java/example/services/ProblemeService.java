package example.services;

import example.entities.Probleme;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemeService implements IProblemeService {

    private final ProblemeRepository problemeRepository;

    public ProblemeService(ProblemeRepository problemeRepository) {

        this.problemeRepository = problemeRepository;
    }


    @Override
    public Probleme createProbleme(Probleme probleme) {
        boolean exists = problemeRepository.existsByDescriptionProblemeAndDetailsProbleme(
                probleme.getDescriptionProbleme(), probleme.getDetailsProbleme());

        if (exists) {
            throw new IllegalStateException("Duplicate problem detected");
        }

        return problemeRepository.save(probleme);
    }

    @Override
    public Probleme updateProbleme(int id, Probleme probleme) {
        if (!problemeRepository.existsById(id)) {
            throw new RuntimeException("Problem not found");
        }

        if (probleme == null || probleme.getId() != id) {
            throw new RuntimeException("Problem ID does not match expected ID");
        }

        return problemeRepository.save(probleme);
    }

    @Override
    public void deleteProbleme(int id) {
        if (!problemeRepository.existsById(id)) {
            throw new RuntimeException("Problem not found");
        }
        problemeRepository.deleteById(id);
    }

    @Override
    public Probleme getProblemeById(int id) {

        return problemeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Probleme> getAllProblemes() {

        return problemeRepository.findAll();
    }
    @Override
    public Probleme getProbleme(int id) {

        return problemeRepository.findById(id).orElse(null);
    }
    @Override
    public boolean existsByDescriptionAndDetails(String description, String details) {
        return problemeRepository.existsByDescriptionProblemeAndDetailsProbleme(description, details);
    }


}

