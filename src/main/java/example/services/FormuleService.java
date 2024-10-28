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
    public Formule updateFormule(int id, Formule formuleDetails) {
        Formule existingFormule = formuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formule not found for ID " + id));

        existingFormule.setLibelle(formuleDetails.getLibelle());
        existingFormule.setFormule(formuleDetails.getFormule());
        existingFormule.setRegle(formuleDetails.getRegle());

        return formuleRepository.save(existingFormule);
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
