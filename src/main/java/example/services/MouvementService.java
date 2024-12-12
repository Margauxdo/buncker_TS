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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public MouvementDTO getMouvementById(int id) {
        return mouvementRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id));
    }

    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

    @Override
    public void persistMouvement(Mouvement mouvement) {
        // Optionnel, selon les besoins spécifiques
    }

    @Override
    public Optional<Mouvement> findByIdWithValise(Long id) {
        return mouvementRepository.findById(id.intValue()); // Conversion de Long à int
    }

    @Override
    public List<Mouvement> getAllMouvementsWithValise() {
        // Exemple fictif, ajoutez la logique selon votre besoin
        return mouvementRepository.findAll();
    }

    @Override
    public MouvementDTO createMouvement(MouvementDTO mouvementDTO) {
        Mouvement mouvement = convertToEntity(mouvementDTO);

        // Vérification des relations
        if (mouvementDTO.getValiseId() != null) {
            ValiseDTO valiseDTO = valiseService.getValiseById(mouvementDTO.getValiseId());
            if (valiseDTO == null) {
                throw new EntityNotFoundException("Valise introuvable avec l'ID : " + mouvementDTO.getValiseId());
            }
            mouvement.setValise(convertToEntity(valiseDTO));
        } else {
            mouvement.setValise(null);
        }

        return convertToDTO(mouvementRepository.save(mouvement));
    }

    @Override
    public MouvementDTO updateMouvement(int id, MouvementDTO mouvementDTO) {
        // Récupérer le Mouvement existant
        Mouvement existingMouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id));

        // Mettre à jour les champs de Mouvement
        existingMouvement.setDateHeureMouvement(mouvementDTO.getDateHeureMouvement());
        existingMouvement.setStatutSortie(mouvementDTO.getStatutSortie());
        existingMouvement.setDateSortiePrevue(mouvementDTO.getDateSortiePrevue());
        existingMouvement.setDateRetourPrevue(mouvementDTO.getDateRetourPrevue());

        // Vérifier si un ValiseId est fourni et mettre à jour la Valise
        if (mouvementDTO.getValiseId() != null) {
            ValiseDTO valiseDTO = valiseService.getValiseById(mouvementDTO.getValiseId());
            if (valiseDTO == null) {
                throw new EntityNotFoundException("Valise introuvable avec l'ID : " + mouvementDTO.getValiseId());
            }
            existingMouvement.setValise(convertToEntity(valiseDTO));
        } else {
            existingMouvement.setValise(null);
        }

        return convertToDTO(mouvementRepository.save(existingMouvement));
    }

    @Override
    public Optional<Mouvement> findById(Long id) {
        return mouvementRepository.findById(id.intValue()); // Conversion Long en int
    }

    @Override
    public Mouvement createMouvement(Mouvement mouvement) {
        return mouvementRepository.save(mouvement);
    }

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        if (!mouvementRepository.existsById(id)) {
            throw new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id);
        }
        return mouvementRepository.save(mouvement);
    }

    @Override
    public void deleteMouvement(int id) {
        if (!mouvementRepository.existsById(id)) {
            throw new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id);
        }
        mouvementRepository.deleteById(id);
    }

    // Méthodes utilitaires

    private Mouvement convertToEntity(MouvementDTO dto) {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(dto.getId());
        mouvement.setDateHeureMouvement(dto.getDateHeureMouvement());
        mouvement.setStatutSortie(dto.getStatutSortie());
        mouvement.setDateSortiePrevue(dto.getDateSortiePrevue());
        mouvement.setDateRetourPrevue(dto.getDateRetourPrevue());

        if (dto.getValiseId() != null) {
            ValiseDTO valiseDTO = valiseService.getValiseById(dto.getValiseId());
            if (valiseDTO != null) {
                mouvement.setValise(convertToEntity(valiseDTO));
            }
        }
        return mouvement;
    }

    private Valise convertToEntity(ValiseDTO valiseDTO) {
        if (valiseDTO == null) {
            throw new IllegalArgumentException("ValiseDTO ne peut pas être null");
        }
        return Valise.builder()
                .id(valiseDTO.getId())
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .build();
    }

    private MouvementDTO convertToDTO(Mouvement mouvement) {
        return MouvementDTO.builder()
                .id(mouvement.getId())
                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                .statutSortie(mouvement.getStatutSortie())
                .dateSortiePrevue(mouvement.getDateSortiePrevue())
                .dateRetourPrevue(mouvement.getDateRetourPrevue())
                .valiseId(mouvement.getValise() != null ? mouvement.getValise().getId() : null)
                .valise(mouvement.getValise() != null ? ValiseDTO.builder()
                        .id(mouvement.getValise().getId())
                        .description(mouvement.getValise().getDescription())
                        .numeroValise(mouvement.getValise().getNumeroValise())
                        .build() : null)
                .build();
    }
}
