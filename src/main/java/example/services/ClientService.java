package example.services;

import example.DTO.ClientDTO;
import example.entity.*;
import example.interfaces.IClientService;
import example.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);


    private final ClientRepository clientRepository;
    private final RetourSecuriteRepository retourSecuriteRepository;
    private final RegleRepository regleRepository;
    private final ProblemeRepository problemeRepository;
    private final ValiseRepository valiseRepository;
    private final MouvementRepository mouvementRepository;

    public ClientService(ClientRepository clientRepository,
                         RetourSecuriteRepository retourSecuriteRepository,
                         RegleRepository regleRepository,
                         ProblemeRepository problemeRepository,
                         ValiseRepository valiseRepository, MouvementRepository mouvementRepository) {
        this.clientRepository = clientRepository;
        this.retourSecuriteRepository = retourSecuriteRepository;
        this.regleRepository = regleRepository;
        this.problemeRepository = problemeRepository;
        this.valiseRepository = valiseRepository;
        this.mouvementRepository = mouvementRepository;
    }

    private ClientDTO convertToDTO(Client client) {
        System.out.println("Client ID: " + client.getId());
        System.out.println("Valises: " + client.getValises());
        System.out.println("RetourSecurites: " + client.getRetourSecurites());
       // Hibernate.initialize(client.getValises()); // Charge explicitement les valises
        //Hibernate.initialize(client.getRetourSecurites()); // Charge explicitement les retours sécurité

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
                        client.getRetourSecurites().stream().map(RetourSecurite::getId).collect(Collectors.toList()) : new ArrayList<>())
                .valiseIds(client.getValises() != null ?
                        client.getValises().stream().map(Valise::getId).collect(Collectors.toList()) : new ArrayList<>())
                .numeroRetourSecurite(client.getRetourSecurites() != null ?
                        client.getRetourSecurites().stream().map(RetourSecurite::getNumero).collect(Collectors.toList()) : new ArrayList<>())
                .numeroValise(client.getValises() != null ?
                        client.getValises().stream().map(Valise::getNumeroValise).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        // Création de l'entité Client à partir des champs simples
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

        // Gestion du problème
        if (clientDTO.getProblemeId() != null) {
            Probleme probleme = problemeRepository.findById(clientDTO.getProblemeId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Problème introuvable avec l'ID : " + clientDTO.getProblemeId()));
            client.setProbleme(probleme);
        }

        // Gestion des valises
        if (clientDTO.getValiseIds() != null && !clientDTO.getValiseIds().isEmpty()) {
            List<Valise> valises = valiseRepository.findAllById(clientDTO.getValiseIds());
            if (valises.isEmpty()) {
                throw new EntityNotFoundException("Aucune valise trouvée pour les IDs fournis : " + clientDTO.getValiseIds());
            }
            client.setValises(valises);
        } else {
            client.setValises(new ArrayList<>()); // Initialisation par défaut
        }

        // Gestion des retours sécurité
        if (clientDTO.getRetourSecuriteIds() != null && !clientDTO.getRetourSecuriteIds().isEmpty()) {
            List<RetourSecurite> retourSecurites = retourSecuriteRepository.findAllById(clientDTO.getRetourSecuriteIds());
            if (retourSecurites.isEmpty()) {
                throw new EntityNotFoundException("Aucun RetourSecurite trouvé pour les IDs fournis : " + clientDTO.getRetourSecuriteIds());
            }
            client.setRetourSecurites(retourSecurites);
        } else {
            client.setRetourSecurites(new ArrayList<>()); // Initialisation par défaut
        }

        // Gestion de la règle (optionnel si nécessaire)
        if (clientDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(clientDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Règle introuvable avec l'ID : " + clientDTO.getRegleId()));
            client.setRegle(regle);
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
        // Mise à jour des valises
        if (clientDTO.getValiseIds() != null) {
            List<Valise> existingValises = existingClient.getValises();
            List<Valise> newValises = valiseRepository.findAllById(clientDTO.getValiseIds());

            // Supprimer les valises qui ne sont plus présentes
            existingValises.removeIf(valise -> !newValises.contains(valise));

            // Ajouter les nouvelles valises
            newValises.forEach(valise -> {
                if (!existingValises.contains(valise)) {
                    existingValises.add(valise);
                    valise.setClient(existingClient); // Mettre à jour la relation
                }
            });
        }

        // Mise à jour des retours sécurité
        if (clientDTO.getRetourSecuriteIds() != null) {
            List<RetourSecurite> existingRetourSecurites = existingClient.getRetourSecurites();
            List<RetourSecurite> newRetourSecurites = retourSecuriteRepository.findAllById(clientDTO.getRetourSecuriteIds());

            // Supprimer les retours qui ne sont plus présents
            existingRetourSecurites.removeIf(retour -> !newRetourSecurites.contains(retour));

            // Ajouter les nouveaux retours
            newRetourSecurites.forEach(retour -> {
                if (!existingRetourSecurites.contains(retour)) {
                    existingRetourSecurites.add(retour);
                    retour.setClient(existingClient); // Mettre à jour la relation
                }
            });
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

        // Supprimer les mouvements associés à chaque valise
        if (client.getValises() != null) {
            client.getValises().forEach(valise -> {
                if (valise.getMouvements() != null) {
                    mouvementRepository.deleteAll(valise.getMouvements());
                }
            });
        }

        // Supprimer toutes les valises associées
        if (client.getValises() != null) {
            valiseRepository.deleteAll(client.getValises());
        }

        // Supprimer tous les retours sécurité associés
        if (client.getRetourSecurites() != null) {
            retourSecuriteRepository.deleteAll(client.getRetourSecurites());
        }

        // Supprimer le client
        clientRepository.delete(client);
    }



    public ClientDTO getClientById(int id) {
        // Utilisation d'une requête JOIN FETCH pour charger aussi les retours de sécurité
        Client client = clientRepository.findByIdWithRetourSecurites(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + id));

        return convertToDTO(client);
    }

    @Transactional
    public ClientDTO getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + id));

        // Force le chargement des collections
        client.getValises().size();
        client.getRetourSecurites().size();

        return convertToDTO(client);
    }




    @Transactional
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();

        clients.forEach(client -> {
            String retourSecuriteNumeros = client.getRetourSecurites() != null
                    ? client.getRetourSecurites().stream()
                    .map(RetourSecurite::getNumero) // Extraire les numéros
                    .map(String::valueOf)           // Convertir en String
                    .collect(Collectors.joining(", ")) // Joindre en une chaîne
                    : "Non défini";

            String valiseNumeros = client.getValises() != null
                    ? client.getValises().stream()
                    .map(Valise::getNumeroValise)  // Extraire les numéros de valise
                    .collect(Collectors.joining(", ")) // Joindre en une chaîne
                    : "Non défini";

            log.info("Client ID: {}, RetourSecurite: {}, Valises: {}",
                    client.getId(),
                    retourSecuriteNumeros,
                    valiseNumeros);
        });

        return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
    }






    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}
