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

    @Override
    @Transactional
    public ValiseDTO createValise(ValiseDTO valiseDTO) {
        Valise valise = mapToEntity(valiseDTO);

        // Associer le Type de Valise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise introuvable avec l'ID : " + valiseDTO.getTypeValiseId()));
            valise.setTypeValise(typeValise);
        }

        // Associer les Mouvements
        if (valiseDTO.getMouvementIds() != null && !valiseDTO.getMouvementIds().isEmpty()) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            valise.setMouvements(mouvements);
        }

        // Associer le Client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID : " + valiseDTO.getClientId()));
            valise.setClient(client);
        }

        // Associer les Règles de Sortie
        if (valiseDTO.getRegleSortieIds() != null && !valiseDTO.getRegleSortieIds().isEmpty()) {
            Regle regle = regleRepository.findById(valiseDTO.getRegleSortieIds().get(0))
                    .orElseThrow(() -> new ResourceNotFoundException("Règle introuvable avec l'ID : " + valiseDTO.getRegleSortieIds().get(0)));
            valise.setReglesSortie(regle);
        }

        // Sauvegarder la Valise
        Valise savedValise = valiseRepository.save(valise);
        return mapToDTO(savedValise);
    }

    @Override
    @Transactional
    public ValiseDTO updateValise(Integer id, ValiseDTO valiseDTO) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise introuvable avec l'ID : " + id));

        // Mettre à jour les champs simples
        valise.setDescription(valiseDTO.getDescription());
        valise.setNumeroValise(String.valueOf(valiseDTO.getNumeroValise()));
        valise.setSortie(valiseDTO.getSortie());
        valise.setDateDernierMouvement(valiseDTO.getDateDernierMouvement());
        valise.setDateSortiePrevue(valiseDTO.getDateSortiePrevue());
        valise.setDateRetourPrevue(valiseDTO.getDateRetourPrevue());
        valise.setDateCreation(valiseDTO.getDateCreation());
        valise.setNumeroDujeu(valiseDTO.getNumeroDujeu());

        // Mettre à jour le Type de Valise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise introuvable avec l'ID : " + valiseDTO.getTypeValiseId()));
            valise.setTypeValise(typeValise);
        }

        // Mettre à jour les Mouvements
        if (valiseDTO.getMouvementIds() != null) {
            List<Mouvement> mouvements = mouvementRepository.findAllById(valiseDTO.getMouvementIds());
            valise.setMouvements(mouvements);
        }

        // Mettre à jour le Client
        if (valiseDTO.getClientId() != null) {
            Client client = clientRepository.findById(valiseDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID : " + valiseDTO.getClientId()));
            valise.setClient(client);
        }

        // Mettre à jour les Règles de Sortie
        if (valiseDTO.getRegleSortieIds() != null && !valiseDTO.getRegleSortieIds().isEmpty()) {
            Regle regle = regleRepository.findById(valiseDTO.getRegleSortieIds().get(0))
                    .orElseThrow(() -> new ResourceNotFoundException("Règle introuvable avec l'ID : " + valiseDTO.getRegleSortieIds().get(0)));
            valise.setReglesSortie(regle);
        }

        // Sauvegarder les modifications
        Valise updatedValise = valiseRepository.save(valise);
        return mapToDTO(updatedValise);
    }

    @Override
    public void deleteValise(Integer id) {

    }

    @Override
    public ValiseDTO updateValise(int id, ValiseDTO valiseDTO) {
        return null;
    }

    @Override
    public ValiseDTO getValiseById(Integer id) {
        return null;
    }

    @Override
    @Transactional
    public List<ValiseDTO> getAllValises() {
        List<Valise> valises = valiseRepository.findAll();

        // Log des Valises
        valises.forEach(v -> log.info("Valise ID: {}, TypeValise: {}, Mouvements: {}, Regles: {}",
                v.getId(),
                v.getTypeValise() != null ? v.getTypeValise().getDescription() : "Non défini",
                v.getMouvements().size(),
                v.getReglesSortie() != null ? v.getReglesSortie().getCoderegle() : "Non défini"));

        // Conversion des entités en DTOs
        return valises.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteValise(int id) {

    }

    private ValiseDTO mapToDTO(Valise valise) {
        return ValiseDTO.builder()
                .id(valise.getId())
                .description(valise.getDescription())
                .numeroValise(Integer.valueOf(valise.getNumeroValise()))
                .refClient(valise.getClient() != null ? valise.getClient().getName() : "Non défini")
                .sortie(valise.getSortie())
                .dateDernierMouvement(valise.getDateDernierMouvement())
                .dateSortiePrevue(valise.getDateSortiePrevue())
                .dateRetourPrevue(valise.getDateRetourPrevue())
                .dateCreation(valise.getDateCreation())
                .numeroDujeu(valise.getNumeroDujeu())
                .typeValiseDescription(valise.getTypeValise() != null ? valise.getTypeValise().getDescription() : "Non défini")
                .mouvementList(valise.getMouvements().stream()
                        .map(m -> new MouvementDTO(m.getId(), m.getStatutSortie(), m.getDateHeureMouvement()))
                        .collect(Collectors.toList()))
                .reglesSortie(valise.getReglesSortie() != null
                        ? List.of(new RegleDTO(valise.getReglesSortie().getId(), valise.getReglesSortie().getCoderegle()))
                        : null)
                .build();
    }

    private Valise mapToEntity(ValiseDTO valiseDTO) {
        Valise valise = Valise.builder()
                .description(valiseDTO.getDescription())
                .numeroValise(String.valueOf(valiseDTO.getNumeroValise()))
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

        // Associer le Type de Valise
        if (valiseDTO.getTypeValiseId() != null) {
            TypeValise typeValise = typeValiseRepository.findById(valiseDTO.getTypeValiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("TypeValise introuvable avec l'ID : " + valiseDTO.getTypeValiseId()));
            valise.setTypeValise(typeValise);
        }

        // Associer les Règles de Sortie
        if (valiseDTO.getRegleSortieIds() != null && !valiseDTO.getRegleSortieIds().isEmpty()) {
            Regle regle = regleRepository.findById(valiseDTO.getRegleSortieIds().get(0))
                    .orElseThrow(() -> new ResourceNotFoundException("Règle introuvable avec l'ID : " + valiseDTO.getRegleSortieIds().get(0)));
            valise.setReglesSortie(regle);
        }

        return valise;
    }
}
