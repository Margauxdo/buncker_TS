package example.services;

import example.DTO.RegleDTO;
import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegleService implements IRegleService {

    private final RegleRepository regleRepository;

    public RegleService(RegleRepository regleRepository) {
        this.regleRepository = regleRepository;
    }
    @Override
    public RegleDTO readRegle(int id) {
        Regle regle = regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID " + id));
        return mapToDTO(regle);
    }

    @Override
    public List<RegleDTO> readAllRegles() {
        return regleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RegleDTO updateRegle(int id, RegleDTO regleDTO) {
        Regle existingRegle = regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID " + id));

        updateEntityFromDTO(existingRegle, regleDTO);
        Regle updatedRegle = regleRepository.save(existingRegle);

        return mapToDTO(updatedRegle);
    }

    @Override
    public RegleDTO createRegle(RegleDTO regleDTO) {
        Regle regle = mapToEntity(regleDTO);
        return mapToDTO(regleRepository.save(regle));
    }



    @Override
    public RegleDTO getRegleById(int id) {
        Regle regle = regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID " + id));
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
        if (!regleRepository.existsById(id)) {
            throw new RegleNotFoundException("Règle non trouvée avec l'ID " + id);
        }
        regleRepository.deleteById(id);
    }

    // Méthodes de conversion entre DTO et entité
    private Regle mapToEntity(RegleDTO regleDTO) {
        return Regle.builder()
                .id(regleDTO.getId())
                .reglePourSortie(regleDTO.getReglePourSortie())
                .coderegle(regleDTO.getCoderegle())
                .dateRegle(regleDTO.getDateRegle())
                .nombreJours(regleDTO.getNombreJours())
                .calculCalendaire(regleDTO.getCalculCalendaire())
                .fermeJS1(regleDTO.getFermeJS1())
                .fermeJS2(regleDTO.getFermeJS2())
                .fermeJS3(regleDTO.getFermeJS3())
                .fermeJS4(regleDTO.getFermeJS4())
                .fermeJS5(regleDTO.getFermeJS5())
                .fermeJS6(regleDTO.getFermeJS6())
                .fermeJS7(regleDTO.getFermeJS7())
                .typeEntree(regleDTO.getTypeEntree())
                .nbjsmEntree(regleDTO.getNbjsmEntree())
                .build();
    }

    private RegleDTO mapToDTO(Regle regle) {
        return RegleDTO.builder()
                .id(regle.getId())
                .reglePourSortie(regle.getReglePourSortie())
                .coderegle(regle.getCoderegle())
                .dateRegle(regle.getDateRegle())
                .nombreJours(regle.getNombreJours())
                .calculCalendaire(regle.getCalculCalendaire())
                .fermeJS1(regle.getFermeJS1())
                .fermeJS2(regle.getFermeJS2())
                .fermeJS3(regle.getFermeJS3())
                .fermeJS4(regle.getFermeJS4())
                .fermeJS5(regle.getFermeJS5())
                .fermeJS6(regle.getFermeJS6())
                .fermeJS7(regle.getFermeJS7())
                .typeEntree(regle.getTypeEntree())
                .nbjsmEntree(regle.getNbjsmEntree())
                .build();
    }

    private void updateEntityFromDTO(Regle existingRegle, RegleDTO regleDTO) {
        existingRegle.setReglePourSortie(regleDTO.getReglePourSortie());
        existingRegle.setCoderegle(regleDTO.getCoderegle());
        existingRegle.setDateRegle(regleDTO.getDateRegle());
        existingRegle.setNombreJours(regleDTO.getNombreJours());
        existingRegle.setCalculCalendaire(regleDTO.getCalculCalendaire());
        existingRegle.setFermeJS1(regleDTO.getFermeJS1());
        existingRegle.setFermeJS2(regleDTO.getFermeJS2());
        existingRegle.setFermeJS3(regleDTO.getFermeJS3());
        existingRegle.setFermeJS4(regleDTO.getFermeJS4());
        existingRegle.setFermeJS5(regleDTO.getFermeJS5());
        existingRegle.setFermeJS6(regleDTO.getFermeJS6());
        existingRegle.setFermeJS7(regleDTO.getFermeJS7());
        existingRegle.setTypeEntree(regleDTO.getTypeEntree());
        existingRegle.setNbjsmEntree(regleDTO.getNbjsmEntree());
    }
}
