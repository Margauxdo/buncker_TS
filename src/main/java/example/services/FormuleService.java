package example.services;

import example.entities.Formule;
import example.entities.Regle;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormuleService implements IFormuleService {

    @Autowired
    private FormuleRepository formuleRepository;
    @Autowired
    private RegleRepository regleRepository;

    public FormuleService(FormuleRepository formuleRepository) {

        this.formuleRepository = formuleRepository;
        this.regleRepository = regleRepository;
    }

    @Override
    public Formule createFormule(Formule formule) {
        System.out.println("Formule reçue : " + formule);
        System.out.println("Regle ID : " + (formule.getRegle() != null ? formule.getRegle().getId() : "Aucune Regle"));

        if (formule.getRegle() == null || !regleRepository.existsById(formule.getRegle().getId())) {
            throw new IllegalArgumentException("Regle not found with ID: " + formule.getRegle().getId());
        }

        return formuleRepository.save(formule);
    }




    @Override
    public Formule updateFormule(int id, Formule formule) {
        return formuleRepository.findById(id)
                .map(existingFormule -> {
                    existingFormule.setLibelle(formule.getLibelle());
                    existingFormule.setFormule(formule.getFormule());
                    return formuleRepository.save(existingFormule);
                })
                .orElseThrow(() -> new FormuleNotFoundException("Formule not found for ID " + id));
    }

    @Override
    public Formule getFormuleById(int id) {
        return formuleRepository.findById(id).orElse(null);
    }


    public List<Formule> getAllFormules() {
        return formuleRepository.findAll();
    }



    @Override
    public void deleteFormule(int id) {
        if (!formuleRepository.existsById(id)) {
            throw new FormuleNotFoundException("Formule not found for ID " + id);
        }
        formuleRepository.deleteById(id);
    }


}






