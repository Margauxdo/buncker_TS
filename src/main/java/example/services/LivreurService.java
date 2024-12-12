package example.services;

import example.DTO.LivreurDTO;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import example.repositories.LivreurRepository;
import example.repositories.MouvementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .mouvementStatutSortie(livreur.getMouvements() != null && !livreur.getMouvements().isEmpty()
                        ? livreur.getMouvements().stream()
                        .map(Mouvement::getStatutSortie) // Récupérer le statut de chaque mouvement
                        .collect(Collectors.joining(", ")) // Joindre les statuts avec une virgule
                        : "Aucun mouvement")

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
            livreur.setMouvements((List<Mouvement>) mouvement);
        }

        return livreur;
    }

    @Override
    public LivreurDTO createLivreur(LivreurDTO livreurDTO) {
        if (livreurDTO.getMouvementId() != null) {
            Mouvement mouvement = mouvementRepository.findById(livreurDTO.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID " + livreurDTO.getMouvementId()));

        }

        Livreur livreur = convertToEntity(livreurDTO);
        return convertToDTO(livreurRepository.save(livreur));
    }

    @Override
    public LivreurDTO updateLivreur(int id, LivreurDTO livreurDTO) {
        if (!livreurRepository.existsById(id)) {
            throw new EntityNotFoundException("Livreur not found with ID " + id);
        }
        Livreur livreur = convertToEntity(livreurDTO);
        livreur.setId(id);
        return convertToDTO(livreurRepository.save(livreur));
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
    public void deleteLivreur(int id) {
        if (!livreurRepository.existsById(id)) {
            throw new EntityNotFoundException("Livreur not found with ID " + id);
        }
        livreurRepository.deleteById(id);
    }

    @Override
    public LivreurDTO getLivreurById(int id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livreur not found with ID " + id));
        return convertToDTO(livreur);
    }

    @Override
    public List<LivreurDTO> getAllLivreurs() {
        return livreurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLivreur(Livreur livreur) {

    }
}
