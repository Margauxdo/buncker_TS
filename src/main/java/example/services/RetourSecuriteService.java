package example.services;

import example.DTO.RetourSecuriteDTO;
import example.entity.Client;
import example.entity.Mouvement;
import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetourSecuriteService implements IRetourSecuriteService {

    private static final Logger logger = LoggerFactory.getLogger(RetourSecuriteService.class);


    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Override
    @Transactional
    public RetourSecuriteDTO createRetourSecurite(RetourSecuriteDTO retourSecuriteDTO) {
        RetourSecurite retourSecurite = mapDtoToEntity(retourSecuriteDTO);

        // Association du client
        if (retourSecuriteDTO.getClientId() != null) {
            Client client = clientRepository.findById(retourSecuriteDTO.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + retourSecuriteDTO.getClientId()));
            retourSecurite.setClient(client);
        } else {
            throw new IllegalArgumentException("Le client est requis pour créer un Retour Sécurité.");
        }

        // Association des mouvements (si fournis)
        if (retourSecuriteDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(retourSecuriteDTO.getMouvementIds());
            retourSecurite.setMouvements(mouvements);
            mouvements.forEach(mouvement -> mouvement.setRetourSecurite(retourSecurite)); // Relation bidirectionnelle
        }

        // Sauvegarde de l'entité
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
        return mapEntityToDto(savedRetourSecurite);
    }



    @Override
    @Transactional
    public RetourSecuriteDTO updateRetourSecurite(int id, RetourSecuriteDTO retourSecuriteDTO) {
        // Récupérer l'entité existante
        RetourSecurite existingRetourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + id));

        // Mise à jour des champs simples
        existingRetourSecurite.setNumero(retourSecuriteDTO.getNumero());
        existingRetourSecurite.setDatesecurite(retourSecuriteDTO.getDatesecurite());
        existingRetourSecurite.setCloture(retourSecuriteDTO.getCloture());
        existingRetourSecurite.setDateCloture(retourSecuriteDTO.getDateCloture());

        // Mise à jour du client associé
        if (retourSecuriteDTO.getClientId() != null) {
            Client client = clientRepository.findById(retourSecuriteDTO.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + retourSecuriteDTO.getClientId()));
            existingRetourSecurite.setClient(client);
        }

        // Mise à jour des mouvements
        if (retourSecuriteDTO.getMouvementIds() != null) {
            // Récupérer les mouvements actuels associés au RetourSecurite
            List<Mouvement> currentMouvements = existingRetourSecurite.getMouvements();
            List<Mouvement> newMouvements = mouvementRepository.findAllById(retourSecuriteDTO.getMouvementIds());

            // Supprimer les mouvements obsolètes
            List<Mouvement> mouvementsToRemove = currentMouvements.stream()
                    .filter(mouvement -> !newMouvements.contains(mouvement))
                    .collect(Collectors.toList());
            for (Mouvement mouvementToRemove : mouvementsToRemove) {
                currentMouvements.remove(mouvementToRemove);
                mouvementToRemove.setRetourSecurite(null); // Rompre la relation bidirectionnelle
            }

            // Ajouter les nouveaux mouvements qui ne sont pas encore associés
            for (Mouvement mouvement : newMouvements) {
                if (!currentMouvements.contains(mouvement)) {
                    currentMouvements.add(mouvement);
                    mouvement.setRetourSecurite(existingRetourSecurite); // Établir la relation bidirectionnelle
                }
            }
        }

        // Sauvegarder les modifications dans la base de données
        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.save(existingRetourSecurite);

        // Retourner l'entité mise à jour sous forme de DTO
        return mapEntityToDto(updatedRetourSecurite);
    }

    @Override
    public void deleteRetourSecurite(int id) {
        logger.info("Suppression du Retour Sécurité avec l'ID : {}", id);
        if (!retourSecuriteRepository.existsById(id)){
            logger.warn("Retour Sécurité introuvable avec l'ID : {}", id);
            throw new EntityNotFoundException("Retour Sécurité introuvable avec l'ID : " + id);
        }
        retourSecuriteRepository.deleteById(id);
        logger.info("Retour Sécurité supprimé avec succès");
    }



    @Override
    public RetourSecuriteDTO getRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Retour Sécurité introuvable avec l'ID : " + id));

        return RetourSecuriteDTO.builder()
                .id(retourSecurite.getId())
                .numero(retourSecurite.getNumero())
                .datesecurite(retourSecurite.getDatesecurite())
                .cloture(retourSecurite.getCloture())
                .dateCloture(retourSecurite.getDateCloture())
                .clientNom(retourSecurite.getClient() != null ? retourSecurite.getClient().getName() : "Non spécifié")
                .build();
    }


    @Override
    public List<RetourSecuriteDTO> getAllRetourSecurites() {
        return retourSecuriteRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // Conversion DTO -> Entité
    private RetourSecurite mapDtoToEntity(RetourSecuriteDTO dto) {
        RetourSecurite retourSecurite = RetourSecurite.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .datesecurite(dto.getDatesecurite())
                .cloture(dto.getCloture())
                .dateCloture(dto.getDateCloture())
                .build();
        return retourSecurite;
    }

    // Conversion Entité -> DTO
    private RetourSecuriteDTO mapEntityToDto(RetourSecurite entity) {
        return RetourSecuriteDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .datesecurite(entity.getDatesecurite())
                .cloture(entity.getCloture())
                .dateCloture(entity.getDateCloture())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientNom(entity.getClient() != null ? entity.getClient().getName() : null)
                .mouvementIds(entity.getMouvements().stream().map(Mouvement::getId).toList())
                .mouvementStatuts(entity.getMouvements().stream().map(Mouvement::getStatutSortie).toList())
                .build();
    }

}
