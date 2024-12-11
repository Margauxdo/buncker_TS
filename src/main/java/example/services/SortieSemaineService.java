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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortieSemaineService implements ISortieSemaineService {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Override
    public SortieSemaineDTO createSortieSemaine(SortieSemaineDTO dto) {
        // Recherche de la règle associée
        Regle regle = regleRepository.findById(dto.getRegleId())
                .orElseThrow(() -> new EntityNotFoundException("Regle not found with ID: " + dto.getRegleId()));

        // Création de l'entité SortieSemaine
        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(dto.getDateSortieSemaine())
                .regle(regle)
                .build();

        // Sauvegarde dans la base de données
        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        // Conversion en DTO
        return mapToDTO(sortieSemaine);
    }
    public List<Regle> getAllRegles() {
        return regleRepository.findAll();
    }


    @Override
    public SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO) {
        // Recherche de l'entité existante
        SortieSemaine existingSortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SortieSemaine not found with ID: " + id));

        // Mise à jour des champs
        existingSortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());
        if (sortieSemaineDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(sortieSemaineDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("Regle not found with ID: " + sortieSemaineDTO.getRegleId()));
            existingSortieSemaine.setRegle(regle);
        }

        // Sauvegarde dans la base
        SortieSemaine updatedSortieSemaine = sortieSemaineRepository.save(existingSortieSemaine);

        // Conversion en DTO pour le retour
        return mapToDTO(updatedSortieSemaine);
    }


    @Override
    public void deleteSortieSemaine(int id) {
        // Vérification de l'existence
        if (!sortieSemaineRepository.existsById(id)) {
            throw new EntityNotFoundException("SortieSemaine not found with ID: " + id);
        }
        // Suppression
        sortieSemaineRepository.deleteById(id);
    }

    @Override
    public SortieSemaineDTO getSortieSemaine(int id) {
        // Recherche et conversion en DTO
        SortieSemaine sortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SortieSemaine not found with ID: " + id));
        return mapToDTO(sortieSemaine);
    }

    @Override
    public SortieSemaine findById(Long id) {
        return null;
    }

    @Override
    public List<SortieSemaineDTO> getAllSortieSemaine() {
        // Récupération de toutes les entités et conversion en DTO
        List<SortieSemaine> sorties = sortieSemaineRepository.findAll();
        return sorties.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Conversion SortieSemaine -> SortieSemaineDTO
    private SortieSemaineDTO mapToDTO(SortieSemaine sortieSemaine) {
        return SortieSemaineDTO.builder()
                .id(sortieSemaine.getId())
                .dateSortieSemaine(sortieSemaine.getDateSortieSemaine())
                .regleId(sortieSemaine.getRegle() != null ? sortieSemaine.getRegle().getId() : null)
                .regleCode(sortieSemaine.getRegle() != null ? sortieSemaine.getRegle().getCoderegle() : "Aucune")
                .build();
    }


    // Conversion SortieSemaineDTO -> SortieSemaine
    private SortieSemaine mapToEntity(SortieSemaineDTO sortieSemaineDTO) {
        Regle regle = sortieSemaineDTO.getRegleId() != null
                ? regleRepository.findById(sortieSemaineDTO.getRegleId())
                .orElseThrow(() -> new EntityNotFoundException("Regle not found with ID: " + sortieSemaineDTO.getRegleId()))
                : null;

        return SortieSemaine.builder()
                .id(sortieSemaineDTO.getId())
                .dateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine())
                .regle(regle)
                .build();
    }
}
