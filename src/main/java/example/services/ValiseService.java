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

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ValiseService implements IValiseService {

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private RegleRepository regleRepository;

    @Override
    public ValiseDTO createValise(ValiseDTO valiseDTO) {
        Client client = clientRepository.findById(valiseDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Valise valise = Valise.builder()
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .client(client)
                .build();
        valise = valiseRepository.save(valise);
        return mapToDTO(valise);
    }
    @Override
    @Transactional
    public ValiseDTO updateValise(int id, ValiseDTO valiseDTO) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));

        // Mise à jour des champs simples
        valise.setDescription(valiseDTO.getDescription());
        valise.setNumeroValise(valiseDTO.getNumeroValise());

        // Mise à jour du client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
            valise.setClient(client);
        }

        // Mise à jour des types de valise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found"));
            valise.setTypeValise(typeValise);
        }

        // Mise à jour des mouvements
        if (valiseDTO.getMouvementIds() != null) {
            // Supprimer les mouvements qui ne sont plus présents
            List<Mouvement> mouvementsActuels = valise.getMouvements();
            List<Mouvement> mouvementsASupprimer = mouvementsActuels.stream()
                    .filter(mouvement -> !valiseDTO.getMouvementIds().contains(mouvement.getId()))
                    .collect(Collectors.toList());

            mouvementRepository.deleteAll(mouvementsASupprimer);

            // Ajouter les nouveaux mouvements
            List<Mouvement> nouveauxMouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            for (Mouvement nouveauMouvement : nouveauxMouvements) {
                if (!valise.getMouvements().contains(nouveauMouvement)) {
                    valise.getMouvements().add(nouveauMouvement);
                }
            }
        }

        // Mise à jour des règles associées
        if (valiseDTO.getRegleSortieIds() != null) {
            List<Regle> regles = regleRepository.findAllById(valiseDTO.getRegleSortieIds());
            valise.setRegleSortie(regles);
        }

        // Enregistrer la mise à jour
        valiseRepository.save(valise);
        return mapToDTO(valise);
    }



    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Valise not found");
        }
        valiseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ValiseDTO getValiseById(int id) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));
        return mapToDTO(valise);
    }

    @Override
    @Transactional
    public List<ValiseDTO> getAllValises() {
        return valiseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private ValiseDTO mapToDTO(Valise valise) {
        return ValiseDTO.builder()
                .id(valise.getId())
                .description(valise.getDescription())
                .numeroValise(valise.getNumeroValise())
                .typeValiseDescription(valise.getTypeValise() != null ? valise.getTypeValise().getDescription() : "Non défini")
                .clientName(valise.getClient() != null ? valise.getClient().getName() : "Non défini")
                .typeValiseId(valise.getTypeValise() != null ? valise.getTypeValise().getId() : null)
                .mouvementList(valise.getMouvements() != null
                        ? valise.getMouvements().stream()
                        .map(mouvement -> MouvementDTO.builder()
                                .id(mouvement.getId())
                                .dateHeureMouvement(mouvement.getDateHeureMouvement())
                                .statutSortie(mouvement.getStatutSortie())
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .regleSortie(valise.getRegleSortie() != null
                        ? valise.getRegleSortie().stream()
                        .map(regle -> RegleDTO.builder()
                                .id(regle.getId())
                                .coderegle(regle.getCoderegle()) // Adjust based on RegleDTO properties
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }


    public ValiseDTO convertToDTO(Valise valise) {
        if (valise == null) {
            return null;
        }
        return ValiseDTO.builder()
                .id(valise.getId())
                .description(valise.getDescription())
                .numeroValise(valise.getNumeroValise())
                .build();
    }

    public Valise getValiseById(Integer id) {
        return valiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + id));
    }





}
