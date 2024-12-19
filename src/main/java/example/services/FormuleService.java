package example.services;

import example.DTO.FormuleDTO;
import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import example.repositories.FormuleRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormuleService implements IFormuleService {

    private static final Logger logger = LoggerFactory.getLogger(FormuleService.class);

    private final FormuleRepository formuleRepository;
    private final RegleRepository regleRepository;

    public FormuleService(FormuleRepository formuleRepository, RegleRepository regleRepository) {
        this.formuleRepository = formuleRepository;
        this.regleRepository = regleRepository;
    }

    // Conversion Entité -> DTO
    private FormuleDTO convertToDTO(Formule formule) {
        return FormuleDTO.builder()
                .id(formule.getId())
                .libelle(formule.getLibelle())
                .formule(formule.getFormule())
                .cleFormule(String.valueOf(formule.getId() != null ? formule.getId() : null))  // Example mapping
                .regleIds(formule.getRegles().stream().map(Regle::getId).collect(Collectors.toList()))
                .codeRegles(formule.getRegles().stream().map(Regle::getCoderegle).collect(Collectors.toList()))
                .build();
    }


    // Conversion DTO -> Entité
    private Formule convertToEntity(FormuleDTO formuleDTO) {
        Formule formule = Formule.builder()
                .id(formuleDTO.getId())
                .libelle(formuleDTO.getLibelle())
                .formule(formuleDTO.getFormule())
                .build();

        if (formuleDTO.getRegleIds() != null && !formuleDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = formuleDTO.getRegleIds().stream()
                    .map(id -> regleRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Règle introuvable avec l'ID : " + id)))
                    .collect(Collectors.toList());

            // Ajoutez les règles sans dissocier celles déjà associées
            formule.setRegles(regles);
        }

        return formule;
    }


    @Override
    @Transactional
    public FormuleDTO createFormule(FormuleDTO formuleDTO) {
        Formule formule = convertToEntity(formuleDTO);

        // Logs pour déboguer
        logger.info("Formule avant sauvegarde : {}", formule);
        if (formule.getRegles() != null) {
            formule.getRegles().forEach(regle -> regle.setFormule(formule));

        }

        Formule savedFormule = formuleRepository.save(formule);
        // Logs pour vérifier les associations
        logger.info("Formule créée : {}", savedFormule);
        savedFormule.getRegles().forEach(regle -> logger.info("Règle associée : {}", regle));

        return convertToDTO(savedFormule);
    }



    @Override
    public FormuleDTO updateFormule(int id, FormuleDTO formuleDTO) {
        Formule existingFormule = formuleRepository.findById(id)
                .orElseThrow(() -> new FormuleNotFoundException("Formule introuvable avec l'ID : " + id));

        // Mise à jour des champs simples
        existingFormule.setLibelle(formuleDTO.getLibelle());
        existingFormule.setFormule(formuleDTO.getFormule());

        // Mise à jour des règles associées
        if (formuleDTO.getRegleIds() != null) {
            List<Regle> regles = regleRepository.findAllById(formuleDTO.getRegleIds());
            existingFormule.setRegles(regles);
        }

        Formule updatedFormule = formuleRepository.save(existingFormule);
        return convertToDTO(updatedFormule);
    }

    @Override
    public void deleteFormule(int id) {

            Formule formule = formuleRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Formule introuvable avec l'ID : " + id));

            // Dissocier les règles associées
            if (formule.getRegles() != null && !formule.getRegles().isEmpty()) {
                formule.getRegles().forEach(regle -> regle.setFormule(null));
            }

            // Supprimer la formule
            formuleRepository.delete(formule);

    }

    @Override
    public FormuleDTO getFormuleById(int id) {
        Formule formule = formuleRepository.findById(id)
                .orElseThrow(() -> new FormuleNotFoundException("Formule introuvable avec l'ID : " + id));
        return convertToDTO(formule);
    }

    @Override
    public List<FormuleDTO> getAllFormules() {
        return formuleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
