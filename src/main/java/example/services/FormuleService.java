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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormuleService implements IFormuleService {

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
                .cleFormule(formule.getId() != null ? "formule-" + formule.getId() : null)  // Example mapping
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

        if (formuleDTO.getRegleIds() != null) {
            List<Regle> regles = regleRepository.findAllById(formuleDTO.getRegleIds());
            formule.setRegles(regles);
        }

        return formule;
    }

    @Override
    public FormuleDTO createFormule(FormuleDTO formuleDTO) {
        Formule formule = convertToEntity(formuleDTO);
        Formule savedFormule = formuleRepository.save(formule);
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
        if (!formuleRepository.existsById(id)) {
            throw new FormuleNotFoundException("Formule introuvable avec l'ID : " + id);
        }
        formuleRepository.deleteById(id);
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
