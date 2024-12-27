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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivreurService implements ILivreurService {


    private static final Logger logger = LoggerFactory.getLogger(LivreurService.class);

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    public LivreurService(LivreurRepository livreurRepository, MouvementRepository mouvementRepository) {
        this.livreurRepository = livreurRepository;
        this.mouvementRepository = mouvementRepository;
    }

    @Override
    @Transactional
    public LivreurDTO createLivreur(LivreurDTO livreurDTO) {
        Livreur livreur = mapToEntity(livreurDTO);

        // Associer les mouvements
        if (livreurDTO.getMouvementIds() != null && !livreurDTO.getMouvementIds().isEmpty()) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(livreurDTO.getMouvementIds());

            mouvements.forEach(m -> {
                // Si le mouvement est déjà associé à un autre livreur, dissociez-le
                if (m.getLivreur() != null) {
                    logger.warn("Le mouvement ID {} est déjà associé au livreur ID {}. Dissociation en cours.", m.getId(), m.getLivreur().getId());
                    m.setLivreur(null); // Dissocier le mouvement de son livreur actuel
                }
                m.setLivreur(livreur); // Associer le mouvement au nouveau livreur
            });

            livreur.setMouvements(mouvements);
            mouvementRepository.saveAll(mouvements); // Sauvegarder les changements des mouvements
        }

        Livreur savedLivreur = livreurRepository.save(livreur);
        logger.info("Nouveau livreur créé avec ID: {}", savedLivreur.getId());
        return mapToDTO(savedLivreur);
    }


    @Transactional
    public LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO) {
        logger.info("Mise à jour du livreur avec ID: {}", id);

        // Récupérer le livreur existant
        Livreur existingLivreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + id));

        // Mise à jour des informations du livreur
        existingLivreur.setCodeLivreur(livreurDTO.getCodeLivreur());
        existingLivreur.setNomLivreur(livreurDTO.getNomLivreur());
        existingLivreur.setPrenomLivreur(livreurDTO.getPrenomLivreur());
        existingLivreur.setNumeroCartePro(livreurDTO.getNumeroCartePro());
        existingLivreur.setTelephonePortable(livreurDTO.getTelephonePortable());
        existingLivreur.setTelephoneKobby(livreurDTO.getTelephoneKobby());
        existingLivreur.setTelephoneAlphapage(livreurDTO.getTelephoneAlphapage());
        existingLivreur.setDescription(livreurDTO.getDescription());

        // Gestion des mouvements associés
        if (livreurDTO.getMouvementIds() != null && !livreurDTO.getMouvementIds().isEmpty()) {
            logger.info("Mise à jour des mouvements pour le livreur ID: {}", id);

            List<Mouvement> nouveauxMouvements = mouvementRepository.findAllById(livreurDTO.getMouvementIds());

            // Vérifier que chaque mouvement n'est pas déjà associé à un autre livreur
            for (Mouvement mouvement : nouveauxMouvements) {
                if (mouvement.getLivreur() != null && !mouvement.getLivreur().equals(existingLivreur)) {
                    throw new IllegalArgumentException(
                            "Le mouvement ID " + mouvement.getId() + " est déjà associé au livreur ID " + mouvement.getLivreur().getId()
                    );
                }
            }

            // Supprimer les anciens mouvements
            existingLivreur.getMouvements().forEach(m -> m.setLivreur(null));

            // Associer les nouveaux mouvements
            nouveauxMouvements.forEach(m -> m.setLivreur(existingLivreur));
            existingLivreur.getMouvements().clear();
            existingLivreur.getMouvements().addAll(nouveauxMouvements);
        } else {
            // Si aucun mouvement, dissocier tous les mouvements existants
            existingLivreur.getMouvements().forEach(m -> m.setLivreur(null));
            existingLivreur.getMouvements().clear();
        }

        Livreur updatedLivreur = livreurRepository.save(existingLivreur);
        logger.info("Livreur mis à jour avec succès: {}", updatedLivreur);
        return mapToDTO(updatedLivreur);
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
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + id));

        // Dissocier et supprimer explicitement les mouvements associés
        if (livreur.getMouvements() != null && !livreur.getMouvements().isEmpty()) {
            for (Mouvement mouvement : livreur.getMouvements()) {
                mouvement.setLivreur(null); // Dissocier
            }
            mouvementRepository.saveAll(livreur.getMouvements()); // Sauvegarder les changements
            mouvementRepository.deleteAll(livreur.getMouvements()); // Supprimer
        }

        // Supprimer le livreur
        livreurRepository.delete(livreur);
    }










    @Override
    @Transactional(readOnly = true)
    public LivreurDTO getLivreurById(int id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + id));
        return new LivreurDTO(livreur); // Utilisation du constructeur
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurDTO> getAllLivreurs() {
        return livreurRepository.findAll().stream()
                .map(LivreurDTO::new) // Utilisation du constructeur pour chaque livreur
                .collect(Collectors.toList());
    }
    @Override
    public void saveLivreur(Livreur livreur) {

    }

    // Méthode de conversion Entité -> DTO
    private LivreurDTO mapToDTO(Livreur livreur) {
        return LivreurDTO.builder()
                .id(livreur.getId())
                .codeLivreur(livreur.getCodeLivreur())
                .nomLivreur(livreur.getNomLivreur())
                .prenomLivreur(livreur.getPrenomLivreur())
                .numeroCartePro(livreur.getNumeroCartePro())
                .telephonePortable(livreur.getTelephonePortable())
                .telephoneKobby(livreur.getTelephoneKobby())
                .telephoneAlphapage(livreur.getTelephoneAlphapage())
                .description(livreur.getDescription())
                .mouvementIds(livreur.getMouvements() != null ? livreur.getMouvements().stream().map(Mouvement::getId).toList() : null)
                .mouvementStatuts(livreur.getMouvements() != null ? livreur.getMouvements().stream().map(Mouvement::getStatutSortie).toList() : null)
                .build();
    }

    // Méthode de conversion DTO -> Entité
    private Livreur mapToEntity(LivreurDTO livreurDTO) {
        Livreur livreur = Livreur.builder()
                .id(livreurDTO.getId())
                .codeLivreur(livreurDTO.getCodeLivreur())
                .nomLivreur(livreurDTO.getNomLivreur())
                .prenomLivreur(livreurDTO.getPrenomLivreur())
                .numeroCartePro(livreurDTO.getNumeroCartePro())
                .telephonePortable(livreurDTO.getTelephonePortable())
                .telephoneKobby(livreurDTO.getTelephoneKobby())
                .telephoneAlphapage(livreurDTO.getTelephoneAlphapage())
                .description(livreurDTO.getDescription())
                .build();

        if (livreurDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(livreurDTO.getMouvementIds());
            livreur.setMouvements(mouvements);
        }

        return livreur;
    }


}
