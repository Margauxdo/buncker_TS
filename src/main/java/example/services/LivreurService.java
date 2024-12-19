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
            livreur.setMouvements(mouvements); // Associer les mouvements
            mouvements.forEach(m -> m.setLivreur(livreur)); // Mettre à jour la relation inverse
        }

        Livreur savedLivreur = livreurRepository.save(livreur);
        return mapToDTO(savedLivreur);
    }




    @Transactional
    public LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO) {
        logger.info("Mise à jour du livreur avec ID: {}", id);
        Livreur existingLivreur = livreurRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Livreur introuvable avec ID: {}", id);
                    return new EntityNotFoundException("Livreur introuvable avec l'ID : " + id);
                });

        logger.info("Livreur existant trouvé: {}", existingLivreur);

        // Mise à jour des champs
        existingLivreur.setCodeLivreur(livreurDTO.getCodeLivreur());
        existingLivreur.setNomLivreur(livreurDTO.getNomLivreur());
        existingLivreur.setPrenomLivreur(livreurDTO.getPrenomLivreur());
        existingLivreur.setNumeroCartePro(livreurDTO.getNumeroCartePro());
        existingLivreur.setTelephonePortable(livreurDTO.getTelephonePortable());
        existingLivreur.setTelephoneKobby(livreurDTO.getTelephoneKobby());
        existingLivreur.setTelephoneAlphapage(livreurDTO.getTelephoneAlphapage());
        existingLivreur.setDescription(livreurDTO.getDescription());

        logger.info("Mise à jour des mouvements associés pour le livreur avec ID: {}", id);
        if (livreurDTO.getMouvementIds() != null) {
            var mouvements = mouvementRepository.findAllById(livreurDTO.getMouvementIds());
            existingLivreur.getMouvements().clear();
            existingLivreur.getMouvements().addAll(mouvements);
            mouvements.forEach(m -> m.setLivreur(existingLivreur));

            logger.info("Mouvements associés mis à jour: {}", mouvements);
        }

        Livreur updatedLivreur = livreurRepository.save(existingLivreur);
        logger.info("Livreur mis à jour: {}", updatedLivreur);
        return new LivreurDTO(updatedLivreur);
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
            throw new EntityNotFoundException("Livreur introuvable avec l'ID : " + id);
        }
        livreurRepository.deleteById(id);
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
