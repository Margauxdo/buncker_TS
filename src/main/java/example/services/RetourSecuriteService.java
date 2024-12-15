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

    private static final Logger log = LoggerFactory.getLogger(RetourSecuriteService.class);

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    /*@Override
    public RetourSecuriteDTO createRetourSecurite(RetourSecuriteDTO retourSecuriteDTO) {
        // Initialisation de l'entité RetourSecurite
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setNumero(Long.valueOf(retourSecuriteDTO.getNumero()));
        retourSecurite.setDatesecurite(retourSecuriteDTO.getDatesecurite());
        retourSecurite.setCloture(retourSecuriteDTO.getCloture());
        retourSecurite.setDateCloture(retourSecuriteDTO.getDateCloture());

        // Association avec un Mouvement (si spécifié)
        if (retourSecuriteDTO.getMouvementStatut() != null) {
            Mouvement mouvement = mouvementRepository.findById(Integer.valueOf(retourSecuriteDTO.getMouvementStatut()))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Mouvement introuvable avec l'ID : " + retourSecuriteDTO.getMouvementStatut()));
            retourSecurite.setMouvement(mouvement);
        }

        // Association avec des Clients (si spécifié)
        if (retourSecuriteDTO.getNombreClients() != null && !retourSecuriteDTO.getNombreClients().equals(1)) {
            // Récupérer les clients par leurs IDs
            List<Client> clients = clientRepository.findAllById(retourSecuriteDTO.getNombreClients());

            // Valider que tous les IDs de clients sont valides
            //if (clie) retourSecuriteDTO.getNombreClients() {
                boolean invalidIds = retourSecuriteDTO.getNombreClients().equals(false);
                throw new EntityNotFoundException("Les clients suivants n'ont pas été trouvés : " + invalidIds);
            }

            // Réattacher les clients et établir les relations
            //for (Client client : clients) {
                client.setRetourSecurite(retourSecurite); // Relation bidirectionnelle
            }

            retourSecurite.setClients(clients);
        }

        // Sauvegarder l'entité RetourSecurite (et ses relations)
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);

        // Mapper l'entité sauvegardée vers un DTO et le retourner
        return mapEntityToDto(savedRetourSecurite);
    }*/


    @Override
    public RetourSecuriteDTO createRetourSecurite(RetourSecuriteDTO retourSecuriteDTO) {
        return null;
    }

    @Override
    public RetourSecuriteDTO updateRetourSecurite(int id, RetourSecuriteDTO retourSecuriteDTO) {


    return null;}

    @Override
    public void deleteRetourSecurite(int id) {

    }

    @Override
    public RetourSecuriteDTO getRetourSecurite(int id) {
        return null;
    }

    @Override
    public List<RetourSecuriteDTO> getAllRetourSecurites() {
        return List.of();
    }

    /*@Override
    public RetourSecuriteDTO updateRetourSecurite(int retourSecuriteId, RetourSecuriteDTO retourSecuriteDTO) {
        // Fetch the existing RetourSecurite entity
        RetourSecurite existingRetourSecurite = retourSecuriteRepository.findById(retourSecuriteId)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite not found with id: " + retourSecuriteId));

        // Update basic fields
        existingRetourSecurite.setNumero(Long.valueOf(retourSecuriteDTO.getNumero()));
        existingRetourSecurite.setDatesecurite(retourSecuriteDTO.getDatesecurite());
        existingRetourSecurite.setCloture(retourSecuriteDTO.getCloture());
        existingRetourSecurite.setDateCloture(retourSecuriteDTO.getDateCloture());

        // Update Mouvement association if provided
        if (retourSecuriteDTO.getMouvementStatut() != null) {
            Mouvement mouvement = mouvementRepository.findById(Integer.valueOf(retourSecuriteDTO.getMouvementStatut()))
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with id: " + retourSecuriteDTO.getMouvementStatut()));
            existingRetourSecurite.setMouvement(mouvement);
        }

        // Update Client associations
        if (retourSecuriteDTO.getNombreClients() != null) {
            List<Client> newClients = clientRepository.findAllById(retourSecuriteDTO.getNombreClients());

            // Ensure all provided client IDs exist
            if (newClients.size() != retourSecuriteDTO.getNombreClients().size()) {
                List<Integer> invalidIds = retourSecuriteDTO.getNombreClients().stream()
                        .filter(clientId -> newClients.stream().noneMatch(client -> client.getId().equals(clientId)))
                        .collect(Collectors.toList());
                throw new EntityNotFoundException("Clients not found for IDs: " + invalidIds);
            }

            // Detach existing clients
            for (Client client : existingRetourSecurite.getClients()) {
                client.setRetourSecurite(null);
            }

            // Attach new clients
            for (Client client : newClients) {
                client.setRetourSecurite(existingRetourSecurite);
            }

            existingRetourSecurite.setClients(newClients);
        }

        // Save the updated RetourSecurite
        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.save(existingRetourSecurite);

        // Map the updated entity to a DTO and return
        return mapEntityToDto(updatedRetourSecurite);
    }


    @Override
    public void deleteRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite not found with id: " + id));
        retourSecuriteRepository.delete(retourSecurite);
    }

    @Override
    public RetourSecuriteDTO getRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite not found with id: " + id));
        return mapEntityToDto(retourSecurite);
*/

    /*@Override
    @Transactional
    public List<RetourSecuriteDTO> getAllRetourSecurites() {
        List<RetourSecuriteDTO> retourSecuriteDTOS = new ArrayList<>();
        List<RetourSecurite> retourList = retourSecuriteRepository.findAll();

        if (retourList == null) {
            retourList = new ArrayList<>(); // Initialisation à une liste vide
        }

        if (retourList.isEmpty()) {
            System.out.println("La liste des retours est vide.");
        }

        for (RetourSecurite retourSecurite : retourList) {
            RetourSecuriteDTO retourSecuriteDTO = mapEntityToDto(retourSecurite);

            // Calcul du nombre de clients associés à ce retour de sécurité
            int nombreClients = retourSecurite.getClients() != null ? retourSecurite.getClients().size() : 0;
            retourSecuriteDTO.setNombreClients(nombreClients);

            retourSecuriteDTOS.add(retourSecuriteDTO);
        }

        return retourSecuriteDTOS;
    }*/

    private RetourSecurite mapDtoToEntity(RetourSecuriteDTO dto) {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(dto.getId());
        retourSecurite.setNumero(Long.valueOf(dto.getNumero()));
        retourSecurite.setDatesecurite(dto.getDatesecurite());
        retourSecurite.setCloture(dto.getCloture());
        retourSecurite.setDateCloture(dto.getDateCloture());

        if (dto.getMouvementStatut() != null) {
            Mouvement mouvement = mouvementRepository.findById(Integer.valueOf(dto.getMouvementStatut()))
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with id: " + dto.getMouvementStatut()));
            retourSecurite.setMouvement(mouvement);
        }

        return retourSecurite;
    }

    /*private RetourSecuriteDTO mapEntityToDto(RetourSecurite entity) {
        // Initialisation explicite de la collection lazy
        List<Integer> clientIds = entity.getClients() != null ?
                entity.getClients().stream().map(Client::getId).collect(Collectors.toList()) :
                new ArrayList<>();

        // Calcul du nombre de clients
        Integer nombreClients = entity.getClients() != null ? entity.getClients().size() : 0;

        return RetourSecuriteDTO.builder()
                .id(entity.getId())
                .numero(String.valueOf(entity.getNumero()))
                .datesecurite(entity.getDatesecurite())
                .cloture(entity.getCloture())
                .dateCloture(entity.getDateCloture())
                .clientIds(clientIds) // Liste des IDs des clients
                .nombreClients(nombreClients)  // Ajout du nombre de clients
                .mouvementId(entity.getMouvement() != null ? entity.getMouvement().getId() : null)
                .mouvementStatut(entity.getMouvement() != null ? entity.getMouvement().getStatutSortie() : "Non défini")
                .build();
    }*/




}
