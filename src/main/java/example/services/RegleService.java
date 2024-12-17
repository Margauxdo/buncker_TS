package example.services;

import example.DTO.RegleDTO;
import example.entity.Regle;
import example.entity.RegleManuelle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegleService implements IRegleService {

    private final RegleRepository regleRepository;
    private final TypeRegleRepository typeRegleRepository;
    private final FormuleRepository formuleRepository;
    private final JourFerieRepository jourFerieRepository;
    private final SortieSemaineRepository sortieSemaineRepository;

    public RegleService(
            RegleRepository regleRepository,
            TypeRegleRepository typeRegleRepository,
            FormuleRepository formuleRepository,
            JourFerieRepository jourFerieRepository,
            SortieSemaineRepository sortieSemaineRepository) {
        this.regleRepository = regleRepository;
        this.typeRegleRepository = typeRegleRepository;
        this.formuleRepository = formuleRepository;
        this.jourFerieRepository = jourFerieRepository;
        this.sortieSemaineRepository = sortieSemaineRepository;
    }

    @Override
    public RegleDTO createRegle(RegleDTO regleDTO) {
        Regle regle = mapToEntity(regleDTO);
        return mapToDTO(regleRepository.save(regle));
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

    @Override
    public void deleteRegle(int id) {
        if (!regleRepository.existsById(id)) {
            throw new RegleNotFoundException("Règle non trouvée avec l'ID : " + id);
        }
        regleRepository.deleteById(id);
    }

    @Override
    public RegleDTO getRegleById(int id) {
        Regle regle = regleRepository.findById(id)
                .orElseThrow(() -> new RegleNotFoundException("Règle non trouvée avec l'ID : " + id));
        return mapToDTO(regle);
    }

    @Override
    public List<RegleDTO> getAllRegles() {
        return regleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Conversion RegleDTO -> Regle
    private Regle mapToEntity(RegleDTO dto) {
        Regle regle = Regle.builder()
                .id(dto.getId())
                .reglePourSortie(dto.getReglePourSortie())
                .coderegle(dto.getCoderegle())
                .dateRegle(dto.getDateRegle())
                .nombreJours(dto.getNombreJours())
                .calculCalendaire(dto.getCalculCalendaire())
                .fermeJS1(dto.getFermeJS1())
                .fermeJS2(dto.getFermeJS2())
                .fermeJS3(dto.getFermeJS3())
                .fermeJS4(dto.getFermeJS4())
                .fermeJS5(dto.getFermeJS5())
                .fermeJS6(dto.getFermeJS6())
                .fermeJS7(dto.getFermeJS7())
                .typeEntree(dto.getTypeEntree())
                .nbjsmEntree(dto.getNbjsmEntree())
                .build();

        // Relations
        if (dto.getTypeRegleId() != null) {
            regle.setTypeRegle(typeRegleRepository.findById(dto.getTypeRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("TypeRegle introuvable")));
        }

        if (dto.getFormuleId() != null) {
            regle.setFormule(formuleRepository.findById(dto.getFormuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Formule introuvable")));
        }

        if (dto.getJourFerieId() != null) {
            regle.setJourFerie(jourFerieRepository.findById(dto.getJourFerieId())
                    .orElseThrow(() -> new EntityNotFoundException("JourFerie introuvable")));
        }

        if (dto.getSortieSemaineId() != null) {
            regle.setSortieSemaine(sortieSemaineRepository.findById(dto.getSortieSemaineId())
                    .orElseThrow(() -> new EntityNotFoundException("SortieSemaine introuvable")));
        }

        return regle;
    }

    // Conversion Regle -> RegleDTO
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
                .typeRegleId(regle.getTypeRegle() != null ? regle.getTypeRegle().getId() : null)
                .formuleId(regle.getFormule() != null ? regle.getFormule().getId() : null)
                .jourFerieId(regle.getJourFerie() != null ? regle.getJourFerie().getId() : null)
                .sortieSemaineId(regle.getSortieSemaine() != null ? regle.getSortieSemaine().getId() : null)
                .build();
    }

    // Mise à jour de l'entité depuis le DTO
    private void updateEntityFromDTO(Regle existingRegle, RegleDTO dto) {
        existingRegle.setReglePourSortie(dto.getReglePourSortie());
        existingRegle.setCoderegle(dto.getCoderegle());
        existingRegle.setDateRegle(dto.getDateRegle());
        existingRegle.setNombreJours(dto.getNombreJours());
        existingRegle.setCalculCalendaire(dto.getCalculCalendaire());
        existingRegle.setFermeJS1(dto.getFermeJS1());
        existingRegle.setFermeJS2(dto.getFermeJS2());
        existingRegle.setFermeJS3(dto.getFermeJS3());
        existingRegle.setFermeJS4(dto.getFermeJS4());
        existingRegle.setFermeJS5(dto.getFermeJS5());
        existingRegle.setFermeJS6(dto.getFermeJS6());
        existingRegle.setFermeJS7(dto.getFermeJS7());
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


    public RegleDTO saveRegleManuelle(RegleManuelle regleManuelle) {
        regleRepository.save(regleManuelle); // Sauvegarde la règle manuelle dans la base de données
        return null;
    }
}
