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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortieSemaineService implements ISortieSemaineService {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Override
    public SortieSemaineDTO createSortieSemaine(SortieSemaineDTO sortieSemaineDTO) {
        // Création de l'entité SortieSemaine
        SortieSemaine sortieSemaine = mapToEntity(sortieSemaineDTO);

        // Assurez-vous que la liste des règles n'est pas null
        if (sortieSemaine.getRegles() == null) {
            sortieSemaine.setRegles(new ArrayList<>());
        }

        // Sauvegarde dans la base
        SortieSemaine savedSortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        // Retourne un DTO
        return mapToDTO(savedSortieSemaine);
    }




    @Override
    public SortieSemaineDTO updateSortieSemaine(Integer id, SortieSemaineDTO sortieSemaineDTO) {
        // Recherche de l'entité existante
        SortieSemaine existingSortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SortieSemaine with ID " + id + " not found"));

        // Mise à jour des champs
        existingSortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());

        // Mise à jour des règles associées
        if (sortieSemaineDTO.getRegleIds() != null) {
            List<Regle> regles = regleRepository.findAllById(sortieSemaineDTO.getRegleIds());
            existingSortieSemaine.setRegles(regles);
        }

        // Sauvegarde de l'entité mise à jour
        SortieSemaine updatedSortieSemaine = sortieSemaineRepository.save(existingSortieSemaine);

        // Retourne un DTO
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
        List<Integer> regleIds = sortieSemaine.getRegles().stream()
                .map(Regle::getId)
                .collect(Collectors.toList());

        List<String> regleCodes = sortieSemaine.getRegles().stream()
                .map(Regle::getCoderegle)
                .collect(Collectors.toList());

        return SortieSemaineDTO.builder()
                .id(sortieSemaine.getId())
                .dateSortieSemaine(sortieSemaine.getDateSortieSemaine())
                .regleIds(regleIds)
                .regleCodes(regleCodes)
                .build();
    }


    // Conversion SortieSemaineDTO -> SortieSemaine
    private SortieSemaine mapToEntity(SortieSemaineDTO sortieSemaineDTO) {
        List<Regle> regles = sortieSemaineDTO.getRegleIds() != null
                ? regleRepository.findAllById(sortieSemaineDTO.getRegleIds())
                : null;

        return SortieSemaine.builder()
                .id(sortieSemaineDTO.getId())
                .dateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine())
                .regles(regles)
                .build();
    }
}
