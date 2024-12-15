package example.services;

import example.DTO.ClientDTO;
import example.DTO.ProblemeDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import example.services.ClientService;  // Assurez-vous que l'import est présent


@Service
public class ProblemeService implements IProblemeService {

    private final ProblemeRepository problemeRepository;
    private final ValiseService valiseService;
    private final ClientService clientService;

    public ProblemeService(ProblemeRepository problemeRepository, ValiseService valiseService, ClientService clientService) {
        this.problemeRepository = problemeRepository;
        this.valiseService = valiseService;
        this.clientService = clientService;
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
        // Récupération de la ValiseDTO
        Valise valiseDTO = valiseService.getValiseById(problemeDTO.getValiseId());
        if (valiseDTO == null) {
            throw new ResourceNotFoundException("Valise not found with ID: " + problemeDTO.getValiseId());
        }

        // Conversion de ValiseDTO en Valise (Assurez-vous d'avoir une méthode utilitaire pour la conversion)
        Valise valise = convertToEntity(valiseDTO);

        // Création de l'entité Probleme
        Probleme probleme = Probleme.builder()
                .descriptionProbleme(problemeDTO.getDescriptionProbleme())
                .detailsProbleme(problemeDTO.getDetailsProbleme())
                .valise(valise) // Utilisation de l'entité Valise
                .build();

        // Sauvegarde de Probleme dans le repository
        Probleme savedProbleme = problemeRepository.save(probleme);

        // Conversion de l'entité sauvegardée en DTO
        return convertToDTO(savedProbleme);
    }

    // Méthode utilitaire pour convertir un ValiseDTO en Valise
    private Valise convertToEntity(Valise valiseDTO) {
        return Valise.builder()
                .id(valiseDTO.getId())
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .build();
    }


    @Override
    public ProblemeDTO updateProbleme(int id, ProblemeDTO problemeDTO) {
        Probleme probleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));

        probleme.setDescriptionProbleme(problemeDTO.getDescriptionProbleme());
        probleme.setDetailsProbleme(problemeDTO.getDetailsProbleme());

        Probleme updatedProbleme = problemeRepository.save(probleme);
        return convertToDTO(updatedProbleme);
    }

    @Override
    public void deleteProbleme(int id) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with ID: " + id);
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
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));

        // Logs pour déboguer le problème
        System.out.println("Probleme trouvé : " + probleme);
        if (probleme.getValise() != null) {
            System.out.println("Valise associée : " + probleme.getValise().getDescription());
        } else {
            System.out.println("Aucune valise associée.");
        }
        if (probleme.getClient() != null) {
            System.out.println("Client associé : " + probleme.getClient().getName());
        } else {
            System.out.println("Aucun client associé.");
        }

        // Initialisation explicite pour éviter les erreurs lazy
        if (probleme.getValise() != null) {
            Hibernate.initialize(probleme.getValise());
        }
        if (probleme.getClient() != null) {
            Hibernate.initialize(probleme.getClient());
        }

        return convertToDTO(probleme);
    }


    @Override
    public List<ProblemeDTO> getAllProblemes() {
        try {
            List<Probleme> problemes = problemeRepository.findAll();
            if (problemes.isEmpty()) {
                System.out.println("Aucun problème trouvé dans la base de données.");
            }
            return problemes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des problèmes : " + e.getMessage());
            throw e;
        }
    }


    @Override
    public boolean existsByDescriptionAndDetails(String description, String details) {
        return false;
    }

    public ClientDTO convertToDTO(Client client) {
        if (client == null) {
            return null;
        }
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
                .build();
    }

    private ProblemeDTO convertToDTO(Probleme probleme) {
        return ProblemeDTO.builder()
                .id(probleme.getId())
                .descriptionProbleme(probleme.getDescriptionProbleme())
                .detailsProbleme(probleme.getDetailsProbleme())
                .valiseId(probleme.getValise() != null ? probleme.getValise().getId() : null)
                .valise(probleme.getValise() != null ? valiseService.convertToDTO(probleme.getValise()) : null)
                .clientId(probleme.getClient() != null ? probleme.getClient().getId() : null)
                .client(probleme.getClient() != null ? clientService.convertToDTO(probleme.getClient()) : null)
                .build();
    }






}

