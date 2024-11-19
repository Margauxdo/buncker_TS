package example.services;

import example.entity.TypeValise;
import example.entity.Valise;
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
        System.out.println("Valises reçues : " + (typeValise.getValises() != null ? typeValise.getValises() : "Aucune valise"));

        try {
            // Récupérer explicitement les valises existantes à partir de leurs IDs
            if (typeValise.getValises() != null && !typeValise.getValises().isEmpty()) {
                List<Valise> managedValises = new ArrayList<>();
                for (Valise valise : typeValise.getValises()) {
                    // Récupérer la valise depuis la base de données
                    Valise managedValise = valiseRepository.findById(valise.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + valise.getId()));
                    managedValise.setTypevalise(typeValise);
                    managedValises.add(managedValise);
                }
                // Associer la liste des valises récupérées
                typeValise.setValises(managedValises);
            }

            // Ajoutez cette ligne ici, avant la sauvegarde
            System.out.println("Avant la sauvegarde de TypeValise : " + typeValise);

            // Sauvegarder le TypeValise
            return typeValiseRepository.save(typeValise);
        } catch (Exception e) {
            e.printStackTrace();
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
