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

        // Association avec un client
        if (retourSecuriteDTO.getClientId() != null) {
            Client client = clientRepository.findById(retourSecuriteDTO.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + retourSecuriteDTO.getClientId()));
            retourSecurite.setClient(client);
        }

        // Association avec les mouvements
        if (retourSecuriteDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(retourSecuriteDTO.getMouvementIds());
            retourSecurite.setMouvements(mouvements);
        }

        // Sauvegarde de l'entité
        RetourSecurite savedRetourSecurite = retourSecuriteRepository.save(retourSecurite);

        return mapEntityToDto(savedRetourSecurite);
    }

    @Override
    @Transactional
    public RetourSecuriteDTO updateRetourSecurite(int id, RetourSecuriteDTO retourSecuriteDTO) {
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
            List<Mouvement> mouvements = mouvementRepository.findAllById(retourSecuriteDTO.getMouvementIds());
            existingRetourSecurite.setMouvements(mouvements);
        }

        // Sauvegarde de l'entité mise à jour
        RetourSecurite updatedRetourSecurite = retourSecuriteRepository.save(existingRetourSecurite);

        return mapEntityToDto(updatedRetourSecurite);
    }

    @Override
    public void deleteRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + id));
        retourSecuriteRepository.delete(retourSecurite);
    }

    @Override
    public RetourSecuriteDTO getRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RetourSecurite introuvable avec l'ID : " + id));
        return mapEntityToDto(retourSecurite);
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
                .mouvementStatuts(entity.getMouvements().stream().map(Mouvement::getStatutSortie).toList()) // Here we populate mouvementStatuts
                .build();
    }

}
