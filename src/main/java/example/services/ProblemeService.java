package example.services;

import example.DTO.ProblemeDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IProblemeService;
import example.repositories.ClientRepository;
import example.repositories.ProblemeRepository;
import example.repositories.ValiseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemeService implements IProblemeService {


    private static final Logger logger = LoggerFactory.getLogger(ProblemeService.class);


    private final ProblemeRepository problemeRepository;
    private final ValiseRepository valiseRepository;
    private final ClientRepository clientRepository;

    public ProblemeService(
            ProblemeRepository problemeRepository,
            ValiseRepository valiseRepository,
            ClientRepository clientRepository) {
        this.problemeRepository = problemeRepository;
        this.valiseRepository = valiseRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Probleme createProbleme(Probleme probleme) {
        return null;
    }

    @Override
    public Probleme updateProbleme(int id, Probleme probleme) {
        return null;
    }

    @Override
    @Transactional
    public ProblemeDTO createProbleme(ProblemeDTO problemeDTO) {
        // Récupération de la valise
        Valise valise = valiseRepository.findById(problemeDTO.getValiseId())
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));

        // Récupération du client
        List<Client> clients = new ArrayList<>();
        for (Integer clientId : problemeDTO.getClientIds()) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
            clients.add(client);
        }

        // Création du problème
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme(problemeDTO.getDescriptionProbleme());
        probleme.setDetailsProbleme(problemeDTO.getDetailsProbleme());
        probleme.setValise(valise);
        probleme.setClients(clients);  // Associer les clients

        // Sauvegarde du problème
        problemeRepository.save(probleme);

        // Retourner le DTO
        return new ProblemeDTO(probleme);  // Retourner un DTO pour la réponse
    }

    @Override
    public ProblemeDTO updateProbleme(int id, ProblemeDTO problemeDTO) {
        // Recherche de l'entité existante
        Probleme existingProbleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problème introuvable avec l'ID : " + id));

        // Mise à jour des champs
        existingProbleme.setDescriptionProbleme(problemeDTO.getDescriptionProbleme());
        existingProbleme.setDetailsProbleme(problemeDTO.getDetailsProbleme());

        // Mise à jour de la valise associée
        if (problemeDTO.getValiseId() != null) {
            Valise valise = valiseRepository.findById(problemeDTO.getValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Valise introuvable avec l'ID : " + problemeDTO.getValiseId()));
            existingProbleme.setValise(valise);
        }

        // Mise à jour des clients associés
        if (problemeDTO.getClientIds() != null) {
            List<Client> clients = clientRepository.findAllById(problemeDTO.getClientIds());
            existingProbleme.setClients(clients);
        }

        // Sauvegarde des modifications
        Probleme updatedProbleme = problemeRepository.save(existingProbleme);

        // Conversion Entité -> DTO
        return mapToDTO(updatedProbleme);
    }

    @Override
    public void deleteProbleme(int id) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problème introuvable avec l'ID : " + id);
        }
        problemeRepository.deleteById(id);
    }

    @Override
    public Probleme getProbleme(int id) {
        return null;
    }

    @Transactional
    @Override
    public ProblemeDTO getProblemeById(int id) {
        // Récupérer le problème avec ses entités liées
        Probleme probleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Probleme not found"));

        // La session est toujours ouverte ici, donc la collection 'clients' peut être chargée correctement
        return new ProblemeDTO(probleme);
    }

    @Override
    public List<ProblemeDTO> getAllProblemes() {
        return problemeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByDescriptionAndDetails(String description, String details) {
        return problemeRepository.existsByDescriptionProblemeAndDetailsProbleme(description, details);
    }

    // Conversion Entité -> DTO
    private ProblemeDTO mapToDTO(Probleme probleme) {
        List<String> clientNoms = probleme.getClients() != null
                ? probleme.getClients().stream().map(Client::getName).collect(Collectors.toList())
                : new ArrayList<>();

        return ProblemeDTO.builder()
                .id(probleme.getId())
                .descriptionProbleme(probleme.getDescriptionProbleme())
                .detailsProbleme(probleme.getDetailsProbleme())
                .valiseId(probleme.getValise() != null ? probleme.getValise().getId() : null)
                .numeroDeValise(probleme.getValise() != null ? probleme.getValise().getNumeroValise() : null)
                .clientNoms(clientNoms)
                .build();
    }


    private Probleme mapToEntity(ProblemeDTO problemeDTO) {
        Probleme probleme = new Probleme();
        probleme.setDescriptionProbleme(problemeDTO.getDescriptionProbleme());
        probleme.setDetailsProbleme(problemeDTO.getDetailsProbleme());

        // Associer la valise à partir de son ID
        if (problemeDTO.getValiseId() != null) {
            Valise valise = valiseRepository.findById(problemeDTO.getValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Valise introuvable avec l'ID : " + problemeDTO.getValiseId()));
            probleme.setValise(valise);
        } else {
            throw new IllegalArgumentException("La valise est obligatoire");
        }

        // Mappage des clients
        if (problemeDTO.getClientIds() != null && !problemeDTO.getClientIds().isEmpty()) {
            List<Client> clients = clientRepository.findAllById(problemeDTO.getClientIds());
            if (clients.isEmpty()) {
                throw new IllegalArgumentException("Aucun client trouvé pour les IDs fournis.");
            }
            probleme.setClients(clients);
        }

        return probleme;
    }



}
