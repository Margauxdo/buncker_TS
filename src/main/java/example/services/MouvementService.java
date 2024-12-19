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

    public List<RetourSecuriteDTO> getAllRetourSecurites() {
        return retourSecuriteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public MouvementDTO createMouvement(MouvementDTO mouvementDTO) {
        // Vérification et association des entités liées
        Valise valise = valiseRepository.findById(mouvementDTO.getValiseId())
                .orElseThrow(() -> new EntityNotFoundException("Valise introuvable avec l'ID : " + mouvementDTO.getValiseId()));

        Livreur livreur = livreurRepository.findById(mouvementDTO.getLivreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + mouvementDTO.getLivreurId()));

        // Gérer la relation RetourSecurite (peut être null)
        RetourSecurite retourSecurite = null;
        if (mouvementDTO.getRetourSecuriteId() != null) {
            retourSecurite = retourSecuriteRepository.findById(mouvementDTO.getRetourSecuriteId())
                    .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + mouvementDTO.getRetourSecuriteId()));
        }

        // Si retourSecuriteId est null, retourSecurite reste à null, ce qui est acceptable dans certains cas.

        // Création de l'entité Mouvement
        Mouvement mouvement = Mouvement.builder()
                .dateHeureMouvement(mouvementDTO.getDateHeureMouvement())
                .statutSortie(mouvementDTO.getStatutSortie())
                .dateSortiePrevue(mouvementDTO.getDateSortiePrevue())
                .dateRetourPrevue(mouvementDTO.getDateRetourPrevue())
                .valise(valise)
                .livreur(livreur)
                .retourSecurite(retourSecurite) // Peut être null si pas d'ID spécifié
                .build();

        // Sauvegarder l'entité Mouvement
        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        return mapToDTO(savedMouvement);
    }

    @Override
    public Mouvement updateMouvement(int id, Mouvement mouvement) {
        return null;
    }

    // Cette méthode mappe une entité RetourSecurite en un DTO RetourSecuriteDTO
    private RetourSecuriteDTO mapToDTO(RetourSecurite retourSecurite) {
        return RetourSecuriteDTO.builder()
                .id(retourSecurite.getId())
                .clientId(retourSecurite.getId()) // Assurez-vous d'avoir une méthode getDescription() dans l'entité
                .build();
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
    @Transactional
    public void deleteMouvement(int id) {
        // Trouver l'entité Mouvement à supprimer
        Mouvement mouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement introuvable avec l'ID : " + id));

        // Détacher la Valise associée
        mouvement.setValise(null);  // Détache la Valise de l'entité Mouvement

        // Vous pouvez également détacher d'autres relations si nécessaire
        mouvement.setRetourSecurite(null); // Si retourSecurite est nullable et doit être supprimé

        // Supprimer le Mouvement
        mouvementRepository.delete(mouvement);
    }






    @Override
    public boolean existsById(int id) {
        return false;
    }

    @Override
    public List<Mouvement> getAllMouvementsWithRetourSecurites() {
        return List.of();
    }

    @Override
    public List<MouvementDTO> getAllMouvementsDTOWithRetourSecurites() {
        return List.of();
    }
    private MouvementDTO mapToDTO(Mouvement mouvement) {
        return MouvementDTO.builder()
                .id(mouvement.getId())
                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                .statutSortie(mouvement.getStatutSortie())
                .dateSortiePrevue(mouvement.getDateSortiePrevue())
                .dateRetourPrevue(mouvement.getDateRetourPrevue())
                .valiseId(mouvement.getValise() != null ? mouvement.getValise().getId() : null)
                .valiseDescription(mouvement.getValise() != null ? mouvement.getValise().getDescription() : "Non spécifié")
                .livreurId(mouvement.getLivreur() != null ? mouvement.getLivreur().getId() : null)
                .livreurNom(mouvement.getLivreur() != null ? mouvement.getLivreur().getNomLivreur() : "Non spécifié")
                .retourSecuriteId(mouvement.getRetourSecurite() != null ? mouvement.getRetourSecurite().getId() : null)
                .retourSecuriteNumero(mouvement.getRetourSecurite() != null ? String.valueOf(mouvement.getRetourSecurite().getNumero()) : "Non spécifié")
                .build();
    }


    public List<MouvementDTO> getMouvementsByValiseId(Integer valiseId) {
        List<Mouvement> mouvements = mouvementRepository.findByValiseId(valiseId);
        return mouvements.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
