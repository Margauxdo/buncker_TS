package example.services;

import example.entity.Probleme;
import example.exceptions.ResourceNotFoundException;
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
        System.out.println("Vérification d'un problème existant : " + probleme.getDescriptionProbleme() + ", " + probleme.getDetailsProbleme());

        boolean exists = problemeRepository.existsByDescriptionProblemeAndDetailsProbleme(
                probleme.getDescriptionProbleme(), probleme.getDetailsProbleme());

        System.out.println("Existence : " + exists);

        if (exists) {
            throw new IllegalStateException("Un problème avec cette description et ces détails existe déjà.");
        }

        return problemeRepository.save(probleme);
    }


    @Override
    public Probleme updateProbleme(int id, Probleme probleme) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with ID: " + id);  // Lever une exception spécifique ici
        }

        if (probleme == null || probleme.getId() != id) {
            throw new IllegalArgumentException("Problem ID does not match expected ID");
        }

        return problemeRepository.save(probleme);
    }


    @Override
    public void deleteProbleme(int id) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with ID: " + id);
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

