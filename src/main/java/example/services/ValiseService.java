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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValiseService implements IValiseService {

    public static final Logger logger = LoggerFactory.getLogger(ValiseService.class);

    @Autowired
    private ValiseRepository valiseRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeValiseRepository  typeValiseRepository;
    @Autowired
    private ClientRepository clientRepository;


    public ValiseService(ValiseRepository valiseRepository) {
        this.valiseRepository = valiseRepository;
    }


    @Override
    public Valise createValise(Valise valise) {
        try {
            if (valise.getClient() == null || valise.getClient().getId() == 0) {
                throw new IllegalArgumentException("Client ID is required");
            }

            // Log d'entrée pour vérifier les données
            logger.debug("Tentative de création de la valise avec les informations : {}", valise);

            // Vérification que le client existe
            Client client = clientRepository.findById(valise.getClient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valise.getClient().getId()));
            valise.setClient(client);
            logger.debug("Client trouvé : {}", client);

            // Vérification de la règle
            if (valise.getRegleSortie() != null && valise.getRegleSortie().getId() != 0) {
                Regle regle = regleRepository.findById(valise.getRegleSortie().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Regle not found with ID: " + valise.getRegleSortie().getId()));
                valise.setRegleSortie(regle);
                logger.debug("Règle associée : {}", regle);
            }

            // Vérification du type de valise
            if (valise.getTypevalise() != null && valise.getTypevalise().getId() != 0) {
                TypeValise typeValise = typeValiseRepository.findById(valise.getTypevalise().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found with ID: " + valise.getTypevalise().getId()));
                valise.setTypevalise(typeValise);
                logger.debug("Type de valise associé : {}", typeValise);
            }

            // Sauvegarde de la valise dans la base de données
            Valise savedValise = valiseRepository.save(valise);
            logger.debug("Valise créée avec succès : {}", savedValise);
            return savedValise;
        }catch (Exception e) {
            logger.error("Erreur lors de la création de la valise : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur interne lors de la création de la valise", e);
        }

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
            throw new RuntimeException("suitcase not found");
        }
    }


    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("The suitcase does not exist");
        }
        valiseRepository.deleteById(id);
    }




    @Override
    public Valise getValiseById(int id) {
        return valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found with ID: " + id));
    }



    @Override
    public List<Valise> getAllValises() {
        return valiseRepository.findAll();
    }
}
