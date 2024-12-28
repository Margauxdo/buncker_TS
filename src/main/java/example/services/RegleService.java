package example.services;

import example.DTO.RegleDTO;
import example.controller.RegleController;
import example.entity.*;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegleService implements IRegleService {


    private static final Logger logger = LoggerFactory.getLogger(RegleController.class);

    private final RegleRepository regleRepository;
    private final TypeRegleRepository typeRegleRepository;
    private final FormuleRepository formuleRepository;
    private final JourFerieRepository jourFerieRepository;
    private final SortieSemaineRepository sortieSemaineRepository;
    private final ValiseRepository valiseRepository;

    public RegleService(
            RegleRepository regleRepository,
            TypeRegleRepository typeRegleRepository,
            FormuleRepository formuleRepository,
            JourFerieRepository jourFerieRepository,
            SortieSemaineRepository sortieSemaineRepository, ValiseRepository valiseRepository) {
        this.regleRepository = regleRepository;
        this.typeRegleRepository = typeRegleRepository;
        this.formuleRepository = formuleRepository;
        this.jourFerieRepository = jourFerieRepository;
        this.sortieSemaineRepository = sortieSemaineRepository;
        this.valiseRepository = valiseRepository;
    }

    @Transactional
    public Regle createRegle(RegleDTO regleDTO) {
        Regle regle = new Regle();

        // Initialisation des champs simples
        regle.setCoderegle(regleDTO.getCoderegle());
        regle.setReglePourSortie(regleDTO.getReglePourSortie());
        regle.setDateRegle(regleDTO.getDateRegle());
        regle.setNombreJours(regleDTO.getNombreJours());
        regle.setTypeEntree(regleDTO.getTypeEntree());
        regle.setNbjsmEntree(regleDTO.getNbjsmEntree());
        regle.setCalculCalendaire(regleDTO.getCalculCalendaire() != null ? regleDTO.getCalculCalendaire() : 1);
        regle.setFermeJS1(regleDTO.getFermeJS1() != null ? regleDTO.getFermeJS1() : false);
        regle.setFermeJS2(regleDTO.getFermeJS2() != null ? regleDTO.getFermeJS2() : false);
        regle.setFermeJS3(regleDTO.getFermeJS3() != null ? regleDTO.getFermeJS3() : false);
        regle.setFermeJS4(regleDTO.getFermeJS4() != null ? regleDTO.getFermeJS4() : false);
        regle.setFermeJS5(regleDTO.getFermeJS5() != null ? regleDTO.getFermeJS5() : false);
        regle.setFermeJS6(regleDTO.getFermeJS6() != null ? regleDTO.getFermeJS6() : false);
        regle.setFermeJS7(regleDTO.getFermeJS7() != null ? regleDTO.getFermeJS7() : false);

        // Attacher une Valise
        if (regleDTO.getValiseId() != null) {
            Valise valise = valiseRepository.findById(regleDTO.getValiseId())
                    .orElseThrow(() -> new IllegalArgumentException("Valise introuvable avec ID : " + regleDTO.getValiseId()));
            regle.addValise(valise); // Utilisation de la méthode utilitaire
        }

        // Attacher JourFerie
        if (regleDTO.getJourFerieId() != null) {
            JourFerie jourFerie = jourFerieRepository.findById(regleDTO.getJourFerieId())
                    .orElseThrow(() -> new EntityNotFoundException("JourFerie introuvable avec ID : " + regleDTO.getJourFerieId()));
            regle.setJourFerie(jourFerie);
        }

        // Attacher TypeRegle
        if (regleDTO.getTypeRegleId() != null) {
            TypeRegle typeRegle = typeRegleRepository.findById(regleDTO.getTypeRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("TypeRegle introuvable avec ID : " + regleDTO.getTypeRegleId()));
            regle.setTypeRegle(typeRegle);
        }

        // Attacher Formule
        if (regleDTO.getFormuleId() != null) {
            Formule formule = formuleRepository.findById(regleDTO.getFormuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Formule introuvable avec ID : " + regleDTO.getFormuleId()));
            regle.setFormule(formule);
        }

        // Sauvegarder la règle
        return regleRepository.save(regle);
    }

    @Override
    public RegleDTO readRegle(int id) {
        return null;
    }

    @Override
    public List<RegleDTO> readAllRegles() {
        return List.of();
    }

    @Override
    public RegleDTO updateRegle(int id, RegleDTO regleDTO) {

        Regle existingRegle = regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID : " + id));

        updateEntityFromDTO(existingRegle, regleDTO);

        return mapToDTO(regleRepository.save(existingRegle));
    }

    @Transactional
    @Override
    public void deleteRegle(String id) {
        logger.info("Vérification de l'existence de la règle avec ID : {}", id);
        if (!regleRepository.existsById(Integer.valueOf(id))) {
            logger.warn("La règle avec ID : {} n'existe pas.", id);
            throw new RegleNotFoundException("Règle non trouvée avec l'ID : " + id);
        }
        try {
            logger.info("Suppression de la règle avec ID : {}", id);
            regleRepository.deleteById(Integer.valueOf(id));
            logger.info("La règle avec ID : {} a été supprimée avec succès.", id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la règle avec ID : {}", id, e);
            throw e;
        }
    }


    public RegleDTO getRegleById(int id) {
        Regle regle = regleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("regle introuvable avec ID : " + id));
        regle.getReglePourSortie();
        regle.getDateRegle();
        regle.getNombreJours();
        regle.getJourFerie();
        regle.getTypeEntree();
        regle.getNbjsmEntree();
        regle.getValises();
        regle.getJourFerie();

        return mapToDTO(regle);
    }





    @Override
    public List<RegleDTO> getAllRegles() {
        return regleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRegle(int id) {

    }

    private Regle mapToEntity(RegleDTO dto) {

        if (dto == null) {
            return null;
        }

        Regle regle = new Regle();

        regle.setCoderegle(dto.getCoderegle()); // Mapping du champ coderegle
        regle.setReglePourSortie(dto.getReglePourSortie());
        regle.setDateRegle(dto.getDateRegle());
        regle.setNombreJours(dto.getNombreJours() != null ? dto.getNombreJours() : 0);
        regle.setCalculCalendaire(dto.getCalculCalendaire() != null ? dto.getCalculCalendaire() : 1);
        regle.setFermeJS1(dto.getFermeJS1() != null ? dto.getFermeJS1() : false);
        regle.setFermeJS2(dto.getFermeJS2() != null ? dto.getFermeJS2() : false);
        regle.setFermeJS3(dto.getFermeJS3() != null ? dto.getFermeJS3() : false);
        regle.setFermeJS4(dto.getFermeJS4() != null ? dto.getFermeJS4() : false);
        regle.setFermeJS5(dto.getFermeJS5() != null ? dto.getFermeJS5() : false);
        regle.setFermeJS6(dto.getFermeJS6() != null ? dto.getFermeJS6() : false);
        regle.setFermeJS7(dto.getFermeJS7() != null ? dto.getFermeJS7() : false);
        regle.setTypeEntree(dto.getTypeEntree());
        regle.setNbjsmEntree(dto.getNbjsmEntree());


        // Relations
        if (dto.getFormuleId() != null) {
            regle.setFormule(formuleRepository.findById(dto.getFormuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Formule introuvable")));
        }

        return regle;
    }


    public RegleDTO mapToDTO(Regle regle) {
        if (regle == null) {
            return null;
        }

        RegleDTO dto = new RegleDTO();

        // Champs simples
        dto.setId(regle.getId());
        dto.setCoderegle(regle.getCoderegle());
        dto.setReglePourSortie(regle.getReglePourSortie());
        dto.setDateRegle(regle.getDateRegle());
        dto.setNombreJours(regle.getNombreJours());
        dto.setCalculCalendaire(regle.getCalculCalendaire());
        dto.setFermeJS1(regle.getFermeJS1() != null ? regle.getFermeJS1() : false);
        dto.setFermeJS2(regle.getFermeJS2() != null ? regle.getFermeJS2() : false);
        dto.setFermeJS3(regle.getFermeJS3() != null ? regle.getFermeJS3() : false);
        dto.setFermeJS4(regle.getFermeJS4() != null ? regle.getFermeJS4() : false);
        dto.setFermeJS5(regle.getFermeJS5() != null ? regle.getFermeJS5() : false);
        dto.setFermeJS6(regle.getFermeJS6() != null ? regle.getFermeJS6() : false);
        dto.setFermeJS7(regle.getFermeJS7() != null ? regle.getFermeJS7() : false);

        dto.setTypeEntree(regle.getTypeEntree()); // Ajout pour TypeEntree
        dto.setNbjsmEntree(regle.getNbjsmEntree()); // Ajout pour NbjsmEntree

        // Relation avec Valise
        if (regle.getValises() != null && !regle.getValises().isEmpty()) {
            dto.setValiseId(regle.getValises().get(0).getId());
            dto.setValiseNumero(regle.getValises().get(0).getDescription());
        }

        // Autres relations (TypeRegle, Formule, etc.)
        if (regle.getTypeRegle() != null) {
            dto.setTypeRegleId(regle.getTypeRegle().getId());
            dto.setTypeRegleNom(regle.getTypeRegle().getNomTypeRegle());
        }

        if (regle.getFormule() != null) {
            dto.setFormuleId(regle.getFormule().getId());
            dto.setFormule(regle.getFormule().getLibelle());
        }

        return dto;
    }



    // Mise à jour de l'entité depuis le DTO
    private void updateEntityFromDTO(Regle existingRegle, RegleDTO dto) {
        existingRegle.setReglePourSortie(dto.getReglePourSortie());
        existingRegle.setCoderegle(dto.getCoderegle());
        existingRegle.setDateRegle(dto.getDateRegle());
        existingRegle.setNombreJours(dto.getNombreJours());
        existingRegle.setCalculCalendaire(dto.getCalculCalendaire());
        existingRegle.setFermeJS1(dto.getFermeJS1() != null ? dto.getFermeJS1() : false);
        existingRegle.setFermeJS2(dto.getFermeJS2() != null ? dto.getFermeJS2() : false);
        existingRegle.setFermeJS3(dto.getFermeJS3() != null ? dto.getFermeJS3() : false);
        existingRegle.setFermeJS4(dto.getFermeJS4() != null ? dto.getFermeJS4() : false);
        existingRegle.setFermeJS5(dto.getFermeJS5() != null ? dto.getFermeJS5() : false);
        existingRegle.setFermeJS6(dto.getFermeJS6() != null ? dto.getFermeJS6() : false);
        existingRegle.setFermeJS7(dto.getFermeJS7() != null ? dto.getFermeJS7() : false);

        existingRegle.setTypeEntree(dto.getTypeEntree());
        existingRegle.setNbjsmEntree(dto.getNbjsmEntree());

        // Relations
        if (dto.getTypeRegleId() != null) {
            existingRegle.setTypeRegle(typeRegleRepository.findById(dto.getTypeRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("TypeRegle introuvable")));
        }

        if (dto.getFormuleId() != null) {
            existingRegle.setFormule(formuleRepository.findById(dto.getFormuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Formule introuvable")));
        }

        if (dto.getJourFerieId() != null) {
            existingRegle.setJourFerie(jourFerieRepository.findById(dto.getJourFerieId())
                    .orElseThrow(() -> new EntityNotFoundException("JourFerie introuvable")));
        }

        if (dto.getSortieSemaineId() != null) {
            existingRegle.setSortieSemaine(sortieSemaineRepository.findById(dto.getSortieSemaineId())
                    .orElseThrow(() -> new EntityNotFoundException("SortieSemaine introuvable")));
        }

    }

}
