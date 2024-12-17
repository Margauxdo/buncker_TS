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

    @Override
    @Transactional
    public LivreurDTO createLivreur(LivreurDTO livreurDTO) {
        Livreur livreur = mapToEntity(livreurDTO);

        // Sauvegarde de l'entité
        Livreur savedLivreur = livreurRepository.save(livreur);

        return mapToDTO(savedLivreur);
    }

    @Override
    @Transactional
    public LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO) {
        Livreur existingLivreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur introuvable avec l'ID : " + id));

        // Mise à jour des champs
        existingLivreur.setCodeLivreur(livreurDTO.getCodeLivreur());
        existingLivreur.setNomLivreur(livreurDTO.getNomLivreur());
        existingLivreur.setPrenomLivreur(livreurDTO.getPrenomLivreur());
        existingLivreur.setNumeroCartePro(livreurDTO.getNumeroCartePro());
        existingLivreur.setTelephonePortable(livreurDTO.getTelephonePortable());
        existingLivreur.setTelephoneKobby(livreurDTO.getTelephoneKobby());
        existingLivreur.setTelephoneAlphapage(livreurDTO.getTelephoneAlphapage());
        existingLivreur.setDescription(livreurDTO.getDescription());

        // Mise à jour des mouvements associés
        if (livreurDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(livreurDTO.getMouvementIds());
            existingLivreur.setMouvements(mouvements);
        }

        Livreur updatedLivreur = livreurRepository.save(existingLivreur);
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

        return mapToDTO(livreur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurDTO> getAllLivreurs() {
        return livreurRepository.findAll().stream()
                .map(this::mapToDTO)
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
