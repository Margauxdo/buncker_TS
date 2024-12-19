package example.services;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;
import example.entity.Regle;
import example.interfaces.ISortieSemaineService;
import example.repositories.SortieSemaineRepository;
import example.repositories.RegleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class SortieSemaineService implements ISortieSemaineService {

    private static final Logger logger = LoggerFactory.getLogger(SortieSemaineService.class);


    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;
    @Autowired
    private RegleRepository regleRepository;

    @Transactional
    public SortieSemaineDTO createSortieSemaine(SortieSemaineDTO sortieSemaineDTO) {
        logger.info("Début de la création d'une SortieSemaine avec les données : {}", sortieSemaineDTO);

        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());

        if (sortieSemaineDTO.getRegleIds() != null && !sortieSemaineDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = regleRepository.findAllById(sortieSemaineDTO.getRegleIds());
            sortieSemaine.setRegles(regles);
            logger.info("Règles associées : {}", regles);
        }


        SortieSemaine savedSortieSemaine = sortieSemaineRepository.save(sortieSemaine);
        logger.info("SortieSemaine créée avec succès : {}", savedSortieSemaine);

        return mapToDTO(savedSortieSemaine);
    }

    @Override
    public SortieSemaineDTO updateSortieSemaine(Integer id, SortieSemaineDTO sortieSemaineDTO) {
        return null;
    }


    @Transactional
    public SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO) {
        logger.info("Mise à jour de la SortieSemaine avec ID : {}", id);

        SortieSemaine existingSortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("SortieSemaine introuvable avec ID : {}", id);
                    return new EntityNotFoundException("SortieSemaine introuvable avec ID : " + id);
                });

        existingSortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());
        logger.info("Nouvelle date de sortie semaine : {}", sortieSemaineDTO.getDateSortieSemaine());

        if (sortieSemaineDTO.getRegleIds() != null && !sortieSemaineDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = regleRepository.findAllById(sortieSemaineDTO.getRegleIds());
            existingSortieSemaine.setRegles(regles);
            logger.info("Règles mises à jour associées : {}", regles);
        } else {
            existingSortieSemaine.getRegles().clear();
            logger.warn("Aucune règle associée, liste des règles vidée.");
        }

        SortieSemaine updatedSortieSemaine = sortieSemaineRepository.save(existingSortieSemaine);
        logger.info("SortieSemaine mise à jour avec succès : {}", updatedSortieSemaine);

        return mapToDTO(updatedSortieSemaine);
    }



    @Override
    public void deleteSortieSemaine(Integer id) {
        if (!sortieSemaineRepository.existsById(id)) {
            throw new EntityNotFoundException("SortieSemaine with ID " + id + " not found");
        }
        sortieSemaineRepository.deleteById(id);  // Effectue la suppression
    }




    @Override
    public SortieSemaineDTO getSortieSemaine(Integer id) {
        SortieSemaine sortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SortieSemaine with ID " + id + " not found"));
        return mapToDTO(sortieSemaine);
    }

    @Override
    public SortieSemaine findById(Long id) {
        return null;
    }

    @Override
    public List<SortieSemaineDTO> getAllSortieSemaine() {
        return sortieSemaineRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SortieSemaineDTO mapToDTO(SortieSemaine sortieSemaine) {
        return SortieSemaineDTO.builder()
                .id(sortieSemaine.getId())
                .dateSortieSemaine(sortieSemaine.getDateSortieSemaine())
                .regleIds(sortieSemaine.getRegles().stream().map(Regle::getId).toList())
                .regleCodes(sortieSemaine.getRegles().stream().map(Regle::getCoderegle).toList())
                .build();
    }



    // Conversion SortieSemaineDTO -> SortieSemaine
    private SortieSemaine mapToEntity(SortieSemaineDTO sortieSemaineDTO) {
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setId(sortieSemaineDTO.getId());
        sortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());

        if (sortieSemaineDTO.getRegleIds() != null && !sortieSemaineDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = regleRepository.findAllById(sortieSemaineDTO.getRegleIds());
            sortieSemaine.setRegles(regles);
        }

        return sortieSemaine;
    }

}
