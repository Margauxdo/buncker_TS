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
    public static final Logger logger = LoggerFactory.getLogger(FormuleService.class);

    public FormuleService(FormuleRepository formuleRepository, RegleRepository regleRepository) {
        this.formuleRepository = formuleRepository;
        this.regleRepository = regleRepository;
    }

    private FormuleDTO convertToDTO(Formule formule) {
        if (formule.getRegle() != null) {
            return FormuleDTO.builder()
                    .id(formule.getId())
                    .libelle(formule.getLibelle())
                    .formule(formule.getFormule())
                    .regleId(formule.getRegle().getId())
                    .codeRegle(formule.getRegle().getCoderegle())
                    .build();
        } else {
            return FormuleDTO.builder()
                    .id(formule.getId())
                    .libelle(formule.getLibelle())
                    .formule(formule.getFormule())
                    .build();
        }
    }




    private Formule convertToEntity(FormuleDTO formuleDTO) {
        Formule formule = new Formule();
        formule.setId(formuleDTO.getId());
        formule.setLibelle(formuleDTO.getLibelle());
        formule.setFormule(formuleDTO.getFormule());

        if (formuleDTO.getRegleId() != null) { // Vérifiez si regleId n'est pas null
            Regle regle = regleRepository.findById(formuleDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("Règle introuvable pour l'ID " + formuleDTO.getRegleId()));
            formule.setRegle(regle);
        }
        return formule;
    }


    @Override
    public FormuleDTO createFormule(FormuleDTO formuleDTO) {

        Regle regle = regleRepository.findById(formuleDTO.getRegleId())
                .orElseThrow(() -> new RuntimeException("Règle non trouvée avec l'id : " + formuleDTO.getRegleId()));

        Formule formule = new Formule(
                formuleDTO.getLibelle(),
                formuleDTO.getFormule(),
                regle
        );

        Formule savedFormule = formuleRepository.save(formule);

        return convertToDTO(savedFormule);


    }

    @Override
    public FormuleDTO updateFormule(int id, FormuleDTO formuleDTO) {
        return formuleRepository.findById(id)
                .map(existingFormule -> {
                    existingFormule.setLibelle(formuleDTO.getLibelle());
                    existingFormule.setFormule(formuleDTO.getFormule());
                    if (formuleDTO.getRegleId() != null) { // Vérifiez si regleId n'est pas null
                        Regle regle = regleRepository.findById(formuleDTO.getRegleId())
                                .orElseThrow(() -> new EntityNotFoundException("Règle introuvable pour l'ID " + formuleDTO.getRegleId()));
                        existingFormule.setRegle(regle);
                    }
                    return convertToDTO(formuleRepository.save(existingFormule));
                })
                .orElseThrow(() -> new FormuleNotFoundException("Formule introuvable pour l'ID " + id));
    }


    @Override
    public void deleteFormule(int id) {
        if (!formuleRepository.existsById(id)) {
            throw new FormuleNotFoundException("Formule introuvable pour l'ID " + id);
        }
        formuleRepository.deleteById(id);
    }

    @Override
    public FormuleDTO getFormuleById(int id) {
        Formule formule = formuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formule introuvable pour l'ID " + id));
        return convertToDTO(formule);
    }

    @Override
    public List<FormuleDTO> getAllFormules() {
        return formuleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




}
