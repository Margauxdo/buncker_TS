package example.services;

import example.DTO.TypeValiseDTO;
import example.entity.TypeValise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeValiseService implements ITypeValiseService {

    private final TypeValiseRepository typeValiseRepository;

    @Autowired
    public TypeValiseService(TypeValiseRepository typeValiseRepository) {
        this.typeValiseRepository = typeValiseRepository;
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
    public void deleteTypeValise(int id) {
        if (!typeValiseRepository.existsById(id)) {
            throw new EntityNotFoundException("TypeValise with ID " + id + " not found");
        }
        typeValiseRepository.deleteById(id);
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
