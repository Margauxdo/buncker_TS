package example.services;

import example.DTO.TypeValiseDTO;
import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public TypeValiseDTO createTypeValise(TypeValiseDTO typeValiseDTO) {
        // Vérification des champs obligatoires
        if (typeValiseDTO.getProprietaire() == null || typeValiseDTO.getDescription() == null) {
            throw new IllegalArgumentException("Propriétaire et description sont obligatoires");
        }

        TypeValise typeValise = TypeValise.builder()
                .proprietaire(typeValiseDTO.getProprietaire())
                .description(typeValiseDTO.getDescription())
                .build();

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        return mapToDTO(savedTypeValise);
    }

    @Override
    public TypeValiseDTO updateTypeValise(int id, TypeValiseDTO typeValiseDTO) {
        TypeValise existingTypeValise = typeValiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeValise with ID " + id + " not found"));

        // Mise à jour des champs
        existingTypeValise.setProprietaire(typeValiseDTO.getProprietaire());
        existingTypeValise.setDescription(typeValiseDTO.getDescription());

        TypeValise updatedTypeValise = typeValiseRepository.save(existingTypeValise);

        return mapToDTO(updatedTypeValise);
    }

    @Override
    @Transactional
    public void deleteTypeValise(int id) {
        TypeValise typeValise = typeValiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeValise avec l'ID " + id + " introuvable"));

        // Dissocier les Valises associées
        List<Valise> valises = valiseRepository.findByTypeValise(typeValise);
        for (Valise valise : valises) {
            valise.setTypeValise(null); // Dissocie le TypeValise
            valiseRepository.save(valise); // Sauvegarde la modification
        }

        // Supprimer le TypeValise
        typeValiseRepository.delete(typeValise);
    }









    @Override
    public TypeValiseDTO getTypeValise(int id) {
        TypeValise typeValise = typeValiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeValise with ID " + id + " not found"));

        return mapToDTO(typeValise);
    }

    @Override
    public List<TypeValiseDTO> getTypeValises() {
        return typeValiseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Mapping d'une entité vers un DTO
    private TypeValiseDTO mapToDTO(TypeValise typeValise) {
        return TypeValiseDTO.builder()
                .id(typeValise.getId())
                .proprietaire(typeValise.getProprietaire())
                .description(typeValise.getDescription())
                .build();
    }

    // Mapping d'un DTO vers une entité
    private TypeValise mapToEntity(TypeValiseDTO typeValiseDTO) {
        return TypeValise.builder()
                .id(typeValiseDTO.getId())
                .proprietaire(typeValiseDTO.getProprietaire())
                .description(typeValiseDTO.getDescription())
                .build();
    }
}
