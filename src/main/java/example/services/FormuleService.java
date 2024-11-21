package example.services;

import example.entity.Formule;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormuleService implements IFormuleService {


    private FormuleRepository formuleRepository;
    private final RegleRepository regleRepository;

    private static final Logger logger = LoggerFactory.getLogger(FormuleService.class);

    @Autowired
    public FormuleService(FormuleRepository formuleRepository, RegleRepository regleRepository) {



        this.formuleRepository = formuleRepository;
        this.regleRepository = regleRepository;
    }

    @Override
    public Formule createFormule(Formule formule) {
        logger.info("Création de la formule avec les détails: {}", formule);

        if (formule.getRegle() == null || !regleRepository.existsById(formule.getRegle().getId())) {
            logger.error("Regle not found with ID: {}", formule.getRegle().getId());
            throw new IllegalArgumentException("Regle not found with ID: " + formule.getRegle().getId());
        }

        Formule createdFormule = formuleRepository.save(formule);
        logger.info("Formule créée avec succès : {}", createdFormule);
        return createdFormule;
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






