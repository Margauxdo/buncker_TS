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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetourSecuriteService implements IRetourSecuriteService {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Override
    public RetourSecuriteDTO createRetourSecurite(RetourSecuriteDTO retourSecuriteDTO) {
        RetourSecurite retourSecurite = mapDtoToEntity(retourSecuriteDTO);
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);
        return mapEntityToDto(savedRetourSecurite);
    }

    @Override
    public RetourSecuriteDTO updateRetourSecurite(int id, RetourSecuriteDTO retourSecuriteDTO) {
        RetourSecurite existingRetourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite not found with id: " + id));

        existingRetourSecurite.setNumero(retourSecuriteDTO.getNumero());
        existingRetourSecurite.setDatesecurite(retourSecuriteDTO.getDatesecurite());
        existingRetourSecurite.setCloture(retourSecuriteDTO.getCloture());
        existingRetourSecurite.setDateCloture(retourSecuriteDTO.getDateCloture());

        if (retourSecuriteDTO.getMouvementId() != null) {
            Mouvement mouvement = mouvementRepository.findById(retourSecuriteDTO.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with id: " + retourSecuriteDTO.getMouvementId()));
            existingRetourSecurite.setMouvement(mouvement);
        }

        List<Client> clients = clientRepository.findAllById(retourSecuriteDTO.getClientIds());
        existingRetourSecurite.setClients(clients);

        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.save(existingRetourSecurite);
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
    }


    @Override
    public List<RetourSecuriteDTO> getAllRetourSecurites() {
        return retourSecuriteRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }


    private RetourSecurite mapDtoToEntity(RetourSecuriteDTO dto) {
        RetourSecurite retourSecurite = new RetourSecurite();
        retourSecurite.setId(dto.getId());
        retourSecurite.setNumero(dto.getNumero());
        retourSecurite.setDatesecurite(dto.getDatesecurite());
        retourSecurite.setCloture(dto.getCloture());
        retourSecurite.setDateCloture(dto.getDateCloture());

        if (dto.getMouvementId() != null) {
            Mouvement mouvement = mouvementRepository.findById(dto.getMouvementId())
                    .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with id: " + dto.getMouvementId()));
            retourSecurite.setMouvement(mouvement);
        }

        return retourSecurite;
    }

    private RetourSecuriteDTO mapEntityToDto(RetourSecurite entity) {
        // Initialisation explicite de la collection lazy
        List<Integer> clientIds = entity.getClients() != null ?
                entity.getClients().stream().map(Client::getId).collect(Collectors.toList()) :
                new ArrayList<>();

        return RetourSecuriteDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .datesecurite(entity.getDatesecurite())
                .cloture(entity.getCloture())
                .dateCloture(entity.getDateCloture())
                .clientIds(clientIds) // Assurez-vous que cette liste est initialisée
                .mouvementId(entity.getMouvement() != null ? entity.getMouvement().getId() : null)
                .mouvementStatut(entity.getMouvement() != null ? entity.getMouvement().getStatutSortie() : "Non défini")
                .build();
    }



}
