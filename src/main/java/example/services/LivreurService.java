package example.services;

import example.DTO.LivreurDTO;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivreurService implements ILivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    public LivreurService(LivreurRepository livreurRepository, MouvementRepository mouvementRepository) {
        this.livreurRepository = livreurRepository;
        this.mouvementRepository = mouvementRepository;
    }

    private LivreurDTO convertToDTO(Livreur livreur) {
        return LivreurDTO.builder()
                .id(livreur.getId())
                .codeLivreur(livreur.getCodeLivreur())
                .motDePasse(livreur.getMotDePasse())
                .nomLivreur(livreur.getNomLivreur())
                .prenomLivreur(livreur.getPrenomLivreur())
                .numeroCartePro(livreur.getNumeroCartePro())
                .telephonePortable(livreur.getTelephonePortable())
                .telephoneKobby(livreur.getTelephoneKobby())
                .telephoneAlphapage(livreur.getTelephoneAlphapage())
                .mouvementStatutSortie(livreur.getMouvements() != null ?
                        livreur.getMouvements().stream()
                                .map(Mouvement::getStatutSortie)
                                .collect(Collectors.joining(", ")) : null)
                .build();
    }

    private Livreur convertToEntity(LivreurDTO livreurDTO) {
        Livreur livreur = new Livreur();
        livreur.setId(livreurDTO.getId());
        livreur.setCodeLivreur(livreurDTO.getCodeLivreur());
        livreur.setMotDePasse(livreurDTO.getMotDePasse());
        livreur.setNomLivreur(livreurDTO.getNomLivreur());
        livreur.setPrenomLivreur(livreurDTO.getPrenomLivreur());
        livreur.setNumeroCartePro(livreurDTO.getNumeroCartePro());
        livreur.setTelephonePortable(livreurDTO.getTelephonePortable());
        livreur.setTelephoneKobby(livreurDTO.getTelephoneKobby());
        livreur.setTelephoneAlphapage(livreurDTO.getTelephoneAlphapage());

        if (livreurDTO.getMouvementId() != null) {
            Mouvement mouvement = mouvementRepository.findById(livreurDTO.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID " + livreurDTO.getMouvementId()));
            livreur.setMouvements(List.of(mouvement));
        }

        return livreur;
    }

    @Override
    @Transactional
    public LivreurDTO createLivreur(LivreurDTO livreurDTO) {
        if (livreurDTO.getMouvementId() != null) {
            mouvementRepository.findById(livreurDTO.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID " + livreurDTO.getMouvementId()));
        }

        Livreur livreur = convertToEntity(livreurDTO);
        return convertToDTO(livreurRepository.save(livreur));
    }

    @Override
    public LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO) {
        return null;
    }

    @Transactional
    public void updateLivreur(Integer id, LivreurDTO livreurDTO) {
        // Recherche du Livreur existant
        Livreur existingLivreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur non trouvé"));

        // Mettre à jour les propriétés de l'entité Livreur avec celles du DTO
        existingLivreur.setNomLivreur(livreurDTO.getNomLivreur());
        existingLivreur.setPrenomLivreur(livreurDTO.getPrenomLivreur());
        // Ajoutez toutes les autres propriétés à mettre à jour

        // Vérification et mise à jour du Mouvement si l'ID est présent
        if (livreurDTO.getMouvementId() != null) {
            // Vérification de l'existence du Mouvement dans la base de données
            Mouvement mouvement = mouvementRepository.findById(livreurDTO.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement non trouvé"));

            // Associer le Mouvement au Livreur
            existingLivreur.setMouvements(List.of(mouvement));
        }

        // Sauvegarde de l'entité mise à jour
        livreurRepository.save(existingLivreur);
    }

    @Override
    public Livreur createLivreur(Livreur livreur) {
        return null;
    }

    @Override
    public Livreur updateLivreur(int id, Livreur livreur) {
        return null;
    }

    @Override
    @Transactional
    public void deleteLivreur(int id) {
        if (!livreurRepository.existsById(id)) {
            throw new EntityNotFoundException("Livreur not found with ID " + id);
        }
        livreurRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LivreurDTO getLivreurById(int id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur not found with ID " + id));
        Hibernate.initialize(livreur.getMouvements()); // Initialize the collection
        return convertToDTO(livreur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurDTO> getAllLivreurs() {
        return livreurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLivreur(Livreur livreur) {

    }
}
