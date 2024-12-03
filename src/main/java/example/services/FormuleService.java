package example.services;

import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
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
        logger.info("Creating formule with details: {}", formule);

        // Validate that the Regle exists and is valid
        if (formule.getRegle() == null || formule.getRegle().getId() == 0) {
            logger.error("Regle is null or missing ID during formule creation.");
            throw new IllegalArgumentException("A valid Regle must be provided to create a formule.");
        }

        if (!regleRepository.existsById(formule.getRegle().getId())) {
            logger.error("Regle with ID {} not found.", formule.getRegle().getId());
            throw new RegleNotFoundException("Regle not found with ID: " + formule.getRegle().getId());
        }

        logger.info("Checking existence of Regle with ID: {}", formule.getRegle().getId());
        Regle regle = regleRepository.findById(formule.getRegle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Regle not found with ID: " + formule.getRegle().getId()));

        // Associate the Regle and save the Formule
        formule.setRegle(regle);
        logger.info("Creating formule: libelle={}, regleId={}",
                formule.getLibelle(),
                formule.getRegle().getId());

        Formule savedFormule = formuleRepository.save(formule);
        logger.info("Formule created successfully with ID: {}", savedFormule.getId());
        return savedFormule;
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

    @Override
    public Formule getFormuleById(int id) {
        Formule formule = formuleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Formule avec l'Id " + id + " n'existe pas !"));
        if (formule.getRegle() == null) {
            formule.setRegle(Regle.builder().coderegle("Non défini").build());
        }
        return formule;
    }




}






