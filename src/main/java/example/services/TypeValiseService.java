package example.services;

import example.entities.TypeValise;
import example.entities.Valise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeValiseService implements ITypeValiseService {

    private final TypeValiseRepository typeValiseRepository;
    private final ValiseRepository valiseRepository;

    @Autowired
    public TypeValiseService(TypeValiseRepository typeValiseRepository, ValiseRepository valiseRepository) {
        this.typeValiseRepository = typeValiseRepository;
        this.valiseRepository = valiseRepository;
    }


    @Override
    public TypeValise createTypeValise(TypeValise typeValise) {
        if (typeValise == null) {
            throw new IllegalArgumentException("TypeValise cannot be null");
        }

        // Log pour vérifier l'objet reçu
        System.out.println("TypeValise reçu : " + typeValise);
        System.out.println("Propriétaire : " + typeValise.getProprietaire());
        System.out.println("Description : " + typeValise.getDescription());
        System.out.println("Valises : " + (typeValise.getValises() != null ? typeValise.getValises() : "Aucune valise"));

        try {
            if (typeValise.getValises() != null && !typeValise.getValises().isEmpty()) {
                List<Valise> existingValises = new ArrayList<>();
                for (Valise valise : typeValise.getValises()) {
                    // Récupérer explicitement chaque valise de la base de données
                    Valise existingValise = valiseRepository.findById(valise.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + valise.getId()));
                    existingValises.add(existingValise);
                }
                // Associer les valises récupérées
                typeValise.setValises(existingValises);
            }

            // Log pour vérifier l'état avant la sauvegarde
            System.out.println("TypeValise avant sauvegarde : " + typeValise);

            return typeValiseRepository.save(typeValise);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la trace complète de l'erreur
            throw new RuntimeException("Erreur lors de la création de TypeValise", e);
        }
    }



    @Override
    public TypeValise updateTypeValise(int id, TypeValise typeValise) {
        if (!typeValiseRepository.existsById(id)) {
            throw new EntityNotFoundException("The suitcase type does not exist");
        }
        if (id != typeValise.getId()) {
            throw new IllegalArgumentException("Suitcase type ID does not match");
        }
        return typeValiseRepository.save(typeValise);
    }




    @Override
    public void deleteTypeValise(int id) {
        if (!typeValiseRepository.existsById(id)) {
            throw new RuntimeException("The suitcase type does not exist");
        }
        typeValiseRepository.deleteById(id);
    }




    @Override
    public TypeValise getTypeValise(int id) {

        return typeValiseRepository.findById(id).orElse(null);
    }

    @Override
    public List<TypeValise> getTypeValises() {

        return typeValiseRepository.findAll();
    }
}
