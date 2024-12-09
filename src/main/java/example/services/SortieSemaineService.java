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
        Regle regle = regleRepository.findById(dto.getRegleId())
                .orElseThrow(() -> new EntityNotFoundException("Regle not found with ID: " + dto.getRegleId()));

        SortieSemaine sortieSemaine = SortieSemaine.builder()
                .dateSortieSemaine(dto.getDateSortieSemaine())
                .regle(regle)
                .build();

        sortieSemaine = sortieSemaineRepository.save(sortieSemaine);

        return mapToDTO(sortieSemaine);
    }

    @Override
    public SortieSemaineDTO updateSortieSemaine(int id, SortieSemaine sortieSemaineDTO) {
        return null;
    }

    @Override
    public SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO) {
        SortieSemaine existingSortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Week Output Not Found for ID " + id));

        // Map DTO to Entity
        existingSortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());
        if (sortieSemaineDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(sortieSemaineDTO.getRegleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rule Not Found for ID " + sortieSemaineDTO.getRegleId()));
            existingSortieSemaine.setRegle(regle);
        }

        // Save and return updated DTO
        SortieSemaine updatedSortieSemaine = sortieSemaineRepository.save(existingSortieSemaine);
        return mapToDTO(updatedSortieSemaine);
    }

    @Override
    public void deleteSortieSemaine(int id) {
        if (!sortieSemaineRepository.existsById(id)) {
            throw new EntityNotFoundException("SortieSemaine not found with ID: " + id);
        }
        sortieSemaineRepository.deleteById(id);
    }

    @Override
    public SortieSemaineDTO getSortieSemaine(int id) {
        SortieSemaine sortieSemaine = sortieSemaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SortieSemaine not found with ID: " + id));
        return mapToDTO(sortieSemaine);
    }

    @Override
    public List<SortieSemaineDTO> getAllSortieSemaine() {
        return sortieSemaineRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SortieSemaineDTO mapToDTO(SortieSemaine sortieSemaine) {
        return SortieSemaineDTO.builder()
                .id(sortieSemaine.getId())
                .dateSortieSemaine(sortieSemaine.getDateSortieSemaine())
                .regleId(sortieSemaine.getRegle() != null ? sortieSemaine.getRegle().getId() : null)
                .build();
    }

    private SortieSemaine mapToEntity(SortieSemaineDTO sortieSemaineDTO) {
        SortieSemaine sortieSemaine = new SortieSemaine();
        sortieSemaine.setId(sortieSemaineDTO.getId());
        sortieSemaine.setDateSortieSemaine(sortieSemaineDTO.getDateSortieSemaine());
        if (sortieSemaineDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(sortieSemaineDTO.getRegleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rule Not Found for ID " + sortieSemaineDTO.getRegleId()));
            sortieSemaine.setRegle(regle);
        }
        return sortieSemaine;
    }

}
