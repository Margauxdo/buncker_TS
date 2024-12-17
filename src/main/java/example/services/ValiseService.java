package example.services;


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


@Service
public class ValiseService implements IValiseService {

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

    @Override
    @Transactional
    public ValiseDTO createValise(ValiseDTO valiseDTO) {
        Valise valise = mapToEntity(valiseDTO);

        // Vérification et ajout des mouvements (si présents)
        if (valiseDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            valise.setMouvements(mouvements);  // Assurez-vous que la liste est initialisée
        } else {
            valise.setMouvements(new ArrayList<>());  // Initialisation d'une liste vide si aucun mouvement n'est fourni
        }

        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valiseDTO.getClientId()));
            valise.setClient(client);
        } else {
            throw new ResourceNotFoundException("Client ID is required");
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

    @Transactional
    @Override
    public ValiseDTO updateValise(int id, ValiseDTO valiseDTO) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found with ID: " + id));

        // Mise à jour des champs simples
        valise.setDescription(valiseDTO.getDescription());
        valise.setNumeroValise(valiseDTO.getNumeroValise());
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

        // Mise à jour des mouvements
        if (valiseDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            valise.setMouvements(mouvements);
        }

        // Mise à jour des règles de sortie
        if (valiseDTO.getRegleSortieIds() != null) {
            List<Regle> regles = regleRepository.findAllById(valiseDTO.getRegleSortieIds());
            valise.setReglesSortie(regles.isEmpty() ? null : regles.get(0)); // Gérer une seule règle
        }

        // Mise à jour du client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valiseDTO.getClientId()));
            valise.setClient(client);  // Mise à jour de la référence du client
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



    @Override
    @Transactional
    public List<ValiseDTO> getAllValises() {
        return valiseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
                .numeroValise(valise.getNumeroValise())
                .refClient(valise.getRefClient())
                .sortie(valise.getSortie())
                .dateDernierMouvement(valise.getDateDernierMouvement())
                .dateSortiePrevue(valise.getDateSortiePrevue())
                .dateRetourPrevue(valise.getDateRetourPrevue())
                .dateCreation(valise.getDateCreation())
                .numeroDujeu(valise.getNumeroDujeu())
                .typeValiseId(valise.getTypeValise() != null ? valise.getTypeValise().getId() : null)
                .typeValiseDescription(valise.getTypeValise() != null ? valise.getTypeValise().getDescription() : null)
                .mouvementIds(valise.getMouvements().stream().map(Mouvement::getId).toList())
                .regleSortieIds(valise.getReglesSortie() != null ? List.of(valise.getReglesSortie().getId()) : null)
                .build();
    }

    // Conversion ValiseDTO vers Valise
    private Valise mapToEntity(ValiseDTO valiseDTO) {
        Valise valise = Valise.builder()
                .id(valiseDTO.getId())
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .refClient(valiseDTO.getRefClient())
                .sortie(valiseDTO.getSortie())
                .dateDernierMouvement(valiseDTO.getDateDernierMouvement())
                .dateSortiePrevue(valiseDTO.getDateSortiePrevue())
                .dateRetourPrevue(valiseDTO.getDateRetourPrevue())
                .dateCreation(valiseDTO.getDateCreation())
                .numeroDujeu(valiseDTO.getNumeroDujeu())
                .build();

        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found"));
            valise.setTypeValise(typeValise);
        }

        return valise;
    }
}
