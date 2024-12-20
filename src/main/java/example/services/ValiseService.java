package example.services;


import example.DTO.MouvementDTO;
import example.DTO.RegleDTO;
import example.DTO.ValiseDTO;
import example.entity.*;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IValiseService;
import example.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class ValiseService implements IValiseService {

    private static final Logger log = LoggerFactory.getLogger(ValiseService.class);


    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MouvementService mouvementService;


    public ValiseService(ValiseRepository valiseRepository) {
        this.valiseRepository = valiseRepository;
    }

    public boolean checkIfValiseExists(String numeroValise) {
        return valiseRepository.existsByNumeroValise(numeroValise);
    }

    @Override
    @Transactional
    public ValiseDTO createValise(ValiseDTO valiseDTO) {
        Valise valise = mapToEntity(valiseDTO);

        // Vérification et association de TypeValise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise introuvable avec l'ID : " + valiseDTO.getTypeValiseId()));
            valise.setTypeValise(typeValise);
        } else {
            throw new ResourceNotFoundException("TypeValise est requis");
        }

        // Vérification et association des mouvements
        if (valiseDTO.getMouvementIds() != null && !valiseDTO.getMouvementIds().isEmpty()) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            if (mouvements.size() != valiseDTO.getMouvementIds().size()) {
                throw new ResourceNotFoundException("Certains mouvements sont introuvables");
            }
            valise.setMouvements(mouvements);
        } else {
            valise.setMouvements(new ArrayList<>());
        }

        // Vérification et association du client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID : " + valiseDTO.getClientId()));
            valise.setClient(client);
        } else {
            throw new ResourceNotFoundException("L'ID du client est requis");
        }

        // Vérification et association des règles de sortie
        if (valiseDTO.getRegleSortieIds() != null && !valiseDTO.getRegleSortieIds().isEmpty()) {
            Regle regle = regleRepository.findById(valiseDTO.getRegleSortieIds().get(0))
                    .orElseThrow(() -> new ResourceNotFoundException("Règle introuvable avec l'ID : " + valiseDTO.getRegleSortieIds().get(0)));
            valise.setReglesSortie(regle);
        } else {
            valise.setReglesSortie(null); // Aucune règle associée
        }

        // Sauvegarde de la valise
        Valise savedValise = valiseRepository.save(valise);

        return mapToDTO(savedValise);
    }




    @Override
    public ValiseDTO updateValise(Integer id, ValiseDTO valiseDTO) {
        return null;
    }

    @Override
    public void deleteValise(Integer id) {

    }

    public ValiseDTO updateValise(int id, ValiseDTO valiseDTO) {
        // Récupération de la valise existante
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found with ID: " + id));

        // Mise à jour des champs simples
        valise.setDescription(valiseDTO.getDescription());
        valise.setNumeroValise(String.valueOf(valiseDTO.getNumeroValise()));
        valise.setRefClient(valiseDTO.getRefClient());
        valise.setSortie(valiseDTO.getSortie());
        valise.setDateDernierMouvement(valiseDTO.getDateDernierMouvement());
        valise.setDateSortiePrevue(valiseDTO.getDateSortiePrevue());
        valise.setDateRetourPrevue(valiseDTO.getDateRetourPrevue());
        valise.setDateCreation(valiseDTO.getDateCreation());
        valise.setNumeroDujeu(valiseDTO.getNumeroDujeu());

        // Mise à jour du type de valise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found with ID: " + valiseDTO.getTypeValiseId()));
            valise.setTypeValise(typeValise);
        }

        // Gestion des mouvements
        if (valiseDTO.getMouvementIds() != null) {
            if (valise.getMouvements() == null) {
                valise.setMouvements(new ArrayList<>());
            }

            // Supprimez les anciens mouvements qui ne sont plus présents
            valise.getMouvements().removeIf(mouvement -> !valiseDTO.getMouvementIds().contains(mouvement.getId()));

            // Ajoutez les nouveaux mouvements
            for (Integer mouvementId : valiseDTO.getMouvementIds()) {
                if (valise.getMouvements().stream().noneMatch(m -> m.getId().equals(mouvementId))) {
                    Mouvement mouvement = mouvementRepository.findById(mouvementId)
                            .orElseThrow(() -> new ResourceNotFoundException("Mouvement not found with ID: " + mouvementId));
                    valise.getMouvements().add(mouvement);
                }
            }
        }

        // Mise à jour des règles de sortie
        if (valiseDTO.getRegleSortieIds() != null && !valiseDTO.getRegleSortieIds().isEmpty()) {
            Regle regle = regleRepository.findById(valiseDTO.getRegleSortieIds().get(0))
                    .orElseThrow(() -> new ResourceNotFoundException("Regle not found with ID: " + valiseDTO.getRegleSortieIds().get(0)));
            valise.setReglesSortie(regle);
        } else {
            valise.setReglesSortie(null);
        }

        // Mise à jour du client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valiseDTO.getClientId()));
            valise.setClient(client);
        } else {
            throw new ResourceNotFoundException("Client ID is required");
        }

        // Enregistrement de la valise mise à jour
        valiseRepository.save(valise);

        return mapToDTO(valise);
    }



    @Override
    public ValiseDTO getValiseById(Integer id) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valise not found"));

        // Créez le ValiseDTO
        ValiseDTO valiseDTO = new ValiseDTO();
        valiseDTO.setId(valise.getId());
        valiseDTO.setDescription(valise.getDescription());
        valiseDTO.setMouvementList(mouvementService.getMouvementsByValiseId(valise.getId()));

        // Si valise a une seule règle de sortie (pas une liste)
        if (valise.getReglesSortie() != null) {
            RegleDTO regleSortieDTO = convertRegleToDTO(valise.getReglesSortie());
            valiseDTO.setReglesSortie(List.of(regleSortieDTO));  // Encapsuler dans une liste
        }

        return valiseDTO;
    }

    // Méthode pour convertir un objet Regle en RegleDTO
    private RegleDTO convertRegleToDTO(Regle regle) {
        return RegleDTO.builder()
                .id(regle.getId())
                .coderegle(regle.getCoderegle())
                .build();
    }

    @Transactional
    @Override
    public List<ValiseDTO> getAllValises() {
        List<Valise> valises = valiseRepository.findAll();

        // Log des valises récupérées
        valises.forEach(v -> {
            log.info("Valise ID: {}, Mouvements: {}", v.getId(), v.getMouvements());
        });

        // Conversion des entités en DTOs
        return valises.stream()
                .map(v -> {
                    log.info("Mapping valise: {}", v);
                    ValiseDTO dto = mapToDTO(v);

                    // Conversion des mouvements
                    dto.setMouvementList(
                            v.getMouvements()
                                    .stream()
                                    .map(mouvement -> {
                                        log.info("Mapping mouvement: {}", mouvement);
                                        return new MouvementDTO(
                                                mouvement.getId(),
                                                mouvement.getStatutSortie(),
                                                mouvement.getDateHeureMouvement()
                                        );
                                    })
                                    .toList()
                    );

                    // Conversion des règles
                    dto.setReglesSortie(
                            v.getReglesSortie() != null
                                    ? List.of(new RegleDTO(
                                    v.getReglesSortie().getId(),
                                    v.getReglesSortie().getCoderegle()))
                                    : null
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }



    public ValiseDTO getValiseByIdWithMouvements(Integer id) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));

        log.info("Mouvements associés à la valise {} : {}", valise.getId(), valise.getMouvements());

        return mapToDTO(valise);
    }


    @Transactional
    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Valise not found with ID: " + id);
        }
        valiseRepository.deleteById(id);
    }

    // Conversion Valise vers ValiseDTO
    private ValiseDTO mapToDTO(Valise valise) {
        return ValiseDTO.builder()
                .id(valise.getId())
                .description(valise.getDescription())
                .numeroValise(Integer.valueOf(valise.getNumeroValise()))
                .refClient(valise.getClient() != null ? valise.getClient().getName() : null)
                .sortie(valise.getSortie())
                .dateDernierMouvement(valise.getDateDernierMouvement())
                .dateSortiePrevue(valise.getDateSortiePrevue())
                .dateRetourPrevue(valise.getDateRetourPrevue())
                .dateCreation(valise.getDateCreation())
                .numeroDujeu(valise.getNumeroDujeu())
                .mouvementList(
                        valise.getMouvements().stream().map(mouvement -> {
                            log.info("Mapping Mouvement ID: {}", mouvement.getId());
                            return new MouvementDTO(
                                    mouvement.getId(),
                                    mouvement.getStatutSortie(),
                                    mouvement.getDateHeureMouvement()
                            );
                        }).toList()
                )
                .reglesSortie(
                        valise.getReglesSortie() != null
                                ? List.of(new RegleDTO(
                                valise.getReglesSortie().getId(),
                                valise.getReglesSortie().getCoderegle()))
                                : null
                )
                .build();
    }

    // Conversion ValiseDTO vers Valise

    private Valise mapToEntity(ValiseDTO valiseDTO) {
        Valise valise = Valise.builder()
                .description(valiseDTO.getDescription())
                .numeroValise(String.valueOf(valiseDTO.getNumeroValise()))
                .refClient(valiseDTO.getRefClient())
                .sortie(valiseDTO.getSortie())
                .dateDernierMouvement(valiseDTO.getDateDernierMouvement())
                .dateSortiePrevue(valiseDTO.getDateSortiePrevue())
                .dateRetourPrevue(valiseDTO.getDateRetourPrevue())
                .dateCreation(valiseDTO.getDateCreation())
                .numeroDujeu(valiseDTO.getNumeroDujeu())
                .build();

        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
            valise.setClient(client);
        }

        // Associer les mouvements
        if (valiseDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            valise.setMouvements(mouvements);
        }

        // Logique similaire pour les règles, types de valise, etc.
        return valise;

    }




}
