package example.services;

import example.DTO.LivreurDTO;
import example.DTO.MouvementDTO;
import example.DTO.RetourSecuriteDTO;
import example.DTO.ValiseDTO;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import example.repositories.RetourSecuriteRepository;
import example.repositories.ValiseRepository;
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
    private ValiseRepository valiseRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Override
    public List<MouvementDTO> getAllMouvements() {
        return mouvementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MouvementDTO getMouvementById(int id) {
        Mouvement mouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id));
        return mapToDTO(mouvement);
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
    @Transactional
    public MouvementDTO createMouvement(MouvementDTO mouvementDTO) {
        // Vérification et association des entités liées
        Valise valise = valiseRepository.findById(mouvementDTO.getValiseId())
                .orElseThrow(() -> new EntityNotFoundException("Valise introuvable avec l'ID : " + mouvementDTO.getValiseId()));

        Livreur livreur = livreurRepository.findById(mouvementDTO.getLivreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + mouvementDTO.getLivreurId()));

        RetourSecurite retourSecurite = null;
        if (mouvementDTO.getRetourSecuriteId() != null) {
            retourSecurite = retourSecuriteRepository.findById(mouvementDTO.getRetourSecuriteId())
                    .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + mouvementDTO.getRetourSecuriteId()));
        }

        // Création de l'entité Mouvement
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(mouvementDTO.getDateHeureMouvement())
                .statutSortie(mouvementDTO.getStatutSortie())
                .dateSortiePrevue(mouvementDTO.getDateSortiePrevue())
                .dateRetourPrevue(mouvementDTO.getDateRetourPrevue())
                .valise(valise)
                .livreur(livreur)
                .retourSecurite(retourSecurite)
                .build();

        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        return mapToDTO(savedMouvement);
    }

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        return null;
    }

    @Override
    @Transactional
    public MouvementDTO updateMouvement(int id, MouvementDTO mouvementDTO) {
        Mouvement existingMouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id));

        // Mise à jour des champs
        existingMouvement.setDateHeureMouvement(mouvementDTO.getDateHeureMouvement());
        existingMouvement.setStatutSortie(mouvementDTO.getStatutSortie());
        existingMouvement.setDateSortiePrevue(mouvementDTO.getDateSortiePrevue());
        existingMouvement.setDateRetourPrevue(mouvementDTO.getDateRetourPrevue());

        // Mise à jour des entités liées
        if (mouvementDTO.getValiseId() != null) {
            Valise valise = valiseRepository.findById(mouvementDTO.getValiseId())
                    .orElseThrow(() -> new EntityNotFoundException("Valise introuvable avec l'ID : " + mouvementDTO.getValiseId()));
            existingMouvement.setValise(valise);
        }

        if (mouvementDTO.getLivreurId() != null) {
            Livreur livreur = livreurRepository.findById(mouvementDTO.getLivreurId())
                    .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + mouvementDTO.getLivreurId()));
            existingMouvement.setLivreur(livreur);
        }

        if (mouvementDTO.getRetourSecuriteId() != null) {
            RetourSecurite retourSecurite = retourSecuriteRepository.findById(mouvementDTO.getRetourSecuriteId())
                    .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + mouvementDTO.getRetourSecuriteId()));
            existingMouvement.setRetourSecurite(retourSecurite);
        }

        Mouvement updatedMouvement = mouvementRepository.save(existingMouvement);
        return mapToDTO(updatedMouvement);
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
    public void deleteMouvement(int id) {
        if (!mouvementRepository.existsById(id)) {
            throw new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id);
        }
        mouvementRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return false;
    }

    @Override
    public List<Mouvement> getAllMouvementsWithRetourSecurites() {
        return List.of();
    }

    // Méthode de mapping Mouvement -> MouvementDTO
    private MouvementDTO mapToDTO(Mouvement mouvement) {
        return MouvementDTO.builder()
                .id(mouvement.getId())
                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                .statutSortie(mouvement.getStatutSortie())
                .dateSortiePrevue(mouvement.getDateSortiePrevue())
                .dateRetourPrevue(mouvement.getDateRetourPrevue())
                .valiseId(mouvement.getValise() != null ? mouvement.getValise().getId() : null)
                .valiseDescription(mouvement.getValise() != null ? mouvement.getValise().getDescription() : null)
                .livreurId(mouvement.getLivreur() != null ? mouvement.getLivreur().getId() : null)
                .livreurNom(mouvement.getLivreur() != null ? mouvement.getLivreur().getNomLivreur() : null)
                .retourSecuriteId(mouvement.getRetourSecurite() != null ? mouvement.getRetourSecurite().getId() : null)
                .build();
    }

    public List<MouvementDTO> getMouvementsByValiseId(Integer valiseId) {
        List<Mouvement> mouvements = mouvementRepository.findByValiseId(valiseId);
        return mouvements.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
