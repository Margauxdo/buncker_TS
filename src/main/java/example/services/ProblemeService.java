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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemeService implements IProblemeService {

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
    public ProblemeDTO createProbleme(ProblemeDTO problemeDTO) {
        // Vérification que valiseId est non nul
        if (problemeDTO.getValiseId() == null) {
            throw new IllegalArgumentException("La valise est requise pour créer un problème.");
        }

        // Récupérer la valise depuis l'ID
        Valise valise = valiseRepository.findById(problemeDTO.getValiseId())
                .orElseThrow(() -> new ResourceNotFoundException("Valise introuvable avec l'ID : " + problemeDTO.getValiseId()));

        // Récupérer les clients depuis leurs identifiants (si clientIds est non nul et non vide)
        List<Client> clients = new ArrayList<>();
        if (problemeDTO.getClientIds() != null && !problemeDTO.getClientIds().isEmpty()) {
            clients = clientRepository.findAllById(problemeDTO.getClientIds())
                    .stream()
                    .filter(client -> client != null)  // Assurer qu'aucun client n'est null
                    .collect(Collectors.toList());
        }

        // Conversion DTO -> Entité
        Probleme probleme = mapToEntity(problemeDTO);
        probleme.setValise(valise);  // Associer la valise au problème
        probleme.setClients(clients);  // Associer les clients au problème

        // Sauvegarde de l'entité
        Probleme savedProbleme = problemeRepository.save(probleme);

        // Conversion Entité -> DTO
        return mapToDTO(savedProbleme);
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

    @Override
    public ProblemeDTO getProblemeById(int id) {
        Probleme probleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problème introuvable avec l'ID : " + id));
        return mapToDTO(probleme);
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
        return ProblemeDTO.builder()
                .id(probleme.getId())
                .descriptionProbleme(probleme.getDescriptionProbleme())
                .detailsProbleme(probleme.getDetailsProbleme())
                .valiseId(probleme.getValise() != null ? probleme.getValise().getId() : null)
                .valiseNumero(String.valueOf(probleme.getValise() != null ? probleme.getValise().getNumeroValise() : null))
                .clientIds(probleme.getClients() != null ? probleme.getClients().stream().map(Client::getId).toList() : null)
                .clientNoms(probleme.getClients() != null ? probleme.getClients().stream().map(Client::getName).toList() : null)
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
