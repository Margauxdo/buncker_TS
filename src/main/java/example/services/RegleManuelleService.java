package example.services;

import example.DTO.RegleManuelleDTO;
import example.entity.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.RegleManuelleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegleManuelleService implements IRegleManuelleService {

    @Autowired
    private final RegleManuelleRepository regleManuelleRepository;

    public RegleManuelleService(RegleManuelleRepository regleManuelleRepository) {
        this.regleManuelleRepository = regleManuelleRepository;
    }

    @Override
    public RegleManuelleDTO createRegleManuelle(RegleManuelleDTO regleManuelleDTO) {
        RegleManuelle regleManuelle = toEntity(regleManuelleDTO);
        RegleManuelle savedRegleManuelle = regleManuelleRepository.save(regleManuelle);
        return toDTO(savedRegleManuelle);
    }

    @Override
    public RegleManuelleDTO updateRegleManuelle(int id, RegleManuelleDTO regleManuelleDTO) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new EntityNotFoundException("Règle manuelle introuvable avec ID " + id);
        }
        RegleManuelle regleManuelle = toEntity(regleManuelleDTO);
        regleManuelle.setId(id);
        RegleManuelle updatedRegleManuelle = regleManuelleRepository.save(regleManuelle);
        return toDTO(updatedRegleManuelle);
    }

    @Override
    public void deleteRegleManuelle(int id) {
        if (!regleManuelleRepository.existsById(id)) {
            throw new EntityNotFoundException("Règle manuelle introuvable avec ID " + id);
        }
        regleManuelleRepository.deleteById(id);
    }

    @Override
    public RegleManuelleDTO getRegleManuelle(int id) {
        RegleManuelle regleManuelle = regleManuelleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Règle manuelle introuvable avec ID " + id));
        return toDTO(regleManuelle);
    }

    @Override
    public List<RegleManuelleDTO> getRegleManuelles() {
        List<RegleManuelle> regleManuelles = regleManuelleRepository.findAll();
        return regleManuelles.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private RegleManuelleDTO toDTO(RegleManuelle regleManuelle) {
        return RegleManuelleDTO.builder()
                .id(regleManuelle.getId())
                .reglePourSortie(regleManuelle.getReglePourSortie())
                .coderegle(regleManuelle.getCoderegle())
                .dateRegle(regleManuelle.getDateRegle())
                .nombreJours(regleManuelle.getNombreJours())
                .calculCalendaire(regleManuelle.getCalculCalendaire())
                .fermeJS1(regleManuelle.getFermeJS1())
                .fermeJS2(regleManuelle.getFermeJS2())
                .descriptionRegle(regleManuelle.getDescriptionRegle())
                .createurRegle(regleManuelle.getCreateurRegle())
                .build();
    }

    private RegleManuelle toEntity(RegleManuelleDTO regleManuelleDTO) {
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(regleManuelleDTO.getId());
        regleManuelle.setReglePourSortie(regleManuelleDTO.getReglePourSortie());
        regleManuelle.setCoderegle(regleManuelleDTO.getCoderegle());
        regleManuelle.setDateRegle(regleManuelleDTO.getDateRegle());
        regleManuelle.setNombreJours(regleManuelleDTO.getNombreJours());
        regleManuelle.setCalculCalendaire(regleManuelleDTO.getCalculCalendaire());
        regleManuelle.setFermeJS1(regleManuelleDTO.getFermeJS1());
        regleManuelle.setFermeJS2(regleManuelleDTO.getFermeJS2());
        regleManuelle.setDescriptionRegle(regleManuelleDTO.getDescriptionRegle());
        regleManuelle.setCreateurRegle(regleManuelleDTO.getCreateurRegle());
        return regleManuelle;
    }
}
