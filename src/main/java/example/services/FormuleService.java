package example.services;

import example.entities.Formule;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormuleService implements IFormuleService {

    @Autowired
    private FormuleRepository formuleRepository;

    public FormuleService(FormuleRepository formuleRepository) {
        this.formuleRepository = formuleRepository;
    }

    @Override
    public Formule createFormule(Formule formule) {

        return formuleRepository.save(formule);
    }

    @Override
    public Formule updateFormule(int id, Formule formule) {
        // Vérifie d'abord si l'entité avec cet ID existe
        if (!formuleRepository.existsById(id)) {
            throw new RuntimeException("Formule not found for ID " + id);
        }

        if (formule.getId() != id) {
            formule.setId(id);
            return formuleRepository.save(formule);
        } else {
            throw new RuntimeException("Expression not valid");
        }
    }


    @Override
    public Formule getFormuleById(int id) {
        return formuleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Formule> getAllFormules() {
        return formuleRepository.findAll();
    }

    @Override
    public void deleteFormule(int id) {
        formuleRepository.deleteById(id);
    }
}
