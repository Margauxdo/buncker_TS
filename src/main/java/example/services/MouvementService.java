package example.services;

import example.DTO.LivreurDTO;
import example.DTO.MouvementDTO;
import example.DTO.RetourSecuriteDTO;
import example.DTO.ValiseDTO;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
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
    private ValiseService valiseService;
    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private RetourSecuriteService retourSecuriteService;


    @Override
    public List<MouvementDTO> getAllMouvements() {
        return mouvementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Mouvement createMouvement(Mouvement mouvement) {
        if (mouvement.getValise() == null) {
            throw new IllegalArgumentException("La valise ne peut pas être nulle pour un mouvement.");
        }

        return mouvementRepository.save(mouvement);
    }


    public MouvementDTO getMouvementById(int id) {
        Mouvement mouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + id));

        // Récupération des retourSecurites associés
        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();

        // Conversion en DTO
        MouvementDTO mouvementDTO = convertToDTO(mouvement);
        mouvementDTO.setRetourSecurites(retourSecurites); // Assigner les retourSecurites

        return mouvementDTO;
    }



    @Override
    public boolean existsById(int id) {
        return mouvementRepository.existsById(id);
    }

    @Override
    public List<Mouvement> getAllMouvementsWithRetourSecurites() {
        return List.of();
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
        // Vérifier si la valise existe
        Valise valise = valiseRepository.findById(mouvementDTO.getValiseId())
                .orElseThrow(() -> new EntityNotFoundException("Valise introuvable."));

        // Vérifier si le livreur existe
        Livreur livreur = livreurRepository.findById(mouvementDTO.getLivreurId())
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable."));

        // Créer l'objet mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setValise(valise);
        mouvement.setLivreur(livreur);  // Assurez-vous que le livreur est bien défini
        mouvement.setDateHeureMouvement(mouvementDTO.getDateHeureMouvement());
        mouvement.setStatutSortie(mouvementDTO.getStatutSortie());
        mouvement.setDateSortiePrevue(mouvementDTO.getDateSortiePrevue());
        mouvement.setDateRetourPrevue(mouvementDTO.getDateRetourPrevue());

        // Enregistrer le mouvement dans la base de données
        Mouvement savedMouvement = mouvementRepository.save(mouvement);

        return convertToDTO(savedMouvement);
    }



    @Override
    @Transactional
    public MouvementDTO updateMouvement(int id, MouvementDTO mouvementDTO) {
        Mouvement existingMouvement = mouvementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found"));

        // Mise à jour des champs
        existingMouvement.setDateHeureMouvement(mouvementDTO.getDateHeureMouvement());
        existingMouvement.setStatutSortie(mouvementDTO.getStatutSortie());
        existingMouvement.setDateSortiePrevue(mouvementDTO.getDateSortiePrevue());
        existingMouvement.setDateRetourPrevue(mouvementDTO.getDateRetourPrevue());

        // Récupération et association de la valise
        if (mouvementDTO.getValiseId() != null) {
            Valise valise = valiseService.getValiseById(mouvementDTO.getValiseId());
            existingMouvement.setValise(valise);
        }

        mouvementRepository.save(existingMouvement);
        return convertToDTO(existingMouvement);
    }

    private Valise convertToEntity(ValiseDTO valiseDTO) {
        if (valiseDTO == null) {
            return null;
        }
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

    private MouvementDTO convertToDTO(Mouvement mouvement) {
        return MouvementDTO.builder()
                .id(mouvement.getId())
                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                .statutSortie(mouvement.getStatutSortie())
                .dateSortiePrevue(mouvement.getDateSortiePrevue())
                .dateRetourPrevue(mouvement.getDateRetourPrevue())
                .valiseDescription(mouvement.getValise() != null ? mouvement.getValise().getDescription() : "Inconnue")
                .valiseNumeroValise(mouvement.getValise() != null ? String.valueOf(mouvement.getValise().getNumeroValise()) : "-")
                .build();
    }

    public Mouvement getMouvementByIdWithRetourSecurites(Integer id) {
        return mouvementRepository.findByIdWithRetourSecurites(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID " + id));
    }












}
