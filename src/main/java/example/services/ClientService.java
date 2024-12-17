package example.services;

import example.DTO.ClientDTO;
import example.entity.*;
import example.interfaces.IClientService;
import example.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final RetourSecuriteRepository retourSecuriteRepository;
    private final RegleRepository regleRepository;
    private final ProblemeRepository problemeRepository;
    private final ValiseRepository valiseRepository;

    public ClientService(ClientRepository clientRepository,
                         RetourSecuriteRepository retourSecuriteRepository,
                         RegleRepository regleRepository,
                         ProblemeRepository problemeRepository,
                         ValiseRepository valiseRepository) {
        this.clientRepository = clientRepository;
        this.retourSecuriteRepository = retourSecuriteRepository;
        this.regleRepository = regleRepository;
        this.problemeRepository = problemeRepository;
        this.valiseRepository = valiseRepository;
    }

    private ClientDTO convertToDTO(Client client) {
        // Initialisation de la collection chargée paresseusement
        client.getRetourSecurites().size(); // Force le chargement

        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .adresse(client.getAdresse())
                .email(client.getEmail())
                .telephoneExploitation(client.getTelephoneExploitation())
                .ville(client.getVille())
                .personnelEtFonction(client.getPersonnelEtFonction())
                .ramassage1(client.getRamassage1())
                .ramassage2(client.getRamassage2())
                .ramassage3(client.getRamassage3())
                .ramassage4(client.getRamassage4())
                .ramassage5(client.getRamassage5())
                .ramassage6(client.getRamassage6())
                .ramassage7(client.getRamassage7())
                .envoiparDefaut(client.getEnvoiparDefaut())
                .memoRetourSecurite1(client.getMemoRetourSecurite1())
                .memoRetourSecurite2(client.getMemoRetourSecurite2())
                .typeSuivie(client.getTypeSuivie())
                .codeClient(client.getCodeClient())
                .problemeId(client.getProbleme() != null ? client.getProbleme().getId() : null)
                .retourSecuriteIds(client.getRetourSecurites() != null ?
                        client.getRetourSecurites().stream().map(RetourSecurite::getId).collect(Collectors.toList()) : null)
                .regleId(client.getRegle() != null ? client.getRegle().getId() : null)
                .valiseIds(client.getValises() != null ?
                        client.getValises().stream().map(Valise::getId).collect(Collectors.toList()) : null)
                .build();
    }


    // Conversion DTO -> Entité
    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = Client.builder()
                .id(clientDTO.getId())
                .name(clientDTO.getName())
                .adresse(clientDTO.getAdresse())
                .email(clientDTO.getEmail())
                .telephoneExploitation(clientDTO.getTelephoneExploitation())
                .ville(clientDTO.getVille())
                .personnelEtFonction(clientDTO.getPersonnelEtFonction())
                .ramassage1(clientDTO.getRamassage1())
                .ramassage2(clientDTO.getRamassage2())
                .ramassage3(clientDTO.getRamassage3())
                .ramassage4(clientDTO.getRamassage4())
                .ramassage5(clientDTO.getRamassage5())
                .ramassage6(clientDTO.getRamassage6())
                .ramassage7(clientDTO.getRamassage7())
                .envoiparDefaut(clientDTO.getEnvoiparDefaut())
                .memoRetourSecurite1(clientDTO.getMemoRetourSecurite1())
                .memoRetourSecurite2(clientDTO.getMemoRetourSecurite2())
                .typeSuivie(clientDTO.getTypeSuivie())
                .codeClient(clientDTO.getCodeClient())
                .build();

        // Gestion des relations
        if (clientDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(clientDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("Regle introuvable avec l'ID : " + clientDTO.getRegleId()));
            client.setRegle(regle);
        }

        if (clientDTO.getProblemeId() != null) {
            Probleme probleme = problemeRepository.findById(clientDTO.getProblemeId())
                    .orElseThrow(() -> new EntityNotFoundException("Probleme introuvable avec l'ID : " + clientDTO.getProblemeId()));
            client.setProbleme(probleme);
        }

        if (clientDTO.getRetourSecuriteIds() != null) {
            List<RetourSecurite> retourSecurites = retourSecuriteRepository.findAllById(clientDTO.getRetourSecuriteIds());
            client.setRetourSecurites(retourSecurites);
        }

        if (clientDTO.getValiseIds() != null) {
            List<Valise> valises = valiseRepository.findAllById(clientDTO.getValiseIds());
            client.setValises(valises);
        }

        return client;
    }

    @Override
    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        if (clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà : " + clientDTO.getEmail());
        }
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }


    @Override
    @Transactional
    public ClientDTO updateClient(Integer id, ClientDTO clientDTO) {
        // Récupérer l'entité existante
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + id));

        // Mise à jour des champs simples
        existingClient.setName(clientDTO.getName());
        existingClient.setAdresse(clientDTO.getAdresse());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setTelephoneExploitation(clientDTO.getTelephoneExploitation());
        existingClient.setVille(clientDTO.getVille());
        existingClient.setPersonnelEtFonction(clientDTO.getPersonnelEtFonction());
        existingClient.setRamassage1(clientDTO.getRamassage1());
        existingClient.setRamassage2(clientDTO.getRamassage2());
        existingClient.setRamassage3(clientDTO.getRamassage3());
        existingClient.setRamassage4(clientDTO.getRamassage4());
        existingClient.setRamassage5(clientDTO.getRamassage5());
        existingClient.setRamassage6(clientDTO.getRamassage6());
        existingClient.setRamassage7(clientDTO.getRamassage7());
        existingClient.setEnvoiparDefaut(clientDTO.getEnvoiparDefaut());
        existingClient.setMemoRetourSecurite1(clientDTO.getMemoRetourSecurite1());
        existingClient.setMemoRetourSecurite2(clientDTO.getMemoRetourSecurite2());
        existingClient.setTypeSuivie(clientDTO.getTypeSuivie());
        existingClient.setCodeClient(clientDTO.getCodeClient());

        // Mise à jour des relations
        // Règle
        if (clientDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(clientDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("Règle introuvable avec l'ID : " + clientDTO.getRegleId()));
            existingClient.setRegle(regle);
        }

        // Problème
        if (clientDTO.getProblemeId() != null) {
            Probleme probleme = problemeRepository.findById(clientDTO.getProblemeId())
                    .orElseThrow(() -> new EntityNotFoundException("Problème introuvable avec l'ID : " + clientDTO.getProblemeId()));
            existingClient.setProbleme(probleme);
        }

        // Dissociation des retours de sécurité avant modification
        if (existingClient.getRetourSecurites() != null) {
            existingClient.getRetourSecurites().clear();  // Dissocier les entités enfant avant la mise à jour
        }

        // Mise à jour des retours de sécurité
        if (clientDTO.getRetourSecuriteIds() != null) {
            List<RetourSecurite> retourSecurites = retourSecuriteRepository.findAllById(clientDTO.getRetourSecuriteIds());
            existingClient.setRetourSecurites(retourSecurites);  // Réaffecter les retours de sécurité
        }

        // Mise à jour des valises
        if (clientDTO.getValiseIds() != null) {
            List<Valise> valises = valiseRepository.findAllById(clientDTO.getValiseIds());
            existingClient.setValises(valises);  // Réaffecter les valises
        }

        // Sauvegarder l'entité client mise à jour
        Client updatedClient = clientRepository.save(existingClient);

        // Convertir en DTO et retourner
        return convertToDTO(updatedClient);
    }



    @Override
    @Transactional
    public void deleteClient(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + id));
        clientRepository.delete(client);
    }

    public ClientDTO getClientById(int id) {
        // Utilisation d'une requête JOIN FETCH pour charger aussi les retours de sécurité
        Client client = clientRepository.findByIdWithRetourSecurites(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + id));

        return convertToDTO(client);
    }




    @Override
    @Transactional
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}
