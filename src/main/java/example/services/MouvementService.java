package example.services;

import example.DTO.MouvementDTO;
import example.DTO.ValiseDTO;
import example.entity.Mouvement;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MouvementService implements IMouvementService {

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private ValiseService valiseService;

    @Override
    public List<MouvementDTO> getAllMouvements() {
        return mouvementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MouvementDTO getMouvementById(int id) {
        Mouvement mouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + id));
        return convertToDTO(mouvement);
    }

    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

    @Override
    public void persistMouvement(Mouvement mouvement) {

    }

    @Override
    public Optional<Mouvement> findByIdWithValise(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Mouvement> getAllMouvementsWithValise() {
        return List.of();
    }

    @Override
    public MouvementDTO createMouvement(MouvementDTO mouvementDTO) {
        Mouvement mouvement = convertToEntity(mouvementDTO);
        return convertToDTO(mouvementRepository.save(mouvement));
    }

    @Override
    public MouvementDTO updateMouvement(int id, MouvementDTO mouvementDTO) {
        // Récupérer le Mouvement existant
        Mouvement existingMouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found"));

        // Mettre à jour les champs de Mouvement
        existingMouvement.setDateHeureMouvement(mouvementDTO.getDateHeureMouvement());
        existingMouvement.setStatutSortie(mouvementDTO.getStatutSortie());
        existingMouvement.setDateSortiePrevue(mouvementDTO.getDateSortiePrevue());
        existingMouvement.setDateRetourPrevue(mouvementDTO.getDateRetourPrevue());

        // Vérifier si un ValiseId est fourni et mettre à jour la Valise
        if (mouvementDTO.getValiseId() != null) {
            ValiseDTO valiseDTO = valiseService.getValiseById(mouvementDTO.getValiseId());
            Valise valise = convertToEntity(valiseDTO); // Convertir ValiseDTO en Valise
            existingMouvement.setValise(valise);
        }

        // Sauvegarder les modifications et retourner le DTO
        return convertToDTO(mouvementRepository.save(existingMouvement));
    }

    // Méthode utilitaire pour convertir un ValiseDTO en une entité Valise
    private Valise convertToEntity(ValiseDTO valiseDTO) {
        return Valise.builder()
                .id(valiseDTO.getId())
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .build();
    }


    @Override
    public Optional<Mouvement> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Mouvement createMouvement(Mouvement mouvement) {
        return null;
    }

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        return null;
    }

    @Override
    public void deleteMouvement(int id) {
        if (!mouvementRepository.existsById(id)) {
            throw new EntityNotFoundException("Mouvement not found with ID: " + id);
        }
        mouvementRepository.deleteById(id);
    }

    private Mouvement convertToEntity(MouvementDTO dto) {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(dto.getId());
        mouvement.setDateHeureMouvement(dto.getDateHeureMouvement());
        mouvement.setStatutSortie(dto.getStatutSortie());
        mouvement.setDateSortiePrevue(dto.getDateSortiePrevue());
        mouvement.setDateRetourPrevue(dto.getDateRetourPrevue());

        if (dto.getValiseId() != null) {
            ValiseDTO valiseDTO = valiseService.getValiseById(dto.getValiseId());
            Valise valise = convertToEntity(valiseDTO); // Convertir ValiseDTO en Valise
            mouvement.setValise(valise);
        }
        return mouvement;
    }




    private MouvementDTO convertToDTO(Mouvement mouvement) {
        return MouvementDTO.builder()
                .id(mouvement.getId())
                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                .statutSortie(mouvement.getStatutSortie())
                .dateSortiePrevue(mouvement.getDateSortiePrevue())
                .dateRetourPrevue(mouvement.getDateRetourPrevue())
                .valiseId(mouvement.getValise() != null ? mouvement.getValise().getId() : null)
                .build();
    }
}
