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
        Valise valise = valiseRepository.findById(typeValiseDTO.getValiseId())
                .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + typeValiseDTO.getValiseId()));


        TypeValise typeValise = TypeValise.builder()
                .proprietaire(typeValiseDTO.getProprietaire())
                .description(typeValiseDTO.getDescription())
                .valise(valise)
                .build();

        TypeValise savedTypeValise = typeValiseRepository.save(typeValise);

        return mapToDTO(savedTypeValise);
    }

    @Override
    public TypeValiseDTO updateTypeValise(int id, TypeValiseDTO typeValiseDTO) {
        TypeValise existingTypeValise = typeValiseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeValise with ID " + id + " not found"));

        Valise valise = valiseRepository.findById(typeValiseDTO.getValiseId())
                .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + typeValiseDTO.getValiseId()));

        existingTypeValise.setProprietaire(typeValiseDTO.getProprietaire());
        existingTypeValise.setDescription(typeValiseDTO.getDescription());
        existingTypeValise.setValise(valise);

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

    private TypeValiseDTO mapToDTO(TypeValise typeValise) {
        return TypeValiseDTO.builder()
                .id(typeValise.getId())
                .proprietaire(typeValise.getProprietaire())
                .description(typeValise.getDescription())
                .valiseId(typeValise.getValise() != null ? typeValise.getValise().getId() : null)
                .numeroValise(typeValise.getValise() != null ? typeValise.getValise().getNumeroValise() : null)
                .build();
    }



    public List<TypeValise> getAllTypeValises() {
        return typeValiseRepository.findAll();
    }
}
