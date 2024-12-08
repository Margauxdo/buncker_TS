package example.services;

import example.entity.Client;
import example.entity.Regle;
import example.entity.TypeValise;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IValiseService;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValiseService implements IValiseService {

    public static final Logger logger = LoggerFactory.getLogger(ValiseService.class);

    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    @Autowired
    private ClientRepository clientRepository;

    public ValiseService(ValiseRepository valiseRepository) {
        this.valiseRepository = valiseRepository;
    }

    @Override
    public Valise createValise(Valise valise) {
        try {
            // Vérification du client
            if (valise.getClient() == null || valise.getClient().getId() == 0) {
                throw new IllegalArgumentException("Client ID is required");
            }
            Client client = clientRepository.findById(valise.getClient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valise.getClient().getId()));
            valise.setClient(client);

            // Vérification des règles associées
            if (valise.getRegleSortie() != null && !valise.getRegleSortie().isEmpty()) {
                valise.setRegleSortie(validateRegles(valise.getRegleSortie()));
            }

            // Vérification du type de valise
            if (valise.getTypeValise() != null && valise.getTypeValise().getId() != 0) {
                TypeValise typeValise = typeValiseRepository.findById(valise.getTypeValise().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found with ID: " + valise.getTypeValise().getId()));
                valise.setTypeValise(typeValise);
            }

            // Sauvegarde de la valise
            Valise savedValise = valiseRepository.save(valise);
            logger.info("Valise créée avec succès : {}", savedValise);
            return savedValise;
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la valise : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur interne lors de la création de la valise", e);
        }
    }

    public List<Regle> validateRegles(List<Regle> regles) {
        List<Regle> validRegles = new ArrayList<>();
        for (Regle regle : regles) {
            if (regle.getId() != 0) {
                Regle foundRegle = regleRepository.findById(regle.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Regle not found with ID: " + regle.getId()));
                validRegles.add(foundRegle);
            } else {
                // Initialiser les champs requis de la règle si nécessaire
                if (regle.getCalculCalendaire() == null) {
                    regle.setCalculCalendaire(0); // Valeur par défaut pour un champ Integer
                }
                validRegles.add(regle);
            }
        }
        return validRegles;
    }




    @Override
    public Valise updateValise(int id, Valise valise) {
        if (valise.getId() != id) {
            throw new IllegalArgumentException("Suitcase ID does not match");
        }
        if (valiseRepository.existsById(id)) {
            valise.setId(id);
            return valiseRepository.save(valise);
        } else {
            throw new RuntimeException("Suitcase not found");
        }
    }

    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("The suitcase does not exist");
        }
        valiseRepository.deleteById(id);
    }

    @Transactional
    public Valise getValiseById(int id) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise non trouvée avec ID : " + id));
        Hibernate.initialize(valise.getClient());
        Hibernate.initialize(valise.getTypeValise());
        Hibernate.initialize(valise.getMouvementList());
        return valise;
    }





    @Transactional
    public List<Valise> getAllValises() {
        List<Valise> valises = valiseRepository.findAll();
        valises.forEach(valise -> {

            Hibernate.initialize(valise.getTypeValise());
            Hibernate.initialize(valise.getClient());
            Hibernate.initialize(valise.getRegleSortie());
            valise.getRegleSortie().forEach(regle -> {
                if (regle.getCalculCalendaire() == null) {
                    regle.setCalculCalendaire(0); // Valeur par défaut pour éviter les erreurs
                }
            });
        });
        return valises;
    }


    @Override
    public void persistValise(Valise valise) {
        valiseRepository.save(valise);
    }

    @Override
    public boolean existsById(int id) {
        return valiseRepository.existsById(id);
    }

    @Override
    public List<Valise> findAllWithClient() {
        return List.of();
    }

    @Transactional
    public Valise getValiseByIdWithDependencies(int id) {
        Valise valise = valiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Valise not found"));
        Hibernate.initialize(valise.getClient());
        Hibernate.initialize(valise.getTypeValise());
        return valise;
    }



}
